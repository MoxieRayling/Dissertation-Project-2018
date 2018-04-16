package views.animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import controller.Constants;

public abstract class Image {

	protected String id;
	protected int xPos;
	protected int yPos;
	protected int xDest;
	protected int yDest;
	protected int xSpeed;
	protected int ySpeed;
	protected int scale = 0;
	protected int size = 0;
	protected int xCoord = 0;
	protected int yCoord = 0;
	protected String room;
	protected Boolean stationary = true;
	private Boolean noCollide = false;
	private String img = "";
	protected BufferedImage image;

	public Image(int x, int y, int scale, String room, String img, String alt) {
		this.xSpeed = 1;
		this.ySpeed = 1;
		this.scale = scale;
		this.size = scale;
		this.room = room;

		xPos = x * scale;
		yPos = y * scale;
		xDest = x * scale;
		yDest = y * scale;
		String texture = alt;
		if (img != "")
			texture = img;
		this.img = texture;
		try {
			File imgSrc = new File(Constants.gameDir + "/textures/" + img);
			if (imgSrc.exists() && img != "")
				image = ImageIO.read(imgSrc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Image(int x, int y, int scale, String room) {
		this.xSpeed = 1;
		this.ySpeed = 1;
		this.scale = scale;
		this.size = scale;
		this.room = room;

		xPos = x * scale;
		yPos = y * scale;
		xDest = x * scale;
		yDest = y * scale;
	}

	public Boolean getNoCollide() {
		return noCollide;
	}

	public void setNoCollide(Boolean noCollide) {
		this.noCollide = noCollide;
	}

	protected void setXSize(int x) {
		this.size = x;
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

	public void setScale(int x) {
		this.scale = x;
		this.size = x;
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
		setYPos(yCoord * scale);
		setXPos(xCoord * scale);
		setYDest(yPos);
		setXDest(xPos);
	}

	public void Move() {
		double speed = 40.0;
		if (this.getXPos() < this.getXDest() + (int) Math.ceil(scale / speed)
				&& this.getXPos() > this.getXDest() - (int) Math.ceil(scale / speed)) {
			this.setXPos(this.getXDest());
		} else if (this.getXPos() > this.getXDest()) {
			this.setXPos(this.getXPos() - (int) Math.ceil(scale / speed));
		} else if (this.getXPos() < this.getXDest()) {
			this.setXPos(this.getXPos() + (int) Math.ceil(scale / speed));
		}

		if (this.getYPos() < this.getYDest() + (int) Math.ceil(scale / speed)
				&& this.getYPos() > this.getYDest() - (int) Math.ceil(scale / speed)) {
			this.setYPos(this.getYDest());
		} else if (this.getYPos() > this.getYDest()) {
			this.setYPos(this.getYPos() - (int) Math.ceil(scale / speed));
		} else if (this.getYPos() < this.getYDest()) {
			this.setYPos(this.getYPos() + (int) Math.ceil(scale / speed));
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
			setXDest(this.xCoord * scale);
			setYDest(this.yCoord * scale);
			setXPos(this.xCoord * scale);
			setYPos(this.yCoord * scale);
		} else {
			setX(x);
			setY(y);
			setXDest(this.xCoord * scale);
			setYDest(this.yCoord * scale);
		}
	}

	public abstract void drawThis(Graphics2D g, int x, int y);
}
