package model.tiles;

public class Coin extends Tile {

	public Coin(int x, int y) {
		super(x, y);
	}


	@Override
	public String toString() {
		return "coin " + x + "," + y + " /" + image + " \"" + getText() + " " + getText().isEmpty();
	}

}
