package views.animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import controller.Constants;
import model.tiles.Coin;
import model.tiles.Hole;
import model.tiles.Key;
import model.tiles.Lock;
import model.tiles.Slide;
import model.tiles.Teleport;
import model.tiles.Tile;
import model.tiles.Wall;

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
			g.fillOval(scale * exits[0] + x, y - scale / 2, scale, scale);
		}
		if (exits[2] != -1) {
			g.fillOval(scale * exits[2] + x, y + scale * getyLength() - scale / 2, scale, scale);
		}
		if (exits[3] != -1) {
			g.fillOval(x - scale / 2, y + scale * exits[3], scale, scale);
		}
		if (exits[1] != -1) {
			g.fillOval(x + scale * getxLength() - scale / 2, y + scale * exits[1], scale, scale);
		}
		g.setColor(Color.BLACK);
		for (Tile t : tiles) {
			BufferedImage img = Constants.getImage(t.getImage());
			if (t instanceof Wall) {
				if (img != null) {
					g.drawImage(img, t.getX() * scale + x, t.getY() * scale + y, scale, scale, null);
				} else {
					g.setColor(new Color(0x444444));
					g.fillRect(t.getX() * scale + x, t.getY() * scale + y, scale, scale);
				}
			} else if (t instanceof Teleport) {
				if (img != null) {
					g.drawImage(img, t.getX() * scale + x, t.getY() * scale + y, scale, scale, null);
				} else {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(t.getX() * scale + getXPos() + x, t.getY() * scale + getYPos() + y, scale, scale);

					int[] xCoords = { 0, scale / 4, scale / 2, scale / 4 };
					int[] yCoords = { scale / 4, 0, scale / 4, scale / 2 };
					Polygon p = new Polygon(xCoords, yCoords, 4);
					p.translate(t.getX() * scale + x + scale / 4, t.getY() * scale + y + scale / 4);
					g.setColor(Color.BLACK);
					g.drawPolygon(p);
				}
			} else if (t instanceof Slide) {
				if (img != null) {
					g.drawImage(img, t.getX() * scale + x, t.getY() * scale + y, scale, scale, null);
				} else {
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
					p.translate(t.getX() * scale + x + scale / 4, t.getY() * scale + y + scale / 4);
					g.setColor(Color.BLACK);
					g.drawPolygon(p);
				}
			} else if (t instanceof Hole) {
				if (img != null) {
					g.drawImage(img, t.getX() * scale + x, t.getY() * scale + y, scale, scale, null);
				} else {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(t.getX() * scale + x, t.getY() * scale + y, scale, scale);
					g.setColor(Color.BLACK);
					g.fillOval(t.getX() * scale + x, t.getY() * scale + y, scale, scale);
				}
			} else if (t instanceof Key) {
				if (img != null) {
					g.drawImage(img, t.getX() * scale + x, t.getY() * scale + y, scale, scale, null);
				} else {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(t.getX() * scale + x, t.getY() * scale + y, scale, scale);
					g.setColor(Color.BLACK);
					g.drawString("K", t.getX() * scale + 10 + x, t.getY() * scale + 20 + y);
				}
			} else if (t instanceof Coin) {
				if (img != null) {
					g.drawImage(img, t.getX() * scale + x, t.getY() * scale + y, scale, scale, null);
				} else {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(t.getX() * scale + x, t.getY() * scale + y, scale, scale);
					g.setColor(Color.BLACK);
					g.drawString("C", t.getX() * scale + 10 + x, t.getY() * scale + 20 + y);
				}
			} else if (t instanceof Lock) {
				if (img != null) {
					g.drawImage(img, t.getX() * scale + x, t.getY() * scale + y, scale, scale, null);
				} else {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(t.getX() * scale + x, t.getY() * scale + y, scale, scale);
					g.setColor(Color.BLACK);
					g.drawString("L", t.getX() * scale + 10 + x, t.getY() * scale + 20 + y);
				}
			} else {
				if (img != null) {
					g.drawImage(img, t.getX() * scale + x, t.getY() * scale + y, scale, scale, null);
				} else {
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(t.getX() * scale + x, t.getY() * scale + y, scale, scale);
				}
			}

			g.setColor(Color.BLACK);
			// g.drawString(String.valueOf(t.getPath()), t.getX() * scale + 10 + x, t.getY()
			// * scale + 20 + y);
		}
		g.drawRect(x, y, scale * getxLength(), scale * getyLength());

	}

}
