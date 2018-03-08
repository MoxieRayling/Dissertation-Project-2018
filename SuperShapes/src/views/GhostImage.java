package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class GhostImage extends Image {
	public GhostImage(String id, int x, int y, int scalex, int scaley, String room) {
		super(x, y, scalex, scaley, room);
		setId(id);
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {

		int[] xCoords = { 0, xSize / 2, xSize, xSize / 4 * 3, xSize, xSize / 2, 0, xSize / 4 };
		int[] yCoords = { 0, ySize / 4, 0, ySize / 2, ySize, ySize * 3 / 4, ySize, ySize / 2 };
		Polygon p = new Polygon(xCoords, yCoords, 8);
		p.translate(getXPos() + x, getYPos() + y);
		g.setColor(new Color(50, 0, 100));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);
	}
}
