package com.mapproject.operations.actions;

import java.util.ArrayList;
import java.util.List;

import com.mapproject.operations.inputoutput.Printer;
import com.mapproject.operations.runHandler.Loader;
import com.mapproject.operations.utilities.enums.Status;
import com.mapproject.resources.Session;
import com.mapproject.resources.events.Enemy;
import com.mapproject.resources.items.Item;
import com.mapproject.resources.items.Weapon;

public class InventoryHandler {

    private static final int ITEM_BASE_ID = 1000;
    private static List<String> temporaryItems = new ArrayList<String>();

    public static void useItem(Session gameSession, String command) {
        boolean itemFound = false;
        Item chosenItem = null;
        Weapon chosenWeapon = null;

        // first case: the player has previously chosen an item to use
        // and now he's choosing the target
        String firstItem = "";
        List<String> combinedUseItems = Loader.loadList("combinedUseItems");
        List<String> tomeTypeItems = Loader.loadList("tomeTypeItems");

        if (combinedUseItems.contains(command)) {
            if (gameSession.getItemHeldInCommand() == null) {
                gameSession.setItemHeldInCommand(command);
                System.out.println("Dimmi l'oggetto sul quale vuoi usarlo.");
                return;
            } else {
                firstItem = gameSession.getItemHeldInCommand();
                gameSession.setItemHeldInCommand(null);
            }
        } else if (tomeTypeItems.contains(command)) {
            if (gameSession.getItemHeldInCommand() == null) {
                gameSession.setItemHeldInCommand(command);
                System.out.println("Cosa vuoi cercare?");
                return;
            } else {
                firstItem = gameSession.getItemHeldInCommand();
                gameSession.setItemHeldInCommand(null);
            }
        }

        if (!firstItem.equals("")) {
            switch (firstItem) {
                case "Punta in titanio":
                    if (Loader.loadWeapon(command) != null) {
                        chosenWeapon = Loader.loadWeapon(command);
                        if (chosenWeapon.getUsage() == 1) {
                            if (gameSession.getInventory().contains(chosenWeapon)) {
                                chosenWeapon.setDamage(chosenWeapon.getDamage() + 5);
                                gameSession.getInventory().set(gameSession.getInventory().indexOf(chosenWeapon),
                                        chosenWeapon);
                                gameSession.getInventory().remove(Loader.loadItem("Punta in titanio"));
                                System.out.println("Hai usato la punta in titanio.");
                                System.out.println(
                                        "Ora " + chosenWeapon.getNameWithDetArticle() + " infliggerà più danni.");
                            } else
                                System.out.println("Non hai " + chosenWeapon.getNameWithDetArticle() + " con te.");

                        } else
                            System.out.println(
                                    "Non puoi usare la punta in titanio su " + chosenWeapon.getNameWithDetArticle()
                                            + ".");
                    } else
                        System.out.println("Non capisco di che oggetto parli.");

                    gameSession.setItemHeldInCommand(null);
                    gameSession.setUsingItem(false);
                    return;

                case "Affilatore":
                    if (Loader.loadWeapon(command) != null) {
                        chosenWeapon = Loader.loadWeapon(command);
                        if (chosenWeapon.getUsage() == 2) {
                            if (gameSession.getInventory().contains(chosenWeapon)) {
                                chosenWeapon.setDamage(chosenWeapon.getDamage() + 5);
                                gameSession.getInventory().set(gameSession.getInventory().indexOf(chosenWeapon),
                                        chosenWeapon);
                                gameSession.getInventory().remove(Loader.loadItem("Affilatore"));
                                System.out.println("Hai usato l'affilatore.");
                                System.out.println(
                                        "Ora " + chosenWeapon.getNameWithDetArticle() + " infliggerà più danni.");
                            } else
                                System.out.println("Non hai " + chosenWeapon.getNameWithDetArticle() + " con te.");

                        } else
                            System.out.println("Non puoi usare l'affilatore su " + chosenWeapon.getNameWithDetArticle()
                                    + ".");
                    } else
                        System.out.println("Non capisco di che oggetto parli.");

                    gameSession.setItemHeldInCommand(null);
                    gameSession.setUsingItem(false);
                    return;
                case "Bestiario":
                    if (Loader.loadEnemy(command) != null)
                        Printer.printFromString(Loader.loadEnemy(command).getManualDescription());
                    else
                        System.out.println("Non capisco di che nemico parli.");

                    gameSession.setItemHeldInCommand(null);
                    gameSession.setUsingItem(false);
                    return;

                case "Libro mastro":
                    if (Loader.loadItem(command) != null) {
                        Item tempItem = Loader.loadItem(command);
                        if (tempItem.getManualDescription() != null &&
                                tempItem.getManualDescription() != "") {
                            Printer.printFromString(tempItem.getManualDescription());
                        } else {
                            System.out.println("Non trovi niente sul manuale.");
                        }
                    } else {
                        System.out.println("Non capisco di che oggetto parli.");
                    }

                    gameSession.setItemHeldInCommand(null);
                    gameSession.setUsingItem(false);
                    return;

            }
        }

        // second case: the player is declaring the item they want to use
        chosenItem = gameSession.getInventory().stream().filter(item -> command.equals(item.getName())).findFirst()
                .orElse(null);

        if (chosenItem != null) {
            // single use item case
            if (temporaryItems.contains(chosenItem.getName())) {
                if (chosenItem.isUsed()) {
                    System.out.println("Hai già usato " + chosenItem.getNameWithDetArticle() + "!");

                } else {
                    chosenItem.setUsed(true);
                    System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + ".");
                }
                // other cases
            } else {
                switch (chosenItem.getName()) {
                    case "Punta in titanio":
                        System.out.println("Su che arma vuoi usare la punta in titanio?");
                        gameSession.setItemHeldInCommand("Punta in titanio");
                        gameSession.setUsingItem(true);
                        break;
                    case "Affilatore":
                        System.out.println("Su che arma vuoi usare l'affilatore?");
                        gameSession.setItemHeldInCommand("Affilatore");
                        gameSession.setUsingItem(true);
                        break;
                    case "Bestiario":
                        System.out.println("Che nemico vuoi cercare sul manuale?");
                        gameSession.setItemHeldInCommand("Bestiario");
                        gameSession.setUsingItem(true);
                        break;
                    case "Libro mastro":
                        System.out.println("Che oggetto vuoi cercare sul manuale?");
                        gameSession.setItemHeldInCommand("Libro mastro");
                        gameSession.setUsingItem(true);
                        break;

                    case "Acchiappasogni":
                        for (Integer i : gameSession.getCurrentMap().getVisitableRooms()) {
                            if (gameSession.getCurrentMap().getRoom(i).getEvent() != null &&
                                    gameSession.getCurrentMap().getRoom(i).getEvent()
                                            .getClass() == Enemy.class)
                                gameSession.getCurrentMap().getRoom(i).getEvent().setSkippable(true);
                        }
                        System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + ".");
                        gameSession.removeItemFromInventory(chosenItem);
                        break;

                    case "Vanga":
                        if (!gameSession.getCurrentRoom().wasSpadeUsed()) {
                            gameSession.getCurrentRoom().setWasSpadeUsed(true);
                            System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + ".");
                            if (Math.random() < 0.3) {
                                Item foundItem;
                                do {
                                    foundItem = Loader.loadItem((int) (Math.random() * 19) + ITEM_BASE_ID + 1);
                                } while (gameSession.getCurrentRoom().getItems().contains(foundItem));

                                System.out.println("Hai trovato " + foundItem.getNameWithIndetArticle() + "!");
                                addItem(gameSession, foundItem);
                            } else
                                System.out.println("Purtroppo non hai trovato nulla.");
                            if (Math.random() < 0.1) {
                                gameSession.removeItemFromInventory(chosenItem);
                                System.out.println("Oh, no! La vanga si è spezzata!");
                            }

                            break;
                        } else
                            System.out.println("Hai già provato a cercare tesori in questa stanza!");
                        break;

                    case "Pergamena magica":
                        System.out.println("Hai " + gameSession.getHealthPoints() + " punti salute.");
                        System.out.println(
                                "La tua forza d'attacco è al " + gameSession.getAttackModifier() * 100 + "%.");
                        System.out.println(
                                "La tua agilità è al " + gameSession.getAgilityModifier() * 100 + "%.");
                        System.out.println(
                                "La tua accuratezza è al " + gameSession.getAccuracyModifier() * 100 + "%.");
                        if (gameSession.getCurrentStatus() == Status.FIGHTING &&
                                gameSession.getCurrentFight() != null) {
                            for (String string : gameSession.getCurrentFight().getPlayerDebuff().keySet()) {
                                if (gameSession.getCurrentFight().getPlayerDebuff().get(string) == true) {
                                    System.out.println("Sei sotto effetto dello status: " + string + "!");
                                }
                            }
                            System.out.println("Il nemico ha " + gameSession.getCurrentFight().getOpponent()
                                    .getHealthPoints() + " punti salute.");
                        }
                        break;

                    case "Sacca":
                        gameSession.setInventoryCapacity(gameSession.getInventoryCapacity() + 8);
                        System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + ".");
                        System.out.println("Ora il tuo inventario può contenere "
                                + gameSession.getInventoryCapacity() + " oggetti.");
                        gameSession.removeItemFromInventory(chosenItem);
                        break;

                    case "Armatura":
                        gameSession.setArmorHits(gameSession.getArmorHits() + 10);
                        System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + ".");
                        System.out.println("Ora la tua armatura può difendere da " + gameSession.getArmorHits()
                                + " colpi.");
                        gameSession.removeItemFromInventory(chosenItem);
                        break;

                    case "Calzari magici":
                        gameSession.setAgilityModifier(gameSession.getAgilityModifier() + 0.25);
                        System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + ".");
                        System.out.println("Ora sarà più semplice schivare i colpi!");
                        gameSession.removeItemFromInventory(chosenItem);
                        break;

                    case "Monocolo del cacciatore":
                        gameSession.setAccuracyModifier(gameSession.getAccuracyModifier() + 0.25);
                        System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + "!");
                        System.out.println("Ora sarà più facile colpire!");
                        gameSession.removeItemFromInventory(chosenItem);
                        break;

                    case "Pitture di guerra":
                        gameSession.setAttackModifier(gameSession.getAttackModifier() + 0.25);
                        System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + "!");
                        System.out.println("I tuoi colpi saranno più potenti!");
                        gameSession.removeItemFromInventory(chosenItem);
                        break;

                    case "Bomba fumogena":
                        if (gameSession.getCurrentStatus() == Status.FIGHTING
                                && gameSession.getCurrentRoomId() != gameSession.getCurrentMap()
                                        .getEndRoomId()) {
                            gameSession.setCurrentStatus(Status.EXPLORING);
                            gameSession.getCurrentRoom().setEvent(null);
                            System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + "!");
                            System.out.println("Sei riuscito a fuggire dal combattimento!");
                            gameSession.removeItemFromInventory(chosenItem);
                        } else
                            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora");
                        break;

                    case "Pozione del fulmine":
                        if (gameSession.getCurrentStatus() == Status.FIGHTING) {
                            gameSession.getCurrentFight().addOpponentDebuff("Fulmine");
                            gameSession.getCurrentFight().getOpponent().setHealthPoints(
                                    gameSession.getCurrentFight().getOpponent().getHealthPoints() - 15);
                            gameSession.removeItemFromInventory(chosenItem);
                            System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + "!");
                        } else
                            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora");

                        break;
                    case "Pozione del fuoco":
                        if (gameSession.getCurrentStatus() == Status.FIGHTING) {
                            gameSession.getCurrentFight().addOpponentDebuff("Fuoco");
                            gameSession.getCurrentFight().getOpponent().setHealthPoints(
                                    gameSession.getCurrentFight().getOpponent().getHealthPoints() - 15);
                            gameSession.removeItemFromInventory(chosenItem);
                            System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + "!");
                        } else
                            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora");
                        break;

                    case "Pozione del gelo":
                        if (gameSession.getCurrentStatus() == Status.FIGHTING) {
                            gameSession.getCurrentFight().addOpponentDebuff("Gelo");
                            gameSession.getCurrentFight().getOpponent().setHealthPoints(
                                    gameSession.getCurrentFight().getOpponent().getHealthPoints() - 15);
                            gameSession.removeItemFromInventory(chosenItem);
                            System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + "!");
                        } else
                            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora");
                        break;

                    case "Pozione del veleno":
                        if (gameSession.getCurrentStatus() == Status.FIGHTING) {
                            gameSession.getCurrentFight().addOpponentDebuff("Tossina");
                            gameSession.getCurrentFight().setOpponentPoisonCounter(5);
                            gameSession.removeItemFromInventory(chosenItem);
                            System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + "!");
                        } else
                            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora");
                        break;

                    case "Pozione di cura":
                        if (gameSession.getHealthPoints() * 1.5 > gameSession.getMaxHealthPoints()) {
                            gameSession.setHealthPoints(gameSession.getMaxHealthPoints());
                        } else {
                            gameSession.setHealthPoints((int) (gameSession.getHealthPoints() * 1.5));
                        }
                        System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + "!");
                        System.out.println("Ora hai " + gameSession.getHealthPoints() + " punti vita.");
                        gameSession.removeItemFromInventory(chosenItem);
                        break;

                    case "Libro delle maledizioni":
                        if (gameSession.getCurrentStatus() == Status.FIGHTING) {
                            gameSession.getCurrentFight().addOpponentDebuff("Maledetto");
                            gameSession.getCurrentFight().setCurseCounter(3);
                            gameSession.removeItemFromInventory(chosenItem);
                            System.out.println("Hai usato " + chosenItem.getNameWithDetArticle() + "!");
                        } else
                            System.out.println("C'è un momento e un luogo per ogni cosa, ma non ora");
                        break;

                    default:
                        System.out.println("Non capisco come intendi usarlo...");
                        break;

                }

            }

        }
        if (!itemFound) {
            System.out.println("Non capisco di che oggetto parli...");
        }

    }

    public static void examineItem(Session gameSession, String command) {
        boolean found = false;
        for (Item item : gameSession.getInventory()) {
            if (command.equals(item.getName())) {
                System.out.println(item.getName());
                System.out.println(item.getDescription());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Non ho capito di che oggetto parli.");
        }

    }

    public static void throwItem(Session gameSession, String command) {
        boolean found = false;
        for (Item item : gameSession.getInventory()) {
            if (command.equals(item.getName())) {

                gameSession.removeItemFromInventory(item);
                if (!item.isUsed())
                    gameSession.getCurrentRoom().addItem(item);

                System.out.println("Hai gettato via " + item.getNameWithDetArticle());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Non ho capito di che oggetto parli.");
        }

    }

    public static void initSingleUseItems() {
        temporaryItems = Loader.loadList("temporaryItems");
    }

    public static void addItem(Session gameSession, Item item) {
        if (gameSession.getInventory().contains(item)) {
            System.out.println("Appena metti " + item.getNameWithDetArticle()
                    + " nell'inventario, svanisce nel nulla!");
            System.out.println(
                    "Senti un soffio caldo provenire da dietro di te... è il Dio Sole che ti fa un dono!");
            EventHandler.selectBlessing(gameSession);
        } else
            gameSession.addItemToInventory(item);
    }
}