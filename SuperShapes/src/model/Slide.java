package model;

public class Slide extends Tile {

	private char direction;

	public Slide(int x, int y, char direction) {
		super(x, y);
		this.direction = direction;
	}

	public char getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return "slide " + x + "," + y + " " + direction + " /" + image + " " + getText();
	}
}
