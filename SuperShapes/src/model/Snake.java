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

	public Snake(String roomId, int count, int x, int y, int length, Room room, Observer o) {
		super(roomId, count, x, y);
		id += "s";
		this.room = room;
		this.length = length;
		this.o = o;
		for (int i = 0; i <= length - 1; i++) {
			addSegment(x, y);
			segments.get(i).addObserver(o);
			segments.get(i).notifyObserver();
			
		}
	}
	
	public SnakeBody getSegment(int index) {
		return segments.get(index%length);
	}

	public void move(int x, int y) {

		setLoc(x, y);

		for (int i = length - 1; i >= 0; i--) {
			if(i == length-1) {
				room.getTile(segments.get(i).getX(), segments.get(i).getY()).setOccupied(false);
			}
			if (i == 0) {
				segments.get(i).setLoc(x, y);
			} else {
				segments.get(i).setLoc(segments.get(i - 1).getX(), segments.get(i - 1).getY());
				room.getTile(segments.get(i - 1).getX(), segments.get(i - 1).getY()).setOccupied(true);
			}
		}
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
		SnakeBody segment = new SnakeBody(getRoomId(), getCount(), getX(), getY());
		segments.add(segment);
		room.addEntity(segment);
	}
	
	@Override
	public void notifyObserver() 
	{
		for (Observer o : observers) {
			o.Update(new ActionEvent(this, 0, null));
		}
		for(Entity e: segments) {
			e.notifyObserver();
		}
	}

	@Override
	public String toString() {
		return new String("s " + getX() + "," + getY() + " " + length +  "\n");
	}
	
	@Override
	public void delete() {
		for(Entity e : segments) {
			e.delete();
		}
		setDelete(true);
	}
}
