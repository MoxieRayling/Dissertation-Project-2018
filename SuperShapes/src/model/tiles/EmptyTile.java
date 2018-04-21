package model.tiles;

public class EmptyTile extends Tile {

	public EmptyTile(int x, int y) {
		super(x, y);
		setImage("empty.png");
	}

	@Override
	public String toString() {
		return "empty " + x + "," + y + " " + image + " " + getText() + " " + getTextRead().toString() + " " + getCommand();
	}

	
}
