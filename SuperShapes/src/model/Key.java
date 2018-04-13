package model;

public class Key extends Tile {

	public Key(int x, int y) {
		super(x, y);
	}


	@Override
	public String toString() {
		return "key " + x + "," + y + " /" + image + " " + getText();
	}

}
