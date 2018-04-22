package views.animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import model.FileManager;

public class MapImage extends Image {

	private String[][] map = new String[11][11];

	public MapImage(String[][] map, int x, int y, int scale) {
		super(x, y, scale, null);
		this.map = map;
	}

	public void setMap(String[][] map) {
		this.map = map;
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {

		BufferedImage player = FileManager.getImage("player.png");
		BufferedImage goal = FileManager.getImage("goal.png");
		BufferedImage empty = FileManager.getImage("empty.png");
		BufferedImage lock = FileManager.getImage("lock.png");
		BufferedImage key = FileManager.getImage("key.png");
		BufferedImage coin = FileManager.getImage("coin.png");

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
					if (map[i][j].contains("C")) {
						tile += "C";
					}
					if (map[i][j].contains("L")) {
						tile += "L";
					}
					if (map[i][j].contains("G")) {
						tile += "G";
					}
				}
				g.drawString(tile, i * scale + getXPos() + x + scale / 4,
						(j - 1) * scale + getYPos() + y + scale * 3 / 2);
				tile = "";
				if (map[i][j] != null && map[i][j].startsWith("R") && empty != null) {
					g.drawImage(empty, i * scale + getXPos() + x, j * scale + getYPos() + y, scale, scale, null);
				}
				if (map[i][j] != null && map[i][j].contains("G") && goal != null) {
					g.drawImage(goal, i * scale + getXPos() + scale / 2 + x, j * scale + scale / 2 + getYPos() + y,
							scale / 2, scale / 2, null);
				}
				if (map[i][j] != null && map[i][j].contains("K") && key != null) {
					g.drawImage(key, i * scale + getXPos() + x, j * scale + getYPos() + y, scale / 2, scale / 2, null);
				}
				if (map[i][j] != null && map[i][j].contains("C") && coin != null) {
					g.drawImage(coin, i * scale + getXPos() + x + scale / 2, j * scale + getYPos() + y, scale / 2,
							scale / 2, null);
				}
				if (map[i][j] != null && map[i][j].contains("L") && lock != null) {
					g.drawImage(lock, i * scale + getXPos() + x, j * scale + getYPos() + y + scale / 2, scale / 2,
							scale / 2, null);
				}
				if (map[i][j] != null && map[i][j].contains("P") && player != null) {
					g.drawImage(player, i * scale + getXPos() + x, j * scale + getYPos() + y, scale, scale, null);
				}
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
