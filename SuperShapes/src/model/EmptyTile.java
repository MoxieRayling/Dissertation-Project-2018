package model;

public class EmptyTile extends Tile {

	public EmptyTile(int x, int y) {
		super(x, y);
	}

	@Override
	public String toString() {
		return "empty " + x + "," + y;
	}

	
}
