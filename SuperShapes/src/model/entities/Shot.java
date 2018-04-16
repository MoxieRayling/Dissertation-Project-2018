package model.entities;

import observers.Observer;

public class Shot extends Entity {

	private char direction;
	private int sx;
	private int sy;

	public Shot(String roomId, int count, int x, int y, Observer o, char direction) {
		super(roomId, count, x, y);
		id += "ts";
		this.direction = direction;
		this.sx = x;
		this.sy = y;
		this.addObserver(o);
	}
	
	public char getDirection() {
		return direction;
	}

	public int getSX() {
		return sx;
	}

	public int getSY() {
		return sy;
	}

	@Override
	public String toString() {
		return null;
	}
}