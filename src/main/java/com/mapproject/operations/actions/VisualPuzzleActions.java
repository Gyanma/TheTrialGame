package com.mapproject.operations.actions;

import com.mapproject.operations.inputoutput.Printer;
import com.mapproject.operations.visualHandler.VisualHandler;
import com.mapproject.resources.Session;
import com.mapproject.resources.events.VisualPuzzle;

public class VisualPuzzleActions {
    protected static void startVisualPuzzle(Session gameSession) {
        try {
            VisualPuzzle visualPuzzle = (VisualPuzzle) gameSession.getCurrentRoom().getEvent();
            Printer.printFromString(visualPuzzle.getDescription());
            int result = VisualHandler.launchVisual(gameSession.getCurrentRoom().getEvent().getEventId());
            if (result == 1) {
                gameSession.getCurrentRoom().setEvent(null);
                System.out
                        .println(
                                "Senti i meccanismi che si attivano dietro i muri. Adesso le porte si possono aprire!");
            } else if (result == 0) {
                System.out.println("Questo enigma è davvero ostico... bisogna pensarci meglio.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
