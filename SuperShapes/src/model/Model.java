package model;

import java.awt.event.ActionEvent;
import java.util.List;
import controller.Constants;
import model.entities.Entity;
import model.entities.Ghost;
import model.entities.Player;
import model.entities.Shot;
import model.tiles.Coin;
import model.tiles.EmptyTile;
import model.tiles.Hole;
import model.tiles.Key;
import model.tiles.Lock;
import model.tiles.Slide;
import model.tiles.Teleport;
import model.tiles.Tile;
import observers.Observer;
import observers.Subject;

public class Model implements Subject {
	protected Room room;
	protected Observer v;
	protected Player player;
	private String roomId;
	private Boolean input = true;
	private String mode = "";
	private int textLength = 50;
	protected FileManager fileManager;
	private boolean endTurn = false;

	public Model(Observer v) {
		this.v = v;
		fileManager = new FileManager();
		mode = "game";
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Boolean getInput() {
		return input;
	}

	public void setInput(Boolean input) {
		this.input = input;
		notifyObserver();
	}

	public void loadGame() {
		fileManager.overWriteWorking();
		String[] saveData = fileManager.getSaveData().split(":");
		changeRoom(saveData[0], false);
		int x = 0;
		int y = 0;
		try {
			x = Integer.parseInt(saveData[1].split(",")[0]);
			y = Integer.parseInt(saveData[1].split(",")[1]);
			player.setRespawnx(Integer.parseInt(saveData[2].split(",")[0]));
			player.setRespawny(Integer.parseInt(saveData[2].split(",")[1]));
			room.setClock(Integer.parseInt(saveData[3]));
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		player.setTeleport(true);
		player.setLoc(x, y);
		player.setTeleport(false);
		player.setLives(5);
	}

	protected Room loadRoom(String roomId) {
		int[] coords = fileManager.idToCoords(roomId);
		Room result = fileManager.makeRoom(coords[0], coords[1], player, v);
		return result;
	}

	public boolean ghostCheck() {
		for (Entity e : room.getEnemies()) {
			if (input && e instanceof Ghost && ((Ghost) e).getPause() > 0 && player.getX() == e.getX()
					&& player.getY() == e.getY() && player.getFly() == 0) {
				return true;
			}
		}
		return false;
	}

	public void checkShield() {
		for (Entity e : room.getEnemies()) {
			if (player.getShield() && (e instanceof Ghost || e instanceof Shot)
					&& (player.getX() == e.getX() && player.getY() == e.getY()
							|| player.getX() == e.getPX() && player.getY() == e.getPY() && player.getPX() == e.getX()
									&& player.getPY() == e.getY())) {
				player.breakShield();
				room.removeEntity(e);
			}
		}
		room.emptyTrash();
	}

	public void checkForDeath() {
		for (Entity e : room.getEnemies()) {
			if (e.getDeadly() && !player.getDead() && player.getFly() <= 0
					&& (player.getX() == e.getX() && player.getY() == e.getY()
							|| player.getX() == e.getPX() && player.getY() == e.getPY() && player.getPX() == e.getX()
									&& player.getPY() == e.getY())) {

				player.die();
				break;
			}
		}
		if (player.getLives() == 0) {
			this.resetRoom();
		}
	}

	public void setRoom(Room room) {
		this.room = room;
		this.room.notifyObserver();
	}

	public String getAdjRoomId(char direction) {
		String result = "";
		switch (direction) {
		case 'N':
			result = room.getX() + "," + (room.getY() - 1);
			break;
		case 'E':
			result = (room.getX() + 1) + "," + room.getY();
			break;
		case 'S':
			result = room.getX() + "," + (room.getY() + 1);
			break;
		case 'W':
			result = (room.getX() - 1) + "," + room.getY();
			break;
		default:
			break;
		}
		return result;

	}

	public void step(char direction) {
		if (ghostCheck()) {
			room.moveEnemies();
			endTurn = true;
			return;
		}
		int x = player.getX();
		int y = player.getY();
		Boolean pMoved = false;
		if (tryExit(direction, x, y)) {
		} else if (tryMove(direction, x, y)) {
			pMoved = true;
		}
		tryUnlock(direction, player.getX(), player.getY());
		if (pMoved) {
			room.updatePath(player.getX(), player.getY());
			room.moveEnemies();
			if (player.getPause() <= 0)
				endTurn = true;
			player.step();
		}
	}

	private Boolean tryExit(char direction, int x, int y) {
		if (x == room.getxLength() - 1 && y == room.getExits()[1] && direction == 'E'
				&& fileManager.getRoomData(room.getX() + 1, room.getY()) != "") {
			changeRoom(getAdjRoomId('E'), true);
			return true;
		} else if (x == 0 && y == room.getExits()[3] && direction == 'W'
				&& fileManager.getRoomData(room.getX() - 1, room.getY()) != "") {
			changeRoom(getAdjRoomId('W'), true);
			return true;
		} else if (x == room.getExits()[0] && y == 0 && direction == 'N'
				&& fileManager.getRoomData(room.getX(), room.getY() - 1) != "") {
			changeRoom(getAdjRoomId('N'), true);
			return true;
		} else if (x == room.getExits()[2] && y == room.getyLength() - 1 && direction == 'S'
				&& fileManager.getRoomData(room.getX(), room.getY() + 1) != "") {
			changeRoom(getAdjRoomId('S'), true);
			return true;
		}
		return false;
	}

	private Boolean tryMove(char direction, int x, int y) {
		if (direction == 'N' && y - 1 >= 0 && room.getTile(x, y - 1).getTrav()) {
			player.setLoc(x, y - 1);
			return true;
		} else if (direction == 'E' && x + 1 <= room.getxLength() - 1 && room.getTile(x + 1, y).getTrav()) {
			player.setLoc(x + 1, y);
			return true;
		} else if (direction == 'S' && y + 1 <= room.getyLength() - 1 && room.getTile(x, y + 1).getTrav()) {
			player.setLoc(x, y + 1);
			return true;
		} else if (direction == 'W' && x - 1 >= 0 && room.getTile(x - 1, y).getTrav()) {
			player.setLoc(x - 1, y);
			return true;
		}
		return false;
	}

	private void tryUnlock(char direction, int x, int y) {
		if (direction == 'N' && y - 1 >= 0 && room.getTile(x, y - 1) instanceof Lock) {
			if (player.hasKey(((Lock) room.getTile(x, y - 1)).getKey())) {
				room.swapTile(new EmptyTile(x, y - 1));
			}
		} else if (direction == 'E' && x + 1 <= room.getxLength() - 1 && room.getTile(x + 1, y) instanceof Lock) {
			if (player.hasKey(((Lock) room.getTile(x + 1, y)).getKey())) {
				room.swapTile(new EmptyTile(x + 1, y));
			}
		} else if (direction == 'S' && y + 1 <= room.getyLength() - 1 && room.getTile(x, y + 1) instanceof Lock) {
			if (player.hasKey(((Lock) room.getTile(x, y + 1)).getKey())) {
				room.swapTile(new EmptyTile(x, y + 1));
			}
		} else if (direction == 'W' && x - 1 >= 0 && room.getTile(x - 1, y) instanceof Lock) {
			if (player.hasKey(((Lock) room.getTile(x - 1, y)).getKey())) {
				room.swapTile(new EmptyTile(x - 1, y));
			}
		}
	}

	public void endTurn() {
		if (endTurn) {
			checkShield();
			checkForDeath();
			checkTile(room.getTile(player));
			room.incClock();
			room.setText(room.getTile(player).getText().replaceAll("_", " "));
			notifyObserver();
			endTurn = false;
		}
	}

	private void checkTile(Tile t) {
		if (t instanceof Slide) {
			tryMove(((Slide) t).getDirection(), player.getX(), player.getY());
		} else if (t instanceof Teleport) {
			player.setTeleport(true);
			player.setLoc(((Teleport) t).getXTele(), ((Teleport) t).getYTele());
			player.setTeleport(false);
		} else if (t instanceof Hole) {
			player.die();
		} else if (t instanceof Key) {
			player.addKey(((Key) t).getKey());
			room.swapTile(new EmptyTile(player.getX(), player.getY()));
			fileManager.exportWorking(room.exportRoom());
		} else if (t instanceof Coin) {
			player.addCoin();
			room.swapTile(new EmptyTile(player.getX(), player.getY()));
			fileManager.exportWorking(room.exportRoom());
		}

	}

	public void resetRoom() {
		player.setDead(false);
		player.setLoc(player.getRespawnx(), player.getRespawny());
		room = this.loadRoom(room.getId());
		notifyObserver();
	}

	public void changeRoom(String roomId, Boolean resetPos) {
		Room temp = loadRoom(roomId);
		int x = room.getX() - temp.getX();
		int y = room.getY() - temp.getY();
		setRoom(temp);
		player.setTeleport(true);
		if (resetPos) {
			resetPos(x, y);
		}
		player.setTeleport(false);
		player.setRoomId(room.getId());
		room.updatePath(player.getX(), player.getY());
		notifyObserver();
	}

	private void resetPos(int x, int y) {
		if (Math.abs(x) >= Math.abs(y)) {
			if (x > 0) {
				if (room.getExits()[1] == -1) {
					player.setLoc(room.getxLength() - 1, room.getyLength() / 2);
					player.setRespawnx(0);
					player.setRespawny(room.getyLength() / 2);
				} else {
					player.setLoc(room.getxLength() - 1, room.getExits()[1]);
					player.setRespawnx(room.getxLength() - 1);
					player.setRespawny(room.getExits()[1]);
				}
			} else {
				if (room.getExits()[3] == -1) {
					player.setLoc(0, room.getyLength() / 2);
					player.setRespawnx(room.getxLength() - 1);
					player.setRespawny(room.getyLength() / 2);
				} else {
					player.setLoc(0, room.getExits()[3]);
					player.setRespawnx(0);
					player.setRespawny(room.getExits()[3]);
				}
			}
		} else {
			if (y > 0) {
				if (room.getExits()[2] == -1) {
					player.setLoc(room.getxLength() / 2, room.getyLength() - 1);
					player.setRespawnx(room.getxLength() / 2);
					player.setRespawny(room.getyLength() - 1);
				} else {
					player.setLoc(room.getExits()[2], room.getyLength() - 1);
					player.setRespawnx(room.getExits()[2]);
					player.setRespawny(room.getyLength() - 1);
				}
			} else {
				if (room.getExits()[0] == -1) {
					player.setLoc(room.getxLength() / 2, 0);
					player.setRespawnx(room.getxLength());
					player.setRespawny(0);
				} else {
					player.setLoc(room.getExits()[0], 0);
					player.setRespawnx(room.getExits()[0]);
					player.setRespawny(0);
				}
			}
		}
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
		notifyObserver();
	}

	@Override
	public void addObserver(Observer o) {
		this.v = o;
		notifyObserver();
	}

	@Override
	public void removeObserver(Observer o) {
		this.v = null;
	}

	@Override
	public void notifyObserver() {
		v.Update(new ActionEvent(this, 0, null));
	}

	public void notifyAllObservers() {
		room.notifyObserver();
	}

	public void fly() {
		player.fly();
	}

	public void rewind() {
		player.rewind();
	}

	public void pauseTime() {
		player.pauseTime();
	}

	public void shield() {
		player.shield();
	}

	public void input(int i) {
		switch (i) {
		case 37:
			step('W');
			break;
		case 38:
			step('N');
			break;
		case 39:
			step('E');
			break;
		case 40:
			step('S');
			break;
		case 70:
			fly();
			break;
		case 82:
			rewind();
			break;
		case 80:
			pauseTime();
			break;
		case 83:
			shield();
			break;
		default:
			break;

		}

	}

	public void newSaveData() {
		fileManager.newSaveData();
	}

	public String[][] getMap(int x, int y) {
		return fileManager.getMap(x, y);
	}

	public int getX() {
		return room.getX();
	}

	public int getY() {
		return room.getY();
	}

	public void makeNewDir() {
		fileManager.makeNewDir();
	}

	public void saveGame() {
		fileManager.saveGame(room, player.toString(), room.getClock());
	}

	public void makeNewSave() {
		fileManager.makeNewSave();
	}

	public void setTextRead() {
		room.setText("");
		room.getTile(player).setTextRead(true);
	}
}
