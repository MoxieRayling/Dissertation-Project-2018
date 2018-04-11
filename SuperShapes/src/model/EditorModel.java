package model;


import observers.Observer;

public class EditorModel extends Model {


	public EditorModel(Observer v) {
		super(v);
		super.player = new Player("0,0", 5, 10, 5);
		super.player.addObserver(v);
		super.room = loadRoom("0,0");
		super.room.addObserver(v);
		super.room.notifyObserver();
	}

	public void addToRoom(String[] lines, int x, int y) {
		for (Tile t : room.getTiles()) {
			if (t.getX() == x && t.getY() == y) {
			}
		}
		room.printEnemies();
		if (lines[0] == "None")
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

	public void changeRoom(String roomId, Boolean resetPos) {
		System.out.println("editor change");
		Room room = loadRoom(roomId);
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

		System.out.println("add room");
		int[] exits = { -1, -1, -1, -1 };
		room = new Room(x, y, 11, 11, exits, player, v);

	}

}
