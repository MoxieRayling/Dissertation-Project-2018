package model;

public class Ghost extends Entity {
	private int pause = 3;

	public Ghost(String roomId, int count, int x, int y, int pause) {
		super(roomId, count, x, y);
		id += "g";
		this.deadly = false;
		this.pause = pause;
	}

	public void decPause() {
		pause--;
		if (pause == 0) {
			this.setDead(true);
		}
	}

	public int getPause() {
		return pause;
	}

	@Override
	public String toString() {
		return new String("ghost " + getX() + "," + getY() + " " + pause );
	}
}
