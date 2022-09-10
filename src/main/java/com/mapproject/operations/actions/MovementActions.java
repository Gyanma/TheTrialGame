package com.mapproject.operations.actions;

import com.mapproject.operations.inputoutput.Printer;
import com.mapproject.operations.utilities.enums.Status;
import com.mapproject.resources.Session;

public class MovementActions {

    
    protected static boolean moveToWest(Session gameSession) {
        if (gameSession.getCurrentStatus() == Status.EXPLORING) {
            int currentRoomId = gameSession.getCurrentRoomId();
            if (gameSession.getCurrentMap().getRoom(currentRoomId).getWest() != null) {
                gameSession.setCurrentRoomId(
                        gameSession.getCurrentMap().getRoom(currentRoomId).getWest().getId());
                System.out.println("Ti sposti verso la stanza a ovest.");
                return true;
            } else {
                System.out.println("Non ci sono stanze a ovest!");
            }
        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
        return false;
    }

    protected static boolean moveToEast(Session gameSession) {
        if (gameSession.getCurrentStatus() == Status.EXPLORING) {
            int currentRoomId = gameSession.getCurrentRoomId();
            if (gameSession.getCurrentMap().getRoom(currentRoomId).getEast() != null) {
                gameSession.setCurrentRoomId(
                        gameSession.getCurrentMap().getRoom(currentRoomId).getEast().getId());
                System.out.println("Ti sposti verso la stanza a est.");
                return true;
            } else {
                System.out.println("Non ci sono stanze a est!");
            }
        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
        return false;
    }

    protected static boolean moveToSouth(Session gameSession) {
        if (gameSession.getCurrentStatus() == Status.EXPLORING) {
            int currentRoomId = gameSession.getCurrentRoomId();
            if (gameSession.getCurrentMap().getRoom(currentRoomId).getSouth() != null) {
                gameSession.setCurrentRoomId(
                        gameSession.getCurrentMap().getRoom(currentRoomId).getSouth().getId());
                System.out.println("Ti sposti verso la stanza a sud.");
                return true;
            } else {
                System.out.println("Non ci sono stanze a sud!");
            }
        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
        return false;
    }

    protected static boolean moveToNorth(Session gameSession) {
        if (gameSession.getCurrentStatus() == Status.EXPLORING) {
            int currentRoomId = gameSession.getCurrentRoomId();
            if (gameSession.getCurrentMap().getRoom(currentRoomId).getNorth() != null) {
                gameSession.setCurrentRoomId(
                        gameSession.getCurrentMap().getRoom(currentRoomId).getNorth().getId());
                System.out.println("Ti sposti verso la stanza a nord.");
                return true;
            } else {
                System.out.println("Non ci sono stanze a nord!");
            }
        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
        return false;

    }

    protected static int changeToNextMap(Session gameSession) {
        if (gameSession.getCurrentStatus() == Status.EXPLORING &&
                gameSession.getCurrentRoom().getId() == gameSession.getCurrentMap().getEndRoomId()) {
            if (gameSession.getCurrentRoom().getEvent() == null) {
                switch (gameSession.getCurrentPhase()) {
                    case 1:
                        gameSession.setCurrentPhase(2);
                        gameSession.setCurrentRoomId(gameSession.getSessionMap(2).getStartingRoomId());
                        gameSession.setMaxHealthPoints(gameSession.getMaxHealthPoints() + 20);
                        gameSession.setHealthPoints(gameSession.getMaxHealthPoints());
                        System.out.println("Hai superato il primo labirinto!");
                        System.out.println("La tua salute è completamente rigenerata!");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Hai raggiunto la prima stanza del secondo labirinto! Buona fortuna!");
                        return 1;
                    case 2:
                        gameSession.setCurrentPhase(3);
                        gameSession.setCurrentRoomId(gameSession.getSessionMap(3).getStartingRoomId());
                        gameSession.setMaxHealthPoints(gameSession.getMaxHealthPoints() + 20);
                        gameSession.setHealthPoints(gameSession.getMaxHealthPoints());
                        System.out.println("Hai superato il secondo labirinto!");
                        System.out.println("La tua salute è completamente rigenerata!");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Hai raggiunto la prima stanza del terzo labirinto! Buona fortuna!");

                        return 1;
                    case 3:
                        Printer.printFromTxt("Finale");
                        return 3;
                    default:
                        return 1;
                }
            } else
                System.out.println("Devi ancora sconfiggere il boss!");
        } else
            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora.");
        return 1;
    }



}
