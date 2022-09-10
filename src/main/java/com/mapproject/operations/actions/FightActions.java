package com.mapproject.operations.actions;

import com.mapproject.operations.utilities.enums.Status;
import com.mapproject.resources.Fight;
import com.mapproject.resources.Session;
import com.mapproject.resources.events.Enemy;
import com.mapproject.resources.items.Weapon;

public class FightActions {
    protected static void escapeBattle(Session gameSession) {
        if (gameSession.getCurrentRoom().getEvent() != null
                && gameSession.getCurrentRoom().getEvent().getClass() == Enemy.class) { // if there is an enemy event in
                                                                                        // the room
            Enemy enemy = (Enemy) gameSession.getCurrentRoom().getEvent();

            if (enemy.isSkippable()) { // if the player can escape the battle
                System.out.println("Riesci a fuggire dalla battaglia!");
                gameSession.setCurrentStatus(Status.EXPLORING);

                if (gameSession.getCurrentRoomId() != gameSession.getCurrentMap().getEndRoomId()) {
                    gameSession.getCurrentRoom().setEvent(null); // the room is empty now, unless it's a boss room
                }

            } else
                System.out.println("Non funziona!");

        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");

    }

    public static int attack(Session gameSession, String command) {
        if (gameSession.getCurrentRoom().getEvent() != null
                && gameSession.getCurrentRoom().getEvent().getClass() == Enemy.class) {
            // if there is an enemy in the room

            if (gameSession.getCurrentStatus() == Status.FIGHTING) { // if the fight is on

                Weapon chosenWeapon = (Weapon) gameSession.getInventory().stream()
                        .filter(item -> command.equals(item.getName())).findFirst().orElse(null);

                if (chosenWeapon == null) { // weapon not found
                    System.out.println("Non capisco come vuoi attaccare...");
                    return 1;
                }

                gameSession.getCurrentFight().playersTurn(gameSession, chosenWeapon); // player attacks

                if (gameSession.getCurrentFight().getOpponent().isAlive()) {
                    gameSession.getCurrentFight().opponentsTurn(gameSession, chosenWeapon); // enemy attacks

                } else {

                    gameSession.setCurrentStatus(Status.EXPLORING);
                    gameSession.getCurrentRoom().setEvent(null);
                    gameSession.setCurrentFight(null);
                    System.out.println("Il nemico è sconfitto!");
                    gameSession.addBeatenEnemy();

                    gameSession.setHealthPoints(gameSession.getHealthPoints() + 5);
                    System.out.println("Guadagni 5 punti vita!");

                    gameSession.getInventory().stream().filter(item -> item.isUsed()).forEach(item -> {
                        gameSession.removeItemFromInventory(item);
                        System.out.println(item.getNameWithDetArticle() + "svanisce nel nulla!");
                    });
                }

                if (!gameSession.isPlayerAlive()) {
                    System.out.println("Sei stato sconfitto. La tua prova si conclude qui...");
                    return 4;
                } else {
                    return 1;
                }

            } else if (gameSession.getCurrentStatus() == Status.EXPLORING) {
                // start the battle

                gameSession.setCurrentStatus(Status.FIGHTING);
                Fight newFight = new Fight();
                newFight.setOpponent((Enemy) gameSession.getCurrentRoom().getEvent());
                gameSession.setCurrentFight(newFight);
                System.out.println("Inizia lo scontro con: " + gameSession.getCurrentRoom().getEvent().getName() + "!");

                attack(gameSession, command); // now that the fight is on, invoke the method again to get in the
                                              // other if branch
            }
        } else
            System.out.println("Non c'è nessuno da attaccare!");

        return 1;
    }

}
