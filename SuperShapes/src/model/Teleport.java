package model;

public class Teleport extends Tile {

	private int xTele = 0;
	private int yTele = 0;

	public Teleport(int x, int y, int xTele, int yTele) {
		super(x, y);
	}

	@Override
	public String toString() {
		return "tele " + x + "," + y + " " + xTele + " " + yTele;
	}

	public int getXTele() {
		return xTele;
	}

	public int getYTele() {
		return yTele;
	}
}
