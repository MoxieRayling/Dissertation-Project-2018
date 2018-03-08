package model;

import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import controller.Constants;
import observers.Observer;
import observers.Subject;

public class Model implements Constants, Subject {
	private Room room;
	private Observer v;
	private Player player;
	private String roomId;
	private int clock = 0;
	private Boolean input = true;
	private int enemyCount = 0;
	private String checkpointId = "0,0";
	private Boolean next = false;
	private String exits = "";

	public Model(Observer v) {
		this.v = v;
		player = new Player("0,0", 5, 10, 5);
		player.addObserver(v);
		room = loadRoom("0,0");
		room.addObserver(v);
		room.updatePath(player.getX(), player.getY());
		room.notifyObserver();
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

	public List<String> getSaveData() {
		String fileName = "resource/save.txt";
		String line = null;
		List<String> lines = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("file no work " + fileName);
		} catch (IOException ex) {
			System.out.println("file no work " + fileName);
		}
		return lines;
	}

	public void loadGame() {
		List<String> lines = getSaveData();
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

	public void saveGame() {
		String fileName = "resource/save.txt";
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(checkpointId);
			bufferedWriter.newLine();
			bufferedWriter.write(String.valueOf(player.getX()));
			bufferedWriter.newLine();
			bufferedWriter.write(String.valueOf(player.getY()));
			bufferedWriter.newLine();
			bufferedWriter.write(String.valueOf(clock));
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("file no work " + fileName);
		}
	}

	public void eraseSaveData() {
		String fileName = "resource/save.txt";
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write("0,0");
			bufferedWriter.newLine();
			bufferedWriter.write(String.valueOf(5));
			bufferedWriter.newLine();
			bufferedWriter.write(String.valueOf(10));
			bufferedWriter.newLine();
			bufferedWriter.write(String.valueOf(0));
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("file no work " + fileName);
		}
	}

	private List<String> getRoomData(int x, int y) {
		String fileName = "resource/world.txt";
		String line = null;
		List<String> lines = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] rParams = line.split(" ");
				if (rParams[0].equalsIgnoreCase("r")) {
					checkAdjacentRoom(x, y, rParams[1]);
					if (rParams[1].equalsIgnoreCase(String.valueOf(x) + "," + String.valueOf(y))) {
						lines.add(line);
						while ((line = bufferedReader.readLine()) != null) {
							String[] params = line.split(" ");
							if (!params[0].startsWith(";")) {
								lines.add(line);
							} else {
								break;
							}
						}
					}
				} else {

					continue;
				}
			}
			bufferedReader.close();
		} catch (

		FileNotFoundException ex) {
		} catch (IOException ex) {
		}
		return lines;
	}

	private void checkAdjacentRoom(int x, int y, String params) {
		String[] coords = params.split(",");
		int rx = 0;
		int ry = 0;
		try {
			rx = Integer.parseInt(coords[0]);
			ry = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		if (x == rx) {
			if (ry == y - 1) {
				exits += 'N';
			} else if (ry == y + 1) {
				exits += 'S';
			}
		}
		if (y == ry) {
			if (rx == x - 1) {
				exits += 'W';
			} else if (rx == x + 1) {
				exits += 'E';
			}
		}
	}

	private Room loadRoom(String roomId) {
		String[] roomCoords = roomId.split(",");
		int x = 0;
		int y = 0;
		try {
			x = Integer.parseInt(roomCoords[0]);
			y = Integer.parseInt(roomCoords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}

		return makeRoom(getRoomData(x, y));
	}

	private Room makeRoom(List<String> lines) {
		List<Entity> enemies = new ArrayList<Entity>();
		int x = 0;
		int y = 0;
		Room r = null;
		for (String l : lines) {
			String[] params = l.split(" ");
			if (params[0].equalsIgnoreCase("r")) {
				r = parseRoom(params);
				r.addObserver(v);
				x = r.getX();
				y = r.getY();
			} else if (params[0].startsWith("T")) {
				r.setTiles(parseTiles(params));
			} else if (params[0].startsWith("b")) {
				enemies.add(parseBlock(params, x, y));
			} else if (params[0].startsWith("g")) {
				enemies.add(parseGhost(params, x, y));
			} else if (params[0].startsWith("t")) {
				enemies.add(parseTurret(params, x, y, r));
			} else if (params[0].startsWith("s")) {
				enemies.add(parseSnake(params, x, y, r));
			}
		}
		r.setEnemies(enemies);
		exits = "";
		return r;
	}

	private Room parseRoom(String[] params) {
		int x = 0;
		int y = 0;
		try {
			x = Integer.parseInt(params[1].split(",")[0]);
			y = Integer.parseInt(params[1].split(",")[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		Boolean checkpoint = false;
		if (params[2].startsWith("1"))
			checkpoint = true;
		return new Room(x, y, exits.toCharArray(), player, checkpoint);
	}

	private List<Tile> parseTiles(String[] params) {
		List<Tile> tiles = new ArrayList<Tile>();
		for (int i = 0; i < params.length - 1; i++) {
			String[] cells = params[i + 1].split(",");

			for (int j = 0; j < cells.length; j++) {

				if (cells[j].startsWith("1")) {
					tiles.add(new Wall(j, i));
				} else {
					tiles.add(new Tile(j, i));
				}
			}
		}
		return tiles;
	}

	private Block parseBlock(String[] params, int x, int y) {
		enemyCount++;
		String[] loc = params[1].split(",");
		int bx = 0;
		int by = 0;
		try {
			bx = Integer.parseInt(loc[0]);
			by = Integer.parseInt(loc[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		return new Block(x + "," + y, enemyCount, bx, by);
	}

	private Ghost parseGhost(String[] params, int x, int y) {
		enemyCount++;
		String[] loc = params[1].split(",");
		int bx = 0;
		int by = 0;
		int pause = 3;
		try {
			bx = Integer.parseInt(loc[0]);
			by = Integer.parseInt(loc[1]);
			pause = Integer.parseInt(params[2]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		return new Ghost(x + "," + y, enemyCount, bx, by, pause);
	}

	private Turret parseTurret(String[] params, int x, int y, Room r) {
		enemyCount++;
		String[] loc = params[1].split(",");
		int delay = 0;
		int ratio = 1;
		int tx = 0;
		int ty = 0;
		try {
			tx = Integer.parseInt(loc[0]);
			ty = Integer.parseInt(loc[1]);
			ratio = Integer.parseInt(params[2]);
			delay = Integer.parseInt(params[4]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		char direction = params[3].charAt(0);
		r.getTile(tx, ty).setTrav(false);
		return new Turret(x + "," + y, enemyCount, tx, ty, ratio, delay, direction, r, v);
	}

	private Snake parseSnake(String[] params, int x, int y, Room r) {
		enemyCount++;
		String[] loc = params[1].split(",");
		int length = 1;
		int sx = 0;
		int sy = 0;
		try {
			sx = Integer.parseInt(loc[0]);
			sy = Integer.parseInt(loc[1]);
			length = Integer.parseInt(params[2]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		return new Snake(x + "," + y, enemyCount, sx, sy, length, r, v);
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
		System.out.println(room.getEnemies().size());
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
		clock++;
		notifyObserver();
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
			saveGame();
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
		room.removeEntity(x, y);
		Entity e = parseEntity(lines[0]);
		if (e != null)
			room.addEntity(e);
		Tile tile = parseTile(lines[1]);
		if (tile != null) {
			room.swapTile(tile);
			System.out.println(tile.toString() + " added");
		}else {
		}
		room.notifyObserver();
	}

	private Tile parseTile(String line) {
		String[] params = line.split(" ");
		if (params[0].startsWith("E")) {
			return parseEmptyTile(params);
		} else if (params[0].startsWith("W")) {
			return parseWall(params);
		} else if (params[0].startsWith("S")) {
			System.out.println("making slide");
			return parseSlide(params);
		} else if (params[0].startsWith("T")) {
			return parseTeleport(params);
		} else if (params[0].startsWith("H")) {
			return parseHole(params);
		}
		return null;
	}

	private Tile parseHole(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		return new Hole(x, y);
	}

	private Tile parseTeleport(String[] params) {
		int x = 0;
		int y = 0;
		int tx = 0;
		int ty = 0;
		String[] coords = params[1].split(",");
		String[] tCoords = params[2].split(",");
		try {
			System.out.println(params[2]);
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
			tx = Integer.parseInt(tCoords[0]);
			ty = Integer.parseInt(tCoords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		return new Teleport(x, y, tx, ty);
	}

	private Tile parseSlide(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		return new Slide(x, y, params[2].charAt(0));
	}

	private Tile parseWall(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		return new Wall(x, y);
	}

	private Tile parseEmptyTile(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		return new Tile(x, y);
	}

	private Entity parseEntity(String line) {
		if (line != null) {
			String[] params = line.split(" ");

			if (params[0].startsWith("b")) {
				return parseBlock(params, room.getX(), room.getY());
			} else if (params[0].startsWith("g")) {
				return parseGhost(params, room.getX(), room.getY());
			} else if (params[0].startsWith("t")) {
				return parseTurret(params, room.getX(), room.getY(), room);
			} else if (params[0].startsWith("s")) {
				return parseSnake(params, room.getX(), room.getY(), room);
			}
		}
		return null;
	}
}
