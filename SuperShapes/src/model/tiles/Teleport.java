package model.tiles;

public class Teleport extends Tile {

	private int xTele = 0;
	private int yTele = 0;

	public Teleport(int x, int y, int xTele, int yTele) {
		super(x, y);
		setImage("teleport.png");
	}

	@Override
	public String toString() {
		return "tele " + x + "," + y + " " + xTele + " " + yTele + " " + image + " \"" + getText() + " " + getTextRead().toString();
	}

	public int getXTele() {
		return xTele;
	}

	public int getYTele() {
		return yTele;
	}
}