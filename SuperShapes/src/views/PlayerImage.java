package views;

import java.awt.Color;
import java.awt.Graphics2D;

public class PlayerImage extends Image {

	private Boolean shrink = false;
	private Boolean dead = false;
	private int shrinkCount = 0;
	private Boolean shield = false;

	public PlayerImage(int x, int y, int scale, String room) {
		super(x, y, scale, room);
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
		x = size -= 1;
		if (x == size && x != 0) {
			x = size - 1;
			xOffset = 1;
		}
		y = size -= 1;
		if (y == size && y != 0) {
			y = size - 1;
			yOffset = 1;
		}
		setXSize(x);
		setXDest(xDest + xOffset);
		setYDest(yDest + yOffset);

		if (size <= 1 && size <= 1) {
			shrink = false;
		}
	}

	public void unshrink() {
		shrink = false;
		setXSize(scale);
		setXPos(xCoord * scale);
		setYPos(yCoord * scale);
	}

	@Override
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
		g.fillOval(getXPos() + x, getYPos() + y, size, size);
		g.setColor(Color.BLACK);
		g.drawOval(getXPos() + x, getYPos() + y, size, size);
		if (shield) {
			g.setColor(new Color(255, 0, 0, 100));
			g.fillOval(getXPos() + x - size * 1 / 10, getYPos() + y - size * 1 / 10, size * 12 / 10,
					size * 12 / 10);
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
