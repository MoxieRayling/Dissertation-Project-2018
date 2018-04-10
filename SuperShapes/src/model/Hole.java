package model;

public class Hole  extends Tile {

	public Hole(int x, int y) {
		super(x, y);
	}

	@Override
	public String toString() {
		return "hole " + x + "," + y ;
	}
}
