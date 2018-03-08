package model;

public class Block extends Entity {
	public Block(String roomId, int count, int x, int y) {
		super(roomId, count, x, y);
		id += "b";
	}

	@Override
	public String toString() {
		return new String("b " + getX() + "," + getY() + "\n");
	}
}
