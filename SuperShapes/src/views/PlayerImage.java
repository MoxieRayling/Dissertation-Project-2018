package views;

import java.awt.Color;
import java.awt.Graphics2D;

public class PlayerImage extends Image {

	private Boolean shrink = false;
	private Boolean dead = false;
	private int shrinkCount = 0;
	private Boolean shield = false;

	public PlayerImage(int x, int y, int scalex, int scaley, String room) {
		super(x, y, scalex, scaley, room);
	}

	public void setShrink(boolean b) {
		this.shrink = b;
	}

	private void shrink() {
		int x = 0;
		int y = 0;
		int xOffset = 1;
		int yOffset = 1;

		shrinkCount++;
		if (shrinkCount < 5) {
			return;
		}
		shrinkCount = 0;
		x = xSize -= 1;
		if (x == xSize && x != 0) {
			x = xSize - 1;
			xOffset = 1;
		}
		y = ySize -= 1;
		if (y == ySize && y != 0) {
			y = ySize - 1;
			yOffset = 1;
		}
		setXSize(x);
		setYSize(y);
		setXDest(xDest + xOffset);
		setYDest(yDest + yOffset);

		if (xSize <= 1 && ySize <= 1) {
			shrink = false;
		}
	}

	public void unshrink() {
		shrink = false;
		setXSize(scalex);
		setYSize(scaley);
		setXPos(xCoord * scalex);
		setYPos(yCoord * scaley);
	}

	@Override
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
		if (!shrink && getXPos() == getXDest() && getYPos() == getYDest()) {
			stationary = true;
		} else {
			stationary = false;
		}
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {

		if (shrink)
			shrink();
		g.setColor(Color.RED);
		g.fillOval(getXPos() + x, getYPos() + y, xSize, ySize);
		g.setColor(Color.BLACK);
		g.drawOval(getXPos() + x, getYPos() + y, xSize, ySize);
		if (shield) {
			g.setColor(new Color(255, 0, 0, 100));
			g.fillOval(getXPos() + x - xSize * 1 / 10, getYPos() + y - ySize * 1 / 10, xSize * 12 / 10,
					ySize * 12 / 10);
		}
	}

	public Boolean getDead() {
		return dead;
	}

	public void setDead(boolean b) {
		dead = b;
	}

	public void setShield(Boolean shield) {
		this.shield = shield;
	}

}
