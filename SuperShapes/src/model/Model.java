package model;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import controller.Constants;
import observers.Observer;
import observers.Subject;

public class Model implements Constants, Subject {
	protected Room room;
	private Observer v;
	protected Player player;
	private String roomId;
	private int clock = 0;
	private Boolean input = true;
	private int enemyCount = 0;
	private String checkpointId = "0,0";
	private Boolean next = false;
	private String exits = "";
	private String text = "";
	private String textToShow = "";
	private String mode = "";
	private int textLength = 50;
	protected FileManager fileManager;

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

	public void setClock(int clock) {
		this.clock = clock;
		notifyObserver();
	}

	public int getClock() {
		return clock;
	}

	public void loadGame() {
		List<String> lines = fileManager.getSaveData();
		changeRoom(lines.get(0), false);
		int x = 0;
		int y = 0;
		try {
			x = Integer.parseInt(lines.get(1));
			y = Integer.parseInt(lines.get(2));
			setClock(Integer.parseInt(lines.get(3)));
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		player.setTeleport(true);
		player.setLoc(x, y);
		player.setTeleport(false);
		player.setLives(5);
	}

	protected Room loadRoom(String roomId) {
		String[] roomCoords = roomId.split(",");
		int x = 0;
		int y = 0;
		try {
			x = Integer.parseInt(roomCoords[0]);
			y = Integer.parseInt(roomCoords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		Room result = makeRoom(fileManager.getRoomData(x, y));
		if (result == null)
			result = new Room(x, y, exits, player, false, v);
		return result;
	}

	private Room makeRoom(List<String> lines) {
		List<Entity> enemies = new ArrayList<Entity>();
		int x = 0;
		int y = 0;
		Room r = null;
		if (!lines.isEmpty()) {
			for (String l : lines) {
				String[] params = l.split(" ");
				if (params[0].equalsIgnoreCase("room")) {
					r = fileManager.parseRoom(params, player);
					r.addObserver(v);
					x = r.getX();
					y = r.getY();
				} else if (params[0].startsWith("tiles")) {
					r.setTiles(fileManager.parseTiles(params));
				} else if (params[0].startsWith("block")) {
					enemies.add(fileManager.parseBlock(params, x, y, getEnemyCount()));
				} else if (params[0].startsWith("ghost")) {
					enemies.add(fileManager.parseGhost(params, x, y, getEnemyCount()));
				} else if (params[0].startsWith("turret")) {
					enemies.add(fileManager.parseTurret(params, x, y, r, getEnemyCount()));
				} else if (params[0].startsWith("snake")) {
					enemies.add(fileManager.parseSnake(params, x, y, r, getEnemyCount(), v));
				}
			}
			r.setEnemies(enemies);
			exits = fileManager.getExits(r.getX(), r.getY());
		}
		return r;
	}

	public boolean ghostCheck() {
		for (Entity e : room.getEnemies()) {
			if (input && e instanceof Ghost && ((Ghost) e).getPause() > 0 && player.getX() == e.getX()
					&& player.getY() == e.getY()) {
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
				player.setDead(true);
				break;
			}
		}
		if (player.getLives() == 0) {
			loadGame();
		}
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
			endTurn();
			return;
		}
		int x = player.getX();
		int y = player.getY();
		Boolean pMoved = false;

		if (tryExit(direction, x, y)) {
		} else if (player.getFly() > 0 && move(direction, player.getX(), player.getY())) {
			pMoved = true;
		} else if (tryMove(direction, x, y)) {
			pMoved = true;
		}
		if (pMoved) {
			if (player.getPause() <= 0)
				endTurn();
			player.step();
		}
	}

	private Boolean tryExit(char direction, int x, int y) {
		if (x == 10 && y == 5 && direction == 'E' && room.checkExits('E')) {
			changeRoom(getAdjRoomId('E'), true);
			return true;
		} else if (x == 0 && y == 5 && direction == 'W' && room.checkExits('W')) {
			changeRoom(getAdjRoomId('W'), true);
			return true;
		} else if (x == 5 && y == 0 && direction == 'N' && room.checkExits('N')) {
			changeRoom(getAdjRoomId('N'), true);
			return true;
		} else if (x == 5 && y == 10 && direction == 'S' && room.checkExits('S')) {
			changeRoom(getAdjRoomId('S'), true);
			return true;
		}
		return false;
	}

	private Boolean tryMove(char direction, int x, int y) {
		if (direction == 'N' && y - 1 >= 0 && room.getTile(x, y - 1).getTrav()) {
			player.setLoc(x, y - 1);
			return true;
		} else if (direction == 'E' && x + 1 <= 10 && room.getTile(x + 1, y).getTrav()) {
			player.setLoc(x + 1, y);
			return true;
		} else if (direction == 'S' && y + 1 <= 10 && room.getTile(x, y + 1).getTrav()) {
			player.setLoc(x, y + 1);
			return true;
		} else if (direction == 'W' && x - 1 >= 0 && room.getTile(x - 1, y).getTrav()) {
			player.setLoc(x - 1, y);
			return true;
		}
		return false;
	}

	private Boolean move(char direction, int x, int y) {
		if (direction == 'N' && y - 1 >= 0) {
			player.setLoc(x, y - 1);
			return true;
		} else if (direction == 'E' && x + 1 <= 10) {
			player.setLoc(x + 1, y);
			return true;
		} else if (direction == 'S' && y + 1 <= 10) {
			player.setLoc(x, y + 1);
			return true;
		} else if (direction == 'W' && x - 1 >= 0) {
			player.setLoc(x - 1, y);
			return true;
		}
		return false;
	}

	public void endTurn() {
		room.updatePath(player.getX(), player.getY());
		room.moveEnemies();
		checkShield();
		checkForDeath();
		checkTile();
		clock++;
		this.setText(room.getTile(player).getText());
		notifyObserver();
	}

	private void checkTile() {
		for (Tile t : room.getTiles()) {
			if (t.getX() == player.getX() && t.getY() == player.getY()) {
				if (t instanceof Slide) {
					tryMove(((Slide) t).getDirection(), player.getX(), player.getY());
				}
			}
		}
	}

	private void next() {
	}

	public void resetRoom() {
		player.setDead(false);
		room = loadRoom(room.getId());
		switch (player.getLastEntrance()) {
		case 'N':
			player.setLoc(5, 0);
			break;
		case 'E':
			player.setLoc(10, 5);
			break;
		case 'S':
			player.setLoc(5, 10);
			break;
		case 'W':
			player.setLoc(0, 5);
			break;
		default:
			break;
		}
		room.addObserver(v);
		player.resetPos();
		room.notifyObserver();
		notifyObserver();
	}

	public void changeRoom(String roomId, Boolean resetPos) {
		Room temp = loadRoom(roomId);
		int x = room.getX() - temp.getX();
		int y = room.getY() - temp.getY();
		room = temp;
		if (Math.abs(x) >= Math.abs(y) && resetPos) {
			if (x > 0) {
				player.setLoc(10, 5);
			} else {
				player.setLoc(0, 5);
			}
		} else {
			if (y > 0) {
				player.setLoc(5, 10);
			} else {
				player.setLoc(5, 0);
			}
		}
		room.addObserver(v);
		player.setRoomId(room.getId());
		if (room.getCheckpoint()) {
			player.setCheckpoint(room.getId());
			checkpointId = room.getId();
			fileManager.saveGame(checkpointId, player, clock);
		}
		room.updatePath(player.getX(), player.getY());
		notifyObserver();

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

	public void setNext(Boolean next) {
		if (this.next != next) {
			this.next = next;
			if (next)
				next();
		}
	}

	public void addToRoom(String[] lines, int x, int y) {
		for (Tile t : room.getTiles()) {
			if (t.getX() == x && t.getY() == y) {
				System.out.println(t.toString());
			}
		}
		room.printEnemies();
		if (lines[0] == "None")
			room.removeEntity(x, y);
		Entity e = fileManager.parseEntity(lines[0], room, getEnemyCount(), v);
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text != "") {
			this.text = text;
			setMode("text");
			notifyObserver();
		}
	}

	public void input(int i) {
		if (mode == "game") {
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
		} else if (mode == "text") {
			if (textToShow == "") {
				setText("");
				room.getTile(player).setTextRead(true);
				mode = "game";
				return;
			}
			if (textToShow.length() < textLength) {
				setText(textToShow);
				textToShow = "";
			} else {
				String text = textToShow.substring(0, textLength);
				setText(textToShow.substring(0, text.lastIndexOf(" ")));
				textToShow = textToShow.substring(text.lastIndexOf(" ") + 1);
			}
			System.out.println(text);

		}
	}

	private int getEnemyCount() {
		enemyCount++;
		return enemyCount;
	}

	public void eraseSaveData() {
		fileManager.eraseSaveData();
	}
}
