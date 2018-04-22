package model.tiles;

public class Lock extends Tile {

	private String key = "";

	public Lock(int x, int y, String key) {
		super(x, y);
		this.setTrav(false);
		this.setPath(122);
		setImage("lock.png");
		this.key = key;
	}

	@Override
	public String toString() {

		return "lock " + x + "," + y + " " + key + " " + image + " " + getText() + " " + getTextRead().toString() + " " + getCommand();
	}

	public String getKey() {
		return key;
	}
}
