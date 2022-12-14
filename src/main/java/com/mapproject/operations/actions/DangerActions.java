package com.mapproject.operations.actions;

import com.mapproject.operations.inputoutput.Printer;
import com.mapproject.operations.runHandler.Loader;
import com.mapproject.operations.utilities.Utilities;
import com.mapproject.operations.utilities.enums.Status;
import com.mapproject.resources.Session;
import com.mapproject.resources.events.Danger;
import com.mapproject.resources.items.Item;

/*
 * The class contains the actions relative to the "Danger" events.
 * 
 */
public class DangerActions {

    protected static void launchDanger(Session gameSession) {
        gameSession.setCurrentStatus(Status.IN_DANGER);
        Danger danger = (Danger) gameSession.getCurrentRoom().getEvent();
        Printer.printFromString(danger.getPresentation());

        class DangerQueue implements Runnable {

            @Override
            public void run() {
                for (int i = 1; i < 5; i++) {

                    // while the event is running, the player might solve the danger
                    // if that happens, the event is concluded
                    if (gameSession.getCurrentStatus() != Status.IN_DANGER) {
                        break;
                    }

                    try {
                        Thread.sleep(danger.getTimeLimit() / 5); // fixed time between a countdown message and the next
                                                                 // one
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // if the player hasn't solved the danger, the message is printed
                    if (gameSession.getCurrentStatus() == Status.IN_DANGER) {
                        System.out.println(danger.getCountdown().get(i));
                    }

                }
                if (gameSession.getCurrentStatus() == Status.IN_DANGER) {
                    try {
                        Thread.sleep(danger.getTimeLimit() / 5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(danger.getCountdown().get(5));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (gameSession.getCurrentStatus() == Status.EXPLORING) { // the status is set to Exploring if the
                                                                          // player has solved the danger
                    Item prize = Loader.loadItem(danger.getPrize());
                    System.out.println("Hai trovato " + prize.getNameWithIndetArticle() + "!");
                    InventoryHandler.addItem(gameSession, prize);
                    gameSession.getCurrentRoom().setEvent(null);

                } else if (gameSession.getCurrentStatus() == Status.IN_DANGER) {
                    System.out.println("Ti risvegli. Chiss?? quanto tempo ?? passato.\n"
                            + "In qualche modo sei ancora tutto intero.");

                    gameSession.getCurrentRoom().setEvent(null);
                    gameSession.setCurrentStatus(Status.EXPLORING);

                    Utilities.placeRandomDebuff(gameSession);

                    System.out.println("Che vuoi fare?");
                }
                
            }
        }

        new Thread(new DangerQueue()).start();
        
    }

    protected static void checkEscape(Session gameSession, String command) {
        Danger danger = (Danger) gameSession.getCurrentRoom().getEvent();

        if (command.equals(Loader.loadItem(danger.getSolution()).getName())
                && gameSession.getInventory().contains(Loader.loadItem(danger.getSolution()))) {
            // check if the command is the correct answer and the player has the item

            Printer.printFromString(danger.getSolved());
            gameSession.addEscapedDanger();

            gameSession.removeItemFromInventory(Loader.loadItem(danger.getSolution())); // the item is used
            gameSession.setCurrentStatus(Status.EXPLORING);
            gameSession.getCurrentRoom().setEvent(null); // the room is empty now

        } else if (!gameSession.getInventory().contains(Loader.loadItem(danger.getSolution()))) {
            System.out.println("Non hai quell'oggetto!");
        } else {
            System.out.println("Non funziona!");
        }
    }

}
