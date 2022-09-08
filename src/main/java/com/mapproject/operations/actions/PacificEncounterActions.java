package com.mapproject.operations.actions;

import com.mapproject.enums.Status;
import com.mapproject.operations.EventHandler;
import com.mapproject.operations.InventoryHandler;
import com.mapproject.operations.Loader;
import com.mapproject.operations.Printer;
import com.mapproject.resources.Session;
import com.mapproject.resources.events.PacificEncounter;
import com.mapproject.resources.items.Item;

public class PacificEncounterActions {
    protected static void pray(Session gameSession) {
        if (gameSession.getCurrentRoom().getEvent() != null
                && gameSession.getCurrentRoom().getEvent().getClass() == PacificEncounter.class
                && gameSession.getCurrentStatus() == Status.IN_PACIFIC_ENCOUNTER) {

            PacificEncounter encounter = (PacificEncounter) gameSession.getCurrentRoom().getEvent();
            if (encounter.getRequestedItemId().size() == 1 && encounter.getRequestedItemId().contains(-1)) {
                Printer.printFromString(encounter.getItemGivenResponse());
                EventHandler.selectBlessing(gameSession);
                gameSession.setCurrentStatus(Status.EXPLORING);
                gameSession.getCurrentRoom().setEvent(null);

            }

        } else {
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
        }
    }

    protected static void donateItem(Session gameSession, String command) {

        if (gameSession.getCurrentRoom().getEvent() != null &&
                gameSession.getCurrentStatus() == Status.IN_PACIFIC_ENCOUNTER) {

            PacificEncounter encounter = (PacificEncounter) gameSession.getCurrentRoom().getEvent();

            // if the encounter presents "-1" as gifted items id, it means it doesn't accept
            // any
            if (encounter.getGiftedItemId().contains(-1)) {
                System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora");
                return;
            }

            String itemRequestedByPlayerName = "";
            String itemGivenByPlayerName = "";
            Item itemReq = null;
            Item itemGiv = null;

            boolean isTransactionGood = true;
            boolean isCommandSingle = false;

            // Recognize the items in the command

            if (command.contains("+")) {

                // command is to be structured as follows:
                // "name of the item the player wants to donate"
                // "+"
                // "name of the item the player wants to receive"
                String[] commandSplit = command.split("\\+");

                itemGivenByPlayerName = commandSplit[0];
                itemGiv = Loader.loadItem(itemGivenByPlayerName);
                if (itemGiv == null) { // if the name doesn't correspond to an item
                    System.out.println("Non capisco di che oggetto parli");
                    return;
                }

                if (isTransactionGood) {
                    itemRequestedByPlayerName = commandSplit[1];
                    itemReq = Loader.loadItem(itemRequestedByPlayerName);
                    if (itemReq == null) {// if the name doesn't correspond to an item

                        System.out.println("Non capisco di che oggetto parli");
                        return;
                    }
                }

            } else if (encounter.getGiftedItemId().size() == 1
                    && encounter.getGiftedItemId().get(0) > 0) {
                // if the npc can only give one item, the command is accepted with one item:
                // the one the player is giving
                itemGivenByPlayerName = command;
                itemGiv = Loader.loadItem(itemGivenByPlayerName);

                if (itemGiv == null) { // if the name doesn't correspond to an item
                    System.out.println("Non capisco di che oggetto parli");
                    return;
                }

                isCommandSingle = true;

            } else {
                System.out.println("Non capisco di che oggetto parli");
                return;
            }

            // check if item given by player is in inventory

            if (isTransactionGood) {
                isTransactionGood = false;

                if (gameSession.getInventory().contains(itemGiv))
                    isTransactionGood = true;

            }
            if (!isTransactionGood) {
                System.out.println("Non hai questo oggetto!");
                return;
            }

            // check if item given by player is accepted by npc
            if (isTransactionGood) {
                isTransactionGood = false;

                if (encounter.getRequestedItemId().contains(-2)) // if the encounter presents "-2" as gifted items id,
                                                                 // it means it can accept any item
                    isTransactionGood = true;
                else if (encounter.getRequestedItemId().contains(itemGiv.getId()))
                    isTransactionGood = true;

            }
            if (!isTransactionGood) {
                System.out.println("Non puoi donare questo oggetto!");
                return;
            }

            // check if item requested by player is appropriate for npc
            if (isTransactionGood) {
                isTransactionGood = false;

                if (isCommandSingle) // it means that only one item could've been requested and no item was actually
                                     // requested, so it's assumed that the player is requesting the only possible
                                     // item
                    isTransactionGood = true;
                else if (encounter.getGiftedItemId().contains(itemReq.getId()))
                    isTransactionGood = true;
            }
            if (!isTransactionGood) {
                System.out.println("Non capisco che oggetto vuoi!");
                return;
            }

            if (isTransactionGood) {
                // remove item from inventory
                Printer.printFromString(encounter.getItemGivenResponse());
                gameSession.removeItemFromInventory(itemGiv);
                System.out.println("Hai consegnato " + itemGiv.getNameWithDetArticle() + "!");

                // add item to inventory
                System.out.println("Hai ricevuto " + itemReq.getNameWithDetArticle() + "!");
                InventoryHandler.addItem(gameSession, itemReq);
                gameSession.setCurrentStatus(Status.EXPLORING);
                gameSession.getCurrentRoom().setEvent(null);
                return;
            }

        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");

    }

}
