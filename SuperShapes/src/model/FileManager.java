package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import controller.Constants;
import observers.Observer;

public class FileManager {

	private int enemyCount = 0;

	public FileManager() {
	}

	public void copyWorld() {
		writeToFile(getWorldData(), Constants.gameDir + "/working.txt");
	}

	public void saveWorld() {
		writeToFile(getWorkingData(), Constants.gameDir + "/world.txt");
	}

	private int getEnemyCount() {
		enemyCount++;
		return enemyCount;
	}

	private List<String> readFromFile(String fileName) {
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

	public String getSaveData() {
		String fileName = Constants.gameDir + "/saves/" + Constants.saveFile + ".txt";
		return readFromFile(fileName).get(0);
	}

	public List<String> getWorldData() {
		String fileName = Constants.gameDir + "/world.txt";
		return readFromFile(fileName);
	}

	public List<String> getWorkingData() {
		String fileName = Constants.gameDir + "/working.txt";
		return readFromFile(fileName);
	}

	public void saveGame(Room r, Player player, int clock) {
		String fileName = Constants.gameDir + "/saves/" + Constants.saveFile + ".txt";
		String saveData = r.getId() + ":" + player.getX() + "," + player.getY() + ":" + clock;
		writeToFile(saveData, fileName);
	}

	public void newSaveData() {
		String fileName = Constants.gameDir + "/saves/" + Constants.saveFile + ".txt";
		String saveData = "0,0:0,0:0";
		writeToFile(saveData, fileName);
	}

	public String getRoomData(int x, int y) {
		List<String> world = getWorldData();
		String roomId = x + "," + y;
		String result = "";
		for (String s : world) {
			if (s.substring(0, 10).contains(roomId)) {
				result = s;
			}
		}
		return result;
	}

	public int[] generateExits(int x, int y) {
		List<String> data = getWorldData();
		int[] result = { -1, -1, -1, -1 };
		for (String l : data) {
			if (l.startsWith("room " + x + "," + (y - 1))) {
				result[0] = 5;
			} else if (l.startsWith("room " + x + "," + (y + 1))) {
				result[2] = 5;
			} else if (l.startsWith("room " + (x - 1) + "," + y)) {
				result[3] = 5;
			} else if (l.startsWith("room " + (x + 1) + "," + y)) {
				result[1] = 5;
			}
		}
		return result;
	}

	public Room makeRoom(int x, int y, Player player, Observer o) {
		enemyCount = 0;
		String room = getRoomData(x, y);
		String[] params = room.split(";");
		Room r = parseRoom(params[0].split(" "), player, o);
		if (params[1].length() > 1)
			r.setEnemies(parseEntities(params[1].substring(1), x, y, r, o));
		if (params[2].length() > 1)
			r.setTiles(parseTiles(params[2].substring(1)));

		return r;
	}

	private List<Tile> parseTiles(String tileLine) {
		List<Tile> tiles = new ArrayList<Tile>();
		String[] params = tileLine.split(":");
		for (int i = 0; i < params.length; i++) {
			tiles.add(parseTile(params[i]));
		}
		return tiles;
	}

	private List<Entity> parseEntities(String entityLine, int x, int y, Room r, Observer o) {
		String[] params = entityLine.split(":");
		List<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < params.length; i++) {
			entities.add(parseEntity(params[i], r, o));
		}
		return entities;
	}

	private void writeToFile(String saveData, String fileName) {
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(saveData);
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("file no work " + fileName);
		}
	}

	private void writeToFile(List<String> lines, String fileName) {
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (String l : lines) {
				bufferedWriter.write(l);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("file no work " + fileName);
		}
	}

	public int[] getExits(String id) {
		List<String> lines = getWorldData();
		int[] result = { -1, -1, -1, -1 };
		for (String l : lines) {
			if (l.startsWith("room " + id)) {
				String[] exits = l.split(";")[0].split(" ")[3].split(",");
				try {
					result[0] = Integer.parseInt(exits[0]);
					result[1] = Integer.parseInt(exits[1]);
					result[2] = Integer.parseInt(exits[2]);
					result[3] = Integer.parseInt(exits[3]);
				} catch (NumberFormatException e) {
					System.out.println("rip exits ints");
				}

			}
		}
		return result;
	}

	public void removeRoom(String id) {
		List<String> world = getWorkingData();
		for (String s : world) {
			if (s.startsWith("room " + id)) {
				world.set(world.indexOf(s), "xxx");
			}
		}
		world.remove("xxx");
		writeToFile(world, Constants.gameDir + "/working.txt");
	}

	public void exportWorking(String room) {
		removeRoom(room.split(" ")[1]);
		List<String> lines = getWorkingData();
		lines.add(room);
		writeToFile(lines, Constants.gameDir + "/working.txt");
	}

	public void exportWorld(String room) {
		removeRoom(room.split(" ")[1]);
		List<String> lines = getWorkingData();
		lines.add(room);
		writeToFile(lines, Constants.gameDir + "/working.txt");
		writeToFile(lines, Constants.gameDir + "/world.txt");
	}

	public void exportMaster(String room) {
		removeRoom(room.split(" ")[1]);
		List<String> lines = getWorkingData();
		lines.add(room);
		writeToFile(lines, Constants.gameDir + "/working.txt");
		writeToFile(lines, Constants.gameDir + "/world.txt");
		writeToFile(lines, Constants.gameDir + "/master.txt");
		System.out.println("exported");
	}

	public Room parseRoom(String[] params, Player player, Observer o) {
		int x = 0;
		int y = 0;
		int xLength = 11;
		int yLength = 11;
		try {
			x = Integer.parseInt(params[1].split(",")[0]);
			y = Integer.parseInt(params[1].split(",")[1]);
			xLength = Integer.parseInt(params[2].split(",")[0]);
			yLength = Integer.parseInt(params[2].split(",")[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip room ints");
		}
		return new Room(x, y, xLength, yLength, getExits(x + "," + y), player, o);
	}

	public Block parseBlock(String[] params, int x, int y, int enemyCount) {
		String[] loc = params[1].split(",");
		int bx = 0;
		int by = 0;
		try {
			bx = Integer.parseInt(loc[0]);
			by = Integer.parseInt(loc[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip block ints");
		}
		Block b = new Block(x + "," + y, enemyCount, bx, by);
		if (params.length == 3) {
			b.setImage(params[2]);
		}
		return b;
	}

	public Ghost parseGhost(String[] params, int x, int y, int enemyCount) {
		String[] loc = params[1].split(",");
		int bx = 0;
		int by = 0;
		int pause = 3;
		try {
			bx = Integer.parseInt(loc[0]);
			by = Integer.parseInt(loc[1]);
			pause = Integer.parseInt(params[2]);
		} catch (NumberFormatException e) {
			System.out.println("rip ghost ints");
		}
		Ghost g = new Ghost(x + "," + y, enemyCount, bx, by, pause);
		if (params.length == 4) {
			g.setImage(params[3]);
		}
		return g;
	}

	public Turret parseTurret(String[] params, int x, int y, Room r, int enemyCount) {
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
			System.out.println("rip turret ints");
		}
		char direction = params[3].charAt(0);
		r.getTile(tx, ty).setTrav(false);
		Turret t = new Turret(x + "," + y, enemyCount, tx, ty, ratio, delay, direction, r);
		if (params.length == 6) {
			t.setImage(params[5]);
		}
		return t;
	}

	public Snake parseSnake(String[] params, int x, int y, Room r, int enemyCount, Observer o) {
		String[] loc = params[1].split(",");
		int length = 1;
		int sx = 0;
		int sy = 0;
		try {
			sx = Integer.parseInt(loc[0]);
			sy = Integer.parseInt(loc[1]);
			length = Integer.parseInt(params[2]);
		} catch (NumberFormatException e) {
			System.out.println("rip snake ints");
		}
		Snake s = new Snake(x + "," + y, enemyCount, sx, sy, length, r, o);
		if (params.length == 4) {
			s.setImage(params[3]);
		}
		return s;
	}

	public Tile parseTile(String line) {
		if (line != null) {
			String[] params = line.split(" ");
			if (params[0].startsWith("empty")) {
				return parseEmptyTile(params);
			} else if (params[0].startsWith("wall")) {
				return parseWall(params);
			} else if (params[0].startsWith("slide")) {
				return parseSlide(params);
			} else if (params[0].startsWith("tele")) {
				return parseTeleport(params);
			} else if (params[0].startsWith("hole")) {
				return parseHole(params);
			}
		}
		return null;
	}

	public Tile parseHole(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip hole ints");
		}
		Hole h = new Hole(x, y);
		if (params.length == 3) {
			h.setImage(params[2]);
		}
		return h;
	}

	public Tile parseTeleport(String[] params) {
		int x = 0;
		int y = 0;
		int tx = 0;
		int ty = 0;
		String[] coords = params[1].split(",");
		String[] tCoords = params[2].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
			tx = Integer.parseInt(tCoords[0]);
			ty = Integer.parseInt(tCoords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip tele ints");
		}
		Teleport t = new Teleport(x, y, tx, ty);
		if (params.length == 4) {
			t.setImage(params[3]);
		}
		return t;
	}

	public Tile parseSlide(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip slide ints");
		}
		Slide s = new Slide(x, y, params[2].charAt(0));
		if (params.length == 4) {
			s.setImage(params[3]);
		}
		return s;
	}

	public Tile parseWall(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip wall ints");
		}
		Wall w = new Wall(x, y);
		if (params.length == 3) {
			w.setImage(params[2]);
		}
		return w;
	}

	public Tile parseEmptyTile(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip empty ints");
		}
		EmptyTile t = new EmptyTile(x, y);
		if (params.length == 3) {
			t.setImage(params[2]);
		}
		return t;
	}

	public Entity parseEntity(String line, Room room, Observer o) {
		if (line != null) {
			String[] params = line.split(" ");

			if (params[0].startsWith("block")) {
				return parseBlock(params, room.getX(), room.getY(), getEnemyCount());
			} else if (params[0].startsWith("ghost")) {
				return parseGhost(params, room.getX(), room.getY(), getEnemyCount());
			} else if (params[0].startsWith("turret")) {
				return parseTurret(params, room.getX(), room.getY(), room, getEnemyCount());
			} else if (params[0].startsWith("snake")) {
				return parseSnake(params, room.getX(), room.getY(), room, getEnemyCount(), o);
			}
		}
		return null;
	}

	public int[] idToCoords(String id) {
		int[] result = new int[2];
		String[] coords = id.split(",");
		try {
			result[0] = Integer.parseInt(coords[0]);
			result[1] = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip these ints");
		}
		return result;
	}

	public String[][] getMap(int x, int y) {
		List<String> rooms = this.getWorkingData();
		String[][] map = new String[11][11];
		for (String line : rooms) {
			if (!map.equals(new String[11][11])) {
				int[] coords = idToCoords(line.split(" ")[1]);
				if (coords[0] >= x - 5 && coords[0] <= x + 5 && coords[1] >= y - 5 && coords[1] <= y + 5) {
					map[coords[0] + 5 - x][coords[1] + 5 - y] = "R";
					if (line.contains("key")) {
						map[coords[0] + 5 - x][coords[1] + 5 - y] += "K";
					}
					if (line.contains("save")) {
						map[coords[0] + 5 - x][coords[1] + 5 - y] += "S";
					}
				}
			}
		}
		return map;
	}

	public void makeNewDir() { 
		File dir = new File(Constants.gameDir);
		dir.mkdir();
		dir = new File(Constants.gameDir + "/saves");
		dir.mkdir();
		dir = new File(Constants.gameDir + "/textures");
		dir.mkdir();
		List<String> world = new ArrayList<String>();
		world.add("room 0,0 11,11 5,5,5,5;E;T");
		writeToFile(world, Constants.gameDir + "/working.txt");
		writeToFile(world, Constants.gameDir + "/world.txt");
		writeToFile(world, Constants.gameDir + "/master.txt");
		newSaveData();
	}
}
