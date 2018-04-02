package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import model.Hole;
import model.Slide;
import model.Teleport;
import model.Tile;
import model.Wall;

public class RoomImage extends Image {
	private List<Tile> tiles = new ArrayList<Tile>();
	private String exits;

	public RoomImage(List<Tile> tiles, int scalex, int scaley, String room, String exits) {
		super(0, 0, scalex, scaley, room);
		this.tiles = tiles;
		this.exits = exits;
	}

	private Boolean checkExit(char exit) {
		Boolean result = false;
		for (int i = 0; i < exits.length(); i++) {
			if (exits.charAt(i) == exit) {
				result = true;
			}
		}
		return result;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}

	@Override
	public void drawThis(Graphics2D g, int x, int y) {
		g.setColor(Color.WHITE);
		if (checkExit('N')) {
			g.fillOval(scalex * 6, scaley / 2, scalex, scaley);
		}
		if (checkExit('S')) {
			g.fillOval(scalex * 6, scaley * 23 / 2, scalex, scaley);
		}
		if (checkExit('W')) {
			g.fillOval(scalex / 2, scaley * 6, scalex, scaley);
		}
		if (checkExit('E')) {
			g.fillOval(scalex * 23 / 2, scaley * 6, scalex, scaley);
		}
		g.setColor(Color.BLACK);
		for (Tile t : tiles) {
			if (t instanceof Wall) {
				g.setColor(new Color(0x444444));
				g.fillRect(t.getX() * scalex + getXPos() + x, t.getY() * scaley + getYPos() + y, scalex, scaley);
			} else if (t instanceof Teleport) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(t.getX() * scalex + getXPos() + x, t.getY() * scaley + getYPos() + y, scalex, scaley);

				int[] xCoords = { 0, scalex / 4, scalex / 2, scalex / 4 };
				int[] yCoords = { scaley / 4, 0, scaley / 4, scaley / 2 };
				Polygon p = new Polygon(xCoords, yCoords, 4);
				p.translate((t.getX() + 1) * scalex + getXPos() + x / 4, (t.getY() + 1) * scaley + getYPos() + y / 4);
				g.setColor(Color.BLACK);
				g.drawPolygon(p);

			} else if (t instanceof Slide) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(t.getX() * scalex + getXPos() + x, t.getY() * scaley + getYPos() + y, scalex, scaley);
				int[] xCoords = { 0, 0, 0 };
				int[] yCoords = { 0, 0, 0 };
				switch (((Slide) t).getDirection()) {
				case 'N':
					xCoords[1] = scalex / 4;
					xCoords[2] = scalex / 2;
					yCoords[0] = scaley / 2;
					yCoords[2] = scaley / 2;
					break;
				case 'E':
					xCoords[1] = scalex / 2;
					yCoords[1] = scaley / 4;
					yCoords[2] = scaley / 2;
					break;
				case 'S':
					xCoords[1] = scalex / 4;
					xCoords[2] = scalex / 2;
					yCoords[1] = scaley / 2;
					break;
				case 'W':
					xCoords[0] = scalex / 2;
					xCoords[2] = scalex / 2;
					yCoords[1] = scaley / 4;
					yCoords[2] = scaley / 2;
					break;
				default:
					break;
				}
				Polygon p = new Polygon(xCoords, yCoords, 3);
				p.translate((t.getX() + 1) * scalex + getXPos() + x / 4, (t.getY() + 1) * scaley + getYPos() + y / 4);
				g.setColor(Color.BLACK);
				g.drawPolygon(p);

			} else if (t instanceof Hole) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(t.getX() * scalex + getXPos() + x, t.getY() * scaley + getYPos() + y, scalex, scaley);
				g.setColor(Color.BLACK);
				g.fillOval(t.getX() * scalex + getXPos() + x, t.getY() * scaley + getYPos() + y, scalex, scaley);
			} else {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(t.getX() * scalex + getXPos() + x, t.getY() * scaley + getYPos() + y, scalex, scaley);
			}
			/* 
			 * g.drawString(String.valueOf(path[j][i]), t.getX() * scalex + 10 + xPos + x,
			 * t.getY() * scaley + 20 + yPos + y);
			 */
		}
		g.drawRect(scalex +

				getXPos(), scaley + getYPos(), scalex * 11, scaley * 11);

	}

}
