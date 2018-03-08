package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class TurretImage extends Image {
	private char direction;

	public TurretImage(String id, int x, int y, int scalex, int scaley, String room, char direction) {
		super(x, y, scalex, scaley, room);
		setId(id);
		this.direction = direction;
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {
		int[] xCoords = { 0, 0, 0 };
		int[] yCoords = { 0, 0, 0 };
		switch (direction) {
		case 'N':
			xCoords[1] = scalex / 2;
			xCoords[2] = scalex;
			yCoords[0] = scaley;
			yCoords[2] = scaley;
			break;
		case 'E':
			xCoords[1] = scalex;
			yCoords[1] = scaley / 2;
			yCoords[2] = scaley;
			break;
		case 'S':
			xCoords[1] = scalex / 2;
			xCoords[2] = scalex;
			yCoords[1] = scaley;
			break;
		case 'W':
			xCoords[0] = scalex;
			xCoords[2] = scalex;
			yCoords[1] = scaley / 2;
			yCoords[2] = scaley;
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
