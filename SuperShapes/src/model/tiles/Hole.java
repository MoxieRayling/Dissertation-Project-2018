package model.tiles;

public class Hole  extends Tile {

	public Hole(int x, int y) {
		super(x, y);
		setImage("hole.png");
	}

	@Override
	public String toString() {
		return "hole " + x + "," + y + " " + image + " \"" + getText() + " " + getTextRead().toString() + " " + getCommand();
	}
}
