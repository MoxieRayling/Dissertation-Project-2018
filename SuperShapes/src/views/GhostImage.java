package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class GhostImage extends Image {

	public GhostImage(String id, int x, int y, int scale, String room) {
		super(x, y, scale, room);
		setId(id);
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {
		int[] xCoords = { 0, size / 2, size, size / 4 * 3, size, size / 2, 0, size / 4 };
		int[] yCoords = { 0, size / 4, 0, size / 2, size, size * 3 / 4, size, size / 2 };
		Polygon p = new Polygon(xCoords, yCoords, 8);
		p.translate(getXPos() + x, getYPos() + y);
		g.setColor(new Color(50, 0, 100));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);
	}
}
