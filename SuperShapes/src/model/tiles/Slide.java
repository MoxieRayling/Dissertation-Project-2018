package model.tiles;

public class Slide extends Tile {

	private char direction;

	public Slide(int x, int y, char direction) {
		super(x, y);
		this.direction = direction;
		setImage("slide.png");
	}

	public char getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return "slide " + x + "," + y + " " + direction + " " + image + " \"" + getText() + " " + getTextRead().toString();
	}
}
