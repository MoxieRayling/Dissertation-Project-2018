package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import controller.Constants;

public class MapImage extends Image implements Constants {

	private String[][] map = new String[11][11];

	public MapImage(String[][] map, int x, int y, int scale) {
		super(x, y, scale, null);
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
					g.fillRect(i * scale + getXPos() + x, j * scale + getYPos() + y, scale, scale);
				} else if (map[i][j].startsWith("R")) {
					g.setColor(new Color(0x999999));
					g.fillRect(i * scale + getXPos() + x, j * scale + getYPos() + y, scale, scale);
					g.setColor(new Color(0x000000));
					g.drawRect(i * scale + getXPos() + x, j * scale + getYPos() + y, scale, scale);
					if (map[i][j].contains("K")) {
						tile += "K";
					}
					if (map[i][j].contains("S")) {
						tile += "S";
					}
				}
				g.drawString(tile, i * scale + getXPos() + x + scale / 4,
						j * scale + getYPos() + y + scale * 3 / 2);
				tile = "";
			}
		}
		int[] xCoords = { 0, 0, 0 };
		int[] yCoords = { 0, 0, 0 };

		xCoords[1] = scale / 2;
		xCoords[2] = scale;
		yCoords[0] = scale;
		yCoords[2] = scale;
		Polygon p = new Polygon(xCoords, yCoords, 3);
		p.translate(getXPos() + x + scale * 5, getYPos() + y - scale);
		g.setColor(new Color(255, 255, 255));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);

		xCoords[2] = 0;
		yCoords[0] = 0;

		xCoords[1] = scale;
		yCoords[1] = scale / 2;
		yCoords[2] = scale;
		p = new Polygon(xCoords, yCoords, 3);
		p.translate(getXPos() + x + scale * 11, getYPos() + y + scale * 5);
		g.setColor(new Color(255, 255, 255));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);

		yCoords[2] = 0;

		xCoords[1] = scale / 2;
		xCoords[2] = scale;
		yCoords[1] = scale;
		p = new Polygon(xCoords, yCoords, 3);
		p.translate(getXPos() + x + scale * 5, getYPos() + y + scale * 11);
		g.setColor(new Color(255, 255, 255));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);

		xCoords[1] = 0;

		xCoords[0] = scale;
		xCoords[2] = scale;
		yCoords[1] = scale / 2;
		yCoords[2] = scale;
		p = new Polygon(xCoords, yCoords, 3);
		p.translate(getXPos() + x - scale, getYPos() + y + scale * 5);
		g.setColor(new Color(255, 255, 255));
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);

	}

}
