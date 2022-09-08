package com.mapproject.operations.runHandler;

import java.util.Scanner;

import com.mapproject.operations.actions.Interpreter;
import com.mapproject.operations.actions.InventoryHandler;
import com.mapproject.operations.inputoutput.Parser;
import com.mapproject.operations.inputoutput.Printer;
import com.mapproject.operations.utilities.enums.Status;
import com.mapproject.resources.Session;

public class RunHandler {

    Session gameSession;
    Scanner gameScanner = new Scanner(System.in);
    Interpreter interpreter = new Interpreter();
    Parser parser = new Parser();

    public RunHandler(Session session) {
        this.gameSession = session;
        gameSession.setCurrentRoomId(gameSession.getCurrentMap().getStartingRoomId());
        gameSession.addItemToInventory(Loader.loadItem("mappa"));
        gameSession.addItemToInventory(Loader.loadItem("pergamena magica"));

        InventoryHandler.initSingleUseItems();
    }

    public void runGame(Session newSession) {

        //TODO rimetti il print
        //Printer.printFromTxt("Presentazione");
        //String input = gameScanner.nextLine();
        //while (!input.equals("N") && !input.equals("S")) {
          //          System.out.println("Come dici?");
            //        input = gameScanner.nextLine();
        //}
        //if (input.equals("S")) {
          //  Printer.printFromTxt("Regole");
       //}
        Printer.printFromTxt("Inizio");
        int action = 1;
        String command;
        String parsedCommand;

        System.out.println("\nCosa vuoi fare?");
        do {
            command = gameScanner.nextLine();
            parsedCommand = Parser.parseCommand(command);
            if (!parsedCommand.equals("Chiudi il gioco")) {
                action = Interpreter.operate(parsedCommand, gameSession);
                if (action == 1 && gameSession.getCurrentStatus() != Status.PUZZLE_SOLVING) {
                    System.out.println("\nCosa vuoi fare?");
                } else if (action == 2) {
                    System.out.println("\nNon ho ben capito cosa vuoi fare...");
                }
            } else
                action = 0;

            if (action == 0) {
                System.out.println("Sei sicuro? (S/N)");
                String answer = gameScanner.nextLine();
                while (!answer.equals("S") && !answer.equals("N")) {
                    System.out.println("Non ho capito.");
                    answer = gameScanner.nextLine();
                }
                if (answer.equals("N")) {
                    action = 1;
                    System.out.println("\nCosa vuoi fare?");
                }
            }

            if (action == 3 || action == 4) {
                action = 0;
            }

        } while (action != 0 && gameSession.isPlayerAlive());

        System.out.println("Arrivederci!");
        System.exit(0);
    }

}
