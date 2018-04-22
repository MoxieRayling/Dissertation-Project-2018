package model.tiles;

public class Key extends Tile {

	private String key = "";

	public Key(int x, int y, String key) {
		super(x, y);
		setImage("key.png");
		this.key = key;
	}

	@Override
	public String toString() {
		return "key " + x + "," + y + " " + key + " " + image + " " + getText() + " " + getTextRead().toString() + " " + getCommand();
	}

	public String getKey() {
		return key;
	}

}
