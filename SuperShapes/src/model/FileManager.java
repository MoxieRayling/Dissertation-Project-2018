package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import observers.Observer;

public class FileManager {

	private int enemyCount = 0;
	private String directory = "games/game1";

	public FileManager() {
	}

	public void setDirectory(String dir) {
		directory = "games/" + dir;
	}

	public void copyWorld() {
		writeToFile(getWorldData(), directory + "/working.txt");
	}

	public void saveWorld() {
		writeToFile(getWorkingData(), directory + "/world.txt");
	}

	private int getEnemyCount() {
		enemyCount++;
		return enemyCount;
	}

	public List<String> getSaveData() {
		String fileName = directory + "/save.txt";
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

	public List<String> getWorldData() {
		String fileName = directory + "/world.txt";
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

	public List<String> getWorkingData() {
		String fileName = directory + "/working.txt";
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

	public void saveGame(String checkpointId, Player player, int clock) {
		String fileName = directory + "/save.txt";
		List<String> lines = new ArrayList<String>();
		lines.add(checkpointId);
		lines.add(String.valueOf(player.getX()));
		lines.add(String.valueOf(player.getY()));
		lines.add(String.valueOf(clock));
		writeToFile(lines, fileName);
	}

	public void eraseSaveData() {
		String fileName = directory + "/save.txt";
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

	public List<String> getRoomData(int x, int y) {
		String fileName = directory + "/world.txt";
		String line = null;
		List<String> lines = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] rParams = line.split(" ");
				if (rParams[0].equalsIgnoreCase("room")) {
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

	public Room makeRoom(List<String> lines, Player player, Observer o) {
		List<Entity> enemies = new ArrayList<Entity>();
		enemyCount = 0;
		int x = 0;
		int y = 0;
		Room r = null;
		if (!lines.isEmpty()) {
			for (String l : lines) {
				String[] params = l.split(" ");
				if (params[0].equalsIgnoreCase("room")) {
					r = parseRoom(params, player);
					r.addObserver(o);
					x = r.getX();
					y = r.getY();
				} else if (params[0].startsWith("block")) {
					enemies.add(parseBlock(params, x, y, getEnemyCount()));
				} else if (params[0].startsWith("ghost")) {
					enemies.add(parseGhost(params, x, y, getEnemyCount()));
				} else if (params[0].startsWith("turret")) {
					enemies.add(parseTurret(params, x, y, r, getEnemyCount()));
				} else if (params[0].startsWith("snake")) {
					enemies.add(parseSnake(params, x, y, r, getEnemyCount(), o));
				} else if (params[0].startsWith("empty")) {
					r.swapTile(parseEmptyTile(params));
				} else if (params[0].startsWith("wall")) {
					r.swapTile(parseWall(params));
				} else if (params[0].startsWith("slide")) {
					r.swapTile(parseSlide(params));
				} else if (params[0].startsWith("tele")) {
					r.swapTile(parseTeleport(params));
				} else if (params[0].startsWith("hole")) {
					r.swapTile(parseHole(params));
				}
			}
			r.setEnemies(enemies);
		}
		return r;
	}

	public void updateWorldFile(String room, String oldLine, String newLine) {
		String fileName = directory + "/working.txt";
		String line = null;
		List<String> lines = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
		} catch (

		FileNotFoundException ex) {
		} catch (IOException ex) {
		}

		Boolean inRoom = false;
		for (String l : lines) {
			if (l.startsWith("room")) {
				if (l.split(" ")[1].contains(room)) {
					inRoom = true;
				}
			}
			if (l.contains(oldLine) && inRoom) {
				lines.set(lines.indexOf(l), newLine);
				break;
			}
			if (l.startsWith(";") && inRoom) {
				lines.add(lines.indexOf(l), newLine);
				break;
			}
		}
		writeToFile(lines, fileName);
	}

	public void writeToFile(List<String> lines, String fileName) {
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
			if (l.startsWith("room")) {
				String[] params = l.split(" ");
				if (params[1].equals(id)) {
					String[] exits = params[3].split(",");
					try {
						result[0] = Integer.parseInt(exits[0]);
						result[1] = Integer.parseInt(exits[1]);
						result[2] = Integer.parseInt(exits[2]);
						result[3] = Integer.parseInt(exits[3]);
					} catch (NumberFormatException e) {
						System.out.println("rip ints");
					}
				}
			}
		}
		return result;
	}

	public void removeRoom(String id) {
		List<String> world = getWorkingData();
		Boolean inRoom = false;
		for (String s : world) {
			if (s.startsWith("room")) {
				if (id.equals(s.split(" ")[1]))
					inRoom = true;
			}
			if (inRoom) {
				world.set(world.indexOf(s), "xxx");
				if (s.startsWith(";")) {
					inRoom = false;
				}
			}
		}
		while (world.contains("xxx")) {
			world.remove("xxx");
		}
		writeToFile(world, directory + "/working.txt");
	}

	public void exportWorking(List<String> room) {
		removeRoom(room.get(0).split(" ")[1]);
		List<String> lines = getWorkingData();
		for (String l : room) {
			lines.add(l);
		}
		writeToFile(lines, directory + "/working.txt");
		writeToFile(lines, directory + "/world.txt");
	}

	public Room parseRoom(String[] params, Player player) {
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
			System.out.println("rip ints");
		}
		return new Room(x, y, xLength, yLength, getExits(x + "," + y), player);
	}

	public Block parseBlock(String[] params, int x, int y, int enemyCount) {
		String[] loc = params[1].split(",");
		int bx = 0;
		int by = 0;
		try {
			bx = Integer.parseInt(loc[0]);
			by = Integer.parseInt(loc[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
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
			System.out.println("rip ints");
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
			System.out.println("rip ints");
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
			System.out.println("rip ints");
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
			System.out.println("rip ints");
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
			System.out.println("rip ints");
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
			System.out.println("rip ints");
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
			System.out.println("rip ints");
		}
		Wall w = new Wall(x, y);
		if (params.length == 3) {
			w.setImage(params[2]);
		}
		return w;
	}

	public EmptyTile parseEmptyTile(String[] params) {
		int x = 0;
		int y = 0;
		String[] coords = params[1].split(",");
		try {
			x = Integer.parseInt(coords[0]);
			y = Integer.parseInt(coords[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
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
			System.out.println("rip ints");
		}
		return result;
	}

	private Boolean searchList(String search, List<String> lines) {
		for (String line : lines) {
			if (line.contains(search)) {
				return true;
			}
		}
		return false;
	}

	public String[][] getMap(int x, int y) {
		List<String> rooms = this.getWorldData();
		List<String> lines = new ArrayList<String>();
		String[][] map = new String[11][11];
		for (String line : rooms) {
			if (line.startsWith("room")) {
				if (!map.equals(new String[11][11])) {
					int[] coords = idToCoords(line.split(" ")[1]);
					if (coords[0] >= x - 5 && coords[0] <= x + 5 && coords[1] >= y - 5 && coords[1] <= y + 5) {
						map[coords[0] + 5 - x][coords[1] + 5 - y] = "R";
						if (searchList("key", lines)) {
							map[coords[0] + 5 - x][coords[1] + 5 - y] += "K";
						}
						if (searchList("save", lines)) {
							map[coords[0] + 5 - x][coords[1] + 5 - y] += "S";
						}
					}
				}
				lines.clear();
			}
			lines.add(line);
		}
		return map;
	}
}
