package model.entities;

public class Block extends Entity {
	public Block(String roomId, int count, int x, int y) {
		super(roomId, count, x, y);
		id += "b";
		image = "block.png";
	}

	@Override
	public String toString() {
		return new String("block " + getX() + "," + getY() + " " + image);
	}
}
