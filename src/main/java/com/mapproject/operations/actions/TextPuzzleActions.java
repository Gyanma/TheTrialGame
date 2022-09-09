package com.mapproject.operations.actions;

import com.mapproject.operations.inputoutput.Printer;
import com.mapproject.operations.runHandler.Loader;
import com.mapproject.operations.utilities.enums.Status;
import com.mapproject.resources.Session;
import com.mapproject.resources.events.JugPuzzle;
import com.mapproject.resources.events.PacificEncounter;
import com.mapproject.resources.events.TextPuzzle;
import com.mapproject.resources.items.Item;

public class TextPuzzleActions {
    protected static void checkAnswer(Session gameSession, String command) {

        if (gameSession.getCurrentRoom().getEvent() != null &&
                gameSession.getCurrentRoom().getEvent().getClass() == TextPuzzle.class) {

            TextPuzzle textPuzzle = (TextPuzzle) gameSession.getCurrentRoom().getEvent();
            if (textPuzzle.getAnswer().equals(command)) {
                Printer.printFromString(textPuzzle.getCorrectReply());
                Item reward = Loader.loadItem(textPuzzle.getRewardId());

                InventoryHandler.addItem(gameSession, reward);

                gameSession.addSolvedPuzzle();
                gameSession.setCurrentStatus(Status.EXPLORING);
                gameSession.getCurrentRoom().setEvent(null);

            } else {
                if (textPuzzle.getTryAgainReply() == null) {
                    Printer.printFromString(textPuzzle.getIncorrectReply());
                    gameSession.setCurrentStatus(Status.EXPLORING);
                    gameSession.getCurrentRoom().setEvent(null);
                } else {
                    Printer.printFromString(textPuzzle.getTryAgainReply());
                }
            }

        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
    }

    protected static void solveJugPuzzle(Session gameSession, String command) {
        if (gameSession.getCurrentRoom().getEvent() != null &&
                gameSession.getCurrentRoom().getEvent().getClass() == JugPuzzle.class &&
                gameSession.getCurrentStatus() == Status.PUZZLE_SOLVING) { // if there is a puzzle in the room and the
                                                                           // player is solving it

            JugPuzzle jugPuzzle = (JugPuzzle) gameSession.getCurrentRoom().getEvent();

            // pour jugs
            if (command.startsWith("brocca 1")) {

                if (command.endsWith("brocca 2")) {
                    jugPuzzle.pourJug(jugPuzzle.getPlayerJugSet().getJug1(), jugPuzzle.getPlayerJugSet().getJug2());
                    System.out.println("Hai svuotato la brocca 1 nella brocca 2.");

                } else if (command.endsWith("brocca 3")) {
                    jugPuzzle.pourJug(jugPuzzle.getPlayerJugSet().getJug1(), jugPuzzle.getPlayerJugSet().getJug3());
                    System.out.println("Hai svuotato la brocca 1 nella brocca 3.");

                } else {
                    System.out.println("Non capisco quali brocche usare!");
                }

            } else if (command.startsWith("brocca 2")) {

                if (command.endsWith("brocca 1")) {
                    jugPuzzle.pourJug(jugPuzzle.getPlayerJugSet().getJug2(), jugPuzzle.getPlayerJugSet().getJug1());
                    System.out.println("Hai svuotato la brocca 2 nella brocca 1.");

                } else if (command.endsWith("brocca 3")) {
                    jugPuzzle.pourJug(jugPuzzle.getPlayerJugSet().getJug2(), jugPuzzle.getPlayerJugSet().getJug3());
                    System.out.println("Hai svuotato la brocca 2 nella brocca 3.");

                } else {
                    System.out.println("Non capisco quali brocche usare!");
                }

            } else if (command.startsWith("brocca 3")) {

                if (command.endsWith("brocca 1")) {
                    jugPuzzle.pourJug(jugPuzzle.getPlayerJugSet().getJug3(), jugPuzzle.getPlayerJugSet().getJug1());
                    System.out.println("Hai svuotato la brocca 3 nella brocca 1.");

                } else if (command.endsWith("brocca 2")) {
                    jugPuzzle.pourJug(jugPuzzle.getPlayerJugSet().getJug3(), jugPuzzle.getPlayerJugSet().getJug2());
                    System.out.println("Hai svuotato la brocca 3 nella brocca 2.");

                } else {
                    System.out.println("Non capisco quali brocche usare!");
                }

            } else {
                System.out.println("Non capisco quali brocche usare!");
            }

            // check if the puzzle is solved
            if (jugPuzzle.isCorrect()) {

                System.out.println("Ora la brocca 1 contiene " + jugPuzzle.getPlayerJugSet().getJug1().getJugContent()
                        + " decilitri. " + "\t" +
                        "Ora la brocca 2 contiene " + jugPuzzle.getPlayerJugSet().getJug2().getJugContent()
                        + " decilitri." + "\t" +
                        "Ora la brocca 3 contiene " + jugPuzzle.getPlayerJugSet().getJug3().getJugContent()
                        + " decilitri.");

                System.out.println(jugPuzzle.getCorrectReply());
                gameSession.setCurrentStatus(Status.EXPLORING);

                Item reward = Loader.loadItem(jugPuzzle.getRewardId());

                InventoryHandler.addItem(gameSession, reward);

                gameSession.getCurrentRoom().setEvent(null);

            } else {
                System.out.println("Ora la brocca 1 contiene " + jugPuzzle.getPlayerJugSet().getJug1().getJugContent()
                        + " decilitri. " + "\t" +
                        "Ora la brocca 2 contiene " + jugPuzzle.getPlayerJugSet().getJug2().getJugContent()
                        + " decilitri." + "\t" +
                        "Ora la brocca 3 contiene " + jugPuzzle.getPlayerJugSet().getJug3().getJugContent()
                        + " decilitri.");
                gameSession.getCurrentRoom().setEvent(jugPuzzle); // updates the puzzle
            }

        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");

    }

    protected static void leaveEncounter(Session gameSession) {
        if (gameSession.getCurrentRoom().getEvent() != null) {

            if (gameSession.getCurrentStatus() == Status.IN_PACIFIC_ENCOUNTER &&
                    gameSession.getCurrentRoom().getEvent().getClass() == PacificEncounter.class) { // leave a pacific
                                                                                                    // encounter
                PacificEncounter encounter = (PacificEncounter) gameSession.getCurrentRoom().getEvent();
                Printer.printFromString(encounter.getItemNotGivenResponse());
                gameSession.setCurrentStatus(Status.EXPLORING);
                gameSession.getCurrentRoom().setEvent(null);

            } else if (gameSession.getCurrentStatus() == Status.PUZZLE_SOLVING
                    && (gameSession.getCurrentRoom().getEvent().getClass() == JugPuzzle.class
                            || gameSession.getCurrentRoom().getEvent().getClass() == TextPuzzle.class)) { // leave a
                                                                                                          // puzzle
                                                                                                          // event
                TextPuzzle puzzle = (TextPuzzle) gameSession.getCurrentRoom().getEvent();
                Printer.printFromString(puzzle.getSurrenderReply());
                gameSession.setCurrentStatus(Status.EXPLORING);
                gameSession.getCurrentRoom().setEvent(null);

            } else
                System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");

        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");

    }

}
