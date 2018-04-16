package model.tiles;

public class Lock extends Tile {

	private String key = "";

	public Lock(int x, int y, String key) {
		super(x, y);
		this.setTrav(false);
		this.setPath(122);
		this.key = key;
	}

	@Override
	public String toString() {

		return "lock " + x + "," + y + " " + key + " " + image + " \"" + getText() + " " + getTextRead().toString();
	}

	public String getKey() {
		return key;
	}
}
