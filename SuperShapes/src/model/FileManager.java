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

	public FileManager() {

	}

	private int getEnemyCount() {
		enemyCount++;
		return enemyCount;
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

	public List<String> getWorldData() {
		String fileName = "resource/world.txt";
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

	public List<String> getRoomData(int x, int y) {
		String fileName = "resource/world.txt";
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
		String fileName = "resource/world.txt";
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
				System.out.println("yes");
				break;
			}
			if (l.startsWith(";") && inRoom) {
				lines.add(lines.indexOf(l), newLine);
				System.out.println("no");
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

	public String getExits(int x, int y) {
		List<String> lines = getWorldData();
		String exits = "";
		for (String l : lines) {
			if (l.startsWith("room")) {
				String id = l.split(" ")[1];
				String[] coords = id.split(",");
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
		}
		return exits;
	}

	public Room parseRoom(String[] params, Player player) {
		int x = 0;
		int y = 0;
		try {
			x = Integer.parseInt(params[1].split(",")[0]);
			y = Integer.parseInt(params[1].split(",")[1]);
		} catch (NumberFormatException e) {
			System.out.println("rip ints");
		}
		return new Room(x, y, getExits(x, y), player);
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
		return new Block(x + "," + y, enemyCount, bx, by);
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
		return new Ghost(x + "," + y, enemyCount, bx, by, pause);
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
		return new Turret(x + "," + y, enemyCount, tx, ty, ratio, delay, direction, r);
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
		return new Snake(x + "," + y, enemyCount, sx, sy, length, r, o);
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
		return new Hole(x, y);
	}

	public Tile parseTeleport(String[] params) {
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
		return new Slide(x, y, params[2].charAt(0));
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
		return new Wall(x, y);
	}

	public Tile parseEmptyTile(String[] params) {
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

	private int[] idToCoords(String id) {
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
						System.out.println(map[coords[0] + 5][coords[1] + 5]);
					}
				}
				lines.clear();
			}
			lines.add(line);
		}
		return map;
	}
}
