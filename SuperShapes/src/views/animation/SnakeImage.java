package views.animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import model.FileManager;

public class SnakeImage extends Image {
	public SnakeImage(String id, int x, int y, int scale, String room, String img, char direction) {
		super(x, y, scale, room, img, direction);
		setId(id);
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {
		BufferedImage image = FileManager.getImage(img);
		if (image != null) {
			double scalex = (double) scale / (double) image.getWidth();
			double scaley = (double) scale / (double) image.getHeight();

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
			int[] xCoords = { 0, scale / 2, scale, scale / 2 };
			int[] yCoords = { scale / 2, 0, scale / 2, scale };
			Polygon p = new Polygon(xCoords, yCoords, 4);
			p.translate(getXPos() + x, getYPos() + y);
			g.setColor(new Color(0, 100, 0));
			g.fillPolygon(p);
			g.setColor(Color.BLACK);
			g.drawPolygon(p);
		}
	}
}
