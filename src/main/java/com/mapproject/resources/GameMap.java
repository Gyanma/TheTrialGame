package com.mapproject.resources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mapproject.operations.MapBuilder;

/*
 *  GameMap class
 *  This class stores a map of the level;
 *  the map has a phase and a fixed number of rooms;
 *  the class also keeps track of the start room, the end room and the set of room that can be reached through the corridors.
 *  
 * 
 *
 */

public class GameMap {
    private int phase;
    private HashMap<Integer, Room> map = new HashMap<>(16);
    private int startingRoomId;
    private int endRoomId;
    private Set<Integer> visitableRooms = new HashSet<>();



    public GameMap() {
        this.phase = 0;
        this.startingRoomId = 0;
        this.endRoomId = 0;
        this.map = new HashMap<>(16);
        this.visitableRooms = new HashSet<>();
    }

    public GameMap(int phase, Map<String, List<Integer>> content) {
        this.phase = phase;
        MapBuilder mapBuilder = new MapBuilder();
        GameMap tempMap = mapBuilder.createMap(phase, content);

        this.map = tempMap.getMap();
        this.startingRoomId = tempMap.getStartingRoomId();
        this.endRoomId = tempMap.getEndRoomId();
        this.visitableRooms = tempMap.getVisitableRooms();
    }

    public int getPhase() {
        return phase;
    }

    public int getStartingRoomId() {
        return startingRoomId;
    }

    public int getEndRoomId() {
        return endRoomId;
    }

    public HashMap<Integer, Room> getMap() {
        return map;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void setStartingRoomId(int startingRoomId) {
        this.startingRoomId = startingRoomId;
    }

    public void setEndRoomId(int endRoomId) {
        this.endRoomId = endRoomId;
    }

    public void setMap(HashMap<Integer, Room> map) {
        this.map = map;
    }

    public void addRoom(int id, Room room) {
        this.map.put(id, room);
    }

    public Room getRoom(int id) {
        return this.map.get(id);
    }

    public Set<Integer> getVisitableRooms() {
        return visitableRooms;
    }

    public void setVisitableRooms(Set<Integer> visitableRooms) {
        this.visitableRooms = visitableRooms;
    }

}
