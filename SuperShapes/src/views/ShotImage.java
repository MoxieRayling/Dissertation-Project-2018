package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class ShotImage extends Image {
	private char direction;

	public ShotImage(String id, int x, int y, int scalex, int scaley, String room, char direction) {
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
			xCoords[0] = scalex / 4;
			xCoords[1] = scalex / 2;
			xCoords[2] = 3 * scalex / 4;
			yCoords[0] = scaley;
			yCoords[1] = scaley / 2;
			yCoords[2] = scaley;
			break;
		case 'E':
			xCoords[1] = scalex / 2;
			yCoords[0] = scaley / 4;
			yCoords[1] = scaley / 2;
			yCoords[2] = 3 * scaley / 4;
			break;
		case 'S':
			xCoords[0] = scalex / 4;
			xCoords[1] = scalex / 2;
			xCoords[2] = 3 * scalex / 4;
			yCoords[1] = scaley / 2;
			break;
		case 'W':
			xCoords[0] = scalex;
			xCoords[1] = scalex / 2;
			xCoords[2] = scalex;
			yCoords[0] = scaley / 4;
			yCoords[1] = scaley / 2;
			yCoords[2] = 3 * scaley / 4;
			break;
		default:
			xCoords[0] = scalex / 4;
			xCoords[1] = scalex / 2;
			xCoords[2] = 3 * scalex / 4;
			yCoords[0] = scaley;
			yCoords[1] = scaley / 2;
			yCoords[2] = scaley;
			break;
		}
		Polygon p = new Polygon(xCoords, yCoords, 3);
		p.translate(getXPos() + x, getYPos() + y);
		g.setColor(Color.YELLOW);
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);
	}
}