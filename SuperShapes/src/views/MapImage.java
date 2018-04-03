package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class MapImage extends Image {

	private int[][] map = new int[11][11];

	public MapImage(int[][] map, int x, int y, int scalex, int scaley, String room) {
		super(x, y, scalex, scaley, room);
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {

		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				if (map[i][j] == 0) {
					g.setColor(new Color(0x000000));
					g.fillRect(i * scalex + getXPos() + x, j * scaley + getYPos() + y, scalex, scaley);
				} else if (map[i][j] == 1) {
					g.setColor(new Color(0x444444));
					g.fillRect(i * scalex + getXPos() + x, j * scaley + getYPos() + y, scalex, scaley);
					g.setColor(new Color(0x000000));
					g.drawRect(i * scalex + getXPos() + x, j * scaley + getYPos() + y, scalex, scaley);
				} else if (map[i][j] == 2) {
					g.setColor(new Color(0x444444));
					g.fillRect(i * scalex + getXPos() + x, j * scaley + getYPos() + y, scalex, scaley);
					g.setColor(new Color(0x000000));
					g.drawOval(i * scalex + getXPos() + x, j * scaley + getYPos() + y, scalex, scaley);
				} else if (map[i][j] == 3) {
					g.setColor(new Color(0x444444));
					g.fillRect(i * scalex + getXPos() + x, j * scaley + getYPos() + y, scalex, scaley);
					g.setColor(new Color(0x000000));
					g.fillOval(i * scalex + getXPos() + x + x / 2, j * scaley + getYPos() + y + y / 4, scalex / 4,
							scaley / 4);
				}
			}
		}
		int[] xCoords = { 0, 0, 0 };
		int[] yCoords = { 0, 0, 0 };

		xCoords[1] = scalex / 2;
		xCoords[2] = scalex;
		yCoords[0] = scaley;
		yCoords[2] = scaley;
		Polygon p = new Polygon(xCoords, yCoords, 3);
		p.translate(getXPos() + x + scalex * 5, getYPos() + y - scaley);
		g.setColor(new Color(255, 255, 255));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);

		xCoords[2] = 0;
		yCoords[0] = 0;

		xCoords[1] = scalex;
		yCoords[1] = scaley / 2;
		yCoords[2] = scaley;
		p = new Polygon(xCoords, yCoords, 3);
		p.translate(getXPos() + x + scalex * 11, getYPos() + y + scaley * 5);
		g.setColor(new Color(255, 255, 255));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);

		yCoords[2] = 0;

		xCoords[1] = scalex / 2;
		xCoords[2] = scalex;
		yCoords[1] = scaley;
		p = new Polygon(xCoords, yCoords, 3);
		p.translate(getXPos() + x + scalex * 5, getYPos() + y + scaley * 11);
		g.setColor(new Color(255, 255, 255));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);

		xCoords[1] = 0;

		xCoords[0] = scalex;
		xCoords[2] = scalex;
		yCoords[1] = scaley / 2;
		yCoords[2] = scaley;
		p = new Polygon(xCoords, yCoords, 3);
		p.translate(getXPos() + x - scalex, getYPos() + y + scaley * 5);
		g.setColor(new Color(255, 255, 255));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);

	}

}
