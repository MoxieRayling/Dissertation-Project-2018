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
	private int[] exits;
	private int xLength = 11;
	private int yLength = 11;

	public RoomImage(List<Tile> tiles, int scale, int xLength, int yLength, String room, int[] exits) {
		super(0, 0, scale, room);
		this.tiles = tiles;
		this.exits = exits;
		this.xLength = xLength;
		this.yLength = yLength;
	}

	public int getxLength() {
		return xLength;
	}

	public int getyLength() {
		return yLength;
	}

	public void setxLength(int xLength) {
		this.xLength = xLength;
	}

	public void setyLength(int yLength) {
		this.yLength = yLength;
	}

	public int getSize() {
		int result = 0;
		for (Tile t : tiles) {
			if (t.getX() > result) {
				result = t.getX();
			}
			if (t.getY() > result) {
				result = t.getY();
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

		if (exits[0] != -1) {
			g.fillOval(scale * exits[0] + x, scale / 2, scale, scale);
		}
		if (exits[2] != -1) {
			g.fillOval(scale * exits[2] + x, scale * getyLength() + scale / 2, scale, scale);
		}
		if (exits[3] != -1) {
			g.fillOval(-scale / 2 + x, scale * (exits[3] + 1), scale, scale);
		}
		if (exits[1] != -1) {
			g.fillOval(scale * getxLength() + scale / 2, scale * (exits[1] + 1), scale, scale);
		}
		g.setColor(Color.BLACK);
		for (Tile t : tiles) {
			if (t instanceof Wall) {
				g.setColor(new Color(0x444444));
				g.fillRect(t.getX() * scale + getXPos() + x, t.getY() * scale + getYPos() + y, scale, scale);
			} else if (t instanceof Teleport) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(t.getX() * scale + getXPos() + x, t.getY() * scale + getYPos() + y, scale, scale);

				int[] xCoords = { 0, scale / 4, scale / 2, scale / 4 };
				int[] yCoords = { scale / 4, 0, scale / 4, scale / 2 };
				Polygon p = new Polygon(xCoords, yCoords, 4);
				p.translate((t.getX() + 1) * scale + getXPos() + x / 4, (t.getY() + 1) * scale + getYPos() + y / 4);
				g.setColor(Color.BLACK);
				g.drawPolygon(p);

			} else if (t instanceof Slide) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(t.getX() * scale + getXPos() + x, t.getY() * scale + getYPos() + y, scale, scale);
				int[] xCoords = { 0, 0, 0 };
				int[] yCoords = { 0, 0, 0 };
				switch (((Slide) t).getDirection()) {
				case 'N':
					xCoords[1] = scale / 4;
					xCoords[2] = scale / 2;
					yCoords[0] = scale / 2;
					yCoords[2] = scale / 2;
					break;
				case 'E':
					xCoords[1] = scale / 2;
					yCoords[1] = scale / 4;
					yCoords[2] = scale / 2;
					break;
				case 'S':
					xCoords[1] = scale / 4;
					xCoords[2] = scale / 2;
					yCoords[1] = scale / 2;
					break;
				case 'W':
					xCoords[0] = scale / 2;
					xCoords[2] = scale / 2;
					yCoords[1] = scale / 4;
					yCoords[2] = scale / 2;
					break;
				default:
					break;
				}
				Polygon p = new Polygon(xCoords, yCoords, 3);
				p.translate((t.getX()) * scale + getXPos() + x, (t.getY() + 1) * scale + getYPos() + y / 4);
				g.setColor(Color.BLACK);
				g.drawPolygon(p);

			} else if (t instanceof Hole) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(t.getX() * scale + getXPos() + x, t.getY() * scale + getYPos() + y, scale, scale);
				g.setColor(Color.BLACK);
				g.fillOval(t.getX() * scale + getXPos() + x, t.getY() * scale + getYPos() + y, scale, scale);
			} else {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(t.getX() * scale + getXPos() + x, t.getY() * scale + getYPos() + y, scale, scale);
			}
			g.setColor(Color.BLACK);
			g.drawString(String.valueOf(t.getPath()), t.getX() * scale + 10 + xPos + x,
					t.getY() * scale + 20 + yPos + y);
		}
		g.drawRect(getXPos() + x, getYPos() + y, scale * getxLength(), scale * getyLength());

	}

}
