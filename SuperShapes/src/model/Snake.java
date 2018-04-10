package model;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import observers.Observer;

public class Snake extends Entity {
	List<SnakeBody> segments = new ArrayList<SnakeBody>();
	private Observer o;
	private Room room;
	private int length;
	private int startX = 0;
	private int startY = 0;

	public Snake(String roomId, int count, int x, int y, int length, Room room, Observer o) {
		super(roomId, count, x, y);
		id += "s";
		this.room = room;
		this.length = length;
		startX = x;
		startY = y;
		this.o = o;
		for (int i = 0; i < 2; i++) {
			addSegment(x, y);
		}
	}

	public SnakeBody getSegment(int index) {
		return segments.get(index % length);
	}

	public void move(int x, int y) {
		setLoc(x, y);

		for (int i = segments.size() - 1; i >= 0; i--) {
			if (i == segments.size() - 1) {
				room.getTile(segments.get(i).getX(), segments.get(i).getY()).setOccupied(false);
			}
			if (i == 0) {
				segments.get(i).setLoc(x, y);
			} else {
				segments.get(i).setLoc(segments.get(i - 1).getX(), segments.get(i - 1).getY());
				room.getTile(segments.get(i - 1).getX(), segments.get(i - 1).getY()).setOccupied(true);
			}
		}
		if (segments.size() < length)
			addSegment(startX, startY);
	}

	public Boolean checkOccupied(int x, int y) {
		for (SnakeBody s : segments) {
			if (s.getX() == x && s.getY() == y) {
				return true;
			}
		}
		return false;
	}

	private void addSegment(int x, int y) {
		SnakeBody segment = new SnakeBody(getRoomId(), getCount(), x, y);
		segments.add(segment);
		room.concurrentAdd(segment);
		segment.addObserver(o);
		segment.notifyObserver();
	}

	@Override
	public void notifyObserver() {
		for (Observer o : observers) {
			o.Update(new ActionEvent(this, 0, null));
		}
		for (Entity e : segments) {
			e.notifyObserver();
		}
	}

	@Override
	public String toString() {
		return new String("snake " + getX() + "," + getY() + " " + length);
	}

	@Override
	public void delete() {
		for (Entity e : segments) {
			e.delete();
		}
		setDelete(true);
	}
}
