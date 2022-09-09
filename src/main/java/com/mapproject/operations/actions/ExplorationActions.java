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

                gameSession.getCurrentRoom().getItems().forEach(item -> {
                    System.out.println("Nella stanza vedi "
                            + (item.getNameWithIndetArticle()) + "!");
                });

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

                gameSession.getCurrentRoom().getItems().stream().filter(item -> command.equals(item.getName()))
                        .forEach(item -> {
                            System.out.println("Hai preso " + item.getNameWithIndetArticle() + "!");
                            gameSession.getInventory().add(item);
                            gameSession.getCurrentRoom().getItems().remove(item);
                        });

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
            int counter = gameSession.getInventory().size();
            int repetition = 0;
            do {
                if (counter >= 4) {
                    System.out.println(
                            gameSession.getInventory().get(repetition).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(repetition + 1).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(repetition + 2).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(repetition + 3).getName());
                    counter -= 4;
                } else if (counter % 4 == 3) {
                    System.out.println(
                            gameSession.getInventory().get(repetition).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(repetition + 1).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(repetition + 2).getName());
                    counter -= 3;
                } else if (counter % 4 == 2) {
                    System.out.println(
                            gameSession.getInventory().get(repetition).getName() + "\t\t\t"
                                    + gameSession.getInventory().get(repetition + 1).getName());
                    counter -= 2;
                } else if (counter % 4 == 1) {
                    System.out.println(
                            gameSession.getInventory().get(repetition).getName());
                    counter -= 1;
                }
                repetition += 4;

            } while (counter > 0);
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
