package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class TurretImage extends Image {
	private char direction;

	public TurretImage(String id, int x, int y, int scale, String room, char direction, String img, String dir) {
		super(x, y, scale, room, img, "turret.png", dir);
		setId(id);
		this.direction = direction;
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {
		if (image != null)
			g.drawImage(image, getXPos() + x, getYPos() + y, size, size, null);
		else {
			int[] xCoords = { 0, 0, 0 };
			int[] yCoords = { 0, 0, 0 };
			switch (direction) {
			case 'N':
				xCoords[1] = scale / 2;
				xCoords[2] = scale;
				yCoords[0] = scale;
				yCoords[2] = scale;
				break;
			case 'E':
				xCoords[1] = scale;
				yCoords[1] = scale / 2;
				yCoords[2] = scale;
				break;
			case 'S':
				xCoords[1] = scale / 2;
				xCoords[2] = scale;
				yCoords[1] = scale;
				break;
			case 'W':
				xCoords[0] = scale;
				xCoords[2] = scale;
				yCoords[1] = scale / 2;
				yCoords[2] = scale;
				break;
			default:
				break;
			}
			Polygon p = new Polygon(xCoords, yCoords, 3);
			p.translate(getXPos() + x, getYPos() + y);
			g.setColor(new Color(255, 255, 0));
			g.fillPolygon(p);
			g.setColor(Color.BLACK);
			g.drawPolygon(p);
		}
	}
}
