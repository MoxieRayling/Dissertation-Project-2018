package views.animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.FileManager;


public class PlayerImage extends Image {

	private Boolean shrink = false;
	private Boolean dead = false;
	private int shrinkCount = 0;
	private Boolean shield = false;
	private Boolean flying = false;

	public PlayerImage(int x, int y, int scale, String room, String img, char direction) {
		super(x, y, scale, room, img, direction);
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

	@Override
	public void drawThis(Graphics2D g, int xCoord, int yCoord) {

		int x = xCoord;
		int y = yCoord;
		if (flying) {
			g.setColor(new Color(0, 0, 0, 100));
			g.fillOval(getXPos() + x, getYPos() + y, size, size);
			y -= size / 3;
		}


		BufferedImage image = FileManager.getImage(img);
		if (image != null) {
			double scalex = (double) size / (double) image.getWidth();
			double scaley = (double) size / (double) image.getHeight();

			AffineTransform at = new AffineTransform();
			switch (direction) {
			case 'N':
				at.translate(x + xPos, y + yPos);
				at.scale(scalex, scaley);
				break;
			case 'E':
				at.translate(x + scale + xPos, y + yPos);
				at.rotate(Math.PI / 2);
				at.scale(scalex, scaley);
				break;
			case 'S':
				at.translate(x + scale + xPos, y + scale + yPos);
				at.rotate(Math.PI);
				at.scale(scalex, scaley);
				break;
			case 'W':
				at.translate(x + xPos, y + scale + yPos);
				at.rotate(3 * Math.PI / 2);
				at.scale(scalex, scaley);
				break;
			default:
				break;
			}
			if (image != null) {
				g.drawImage(image, at, null);
			}
		} else {
			g.setColor(Color.RED);
			g.fillOval(getXPos() + x, getYPos() + y, size, size);
			g.setColor(Color.BLACK);
			g.drawOval(getXPos() + x, getYPos() + y, size, size);
		}
		if (shield) {
			g.setColor(new Color(255, 0, 0, 100));
			g.fillOval(getXPos() + x - size * 1 / 10, getYPos() + y - size * 1 / 10, size * 12 / 10, size * 12 / 10);
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

	public Boolean getFlying() {
		return flying;
	}

	public void setFlying(Boolean flying) {
		this.flying = flying;
	}

}
