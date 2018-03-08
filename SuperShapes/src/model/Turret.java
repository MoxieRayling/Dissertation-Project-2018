package model;

import observers.Observer;

public class Turret extends Entity {
	private int ratio;
	private int count;
	private char direction;
	private Room room;
	private int delay;

	public Turret(String roomId, int enemyCount, int x, int y, int ratio, int delay, char direction, Room room,
			Observer o) {
		super(roomId, enemyCount, x, y);
		id += "t";
		this.ratio = ratio;
		this.count = 0;
		this.delay = delay;
		this.direction = direction;
		this.room = room;
		this.deadly = false;
		addObserver(o);
	}

	public void next() {
		if (delay > 0) {
			delay--;
			return;
		}
		if (count == 0) {
			room.concurrentAdd(
					new Shot(this.roomId, this.count, this.getX(), this.getY(), this.observers.get(0), getDirection()));
		}

		count++;
		if (count == ratio) {
			count = 0;
		}
	}

	public char getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return new String("t " + getX() + "," + getY() + " " + ratio + " " + direction + " " + delay + "\n");
	}
}