package com.mapproject.operations.runHandler;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mapproject.operations.utilities.Utilities;
import com.mapproject.resources.GameMap;
import com.mapproject.resources.Room;
import com.mapproject.resources.events.Danger;
import com.mapproject.resources.events.Enemy;
import com.mapproject.resources.events.JugPuzzle;
import com.mapproject.resources.events.PacificEncounter;
import com.mapproject.resources.events.TextPuzzle;
import com.mapproject.resources.events.VisualPuzzle;
import com.mapproject.resources.items.Item;
import com.mapproject.resources.items.Weapon;

public class MapBuilder {

    private final int BOSS_BASE_ID = 2450;
    private final int ENEMIES_BASE_ID = 2400;
    private final int ITEM_BASE_ID = 1000;
    private final int WEAPON_BASE_ID = 1100;
    private final int ROOMNUMBER = 16;
    private final int ROW = 4;
    private final int COLUMN = 4;

    private GameMap map = new GameMap();

    private void initializeMap() {
        map.setEndRoomId(0);
        map.setStartingRoomId(0);

        for (int i = 1; i <= ROOMNUMBER; i++) {
            map.addRoom(i, new Room(i, map.getPhase()));
        }
    }

    private void randomizeDoors() {
        for (int i = 1; i < ROOMNUMBER; i++) { // works on all room except the last one
                                               // since the last one will be already connected.
            Room tempRoom = map.getRoom(i);

            if (i < ROOMNUMBER - ROW) { // room can have a door to the south
                int random = (int) (Math.random() * 5) + 1;
                if (random < 4) {

                    tempRoom.setSouth(map.getRoom(i + ROW));
                    map.getRoom(i + ROW).setNorth(tempRoom);
                }
            }

            if (i % COLUMN != 0) { // room can have a door to the east
                int random = (int) (Math.random() * 5) + 1;
                if (random < 4) {

                    tempRoom.setEast(map.getRoom(i + 1));
                    map.getRoom(i + 1).setWest(tempRoom);
                }
            }
        }
    }

    private void chooseStartAndEndRoom() {
        int start = (int) (Math.random() * ROOMNUMBER) + 1;
        map.setStartingRoomId(map.getRoom(start).getId());

        int end;
        do {
            end = (int) (Math.random() * ROOMNUMBER) + 1;
        } while (end == start);

        map.setEndRoomId(map.getRoom(end).getId());
    }

    private void addBoss() {

        int bossId = map.getPhase() + BOSS_BASE_ID;

        Room tempRoom = map.getRoom(map.getEndRoomId());

        // add the enemy to the room
        tempRoom.setEvent(Loader.loadEnemy(bossId));
        map.addRoom(map.getEndRoomId(), tempRoom);

    }

    private void addTextPuzzles(Map<String, List<Integer>> content) {

        // choose a random puzzle
        Integer chosenPuzzle;
        Integer puzzleId;
        TextPuzzle puzzle;
        Random random = new Random();

        Double[] puzzleIds = content.get("TextPuzzles").toArray(new Double[content.get("TextPuzzles").size()]);

        // randomly select a puzzle from the ones available
        do {
            chosenPuzzle = random.nextInt(content.get("TextPuzzles").size());

            puzzleId = puzzleIds[chosenPuzzle].intValue();
        } while (map.getPhase() == 1 && puzzleId > 2210);

        // load the puzzle

        if (puzzleId == 2205 || puzzleId == 2212
                || puzzleId == 2214) { // if the puzzle is a jug puzzle, load it as a jug puzzle

            puzzle = Loader.loadJugPuzzle(puzzleId);

        } else {
            puzzle = Loader.loadTextPuzzle(puzzleId);
        }

        content.get("TextPuzzles").remove(chosenPuzzle.intValue());

        // choose a random room
        int roomId;
        do {
            roomId = Utilities.selectRoomFromSet(
                    (int) (Math.random() * map.getVisitableRooms().size() + 1), map.getVisitableRooms());

        } while (map.getRoom(roomId).getEvent() != null || map.getStartingRoomId() == roomId);

        if (puzzle.getClass() == JugPuzzle.class) {
        }

        // add the puzzle to the room
        Room tempRoom = map.getRoom(roomId);
        tempRoom.setEvent(puzzle);
        map.addRoom(roomId, tempRoom);

    }

    private void addDanger(Map<String, List<Integer>> content) {

        // choose a random danger
        Integer chosenDanger;
        Integer dangerId;
        Danger danger;
        Random random = new Random();

        Double[] dangerIds = content.get("Dangers").toArray(new Double[content.get("Dangers").size()]);

        // randomly select a danger from the ones available
        chosenDanger = random.nextInt(content.get("Dangers").size());
        dangerId = dangerIds[chosenDanger].intValue();
        content.get("Dangers").remove(chosenDanger.intValue());

        danger = Loader.loadDanger(dangerId);

        // choose a random room
        int roomId;
        do {
            roomId = Utilities.selectRoomFromSet(
                    (int) (Math.random() * map.getVisitableRooms().size() + 1), map.getVisitableRooms());
        } while (map.getRoom(roomId).getEvent() != null || map.getStartingRoomId() == roomId);

        // add the danger to the room
        Room tempRoom = map.getRoom(roomId);
        tempRoom.setEvent(danger);
        map.addRoom(roomId, tempRoom);
    }

    private void addEnemies() {

        // choose a random enemy
        int enemyId;
        Enemy enemy;

        enemyId = (int) (Math.random() * 4) + ENEMIES_BASE_ID + 1; // enemy id is between 2401 and 2404
        enemy = Loader.loadEnemy(enemyId);

        // choose a random room
        int roomId;
        do {
            roomId = Utilities.selectRoomFromSet(
                    (int) (Math.random() * map.getVisitableRooms().size() + 1), map.getVisitableRooms());
        } while (map.getRoom(roomId).getEvent() != null || map.getStartingRoomId() == roomId);

        // add the enemy to the room
        Room tempRoom = map.getRoom(roomId);
        tempRoom.setEvent(enemy);

        map.addRoom(roomId, tempRoom);

    }

    private void addVisualPuzzles(Map<String, List<Integer>> content) {
        // choose a random puzzle
        Integer chosenPuzzle;
        Integer puzzleId;
        VisualPuzzle puzzle;
        Random random = new Random();

        Double[] puzzleIds = content.get("VisualPuzzles").toArray(new Double[content.get("VisualPuzzles").size()]);

        // randomly select a puzzle from the ones available
        chosenPuzzle = random.nextInt(content.get("VisualPuzzles").size());
        puzzleId = puzzleIds[chosenPuzzle].intValue();

        content.get("VisualPuzzles").remove(chosenPuzzle.intValue());

        puzzle = Loader.loadVisualPuzzle(puzzleId);

        // choose a random room
        int roomId;
        do {
            roomId = Utilities.selectRoomFromSet(
                    (int) (Math.random() * map.getVisitableRooms().size() + 1), map.getVisitableRooms());

        } while (map.getRoom(roomId).getEvent() != null || map.getStartingRoomId() == roomId);

        // add the puzzle to the room
        Room tempRoom = map.getRoom(roomId);
        tempRoom.setEvent(puzzle);
        map.addRoom(roomId, tempRoom);
    }

    private void addPacificEncounter(Map<String, List<Integer>> content) {
        // choose a random encounter
        Integer chosenEncounter;
        Integer encounterId;
        PacificEncounter encounter;
        Random random = new Random();

        Double[] encounterIds = content.get("PacificEncounters")
                .toArray(new Double[content.get("PacificEncounters").size()]);

        // randomly select an encounter from the ones available
        chosenEncounter = random.nextInt(content.get("PacificEncounters").size());
        encounterId = encounterIds[chosenEncounter].intValue();
        content.get("PacificEncounters").remove(chosenEncounter.intValue());

        encounter = Loader.loadPacificEncounter(encounterId);

        // choose a random room
        int roomId;
        do {
            roomId = Utilities.selectRoomFromSet(
                    (int) (Math.random() * map.getVisitableRooms().size() + 1), map.getVisitableRooms());

        } while (map.getRoom(roomId).getEvent() != null || map.getStartingRoomId() == roomId);

        // add the encounter to the room
        Room tempRoom = map.getRoom(roomId);
        tempRoom.setEvent(encounter);
        map.addRoom(roomId, tempRoom);

    }

    private void addItems() {
        // choose a random item
        int itemId;
        itemId = (int) (Math.random() * 19) + ITEM_BASE_ID + 1; // item id is between 1001 and 1020
        Item item = Loader.loadItem(itemId);

        // choose a random room
        int roomId;
        do {
            roomId = Utilities.selectRoomFromSet(
                    (int) (Math.random() * map.getVisitableRooms().size() + 1), map.getVisitableRooms());
        } while (map.getRoom(roomId).getItems().size() != 0);
        // add the item to the room
        Room tempRoom = map.getRoom(roomId);
        tempRoom.addItem(item);
        map.addRoom(roomId, tempRoom);

        if (Math.random() < 0.5) {
            itemId = (int) (Math.random() * 19) + ITEM_BASE_ID + 1; // item id is between 1001 and 1020
            item = Loader.loadItem(itemId);

            tempRoom = map.getRoom(roomId);
            tempRoom.addItem(item);
            map.addRoom(roomId, tempRoom);
        }
    }

    private void addWeapon(Map<String, List<Integer>> content) {
        // choose a random weapon
        Integer chosenWeapon;
        Integer weaponId;
        Weapon weapon;
        boolean checkWeapon;
        Random random = new Random();

        Double[] weaponIds = content.get("Weapons").toArray(new Double[content.get("Weapons").size()]);
        // randomly select a weapon from the ones available

        do {
            checkWeapon = false;

            chosenWeapon = random.nextInt(content.get("Weapons").size());

            weaponId = weaponIds[chosenWeapon].intValue();

            if (map.getPhase() == 1 && weaponId > WEAPON_BASE_ID + 6)
                checkWeapon = true;

        } while (checkWeapon);

        weapon = Loader.loadWeapon(weaponId);
        content.get("Weapons").remove(chosenWeapon.intValue());

        // choose a random room
        int roomId;
        do {
            roomId = Utilities.selectRoomFromSet(
                    (int) (Math.random() * map.getVisitableRooms().size() + 1), map.getVisitableRooms());

        } while (map.getRoom(roomId).getItems().contains(weapon) || map.getStartingRoomId() == roomId);

        // add the weapon to the room
        Room tempRoom = map.getRoom(roomId);
        tempRoom.addItem(weapon);
        map.addRoom(roomId, tempRoom);
    }

    private void addWeaponsToStartRoom(Map<String, List<Integer>> content) {
        Integer chosenWeapon;
        Integer weaponId;
        Weapon weapon;
        int roomId = map.getStartingRoomId();
        Random random = new Random();
        boolean checkWeapon;

        Double[] weaponIds = content.get("Weapons").toArray(new Double[content.get("Weapons").size()]);

        do {
            checkWeapon = false;

            chosenWeapon = random.nextInt(content.get("Weapons").size());

            weaponId = weaponIds[chosenWeapon].intValue();

            weapon = Loader.loadWeapon(weaponId);

            if (map.getPhase() == 1 && weaponId > WEAPON_BASE_ID + 6)
                checkWeapon = true;

            if (weapon.getUsage() == 1)
                checkWeapon = true;

        } while (checkWeapon);

        content.get("Weapons").remove(chosenWeapon.intValue());

        // add the weapon to the room
        Room tempRoom = map.getRoom(roomId);
        tempRoom.addItem(weapon);
        map.addRoom(roomId, tempRoom);

        // choose a ranged weapon
        do {
            checkWeapon = false;

            chosenWeapon = random.nextInt(content.get("Weapons").size());

            weaponId = weaponIds[chosenWeapon].intValue();

            weapon = Loader.loadWeapon(weaponId);

            if (map.getPhase() == 1 && weaponId > WEAPON_BASE_ID + 6)
                checkWeapon = true;

            if (weapon.getUsage() == 2)
                checkWeapon = true;

        } while (checkWeapon);

        content.get("Weapons").remove(chosenWeapon.intValue());
        // add the weapon to the room
        tempRoom = map.getRoom(roomId);
        tempRoom.addItem(weapon);
        map.addRoom(roomId, tempRoom);

    }

    private void fillMap(Map<String, List<Integer>> content) {
        // add the boss
        addBoss();

        // choose the number of puzzles to place
        int puzzleNumber;
        if (map.getVisitableRooms().size() <= 12) {
            puzzleNumber = 2;
        } else {
            puzzleNumber = 3;
        }

        // fill the map with puzzles
        for (int i = 0; i < puzzleNumber; i++) {
            addTextPuzzles(content);
        }

        // count how many enemies the map will have
        int enemyNumber = (map.getVisitableRooms().size() - 1) / 2; // 4 enemies for 9 or 10 rooms, 5 enemies for 11 or
                                                                    // 12 rooms and so on.
        // choose if there will be a danger room
        if (map.getVisitableRooms().size() > 10 && map.getPhase() > 1) {
            if (Math.random() < 0.5) {
                addDanger(content);
                enemyNumber--;
            }
        }
        // fill the map with enemies
        for (int i = 0; i < enemyNumber; i++) {
            addEnemies();
        }

        // add a pacific encounter if the map is large enough
        if (map.getVisitableRooms().size() > 10) {
            addPacificEncounter(content);
        }

        // add a visual puzzle if the map is large enough

        //TODO aggiusta fase
        if (map.getVisitableRooms().size() > 12) {
            addVisualPuzzles(content);
        }

        // choose how many rooms will have an item
        int itemRoomsNumber = (map.getVisitableRooms().size() - 1) / 2 + 2; // 6 items for 9 or 10 rooms, 7 items for 11
                                                                            // or 12 rooms and so on.
        // fill the map with items
        for (int i = 0; i < itemRoomsNumber; i++) {
            addItems();
        }

        // add weapons to the starting room
        addWeaponsToStartRoom(content);

        // add an additional weapon
        addWeapon(content);

    }

    public GameMap createMap(int phase, Map<String, List<Integer>> content) {

        do {

            map.setPhase(phase);

            initializeMap();

            randomizeDoors();

            chooseStartAndEndRoom();

            map.setVisitableRooms(Utilities.findVisitableRooms(map.getStartingRoomId(), map.getEndRoomId(), map));
        } while (map.getVisitableRooms() == null);

        fillMap(content);

        return map;
    }

}