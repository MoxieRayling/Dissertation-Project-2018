package views.animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import model.FileManager;

public class ShotImage extends Image {

	public ShotImage(String id, int x, int y, int scale, String room, char direction, String img) {
		super(x, y, scale, room, img, direction);
		setId(id);
		this.direction = direction;
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {
		BufferedImage image = FileManager.getImage(img);
		double scalex = 0;
		double scaley = 0;
		if (image != null) {
			scalex = (double) scale / (double) image.getWidth();
			scaley = (double) scale / (double) image.getHeight();
		}

		int[] xCoords = { 0, 0, 0 };
		int[] yCoords = { 0, 0, 0 };
		AffineTransform at = new AffineTransform();
		switch (direction) {
		case 'N':
			xCoords[0] = scale / 4;
			xCoords[1] = scale / 2;
			xCoords[2] = 3 * scale / 4;
			yCoords[0] = scale;
			yCoords[1] = scale / 2;
			yCoords[2] = scale;
			at.translate(x + xPos, y + yPos);
			at.scale(scalex, scaley);
			break;
		case 'E':
			xCoords[1] = scale / 2;
			yCoords[0] = scale / 4;
			yCoords[1] = scale / 2;
			yCoords[2] = 3 * scale / 4;
			at.translate(x + scale + xPos, y + yPos);
			at.rotate(Math.PI / 2);
			at.scale(scalex, scaley);
			break;
		case 'S':
			xCoords[0] = scale / 4;
			xCoords[1] = scale / 2;
			xCoords[2] = 3 * scale / 4;
			yCoords[1] = scale / 2;
			at.translate(x + scale + xPos, y + scale + yPos);
			at.rotate(Math.PI);
			at.scale(scalex, scaley);
			break;
		case 'W':
			xCoords[0] = scale;
			xCoords[1] = scale / 2;
			xCoords[2] = scale;
			yCoords[0] = scale / 4;
			yCoords[1] = scale / 2;
			yCoords[2] = 3 * scale / 4;
			at.translate(x + xPos, y + scale + yPos);
			at.rotate(3 * Math.PI / 2);
			at.scale(scalex, scaley);
			break;
		default:
			xCoords[0] = scale / 4;
			xCoords[1] = scale / 2;
			xCoords[2] = 3 * scale / 4;
			yCoords[0] = scale;
			yCoords[1] = scale / 2;
			yCoords[2] = scale;
			break;
		}
		if (image != null) {
			g.drawImage(image, at, null);
		} else {
			Polygon p = new Polygon(xCoords, yCoords, 3);
			p.translate(getXPos() + x, getYPos() + y);
			g.setColor(new Color(255, 255, 0));
			g.fillPolygon(p);
			g.setColor(Color.BLACK);
			g.drawPolygon(p);
		}
	}
}