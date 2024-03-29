package model;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import observers.Observer;

public class Player extends Entity {
	private List<Observer> observers = new ArrayList<Observer>();
	private String name = "Player";
	private Boolean movable = true;
	private String checkpoint;
	private int lives = 3;
	private char checkpointEntrance;
	private char lastEntrance;
	private int fly = 0;
	private int pause = 0;
	private int flyCooldown = 0;
	private int rewindCooldown = 0;
	private int[] xCoords = new int[5];
	private int[] yCoords = new int[5];
	private int pauseCooldown = 0;
	private Boolean shield = false;
	private int shieldCooldown = 0;

	public Player(String room, int x, int y, int lives) {
		super(room, 1, x, y);
		this.deadly = false;
		this.roomId = room;
		this.checkpoint = room;
		this.lives = lives;
		if (Math.abs(x - 5) >= Math.abs(y - 5)) {
			if (x - 5 > 0) {
				setLastEntrance('E');
			} else {
				setLastEntrance('W');
			}
		} else {
			if (y - 5 > 0) {
				setLastEntrance('S');
			} else {
				setLastEntrance('N');
			}
		}
		resetHistory();
	}

	@Override
	public void setRoomId(String room) {

		if (this.roomId != room) {
			if (getX() == 0 && getY() == 5)
				setLastEntrance('W');
			else if (getX() == 10 && getY() == 5)
				setLastEntrance('E');
			else if (getX() == 5 && getY() == 0)
				setLastEntrance('N');
			else if (getX() == 5 && getY() == 10)
				setLastEntrance('S');
		}
		this.roomId = room;
	}

	@Override
	public void setLoc(int x, int y) {
		for (int i = 4; i > 0; i--) {
			xCoords[i] = xCoords[i - 1];
			yCoords[i] = yCoords[i - 1];
		}
		xCoords[0] = x;
		yCoords[0] = y;
		setPX(getX());
		setPY(getY());
		setX(x);
		setY(y);
		notifyObserver();
	}

	public void resetPos() {
		switch (lastEntrance) {
		case 'N':
			setLoc(5, 0);
			break;
		case 'E':
			setLoc(10, 5);
			break;
		case 'S':
			setLoc(5, 10);
			break;
		case 'W':
			setLoc(0, 5);
			break;
		default:
			break;
		}
		resetHistory();
		fly = 0;
		flyCooldown = 0;
		pause = 0;
		pauseCooldown = 0;
		rewindCooldown = 0;
		shield = false;
		shieldCooldown = 0;
		notifyObserver();
	}

	private void resetHistory() {
		for (int i = 0; i < 5; i++) {
			xCoords[i] = getX();
			yCoords[i] = getY();
		}
	}

	public int getFly() {
		return fly;
	}

	public void fly() {
		if (flyCooldown <= 0) {
			fly = 3;
			notifyObserver();
		}
	}

	public void pauseTime() {
		if (pauseCooldown <= 0) {
			pause = 3;
			notifyObserver();
		}
	}

	public void step() {
		flyDec();
		if (rewindCooldown > 0)
			rewindCooldown--;
		pauseDec();
		if (shieldCooldown > 0)
			shieldCooldown--;
	}

	public char getCheckpointEntrance() {
		return checkpointEntrance;
	}

	public void setCheckpointEntrance(char checkpointEntrance) {
		this.checkpointEntrance = checkpointEntrance;
	}

	public char getLastEntrance() {
		return lastEntrance;
	}

	public void setLastEntrance(char lastEntrance) {
		this.lastEntrance = lastEntrance;
	}

	public String getCheckpoint() {
		return checkpoint;
	}

	public void setCheckpoint(String checkpoint) {
		this.checkpoint = checkpoint;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void die() {
		if (lives > 0 && !getDead())
			setDead(true);
		this.lives--;
		notifyObserver();
	}

	public Boolean getMovable() {
		return movable;
	}

	public void setMovable(Boolean bool) {
		movable = bool;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getRewindCooldown() {
		return rewindCooldown;
	}

	public int getPauseCooldown() {
		return pauseCooldown;
	}

	public int getShieldCooldown() {
		return shieldCooldown;
	}

	public void rewind() {
		if (rewindCooldown <= 0) {
			setTeleport(true);
			setLoc(xCoords[4], yCoords[4]);

			resetHistory();
			setTeleport(false);
			rewindCooldown = 10;
			notifyObserver();
		}
	}

	public int getPause() {
		return pause;
	}

	public Boolean getShield() {
		return shield;
	}

	public void shield() {
		if (shieldCooldown <= 0)
			this.shield = true;
		notifyObserver();
	}

	public void breakShield() {
		this.shield = false;
		shieldCooldown = 10;
		notifyObserver();
	}

	public int getFlyCooldown() {
		return flyCooldown;
	}

	private void flyDec() {
		if (flyCooldown <= 0 && fly > 0) {
			fly--;
			if (fly == 0)
				flyCooldown = 10;
		} else if (flyCooldown > 0)
			flyCooldown--;
		notifyObserver();
	}

	private void pauseDec() {
		if (pauseCooldown <= 0 && pause > 0) {
			pause--;
			if (pause == 0)
				pauseCooldown = 10;
		} else if (pauseCooldown > 0)
			pauseCooldown--;
		notifyObserver();
	}
	@Override
	public String toString() {
		return null;
	}

}
