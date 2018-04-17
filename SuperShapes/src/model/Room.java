package model;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import model.entities.Block;
import model.entities.Entity;
import model.entities.Ghost;
import model.entities.Player;
import model.entities.Shot;
import model.entities.Snake;
import model.entities.Turret;
import model.tiles.EmptyTile;
import model.tiles.Key;
import model.tiles.Tile;
import model.tiles.Wall;
import observers.Observer;
import observers.Subject;

public class Room implements Subject {
	private List<Tile> tiles = new ArrayList<Tile>();
	private List<Observer> observers = new ArrayList<Observer>();
	private List<Tile> pathTiles = new ArrayList<Tile>();
	private List<Entity> enemies = new ArrayList<Entity>();
	private int x;
	private int y;
	private int xLength = 11;
	private int yLength = 11;
	private int[] exits = { -1, -1, -1, -1 };
	private int clock = 0;
	private String text = "";
	private Player player;
	private String id;
	private List<Entity> remove = new ArrayList<Entity>();
	private List<Entity> add = new ArrayList<Entity>();

	public Room(int x, int y, int xLength, int yLength, int[] exits, Player player) {
		this.x = x;
		this.y = y;
		id = String.valueOf(x) + "," + String.valueOf(y);
		this.player = player;
		this.exits = exits;
		this.xLength = xLength;
		this.yLength = yLength;
		for (int i = 0; i < yLength; i++) {
			for (int j = 0; j < xLength; j++) {
				tiles.add(new EmptyTile(j, i));
			}
		}
	}

	public Room(int x, int y, int xLength, int yLength, int[] exits, Player player, Observer o) {
		this.x = x;
		this.y = y;
		id = String.valueOf(x) + "," + String.valueOf(y);
		this.player = player;
		this.exits = exits;
		this.xLength = xLength;
		this.yLength = yLength;
		for (int i = 0; i < yLength; i++) {
			for (int j = 0; j < xLength; j++) {
				tiles.add(new EmptyTile(j, i));
			}
		}
		this.addObserver(o);
	}

	public void incClock() {
		this.clock++;
		notifyObserver();
	}

	public int getClock() {
		return clock;
	}

	public void setExit(int index, int coord) {
		if (exits[index] == coord)
			exits[index] = -1;
		else
			exits[index] = coord;
		notifyObserver();
	}

	public int getxLength() {
		return xLength;
	}

	public int getyLength() {
		return yLength;
	}

	public void setSize(int[] size) {
		this.xLength = size[0];
		if (exits[0] >= xLength)
			exits[0] -= 1;
		if (exits[2] >= xLength)
			exits[2] -= 1;
		this.yLength = size[1];
		if (exits[1] >= yLength)
			exits[1] -= 1;
		if (exits[3] >= yLength)
			exits[3] -= 1;
		for (Entity e : enemies) {
			if (e.getX() > xLength - 1 || e.getY() > yLength - 1) {
				this.removeEntity(e);
			}
		}
		this.emptyTrash();

		List<Tile> tiles = new ArrayList<Tile>();
		for (int i = 0; i < yLength; i++) {
			for (int j = 0; j < xLength; j++) {
				Tile t = getTile(j, i);
				if (t != null) {
					tiles.add(t);
				} else
					tiles.add(new EmptyTile(j, i));
			}
		}
		setTiles(tiles);
	}

	public String exportRoom() {
		String result = "";
		result += "room " + id + " " + getxLength() + "," + getyLength() + " " + exits[0] + "," + exits[1] + ","
				+ exits[2] + "," + exits[3];
		result += ";E";
		for (Entity e : enemies) {
			if (e.toString() != null)
				result += e.toString() + ":";
		}
		result += ";T";
		for (Tile t : tiles) {
			if (t instanceof EmptyTile) {
				if (!t.getImage().equals("empty.png") || !t.getText().equals("")) {
					System.out.println("text is:" + t.getText() + "!");
					result += t.toString() + ":";
				}
			} else
				result += t.toString() + ":";
		}
		return result;
	}

	public Boolean containsKey() {
		for (Tile t : tiles) {
			if (t instanceof Key) {
				return true;
			}
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int[] getExits() {
		return exits;
	}

	public void setExits(int[] exits) {
		this.exits = exits;
	}

	public List<Entity> getEnemies() {
		return enemies;
	}

	public void setEnemies(List<Entity> enemies) {
		for (Entity e : enemies) {
			e.addObserver(observers.get(0));
			if (e instanceof Turret) {
				getTile(e).setTrav(false);
			}
		}
		this.enemies = enemies;
	}

	public void removeEntity(int x, int y) {
		for (Entity e : enemies) {
			if (e.getX() == x && e.getY() == y) {
				e.delete();
				removeEntity(e);
			}
		}
		emptyTrash();
		notifyObserver();
	}

	public void removeEntity(Entity e) {
		remove.add(e);
		e.setDelete(true);
	}

	public void addEntity(Entity e) {
		e.addObserver(observers.get(0));
		enemies.add(e);
	}

	public void emptyTrash() {
		for (Entity e : remove) {
			enemies.remove(e);
		}
		remove.clear();
	}

	public void swapTile(Tile tile) {
		for (Tile t : tiles) {
			if (t.getX() == tile.getX() && t.getY() == tile.getY()) {
				tiles.set(tiles.indexOf(t), tile);
			}
		}
		notifyObserver();
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
		notifyObserver();
	}

	public int[][] getPath() {
		int[][] path = new int[getyLength()][getxLength()];
		path[getyLength() - 1][getxLength() - 1] = 1;
		for (int i = 0; i < getyLength(); i++) {
			for (int j = 0; j < getxLength(); j++) {
				path[j][i] = getTile(j, i).getPath();
			}
		}
		return path;
	}

	public void updatePath(int x, int y) {

		Tile tile = getTile(x, y);
		pathTiles.add(tile);
		tile.setPathed(true);
		tile.setPath(1);
		findPath(1);
		if (!tile.getTrav()) {
			tile.setPath(getyLength() * getxLength() + 1);
		}
		getTile(x, y).setPathed(false);
		notifyObserver();
	}

	private void findPath(int val) {
		List<Tile> pathTiles = getUnpathed();
		setPath(val);
		this.pathTiles = pathTiles;

		if (this.pathTiles.size() > 0) {
			findPath(val + 1);
		} else {
			for (int i = 0; i < getyLength(); i++) {
				for (int j = 0; j < getxLength(); j++) {
					getTile(j, i).setPathed(false);
				}
			}
		}
	}

	private List<Tile> getUnpathed() {
		List<Tile> tiles = new ArrayList<Tile>();
		for (Tile p : this.pathTiles) {
			for (Tile t : getAdj(p.getX(), p.getY())) {
				if (!tiles.contains(t) && t.getTrav() && !t.getPathed()) {
					tiles.add(t);
				}
			}
		}
		return tiles;
	}

	private void setPath(int val) {
		for (Tile p : this.pathTiles) {
			for (Tile t : getAdj(p.getX(), p.getY())) {
				if (t.getTrav() && !t.getPathed()) {
					t.setPath(val + 1);
					t.setPathed(true);
				}
			}
		}
	}

	public List<Tile> getAdj(int x, int y) {
		List<Tile> tiles = new ArrayList<Tile>();
		if (getTile(x, y + 1) != null) {
			tiles.add(getTile(x, y + 1));
		}
		if (getTile(x - 1, y) != null) {
			tiles.add(getTile(x - 1, y));
		}
		if (getTile(x, y - 1) != null) {
			tiles.add(getTile(x, y - 1));
		}
		if (getTile(x + 1, y) != null) {
			tiles.add(getTile(x + 1, y));
		}
		return tiles;
	}

	public void moveEnemies() {
		emptyTrash();
		Boolean move = true;
		for (Entity e : enemies) {
			if (e instanceof Turret) {
				((Turret) e).next();
			} else if (e instanceof Ghost) {
				moveGhost((Ghost) e);
			}
		}
		resolveConcurrentAdd();
		while (move) {
			move = false;

			for (Entity e : enemies) {
				move = moveEntity(e);
			}
		}
		for (Entity e : enemies) {
			e.setMoved(false);
		}
	}

	public void concurrentAdd(Entity e) {
		add.add(e);
	}

	public void resolveConcurrentAdd() {
		for (Entity e : add) {
			addEntity(e);
		}
		add.clear();
	}

	private Boolean moveBlock(Block b) {
		List<Tile> adj = getAdj(b.getX(), b.getY());
		Tile tile = getTile(b.getX(), b.getY());
		for (Tile t : adj) {
			if (!b.getMoved() && t.getPath() < tile.getPath() && !t.getOccupied()) {
				tile.setOccupied(false);
				b.setLoc(t.getX(), t.getY());
				b.setMoved(true);
				t.setOccupied(true);
				return true;
			}
		}
		return false;
	}

	private Boolean moveGhost(Ghost g) {
		if (!g.getMoved() && player.getX() == g.getX() && player.getY() == g.getY()) {
			g.decPause();
			if (g.getPause() == 0) {
				g.setDead(true);
				removeEntity(g);
			}
			g.setMoved(true);
			return true;
		}
		if (!g.getMoved()) {
			int x = g.getX() - player.getX();
			int y = g.getY() - player.getY();
			if (Math.abs(x) >= Math.abs(y)) {
				if (x > 0) {
					g.setLoc(g.getX() - 1, g.getY());
				} else {
					g.setLoc(g.getX() + 1, g.getY());
				}
			} else {
				if (y > 0) {
					g.setLoc(g.getX(), g.getY() - 1);
				} else {
					g.setLoc(g.getX(), g.getY() + 1);
				}

				g.setMoved(true);
				return true;
			}
		}
		return false;

	}

	private Boolean moveSnake(Snake s) {
		List<Tile> adj = getAdj(s.getX(), s.getY());
		Tile tile = getTile(s.getX(), s.getY());
		for (Tile t : adj) {
			if (!s.getMoved() && t.getPath() < tile.getPath() && !t.getOccupied()
					&& !s.checkOccupied(t.getX(), t.getY())) {
				tile.setOccupied(false);
				s.move(t.getX(), t.getY());
				s.setMoved(true);
				t.setOccupied(true);
				return true;
			}
		}
		return false;
	}

	private Boolean moveShot(Shot s) {
		if (getTile(s.getX(), s.getY()) == null || getTile(s.getX(), s.getY()) instanceof Wall) {
			removeEntity(s);
			s.setTeleport(true);
			s.setLoc(-10, -10);
			return false;
		}
		switch (s.getDirection()) {
		case 'N':
			s.setLoc(s.getX(), s.getY() - 1);
			break;
		case 'E':
			s.setLoc(s.getX() + 1, s.getY());
			break;
		case 'S':
			s.setLoc(s.getX(), s.getY() + 1);
			break;
		case 'W':
			s.setLoc(s.getX() - 1, s.getY());
			break;
		default:
			return false;
		}
		s.setMoved(true);
		return true;
	}

	private Boolean moveEntity(Entity e) {
		Boolean moved = false;
		if (e instanceof Block) {
			moved = moveBlock((Block) e);
		} else if (e instanceof Snake) {
			moved = moveSnake((Snake) e);
		} else if (e instanceof Shot) {
			if (!e.getMoved())
				moved = moveShot((Shot) e);
		}
		return moved;
	}

	public void print() {
		int[][] path = getPath();
		String result = "";
		for (int i = 0; i < getyLength(); i++) {
			for (int j = 0; j < getxLength(); j++) {
				if (path[j][i] == getyLength() * getxLength() + 1) {
					result += 1;
				} else {
					result += 0;
				}
			}
			result += "\n";
		}
		System.out.println(result);
		result = "";

		for (Tile t : tiles) {
			if (!t.getTrav()) {
				result += "1";
			} else {
				result += "0";
			}
			if ((tiles.indexOf(t) + 1) % getxLength() == 0) {
				result += "\n";
			}
		}
		System.out.println(result);
	}

	public Tile getTile(int x, int y) {
		for (Tile t : tiles) {
			if (t.getX() == x && t.getY() == y) {
				return t;
			}
		}
		return null;
	}

	public Tile getTile(Entity e) {
		for (Tile t : tiles) {
			if (t.getX() == e.getX() && t.getY() == e.getY()) {
				return t;
			}
		}
		return null;
	}

	public void printEnemies() {
		for (Entity e : enemies) {
			System.out.println(e.toString());
		}
	}

	@Override
	public void addObserver(Observer o) {
		this.observers.add(o);

		if (enemies.size() > 0) {
			for (Entity e : enemies) {
				e.addObserver(o);
			}
		}
	}

	@Override
	public void removeObserver(Observer o) {
		this.observers.remove(o);
	}

	@Override
	public void notifyObserver() {
		for (Observer o : observers) {
			o.Update(new ActionEvent(this, 0, null));
		}
		for (Entity e : enemies) {
			e.notifyObserver();
		}
	}

	public List<Observer> getObservers() {
		return observers;
	}

	public void setClock(int clock) {
		this.clock = clock;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		notifyObserver();
	}
}
