package com.mapproject.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mapproject.resources.items.Item;
import com.mapproject.operations.runHandler.Loader;
import com.mapproject.operations.utilities.enums.Status;
import com.mapproject.operations.visualHandler.jframes.VisualMap;

public class Session {

    private int maxHealth = 100;

    private List<Item> inventory;

    private int inventoryCapacity = 16;

    private GameMap sessionMapPhase1;

    private GameMap sessionMapPhase2;

    private GameMap sessionMapPhase3;

    private int currentPhase;

    private int currentRoomId;

    private Status currentStatus;

    private int healthPoints;

    private double attackModifier = 1;

    private double agilityModifier = 1;

    private double accuracyModifier = 1;

    private int armorHits = 0;

    private Fight currentFight;

    private int encounteredPuzzlesCount = 0;

    private int solvedPuzzlesCount = 0;

    private int encounteredEnemiesCount = 0;

    private int beatenEnemiesCount = 0;

    private int encounteredDangersCount = 0;

    private int escapedDangersCount = 0;

    private Map<String, List<Integer>> content;

    public Session() {

        content = Loader.loadContent();

        GameMap tempMap;

        tempMap = new GameMap(1, content);
        this.sessionMapPhase1 = tempMap;

        tempMap = new GameMap(2, content);
        this.sessionMapPhase2 = tempMap;

        tempMap = new GameMap(3, content);
        this.sessionMapPhase3 = tempMap;

        this.inventory = new ArrayList<Item>();
        this.currentPhase = 1;
        this.currentStatus = Status.EXPLORING;
        this.healthPoints = maxHealth;

    }

    public void setSessionMap(int mapNumber, GameMap map) {
        if (mapNumber == 1) {
            this.sessionMapPhase1 = map;
        } else if (mapNumber == 2) {
            this.sessionMapPhase2 = map;
        } else if (mapNumber == 3) {
            this.sessionMapPhase3 = map;
        }
    }

    public GameMap getSessionMap(int mapNumber) {
        if (mapNumber == 1) {
            return this.sessionMapPhase1;
        } else if (mapNumber == 2) {
            return this.sessionMapPhase2;
        } else if (mapNumber == 3) {
            return this.sessionMapPhase3;
        }
        return null;
    }

    public GameMap getCurrentMap() {
        return this.getSessionMap(this.currentPhase);
    }

    public Room getCurrentRoom() {
        return this.getCurrentMap().getRoom(this.currentRoomId);
    }

    public void addItemToInventory(Item item) {
        this.inventory.add(item);
    }

    public void removeItemFromInventory(Item item) {
        this.inventory.remove(item);
    }

    public void drawVisualMap(boolean isMystic) {
        VisualMap currentMap = new VisualMap();
        currentMap.main(getCurrentMap(), getCurrentRoomId(), isMystic);
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentRoomId(int currentRoom) {
        this.currentRoomId = currentRoom;
    }

    public int getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public void setHealthPoints(int healthPoints) {
        if (healthPoints > maxHealth) {
            this.healthPoints = maxHealth;
        } else {
            this.healthPoints = healthPoints;
        }
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public boolean isPlayerAlive() {
        if (this.healthPoints > 0)
            return true;
        else
            return false;

    }

    public int getMaxHealthPoints() {
        return maxHealth;
    }

    public void setMaxHealthPoints(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setAttackModifier(double attackModifier) {
        this.attackModifier = attackModifier;
    }

    public double getAttackModifier() {
        return attackModifier;
    }

    public void setAgilityModifier(double agilityModifier) {
        this.agilityModifier = agilityModifier;
    }

    public double getAgilityModifier() {
        return agilityModifier;
    }

    public void setAccuracyModifier(double accuracyModifier) {
        this.accuracyModifier = accuracyModifier;
    }

    public double getAccuracyModifier() {
        return accuracyModifier;
    }

    public int getInventoryCapacity() {
        return inventoryCapacity;
    }

    public void setInventoryCapacity(int inventoryCapacity) {
        this.inventoryCapacity = inventoryCapacity;
    }

    public int getArmorHits() {
        return armorHits;
    }

    public void setArmorHits(int armorHits) {
        this.armorHits = armorHits;
    }

    public Fight getCurrentFight() {
        return currentFight;
    }

    public void setCurrentFight(Fight currentFighting) {
        this.currentFight = currentFighting;
    }

    public void addEncounteredPuzzle() {
        encounteredPuzzlesCount++;
    }

    public void addSolvedPuzzle() {
        solvedPuzzlesCount++;
    }

    public int getEncounteredPuzzlesCount() {
        return encounteredPuzzlesCount;
    }

    public int getSolvedPuzzlesCount() {
        return solvedPuzzlesCount;
    }

    public void addEncounteredEnemy() {
        encounteredEnemiesCount++;
    }

    public void addBeatenEnemy() {
        beatenEnemiesCount++;
    }

    public int getEncounteredEnemiesCount() {
        return encounteredEnemiesCount;
    }

    public int getBeatenEnemiesCount() {
        return beatenEnemiesCount;
    }

    public void addEncounteredDanger() {
        encounteredDangersCount++;
    }

    public void addEscapedDanger() {
        escapedDangersCount++;
    }

    public int getEncounteredDangersCount() {
        return encounteredDangersCount;
    }

    public int getEscapedDangersCount() {
        return escapedDangersCount;
    }

    public void printScore() {
        int puzzleScore = encounteredPuzzlesCount - solvedPuzzlesCount;

        puzzleScore = 10 - puzzleScore/2;

        int enemyScore = encounteredEnemiesCount - beatenEnemiesCount;

        enemyScore = 10 - enemyScore/2;

        int dangerScore = encounteredDangersCount - escapedDangersCount;

        dangerScore = 10 - dangerScore;

        System.out.println("Voto sull'onore: " + puzzleScore);
        System.out.println("Voto sull'ingegno: " + dangerScore);
        System.out.println("Voto sul valore: " + enemyScore);
        



    }

}
