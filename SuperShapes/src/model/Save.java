package model;

public class Save extends Tile {

	public Save(int x, int y) {
		super(x, y);
	}

	@Override
	public String toString() {
		return "save " + x + "," + y + " /" + image + " " + getText();
	}

}
