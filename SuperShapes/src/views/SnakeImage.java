package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class SnakeImage extends Image {
	public SnakeImage(String id, int x, int y, int scale, String room) {
		super(x, y, scale, room);
		setId(id);
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {
		int[] xCoords = { 0, scale / 2, scale, scale / 2 };
		int[] yCoords = { scale / 2, 0, scale / 2, scale };
		Polygon p = new Polygon(xCoords, yCoords, 4);
		p.translate(getXPos() + x, getYPos() + y);
		g.setColor(new Color(0, 100	, 0));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);
	}
}
