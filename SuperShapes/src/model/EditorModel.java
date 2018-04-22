package model;

import java.util.List;

import model.entities.Entity;
import model.entities.Player;
import model.tiles.Tile;
import observers.Observer;

public class EditorModel extends Model {

	public EditorModel(Observer v) {
		super(v);
		super.player = new Player("0,0", 5, 10, 5);
		super.player.addObserver(v);
		setRoom(loadRoom("0,0"));
		super.room.addObserver(v);
		super.room.notifyObserver();
	}

	public void addToRoom(String[] lines, int x, int y) {
		for (Tile t : room.getTiles()) {
			if (t.getX() == x && t.getY() == y) {
			}
		}
		room.printEnemies();
		if (lines[0] == "remove")
			room.removeEntity(x, y);
		Entity e = fileManager.parseEntity(lines[0], room, v);
		if (e != null) {
			room.removeEntity(x, y);
			room.addEntity(e);
		}
		Tile tile = fileManager.parseTile(lines[1]);
		if (tile != null) {
			room.swapTile(tile);
		} else {
		}
		room.notifyObserver();
	}

	public void exportRoom() {
		fileManager.exportWorking(room.exportRoom());
	}

	public void changeRoom(String roomId) {
		System.out.println(roomId);
		setRoom(loadRoom(roomId));
		room.addObserver(v);
		room.notifyObserver();
	}

	public void setExit(int index, int coord) {
		room.setExit(index, coord);
	}

	public void setRoomSize(int[] size) {
		room.setSize(size);
	}

	public void exportWorld() {
		fileManager.exportMaster(room.exportRoom());
	}

	public void addRoom(int x, int y) {
		int[] exits = { -1, -1, -1, -1 };
		setRoom(new Room(x, y, 11, 11, exits, player, v));
		fileManager.exportMaster(room.exportRoom());
	}

	public void deleteRoom(int x, int y) {
		if (!(x == 0 && y == 0)) {
			changeRoom("0,0");
			fileManager.removeRoomWorld(x + "," + y);
		}
	}/*

	public void exportEvent(String event) {
		fileManager.exportEvent(room.exportRoom(), event);
	}

	public String[][] getEventMap(int mapCentreX, int mapCentreY, String eventName) {
		return fileManager.getEventMap(mapCentreX, mapCentreY, eventName);
	}

	public void eventRemoveRoom(String eventName, String id) {
		fileManager.eventRemoveRoom(eventName, id);
	}*/	
	public void createEntity(String entity) {
		fileManager.createEntity(entity);
	}

	public List<String> getEntities() {
		return fileManager.getEntities();
	}

	public void createTile(String tile) {
		fileManager.createTile(tile);
	}

	public List<String> getTiles() {
		return fileManager .getTiles();
	}

	public boolean gameExists(String game) {
		return fileManager.gameExists(game);
	}

}
