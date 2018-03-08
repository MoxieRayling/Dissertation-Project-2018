package views;

import java.awt.Graphics2D;

public abstract class Image {

	protected String id;
	protected int xPos;
	protected int yPos;
	protected int xDest;
	protected int yDest;
	protected int xSpeed;
	protected int ySpeed;
	protected int scalex = 0;
	protected int scaley = 0;
	protected int xSize = 0;
	protected int ySize = 0;
	protected int xCoord = 0;
	protected int yCoord = 0;
	protected String room;
	protected Boolean stationary = true;
	private Boolean noCollide = false;

	public Image(int x, int y, int scalex, int scaley, String room) {
		this.xSpeed = 1;
		this.ySpeed = 1;
		this.scalex = scalex;
		this.scaley = scaley;
		this.xSize = scalex;
		this.ySize = scaley;
		this.room = room;

		xPos = x * scalex;
		yPos = y * scaley;
		xDest = x * scalex;
		yDest = y * scaley;

	}

	public Boolean getNoCollide() {
		return noCollide;
	}

	public void setNoCollide(Boolean noCollide) {
		this.noCollide = noCollide;
	}

	protected void setYSize(int y) {
		this.ySize = y;
	}

	protected void setXSize(int x) {
		this.xSize = x;
	}

	public Boolean getStationary() {
		return stationary;
	}

	public void setStationary(Boolean stationary) {
		this.stationary = stationary;
	}

	public String getRoom() {
		return this.room;
	}

	public int getX() {
		return xCoord;
	}

	public void setX(int x) {
		this.xCoord = x;
	}

	public int getY() {
		return yCoord;
	}

	public void setY(int y) {
		this.yCoord = y;
	}

	public void setScale(int x, int y) {
		this.scalex = x;
		this.scaley = y;
		this.xSize = x;
		this.ySize = y;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getXPos() {
		return xPos;
	}

	public void setXPos(int xPos) {
		this.xPos = xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setYPos(int yPos) {
		this.yPos = yPos;
	}

	public int getXDest() {
		return xDest;
	}

	public void setXDest(int xDest) {
		this.xDest = xDest;
	}

	public int getYDest() {
		return yDest;
	}

	public void setYDest(int yDest) {
		this.yDest = yDest;
	}

	public int getXSpeed() {
		return xSpeed;
	}

	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public int getYSpeed() {
		return ySpeed;
	}

	public void setYSpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	public void update() {
		setYPos(yCoord * scaley);
		setXPos(xCoord * scalex);
		setYDest(yPos);
		setXDest(xPos);
	}

	public void Move() {
		double speed = 40.0;
		if (this.getXPos() < this.getXDest() + (int) Math.ceil(scalex / speed)
				&& this.getXPos() > this.getXDest() - (int) Math.ceil(scalex / speed)) {
			this.setXPos(this.getXDest());
		} else if (this.getXPos() > this.getXDest()) {
			this.setXPos(this.getXPos() - (int) Math.ceil(scalex / speed));
		} else if (this.getXPos() < this.getXDest()) {
			this.setXPos(this.getXPos() + (int) Math.ceil(scalex / speed));
		}

		if (this.getYPos() < this.getYDest() + (int) Math.ceil(scaley / speed)
				&& this.getYPos() > this.getYDest() - (int) Math.ceil(scaley / speed)) {
			this.setYPos(this.getYDest());
		} else if (this.getYPos() > this.getYDest()) {
			this.setYPos(this.getYPos() - (int) Math.ceil(scaley / speed));
		} else if (this.getYPos() < this.getYDest()) {
			this.setYPos(this.getYPos() + (int) Math.ceil(scaley / speed));
		}
		if (getXPos() == getXDest() && getYPos() == getYDest()) {
			stationary = true;
		} else {
			stationary = false;
		}
	}

	public void next(int x, int y, Boolean teleport) {
		if (teleport) {
			setX(x);
			setY(y);
			setXDest(this.xCoord * scalex);
			setYDest(this.yCoord * scaley);
			setXPos(this.xCoord * scalex);
			setYPos(this.yCoord * scaley);
		} else {
			setX(x);
			setY(y);
			setXDest(this.xCoord * scalex);
			setYDest(this.yCoord * scaley);
		}
	}

	public abstract void drawThis(Graphics2D g, int x, int y);
}
