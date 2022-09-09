package com.mapproject.operations.actions;

import com.mapproject.operations.utilities.Utilities;
import com.mapproject.operations.utilities.enums.Status;
import com.mapproject.resources.Session;
import com.mapproject.resources.events.Danger;
import com.mapproject.resources.events.Enemy;
import com.mapproject.resources.events.PacificEncounter;
import com.mapproject.resources.events.VisualPuzzle;

public class Interpreter {

    public Interpreter() {
    }

    /**
     * 
     * 
     * @param command
     * @param gameSession
     * @return returns 1 if the command is understood, 2 if not, 3 if the conclusion
     *         is reached;
     */
    public static int operate(String command, Session gameSession) {

        if (gameSession.getItemHeldInCommand() != null) {
            if (gameSession.getCurrentStatus() == Status.DONATING_ITEM)
                PacificEncounterActions.donateItem(gameSession, command);
            else if (gameSession.isUsingItem())
                InventoryHandler.useItem(gameSession, command);

            return 1;

        }

        // explore room command
        if (command.equals("Esplora stanza")) {
            ExplorationActions.exploreRoom(gameSession);
            return 1;

            // item search command
        } else if (command.startsWith("Cerca item")) {
            ExplorationActions.exploreRoomForItems(gameSession);
            return 1;

            // change room commands
        } else if (command.startsWith("Spostati")) {
            if (!Utilities.isThereAWeapon(gameSession.getInventory())) {
                System.out.println("Non ti conviene avventurarti senza un'arma...");
                return 1;
            } else {
                if (gameSession.getCurrentRoom().getEvent() == null ||
                        gameSession.getCurrentRoom().getEvent().isSkippable()) { // the player can move if there's no
                                                                                 // event
                                                                                 // or if the event is skippable

                    command = command.replace("Spostati ", "");
                    command = command.trim();
                    boolean moved = false;
                    if (command.equals("nord")) {
                        MovementActions.moveToNorth(gameSession);
                        moved = true;
                    } else if (command.equals("sud")) {
                        MovementActions.moveToSouth(gameSession);
                        moved = true;
                    } else if (command.equals("ovest")) {
                        MovementActions.moveToWest(gameSession);
                        moved = true;
                    } else if (command.equals("est")) {
                        MovementActions.moveToEast(gameSession);
                        moved = true;
                    } else
                        return 2;

                    if (moved) {
                        if (gameSession.getCurrentRoom().getEvent() != null &&
                                gameSession.getCurrentRoom().getEvent().getClass() == Danger.class) {
                            // if the player reaches a danger event
                            DangerActions.launchDanger(gameSession);
                            gameSession.addEncounteredDanger();
                            return 1;
                        }
                        if (gameSession.getCurrentRoom().getEvent() != null &&
                                gameSession.getCurrentMap().getEndRoomId() == gameSession.getCurrentRoomId())
                            // if the player reaches the boss room
                            System.out.println("Senti una presenza inquietante... Dev'essere la stanza finale!");
                    }
                } else {
                    // print the reason why the player can't move
                    if (gameSession.getCurrentRoom().getEvent().getClass() == VisualPuzzle.class) {
                        System.out.println("Le porte sono bloccate!");
                    } else if (gameSession.getCurrentRoom().getEvent().getClass() == Danger.class) {
                        System.out.println("Non si scappa!");
                    } else if (gameSession.getCurrentRoom().getEvent().getClass() == Enemy.class) {
                        System.out.println("Un mostro ti sbarra la strada!");
                    }

                }
            }
            return 1;

            // get item commands
        } else if (command.startsWith("Raccogli")) {
            command = command.replace("Raccogli", "");
            command = command.trim();
            ExplorationActions.getItem(gameSession, command);
            return 1;

            // open map command
        } else if (command.startsWith("Apri mappa")) {
            ExplorationActions.drawMap(gameSession);
            return 1;

            // reach an npc command
        } else if (command.startsWith("Raggiungi")) {
            command = command.replace("Raggiungi", "");
            command = command.trim();
            if (gameSession.getCurrentRoom().getEvent() != null) { // if there's an event in the room
                if (command.equals(gameSession.getCurrentRoom().getEvent().getName())) {
                    // if the input is the correct event name
                    ExplorationActions.startEvent(gameSession);
                    return 1;

                } else if (command.equals("meccanismo") &&
                        gameSession.getCurrentRoom().getEvent().getClass() == VisualPuzzle.class) {
                    // if the event is a visual puzzle and the input is "meccanismo"
                    VisualPuzzleActions.startVisualPuzzle(gameSession);
                    return 1;

                } else
                    return 2;

            } else
                return 2;

            // exit map command
        } else if (command.equals("Esci"))
            return MovementActions.changeToNextMap(gameSession);

        // open inventory command
        else if (command.equals("Apri inventario")) {
            ExplorationActions.openInventory(gameSession);
            return 1;

            // use item command
        } else if (command.startsWith("Usa")) {
            command = command.replace("Usa", "");
            command = command.trim();

            if (gameSession.getCurrentStatus() == Status.IN_DANGER
                    && gameSession.getCurrentRoom().getEvent() != null
                    && gameSession.getCurrentRoom().getEvent().getClass() == Danger.class) {
                // if the item can be used to escape the danger event
                DangerActions.checkEscape(gameSession, command);
                return 1;

            } else {
                InventoryHandler.useItem(gameSession, command);
                return 1;
            }
            // examine item command
        } else if (command.startsWith("Esamina")) {
            command = command.replace("Esamina", "");
            command = command.trim();
            InventoryHandler.examineItem(gameSession, command);
            return 1;
            // throw item command
        } else if (command.startsWith("Getta")) {
            command = command.replace("Getta", "");
            command = command.trim();
            InventoryHandler.throwItem(gameSession, command);
            return 1;

            // Event section

            // Pacific Encounter
            // donate item in pacific encounter command
        } else if (command.startsWith("Consegna")) {
            command = command.replace("Consegna", "");
            command = command.trim();
            if (gameSession.getCurrentRoom().getEvent() != null &&
                    gameSession.getCurrentRoom().getEvent().getClass() == PacificEncounter.class) {
                // if the event is a pacific encounter
                PacificEncounterActions.donateItem(gameSession, command);
            } else
                System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
                
            return 1;

            // pray command
        } else if (command.equals("Prega")) {
            if (gameSession.getCurrentRoom().getEvent() != null &&
                    gameSession.getCurrentRoom().getEvent().getClass() == PacificEncounter.class) {
                // if the event is a pacific encounter
                PacificEncounterActions.pray(gameSession);
            } else
                System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");

            return 1;

            // Jug Puzzle
            // pour jug command
        } else if (command.startsWith("Svuota")) {
            command = command.replace("Svuota", "");
            command = command.trim();
            if (command.contains("brocca")) {
                TextPuzzleActions.solveJugPuzzle(gameSession, command);
                return 1;
            } else {
                return 2;
            }

            // leave event or battle command
        } else if (command.equals("Abbandona")) {
            if (gameSession.getCurrentStatus() == Status.FIGHTING) {
                FightActions.escapeBattle(gameSession);
            } else
                TextPuzzleActions.leaveEncounter(gameSession);
            return 1;

            // Battle section
            // attack command
        } else if (command.startsWith("Attacca")) {
            command = command.replace("Attacca ", "");
            command = command.trim();
            return FightActions.attack(gameSession, command);

            // possible response to text puzzle
        } else if (gameSession.getCurrentStatus() == Status.PUZZLE_SOLVING) {
            TextPuzzleActions.checkAnswer(gameSession, command);
            return 1;

            // Unrecognised command
        } else {
            return 2;
        }
    }
}
