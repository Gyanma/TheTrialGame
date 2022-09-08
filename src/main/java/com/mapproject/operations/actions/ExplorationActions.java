package com.mapproject.operations.actions;

import com.mapproject.operations.inputoutput.Printer;
import com.mapproject.operations.runHandler.Loader;
import com.mapproject.operations.utilities.enums.Status;
import com.mapproject.resources.Fight;
import com.mapproject.resources.Session;
import com.mapproject.resources.events.Enemy;
import com.mapproject.resources.events.JugPuzzle;
import com.mapproject.resources.events.PacificEncounter;
import com.mapproject.resources.events.TextPuzzle;
import com.mapproject.resources.items.Item;

public class ExplorationActions {
    protected static void exploreRoom(Session gameSession) {
        if (gameSession.getCurrentStatus() == Status.EXPLORING) {
            if (gameSession.getCurrentRoom().getEvent() != null) {
                System.out.println("Che succede?");
                Printer.printFromString(gameSession.getCurrentRoom().getEvent().getPresentation());
                if (gameSession.getCurrentRoom().getEvent().getClass() == TextPuzzle.class) {
                    gameSession.addEncounteredPuzzle();
                } else if (gameSession.getCurrentRoom().getEvent().getClass() == Enemy.class) {
                    gameSession.addEncounteredEnemy();
                }

            } else
                System.out.println("Sembra tutto tranquillo qui...");
        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
    }

    protected static void exploreRoomForItems(Session gameSession) {
        if (gameSession.getCurrentStatus() == Status.EXPLORING) {
            if (gameSession.getCurrentRoom().getItems() != null
                    && gameSession.getCurrentRoom().getItems().size() > 0) {
                for (Item item : gameSession.getCurrentRoom().getItems()) {

                    System.out.println("Nella stanza vedi "
                            + (item.getNameWithIndetArticle()) + "!");
                }
            } else
                System.out.println("Non c'è nulla qui...");
        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
    }


    protected static void drawMap(Session gameSession) {
        if (gameSession.getCurrentStatus() == Status.EXPLORING) {

            if (gameSession.getInventory().contains(Loader.loadItem("mappa mistica"))) {
                gameSession.drawVisualMap(true);

            } else if (gameSession.getInventory().contains(Loader.loadItem("mappa"))) {
                gameSession.drawVisualMap(false);

            } else {
                System.out.println("Non hai una mappa!");
            }

        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");

    }

    protected static void getItem(Session gameSession, String command) {

        boolean found = false;
        if (gameSession.getCurrentStatus() == Status.EXPLORING) {

            if (gameSession.getInventory().size() + 1 < gameSession.getInventoryCapacity()) {

                for (Item item : gameSession.getCurrentRoom().getItems()) {

                    if (command.equals(item.getName())) {

                        System.out.println("Hai raccolto " + item.getNameWithDetArticle() + ".");
                        InventoryHandler.addItem(gameSession, item);
                        gameSession.getCurrentRoom().removeItem(item);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Non c'è nessun item con questo nome...");
                }
            } else
                System.out.println("Non hai spazio per altri oggetti!");
        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");

    }

    protected static void openInventory(Session gameSession) {
        if (gameSession.getInventory().size() == 0) {
            System.out.println("Al momento, l'inventario è vuoto.");
        } else {
            System.out.println("Nell'inventario hai:");
            int counter = 0;
            do {
                if (gameSession.getInventory().size() - counter >= 4) {
                    System.out.println(
                            gameSession.getInventory().get(counter).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(counter + 1).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(counter + 2).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(counter + 3).getName());
                    counter += 4;
                } else if (gameSession.getInventory().size() - counter == 3) {
                    System.out.println(
                            gameSession.getInventory().get(counter).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(counter + 1).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(counter + 2).getName());
                    counter += 3;
                } else if (gameSession.getInventory().size() - counter == 2) {
                    System.out.println(
                            gameSession.getInventory().get(counter).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(counter + 1).getName());
                    counter += 2;
                } else if (gameSession.getInventory().size() - counter == 1) {
                    System.out.println(
                            gameSession.getInventory().get(counter).getName());
                    counter += 1;
                }

            } while (counter < gameSession.getInventory().size());
        }
    }

    protected static void startEvent(Session gameSession) {
        if (gameSession.getCurrentStatus() == Status.EXPLORING) {
            if (gameSession.getCurrentRoom().getEvent().getClass() == TextPuzzle.class
                    || gameSession.getCurrentRoom().getEvent().getClass() == JugPuzzle.class) {
                gameSession.setCurrentStatus(Status.PUZZLE_SOLVING);
                TextPuzzle textPuzzle = (TextPuzzle) gameSession.getCurrentRoom().getEvent();
                Printer.printFromString(textPuzzle.getQuestion() + "\n");

            } else if (gameSession.getCurrentRoom().getEvent().getClass() == PacificEncounter.class) {
                gameSession.setCurrentStatus(Status.IN_PACIFIC_ENCOUNTER);
                PacificEncounter encounter = (PacificEncounter) gameSession.getCurrentRoom().getEvent();
                Printer.printFromString(encounter.getDescription() + "\n");

            } else if (gameSession.getCurrentRoom().getEvent().getClass() == Enemy.class) {
                gameSession.setCurrentStatus(Status.FIGHTING);
                Fight newFight = new Fight();
                newFight.setOpponent((Enemy) gameSession.getCurrentRoom().getEvent());
                gameSession.setCurrentFight(newFight);
                System.out.println("Inizia la battaglia!");

            }
        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");

    }


}
