package com.mapproject.operations;

import com.mapproject.resources.Session;
import com.mapproject.resources.items.Item;
import com.mapproject.resources.items.Weapon;

public class EventHandler {
    public static void selectBlessing(Session gameSession) {
        if (gameSession.getHealthPoints() < gameSession.getMaxHealthPoints()) {
            gameSession.setHealthPoints(gameSession.getHealthPoints());
            System.out.println("Ora sei completamente in salute!");
        }

        if (Math.random() < 0.5) {
            if (Math.random() < 0.33) {
                gameSession.setAttackModifier(gameSession.getAttackModifier() + 0.25);
                System.out.println("Ti senti più forte!");
            } else if (Math.random() < 0.66) {
                gameSession.setAccuracyModifier(gameSession.getAccuracyModifier() + 0.25);
                System.out.println("Ti senti più preciso nei colpi!");
            } else {
                gameSession.setAgilityModifier(gameSession.getAgilityModifier() + 0.25);
                System.out.println("Ti senti più agile nel movimento!");
            }
        } else {
            int countWeapon = 0;
            for (Item item : gameSession.getInventory()) {
                if (item.getClass() == Weapon.class) {
                    countWeapon++;
                }
            }
            if (countWeapon > 0) {
                int chosenWeapon = (int) (Math.random() * countWeapon);
                countWeapon = 0;
                for (int i = 0; i < gameSession.getInventory().size(); i++) {
                    if (gameSession.getInventory().get(i).getClass() == Weapon.class) {
                        if (countWeapon == chosenWeapon) {
                            Weapon weapon = (Weapon) gameSession.getInventory().get(i);
                            double randomBuff = Math.random();
                            if (randomBuff < 0.33) {
                                weapon.setDamage(weapon.getDamage() + 5);
                                gameSession.getInventory().set(i, weapon);
                                System.out.println(
                                        "Adesso ti senti più forte quando usi " + weapon.getNameWithDetArticle() + "!");
                            } else if (randomBuff < 0.66) {
                                weapon.setAccuracy(weapon.getAccuracy() + 2.5);
                                gameSession.getInventory().set(i, weapon);
                                System.out.println(
                                        "Adesso sarai più preciso con " + weapon.getNameWithDetArticle() + "!");
                            } else {
                                weapon.setAgility(weapon.getAgility() + 2.5);
                                gameSession.getInventory().set(i, weapon);
                                System.out.println(
                                        "Adesso ti senti più agile quando usi " + weapon.getNameWithDetArticle() + "!");
                            }
                            break;
                        }
                        countWeapon++;
                    }
                }
            }
        }

    }

}
