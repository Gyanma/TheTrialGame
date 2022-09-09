package com.mapproject.operations.actions;

import com.mapproject.operations.inputoutput.Printer;
import com.mapproject.operations.runHandler.Loader;
import com.mapproject.operations.utilities.enums.Status;
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

        PacificEncounter encounter = (PacificEncounter) gameSession.getCurrentRoom().getEvent();
        // if the encounter presents "-1" as gifted items id, it means it doesn't accept
        // any
        if (encounter.getGiftedItemId().contains(-1)) {
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora");
            gameSession.setItemHeldInCommand(null);
            gameSession.setCurrentStatus(Status.IN_PACIFIC_ENCOUNTER);
            return;
        }

        if (gameSession.getCurrentRoom().getEvent() != null) {
            if (gameSession.getCurrentStatus() == Status.IN_PACIFIC_ENCOUNTER) {
                if (Loader.loadItem(command) != null 
                    || Loader.loadWeapon(command) != null) {
                    if (gameSession.getInventory().contains(Loader.loadItem(command))
                            || gameSession.getInventory().contains(Loader.loadWeapon(command))) {
                        gameSession.setItemHeldInCommand(command);
                        gameSession.setCurrentStatus(Status.DONATING_ITEM);
                        System.out.println("Dimmi quale oggetto vuoi ricevere in cambio.");

                    } else {
                        System.out.println("Non hai questo oggetto.");
                        gameSession.setItemHeldInCommand(null);
                        gameSession.setCurrentStatus(Status.IN_PACIFIC_ENCOUNTER);
                    }
                } else {
                    System.out.println("Non capisco di che oggetto parli.");
                    gameSession.setItemHeldInCommand(null);
                    gameSession.setCurrentStatus(Status.IN_PACIFIC_ENCOUNTER);
                }

            } else if (gameSession.getCurrentStatus() == Status.DONATING_ITEM) {

                Item itemGivenByPlayer = Loader.loadItem(gameSession.getItemHeldInCommand());
                if (itemGivenByPlayer == null) {
                    itemGivenByPlayer = Loader.loadWeapon(gameSession.getItemHeldInCommand());
                }

                if (Loader.loadItem(command) == null
                        && Loader.loadWeapon(command) == null) {
                    System.out.println("Non capisco di che oggetto parli.");
                    gameSession.setItemHeldInCommand(null);
                    gameSession.setCurrentStatus(Status.IN_PACIFIC_ENCOUNTER);
                    return;
                }

                Item itemRequestedByPlayer = Loader.loadItem(command);
                if (itemRequestedByPlayer == null) {
                    itemRequestedByPlayer = Loader.loadWeapon(command);
                }

                if (!encounter.getRequestedItemId().contains(-2)) {
                    if (!encounter.getRequestedItemId().contains(itemGivenByPlayer.getId())) {
                        System.out.println("Non sembra volere l'item che hai offerto.");
                        gameSession.setItemHeldInCommand(null);
                        gameSession.setCurrentStatus(Status.IN_PACIFIC_ENCOUNTER);
                        return;
                    }
                }
                if (!encounter.getGiftedItemId().contains(itemRequestedByPlayer.getId())) {
                    System.out.println("Non sembra avere l'item che hai chiesto.");
                    gameSession.setItemHeldInCommand(null);
                    gameSession.setCurrentStatus(Status.IN_PACIFIC_ENCOUNTER);
                    return;
                }
                // remove item from inventory
                Printer.printFromString(encounter.getItemGivenResponse());
                gameSession.removeItemFromInventory(itemGivenByPlayer);
                System.out.println("Hai consegnato " + itemGivenByPlayer.getNameWithDetArticle() + "!");

                // add item to inventory
                System.out.println("Hai ricevuto " + itemRequestedByPlayer.getNameWithDetArticle() + "!");
                InventoryHandler.addItem(gameSession, itemRequestedByPlayer);
                gameSession.setCurrentStatus(Status.EXPLORING);
                gameSession.getCurrentRoom().setEvent(null);
                gameSession.setItemHeldInCommand(null);
                return;

            } else
                System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");

    }

}
