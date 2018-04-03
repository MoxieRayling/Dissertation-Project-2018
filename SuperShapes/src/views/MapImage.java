package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class MapImage extends Image {

	private String[][] map = new String[11][11];

	public MapImage(String[][] map, int x, int y, int scalex, int scaley) {
		super(x, y, scalex, scaley, null);
		this.map = map;
		String line = "";
		for (int i = 10; i >= 0; i--) {
			for (int j = 10; j >= 0; j--) {
				line += map[i][j] + ",";
			}
			System.out.println(line);
			line = "";
		}
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {

		String tile = "";
		for (int i = 10; i >= 0; i--) {
			for (int j = 10; j >= 0; j--) {
				if (map[i][j] == null) {
					g.setColor(new Color(0x000000));
					g.fillRect(i * scalex + getXPos() + x, j * scaley + getYPos() + y, scalex, scaley);
				} else if (map[i][j].startsWith("R")) {
					g.setColor(new Color(0x999999));
					g.fillRect(i * scalex + getXPos() + x, j * scaley + getYPos() + y, scalex, scaley);
					g.setColor(new Color(0x000000));
					g.drawRect(i * scalex + getXPos() + x, j * scaley + getYPos() + y, scalex, scaley);
					if (map[i][j].contains("K")) {
						tile += "K";
					}
					if (map[i][j].contains("S")) {
						tile += "S";
					}
				}
				g.drawString(tile, i * scalex + getXPos() + x + scalex / 4,
						j * scaley + getYPos() + y + scaley * 3 / 2);
				tile = "";
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
