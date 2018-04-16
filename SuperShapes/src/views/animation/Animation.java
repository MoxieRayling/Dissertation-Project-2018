package views.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import controller.Constants;
import model.Model;
import model.Room;
import model.entities.Block;
import model.entities.Entity;
import model.entities.Ghost;
import model.entities.Player;
import model.entities.Shot;
import model.entities.Snake;
import model.entities.SnakeBody;
import model.entities.Turret;
import views.Window;

@SuppressWarnings("serial")
public class Animation extends JPanel {
	private Timer timer;
	private int sizex = 512;
	private int sizey = 512;
	private int scale = sizex / 13;
	private List<Image> images = new ArrayList<Image>();
	private PlayerImage player = null;
	private RoomImage room = null;
	private String roomId = "";
	private int lives = 1;
	private int clock = 0;
	private int flyCooldown = 0;
	private int pauseCooldown = 0;
	private int shieldCooldown = 0;
	private int rewindCooldown = 0;
	private int fly = 0;
	private int pause = 0;
	private Boolean shield = false;
	private Window w;
	private String text = "";

	public Animation(Window w) {
		this.w = w;
		this.setBackground(Color.BLACK);
		start();
	}

	public void setSize(int sizex, int sizey) {
		this.sizex = sizex;
		this.sizey = sizey;
		int divisor = 16;
		if (room != null) {
			divisor = room.getSize() + 4;
		}
		setScale(divisor);
	}

	public void setScale(int size) {
		scale = Math.min(sizex, sizey) / (size + 3);
		for (Image i : images) {
			i.setScale(scale);
			i.update();
		}
	}

	public void updateScale() {
		setScale(Math.max(room.getxLength(), room.getyLength()));
	}
	
	private void removeRooms() {
		List<Image> remove = new ArrayList<Image>();
		for (Image i : images) {
			if (i instanceof RoomImage) {
				remove.add(i);
			}
		}
		for (Image i : remove) {
			images.remove(i);
		}
	}

	public void roomUpdate(Room r) {
		if (roomId != r.getId()) {
			setScale(Math.max(r.getxLength(), r.getyLength()));
			List<Image> remove = new ArrayList<Image>();
			for (Image i : images) {
				if (i.getRoom() != r.getId() && !(i instanceof PlayerImage || i instanceof RoomImage)) {
					remove.add(i);
				}
			}
			for (Image i : remove) {
				images.remove(i);
			}
			roomId = r.getId();
		}
		removeRooms();
		room = new RoomImage(r.getTiles(), scale, r.getxLength(), r.getyLength(), r.getId(), r.getExits());
		images.add(room);
	}

	public void playerUpdate(Player play) {
		lives = play.getLives();
		fly = play.getFly();
		flyCooldown = play.getFlyCooldown();
		pause = play.getPause();
		pauseCooldown = play.getPauseCooldown();
		rewindCooldown = play.getRewindCooldown();
		shield = play.getShield();
		shieldCooldown = play.getShieldCooldown();
		String img = "player.png";
		if (play.getImage() != null) {
			img = play.getImage();
		}
		if (player == null) {
			player = new PlayerImage(play.getX(), play.getY(), scale, play.getRoomId(), img);
			images.add(player);
		}
		player.setShield(play.getShield());
		player.setNoCollide(play.getFly() > 0);
		player.next(play.getX(), play.getY(), play.getTeleport());
		player.setDead(play.getDead());
	}

	public void modelUpdate(Model model) {
		clock = model.getClock();
		text = model.getText();
	}

	public void entityUpdate(Entity e) {
		Boolean exists = false;
		Image image = null;
		for (Image i : images) {
			if (i.getId() == e.getId()) {
				exists = true;
				image = i;
				i.next(e.getX(), e.getY(), e.getTeleport());
			}
		}
		if (e.getDelete()) {
			images.remove(image);
		}
		if (!exists) {
			createImage(e);
		}
	}

	public void createImage(Entity e) {
		if (e instanceof Block) {
			Block b = (Block) e;
			images.add(new BlockImage(b.getId(), b.getX(), b.getY(), scale, b.getRoomId(), e.getImage()));
		} else if (e instanceof Turret) {
			Turret t = (Turret) e;
			images.add(new TurretImage(t.getId(), t.getX(), t.getY(), scale, t.getRoomId(), t.getDirection(),
					e.getImage()));
		} else if (e instanceof Shot) {
			Shot s = (Shot) e;
			images.add(new ShotImage(s.getId(), s.getX(), s.getY(), scale, s.getRoomId(), s.getDirection(),
					e.getImage()));
		} else if (e instanceof SnakeBody) {
			SnakeBody sb = (SnakeBody) e;
			images.add(new SnakeImage(sb.getId(), sb.getX(), sb.getY(), scale, sb.getRoomId(), e.getImage()));
		} else if (e instanceof Ghost) {
			Ghost g = (Ghost) e;
			images.add(new GhostImage(g.getId(), g.getX(), g.getY(), scale, g.getRoomId(), e.getImage()));
		}
	}

	private void start() {
		timer = new Timer(1, new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				for (Image i : images) {
					i.Move();
				}
				repaint();
				playerCollide();
				Boolean stationary = true;
				for (Image i : images) {
					if (!i.getStationary()) {
						stationary = false;
					}
				}
				if (stationary) {
					if(lives == 0) {
						w.gameOver();
					}
					w.setInput(true);
					w.endTurn();
					if (player != null && player.getDead()) {
						w.restart();
						player.unshrink();
						player.setDead(false);
					}
				}
			}
		});
		timer.setRepeats(true);
		timer.setCoalesce(true);
		timer.start();
	}

	private void renderOrder() {
		List<Image> render = new ArrayList<Image>();
		render.add(room);
		for (Image i : images) {
			if (i instanceof BlockImage) {
				render.add(i);
			}
		}
		for (Image i : images) {
			if (i instanceof SnakeImage) {
				render.add(i);
			}
		}
		for (Image i : images) {
			if (i instanceof ShotImage) {
				render.add(i);
			}
		}
		for (Image i : images) {
			if (i instanceof TurretImage) {
				render.add(i);
			}
		}
		render.add(player);
		for (Image i : images) {
			if (images.contains(i) && !render.contains(i)) {
				render.add(i);
			}
		}
		images = render;
	}

	private void pauseAnimation() {
		for (Image i : images) {
			i.setXDest(i.getXPos());
			i.setYDest(i.getYPos());
		}
	}

	private void playerCollide() {
		timer.stop();
		for (Image i : images) {
			if (!player.getNoCollide() && !(i instanceof PlayerImage) && !(i instanceof RoomImage)
					&& !(i instanceof GhostImage) && i.getXPos() > player.getXPos() - 3
					&& i.getXPos() < player.getXPos() + 3 && i.getYPos() > player.getYPos() - 3
					&& i.getYPos() < player.getYPos() + 3) {
				pauseAnimation();
				player.setShrink(true);
				player.setDead(true);
				break;
			}
		}
		timer.start();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(512, 512);
	}

	@Override
	protected synchronized void paintComponent(Graphics g1) {
		timer.stop();
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int[] columns = { scale, scale * 4, scale * 7, scale * 10 };
		int[] rows = { 20, scale * (room.getyLength() + 2) };

		super.paintComponent(g);
		renderOrder();
		for (Image i : images) {
			i.drawThis(g, scale, scale);
		}
		g.setColor(Color.BLACK);

		drawHUD(g, columns, rows);
		if (text != "") {
			g.setColor(new Color(0, 0, 0, 200));
			g.fillRect(scale, scale * room.getyLength(), scale * room.getxLength(), scale);
			g.setColor(Color.WHITE);
			g.drawString(text, scale + 20, scale * 11 + 20);
		}
		timer.start();
	}

	public void drawHUD(Graphics2D g, int[] columns, int[] rows) {
		g.setColor(Color.WHITE);
		g.drawString(String.valueOf("Lives: " + lives), columns[0], rows[0]);
		g.drawString(String.valueOf("Steps: " + clock), columns[1], rows[0]);
		if (Constants.flight) {
			if (flyCooldown > 0) {
				g.setColor(Color.RED);
				g.drawString(String.valueOf("Fly " + flyCooldown), columns[1], rows[1]);
			} else if (fly == 0) {
				g.setColor(Color.GREEN);
				g.drawString(String.valueOf("'F' to Fly"), columns[1], rows[1]);

			} else {
				g.setColor(Color.GREEN);
				g.drawString(String.valueOf("Fly " + fly), columns[1], rows[1]);
			}
		}
		if (Constants.pause) {
			if (pauseCooldown > 0) {
				g.setColor(Color.RED);
				g.drawString(String.valueOf("Pause " + pauseCooldown), columns[0], rows[1]);
			} else if (pause == 0) {
				g.setColor(Color.GREEN);
				g.drawString(String.valueOf("'P' to Pause"), columns[0], rows[1]);

			} else {
				g.setColor(Color.GREEN);
				g.drawString(String.valueOf("Pause " + pause), columns[0], rows[1]);
			}
		}
		if (Constants.rewind) {
			if (rewindCooldown > 0) {
				g.setColor(Color.RED);
				g.drawString(String.valueOf("Rewind " + rewindCooldown), columns[2], rows[1]);
			} else {
				g.setColor(Color.GREEN);
				g.drawString(String.valueOf("'R' to Rewind "), columns[2], rows[1]);
			}
		}
		if (Constants.shield) {
			if (shieldCooldown > 0) {
				g.setColor(Color.RED);
				g.drawString(String.valueOf("Shield " + shieldCooldown), columns[3], rows[1]);
			} else if (shield) {
				g.setColor(Color.GREEN);
				g.drawString(String.valueOf("Shield is active"), columns[3], rows[1]);
			} else {
				g.setColor(Color.GREEN);
				g.drawString(String.valueOf("'S' to Shield "), columns[3], rows[1]);
			}
		}
	}

	public void nextText() {

	}
}
