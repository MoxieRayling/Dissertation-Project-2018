package model;

public class Tile {
	private Boolean trav = true; // traversable
	private int path; // path-finding value
	private int[] loc = new int[2];
	protected int x;
	protected int y;
	private Boolean pathed = false;
	private Boolean occupied = false;

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		loc[0] = x;
		loc[1] = y;
		this.trav = true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int[] GetLoc() {
		return loc;
	}

	public Boolean getOccupied() {
		return occupied;
	}

	public void setOccupied(Boolean occupied) {
		this.occupied = occupied;
	}

	public Boolean getPathed() {
		return pathed;
	}

	public void setPathed(Boolean pathed) {
		this.pathed = pathed;
	}

	public Boolean getTrav() {
		return trav;
	}

	public void setTrav(Boolean trav) {
		this.trav = trav;
	}

	public int getPath() {
		return path;
	}

	public void setPath(int path) {
		this.path = path;
	}

	public String toString() {
		return null;
	}
}
