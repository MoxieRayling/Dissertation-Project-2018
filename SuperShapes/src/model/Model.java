package model;

import java.awt.event.ActionEvent;
import java.util.List;
import controller.Constants;
import observers.Observer;
import observers.Subject;

public class Model implements Constants, Subject {
	protected Room room;
	protected Observer v;
	protected Player player;
	private String roomId;
	private int clock = 0;
	private Boolean input = true;
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

	public void setDirectory(String dir) {
		fileManager.setDirectory(dir);
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
		int[] coords = fileManager.idToCoords(roomId);
		Room result = fileManager.makeRoom(coords[0], coords[1], player, v);
		int[] exits = fileManager.generateExits(coords[0], coords[1]);
		if (result == null)
			result = new Room(coords[0], coords[1], 11, 11, exits, player, v);
		return result;
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
		if (x == room.getxLength() - 1 && y == room.getExits()[1] && direction == 'E') {
			changeRoom(getAdjRoomId('E'), true);
			return true;
		} else if (x == 0 && y == room.getExits()[3] && direction == 'W') {
			changeRoom(getAdjRoomId('W'), true);
			return true;
		} else if (x == room.getExits()[0] && y == 0 && direction == 'N') {
			changeRoom(getAdjRoomId('N'), true);
			return true;
		} else if (x == room.getExits()[2] && y == room.getyLength() - 1 && direction == 'S') {
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

	private Boolean move(char direction, int x, int y) {
		if (direction == 'N' && y - 1 >= 0) {
			player.setLoc(x, y - 1);
			return true;
		} else if (direction == 'E' && x + 1 <= room.getxLength() - 1) {
			player.setLoc(x + 1, y);
			return true;
		} else if (direction == 'S' && y + 1 <= room.getyLength() - 1) {
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
		checkTile(room.getTile(player));
		clock++;
		if (!room.getTile(player).getTextRead())
			this.setText(room.getTile(player).getText());
		notifyObserver();
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

		} else if (t instanceof Save) {

		} else if (t instanceof Coin) {

		}

	}

	public void resetRoom() {
		System.out.println("reset");
		player.setDead(false);
		room = loadRoom(room.getId());
		switch (player.getLastEntrance()) {
		case 'N':
			player.setLoc(5, 0);
			break;
		case 'E':
			player.setLoc(room.getxLength() - 1, 5);
			break;
		case 'S':
			player.setLoc(5, room.getyLength() - 1);
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
		System.out.println("change");
		Room temp = loadRoom(roomId);
		int x = room.getX() - temp.getX();
		int y = room.getY() - temp.getY();
		room = temp;
		if (Math.abs(x) >= Math.abs(y) && resetPos) {
			if (x > 0) {
				player.setLoc(room.getxLength() - 1, room.getExits()[1]);
			} else {
				player.setLoc(0, room.getExits()[3]);
			}
		} else {
			if (y > 0) {
				player.setLoc(room.getExits()[2], room.getyLength() - 1);
			} else {
				player.setLoc(room.getExits()[0], 0);
			}
		}
		room.addObserver(v);
		player.setRoomId(room.getId());
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		setMode("text");
		notifyObserver();
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

	public void eraseSaveData() {
		fileManager.eraseSaveData();
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
}
