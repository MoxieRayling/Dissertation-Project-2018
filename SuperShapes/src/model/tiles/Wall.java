package model.tiles;

public class Wall extends Tile {

	public Wall(int x, int y) {
		super(x, y);
		this.setTrav(false);
		this.setPath(122);
	}

	@Override
	public String toString() {
		return "wall " + x + "," + y + " /" + image + " \"" + getText() + " " + getTextRead().toString();
	}
}
