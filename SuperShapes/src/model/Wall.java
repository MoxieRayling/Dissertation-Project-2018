package model;

public class Wall extends Tile {

	public Wall(int x, int y) {
		super(x, y);
		this.setTrav(false);
		this.setPath(122);
	}

	@Override
	public String toString() {
		return "W " + x + "," + y;
	}
}
