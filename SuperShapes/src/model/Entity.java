package model;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import observers.Observer;
import observers.Subject;

public abstract class Entity implements Subject {
	protected List<Observer> observers = new ArrayList<Observer>();
	private int x;
	private int y;
	private int px;
	private int py;
	private int count;
	protected String roomId;
	protected String id;
	private Boolean dead = false;
	private Boolean moved = false;
	private Boolean teleport = false;
	private Boolean delete = false;
	protected Boolean deadly = true;

	public Entity(String roomId, int count, int x, int y) {
		this.roomId = roomId;
		this.x = x;
		this.y = y;
		this.px = x;
		this.py = y;
		id = roomId + count;
	}

	public Boolean getTeleport() {
		return teleport;
	}

	public void setTeleport(Boolean teleport) {
		this.teleport = teleport;
		notifyObserver();
	}

	public int getCount() {
		return count;
	}

	public String getId() {
		return id;
	}

	public Boolean getMoved() {
		return moved;
	}

	public void setMoved(Boolean moved) {
		this.moved = moved;
	}

	public int getX() {
		return x;
	}

	protected void setX(int x) {
		this.x = x;
	}

	protected void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public int getPX() {
		return px;
	}

	public int getPY() {
		return py;
	}

	protected void setPX(int x) {
		this.px = x;
	}

	protected void setPY(int y) {
		this.py = y;
	}

	public Boolean getDead() {
		return dead;
	}

	public void setDead(Boolean dead) {
		this.dead = dead;
		notifyObserver();
	}

	public void setLoc(int x, int y) {
		setPX(getX());
		setPY(getY());
		setX(x);
		setY(y);
		notifyObserver();
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String room) {
		this.roomId = room;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
		notifyObserver();
	}

	@Override
	public void addObserver(Observer o) {
		this.observers.add(o);
		notifyObserver();
	}

	@Override
	public void removeObserver(Observer o) {
		this.observers.remove(o);
	}

	@Override
	public void notifyObserver() {
		for (Observer o : observers) {
			o.Update(new ActionEvent(this, 0, null));
		}
	}
	
	public abstract String toString();

	public boolean getDeadly() {
		return deadly;
	}
	public void delete() {
		setDelete(true);
	}

}
