package model;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import model.entities.Block;
import model.entities.Entity;
import model.entities.Ghost;
import model.entities.Player;
import model.entities.Snake;
import model.entities.Turret;
import model.tiles.Coin;
import model.tiles.EmptyTile;
import model.tiles.Hole;
import model.tiles.Key;
import model.tiles.Lock;
import model.tiles.Slide;
import model.tiles.Teleport;
import model.tiles.Tile;
import model.tiles.Wall;
import observers.Observer;

public class FileManager {

	private static String gameDir = "games/game1";
	private static String saveDir = "save";
	private static List<String> textureNames = new ArrayList<String>();
	private static List<BufferedImage> textures = new ArrayList<BufferedImage>();
	private int enemyCount = 0;

	public FileManager() {
	}

	public static String getGameDir() {
		return gameDir;
	}

	public static String getSaveDir() {
		return saveDir;
	}

	public static void setGameDir(String gameFile) {
		gameDir = "games/" + gameFile;
		setImages();
	}

	public static void setSaveDir(String save) {
		saveDir = gameDir + "/saves/" + save;
	}

	public static void setImages() {
		File file = new File(gameDir + "/textures");
		String[] files = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});
		textureNames.clear();
		textures.clear();
		for (int i = 0; i < files.length; i++) {
			textureNames.add(files[i]);
			File imgSrc = new File(gameDir + "/textures/" + files[i]);
			try {
				textures.add(ImageIO.read(imgSrc));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private int getEnemyCount() {
		enemyCount++;
		return enemyCount;
	}

	public List<String> readFromFile(String fileName) {
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
		String fileName = saveDir + "/save.txt";
		return readFromFile(fileName).get(0);
	}

	public List<String> getWorldData() {
		String fileName = saveDir + "/world.txt";
		return readFromFile(fileName);
	}

	public List<String> getWorkingData() {
		String fileName = saveDir + "/working.txt";
		return readFromFile(fileName);
	}

	public List<String> getMasterData() {
		String fileName = gameDir + "/master.txt";
		return readFromFile(fileName);
	}

	public void saveGame(Room r, String playerData, int clock) {
		String fileName = saveDir + "/save.txt";
		String saveData = r.getId() + ":" + playerData + ":" + clock;
		writeToFile(saveData, fileName);
	}

	public static void newSaveData() {
		String fileName = saveDir + "/save.txt";
		String saveData = "0,0:0,0:0,0:0";
		writeToFile(saveData, fileName);
	}

	public String getRoomData(int x, int y) {
		List<String> world = getWorkingData();
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
			for (Tile t : parseTiles(params[2].substring(1))) {
				r.swapTile(t);
			}

		return r;
	}

	public Room makeRoom(String id, Player player, Observer o) {
		int[] coords = this.idToCoords(id);
		enemyCount = 0;
		String room = getRoomData(coords[0], coords[1]);
		String[] params = room.split(";");
		Room r = parseRoom(params[0].split(" "), player, o);
		if (params[1].length() > 1)
			r.setEnemies(parseEntities(params[1].substring(1), coords[0], coords[1], r, o));
		if (params[2].length() > 1)
			for (Tile t : parseTiles(params[2].substring(1))) {
				r.swapTile(t);
			}

		return r;
	}

	public List<Tile> parseTiles(String tileLine) {
		List<Tile> tiles = new ArrayList<Tile>();
		String[] params = tileLine.split(":");
		for (int i = 0; i < params.length; i++) {
			tiles.add(parseTile(params[i]));
		}
		return tiles;
	}

	public List<Entity> parseEntities(String entityLine, int x, int y, Room r, Observer o) {
		String[] params = entityLine.split(":");
		List<Entity> entities = new ArrayList<Entity>();
		for (int i = 0; i < params.length; i++) {
			entities.add(parseEntity(params[i], r, o));
		}
		return entities;
	}

	private static void writeToFile(String saveData, String fileName) {
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(saveData);
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("file no work " + fileName);
		}
	}

	private static void writeToFile(List<String> lines, String fileName) {
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (String l : lines) {
				bufferedWriter.write(l);
				if (!(lines.indexOf(l) == lines.size() - 1))
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

	public void removeRoomWorking(String id) {
		List<String> world = getWorkingData();
		for (String s : world) {
			if (s.startsWith("room " + id)) {
				world.set(world.indexOf(s), "xxx");
			}
		}
		world.remove("xxx");
		writeToFile(world, saveDir + "/working.txt");
	}

	public void removeRoomWorld(String id) {
		List<String> world = getWorldData();
		for (String s : world) {
			if (s.startsWith("room " + id)) {
				world.set(world.indexOf(s), "xxx");
			}
		}
		world.remove("xxx");
		writeToFile(world, saveDir + "/working.txt");
		writeToFile(world, saveDir + "/world.txt");
	}

	public void exportWorking(String room) {
		removeRoomWorking(room.split(" ")[1]);
		List<String> lines = getWorkingData();
		lines.add(room);
		writeToFile(lines, saveDir + "/working.txt");
	}

	public void exportWorld(String room) {
		removeRoomWorking(room.split(" ")[1]);
		List<String> lines = getWorkingData();
		lines.add(room);
		writeToFile(lines, saveDir + "/working.txt");
		writeToFile(lines, saveDir + "/world.txt");
	}

	public void exportMaster(String room) {
		removeRoomWorking(room.split(" ")[1]);
		List<String> lines = getWorkingData();
		lines.add(room);
		writeToFile(lines, saveDir + "/working.txt");
		writeToFile(lines, saveDir + "/world.txt");
		writeToFile(lines, gameDir + "/master.txt");
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
			if (!params[2].equals("none")) {
				b.setImage(params[2]);
			}
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
			if (!params[3].equals("none")) {
				g.setImage(params[3]);
			}
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
			if (!params[5].equals("none")) {
				t.setImage(params[5]);
			}
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
			if (!params[3].equals("none")) {
				s.setImage(params[3]);
			}
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
			} else if (params[0].startsWith("key")) {
				return parseKey(params);
			} else if (params[0].startsWith("lock")) {
				return parseLock(params);
			} else if (params[0].startsWith("coin")) {
				return parseCoin(params);
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
		if (!params[2].equals("none")) {
			h.setImage(params[2]);
		}
		if (!params[3].equals("\"")) {
			h.setText(params[3].substring(1));
		}
		if (params[4].equals("true")) {
			h.setTextRead(true);
		} else {
			h.setTextRead(false);
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

		if (!params[3].equals("none")) {
			t.setImage(params[3]);
		}
		if (!params[4].equals("\"")) {
			t.setText(params[4].substring(1));
		}
		if (params[5].equals("true")) {
			t.setTextRead(true);
		} else {
			t.setTextRead(false);
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
		if (!params[3].equals("none")) {
			s.setImage(params[3]);
		}
		if (!params[4].equals("\"")) {
			s.setText(params[4].substring(1));
		}
		if (params[5].equals("true")) {
			s.setTextRead(true);
		} else {
			s.setTextRead(false);
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

		if (!params[2].equals("none")) {
			w.setImage(params[2]);
		}
		if (!params[3].equals("\"")) {
			System.out.println(params[3]);
			w.setText(params[3].substring(1));
		}
		if (params[4].equals("true")) {
			w.setTextRead(true);
		} else {
			w.setTextRead(false);
		}
		return w;
	}

	public Tile parseCoin(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip wall ints");
		}
		Coin c = new Coin(x, y);

		if (!params[2].equals("none")) {
			c.setImage(params[2]);
		}
		if (!params[3].equals("\"")) {
			c.setText(params[3].substring(1));
		}
		if (params[4].equals("true")) {
			c.setTextRead(true);
		} else {
			c.setTextRead(false);
		}
		return c;
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
		if (!params[2].equals("none")) {
			t.setImage(params[2]);
		}
		if (!params[3].equals("") && !params[3].equals("\"")) {
			t.setText(params[3]);
		}
		if (params[4].equals("true")) {
			t.setTextRead(true);
		} else {
			t.setTextRead(false);
		}
		return t;
	}

	public Tile parseLock(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip empty ints");
		}
		Lock l = new Lock(x, y, params[2]);
		if (!params[3].equals("none")) {
			l.setImage(params[3]);
		}
		if (!params[4].equals("\"")) {
			l.setText(params[4].substring(1));
		}
		if (params[4].equals("true")) {
			l.setTextRead(true);
		} else {
			l.setTextRead(false);
		}
		return l;
	}

	public Tile parseKey(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip empty ints");
		}
		Key k = new Key(x, y, params[2]);
		if (!params[3].equals("none")) {
			k.setImage(params[3]);
		}
		if (!params[4].equals("\"")) {
			k.setText(params[4].substring(1));
		}
		if (params[4].equals("true")) {
			k.setTextRead(true);
		} else {
			k.setTextRead(false);
		}
		return k;
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
					if (line.contains("key ")) {
						map[coords[0] + 5 - x][coords[1] + 5 - y] += "K";
					}
					if (line.contains("coin ")) {
						map[coords[0] + 5 - x][coords[1] + 5 - y] += "C";
					}
				}
			}
		}
		return map;
	}

	public static void makeNewDir() {
		File dir = new File(gameDir);
		dir.mkdir();
		dir = new File(gameDir + "/events");
		dir.mkdir();
		dir = new File(gameDir + "/saves");
		dir.mkdir();
		setSaveDir("save");
		dir = new File(saveDir);
		dir.mkdir();
		dir = new File(gameDir + "/textures");
		dir.mkdir();
		List<String> world = new ArrayList<String>();
		world.add("room 0,0 11,11 5,5,5,5;E;T");
		writeToFile(world, saveDir + "/working.txt");
		writeToFile(world, saveDir + "/world.txt");
		writeToFile(world, gameDir + "/master.txt");
		newSaveData();
	}

	public void makeNewSave() {
		File dir = new File(gameDir);
		dir.mkdir();
		dir = new File(gameDir + "/textures");
		dir.mkdir();
		dir = new File(gameDir + "/events");
		dir.mkdir();
		dir = new File(gameDir + "/saves");
		dir.mkdir();
		dir = new File(saveDir);
		dir.mkdir();
		List<String> world = this.getMasterData();
		writeToFile(world, saveDir + "/working.txt");
		writeToFile(world, saveDir + "/world.txt");
		newSaveData();
	}

	public void overWriteWorking() {
		writeToFile(getWorldData(), saveDir + "/working.txt");
	}

	public static BufferedImage getImage(String img) {
		for (String s : textureNames) {
			if (s.equals(img)) {
				return textures.get(textureNames.indexOf(s));
			}
		}
		return null;
	}

	public void createEntity(String entity) {
		writeToFile(entity, "parts/entities.txt");
	}

	public List<String> getEntities() {
		return readFromFile("parts/entities.txt");
	}

	public void createTile(String tile) {
		writeToFile(tile, "parts/tiles.txt");
	}

	public List<String> getTiles() {
		return readFromFile("parts/tiles.txt");
	}
}