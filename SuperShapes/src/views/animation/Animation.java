package views.animation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

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
	public Boolean canFly = false;
	public Boolean canPause = false;
	public Boolean canRewind = false;
	public Boolean canShield = false;
	private int fly = 0;
	private int pause = 0;
	private int coins = 0;
	private Boolean shield = false;
	private Window w;
	private String bufferText = "";
	private String displayText = "";
	private boolean winner = false;
	private boolean endGame = false;
	private String keys = "";

	public Animation(Window w) {
		this.w = w;
		this.setBackground(new Color(0x990000));
		start();
	}

	public void setSize(int sizex, int sizey) {
		this.sizex = sizex;
		this.sizey = sizey;
		int divisor = 16;
		if (room != null) {
			divisor = Math.max(room.getxLength(), room.getyLength());
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
		clock = r.getClock();
		if (!r.getText().equals("")) {
			setBufferText(r.getText());
			w.setMode("text");
		}
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
		if (getGraphics() != null)
			room.drawThis((Graphics2D) this.getGraphics(), scale, scale);
	}

	public void playerUpdate(Player play) {
		coins = play.getCoins();
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
			player = new PlayerImage(play.getX(), play.getY(), scale, play.getRoomId(), img, play.getDirection());
			images.add(player);
		}
		if (play.getFly() > 0) {
			player.setFlying(true);
		} else
			player.setFlying(false);

		canFly = play.getCanFly();
		canPause = play.getCanPause();
		canRewind = play.getCanRewind();
		canShield = play.getCanShield();
		player.setShield(play.getShield());
		player.setNoCollide(play.getFly() > 0);
		player.next(play.getX(), play.getY(), play.getTeleport());
		player.setDead(play.getDead());
		player.setDirection(play.getDirection());
		String keys = "";
		List<String> keyList = play.getKeys();
		for (String s : keyList) {
			keys += s;
			if (keyList.indexOf(s) < keyList.size() - 1 && !(keyList.indexOf(s) == 0)) {
				keys += ", ";
			}
		}
		this.keys = keys;

	}

	public void entityUpdate(Entity e) {
		Boolean exists = false;
		Image image = null;
		for (Image i : images) {
			if (i.getId() == e.getId()) {
				exists = true;
				image = i;
				i.next(e.getX(), e.getY(), e.getTeleport());
				i.setDirection(e.getDirection());
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
			images.add(new BlockImage(b.getId(), b.getX(), b.getY(), scale, b.getRoomId(), e.getImage(),
					e.getDirection()));
		} else if (e instanceof Turret) {
			Turret t = (Turret) e;
			images.add(new TurretImage(t.getId(), t.getX(), t.getY(), scale, t.getRoomId(), t.getDirection(),
					e.getImage()));
		} else if (e instanceof Shot) {
			Shot s = (Shot) e;
			images.add(
					new ShotImage(s.getId(), s.getX(), s.getY(), scale, s.getRoomId(), s.getDirection(), e.getImage()));
		} else if (e instanceof SnakeBody) {
			SnakeBody sb = (SnakeBody) e;
			images.add(new SnakeImage(sb.getId(), sb.getX(), sb.getY(), scale, sb.getRoomId(), e.getImage(),
					e.getDirection()));
		} else if (e instanceof Ghost) {
			Ghost g = (Ghost) e;
			images.add(new GhostImage(g.getId(), g.getX(), g.getY(), scale, g.getRoomId(), e.getImage(),
					e.getDirection()));
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
					if (lives < 0) {
						w.gameOver("Game Over",
								"<html>You ran out of lives and lost the game. Please choose a save file to load and try again.");
						lives = 5;
					}
					if (endGame) {
						if (winner) {
							w.gameOver("You win",
									"<html><div style='text-align: center;'>Congratulations! You completed the game in "
											+ clock + " steps and " + "collected " + coins + "/" + w.getCoins()
											+ " coins.");
							setEndGame(false);
						} else {
							w.gameOver("You Lose",
									"<html><div style='text-align: center;'>Unlucky! You managed to get to room "
											+ roomId + " and " + "collected " + coins + "/" + w.getCoins() + " coins.");
							setEndGame(false);
						}
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
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	@Override
	protected synchronized void paintComponent(Graphics g1) {
		timer.stop();
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		super.paintComponent(g);
		renderOrder();
		for (Image i : images) {
			i.drawThis(g, scale, scale);
		}
		g.setColor(Color.BLACK);

		drawHUD(g, scale * 13, 60);
		if (displayText != "") {
			g.setColor(new Color(0, 0, 0, 200));
			g.fillRect(scale, scale * room.getyLength(), scale * room.getxLength(), scale);
			g.setColor(Color.WHITE);

			g.setFont(new Font("Arial", Font.BOLD, 26));
			g.drawString(displayText, scale + 20, scale * 11 + 40);
		}
		timer.start();
	}

	public void drawHUD(Graphics2D g, int x, int y) {
		g.setFont(new Font("Arial", Font.PLAIN, 30));
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf("Lives remaining: " + lives), x, y + 20);
		g.drawString(String.valueOf("Steps taken: " + clock), x, y * 2 + 20);
		g.drawString(String.valueOf("Coins collected: " + coins), x, y * 3 + 20);
		g.drawString(String.valueOf("Press the arrow keys to move"), x, y * 4 + 20);
		g.drawString(String.valueOf("Press 'ESC' to pause the game"), x, y * 5 + 20);
		g.drawString(String.valueOf("and view the map"), x, y * 6 + 20);
		if (canFly) {
			if (flyCooldown > 0) {
				g.drawString(String.valueOf("Flight cooldown: " + flyCooldown), x, y * 8 + 20);
			} else if (fly == 0) {
				g.drawString(String.valueOf("Press 'F' to Fly"), x, y * 8 + 20);

			} else {
				g.drawString(String.valueOf("Flight activated: " + fly), x, y * 8 + 20);
			}
		} else {
			g.drawString(String.valueOf("Flight Locked"), x, y * 8 + 20);
		}
		if (canPause) {
			if (pauseCooldown > 0) {
				g.drawString(String.valueOf("Pause cooldown: " + pauseCooldown), x, y * 9 + 20);
			} else if (pause == 0) {
				g.drawString(String.valueOf("Press 'P' to Pause"), x, y * 9 + 20);

			} else {
				g.drawString(String.valueOf("Pause activated: " + pause), x, y * 9 + 20);
			}
		} else {
			g.drawString(String.valueOf("Pause Locked"), x, y * 9 + 20);
		}
		if (canRewind) {
			if (rewindCooldown > 0) {
				g.drawString(String.valueOf("Rewind cooldown: " + rewindCooldown), x, y * 10 + 20);
			} else {
				g.drawString(String.valueOf("Press 'R' to Rewind "), x, y * 10 + 20);
			}
		} else {
			g.drawString(String.valueOf("Rewind Locked"), x, y * 10 + 20);
		}
		if (canShield) {
			if (shieldCooldown > 0) {
				g.drawString(String.valueOf("Shield cooldown: " + shieldCooldown), x, y * 7 + 20);
			} else if (shield) {
				g.drawString(String.valueOf("Shield is active"), x, y * 7 + 20);
			} else {
				g.drawString(String.valueOf("Press 'S' to activate Shield "), x, y * 7 + 20);
			}
		} else {
			g.drawString(String.valueOf("Shield Locked"), x, y * 7 + 20);
		}
		g.drawString(String.valueOf("Keys: " + keys), x, y * 11 + 20);
	}

	public void nextText() {
		if (bufferText.equals("")) {
			setDisplayText("");
			w.setTextRead(true);
		} else {
			String text = getBufferText();
			int end = 0;
			if (text.length() >= 50) {
				end = text.substring(0, 50).lastIndexOf(" ");
				setDisplayText(text.substring(0, end));
				setBufferText(text.substring(end + 1));
			} else {
				setDisplayText(text);
				setBufferText("");
			}
		}
	}

	public String getBufferText() {
		return bufferText;
	}

	private void setBufferText(String text) {
		bufferText = text;
		System.out.println("yes");
	}

	private void setDisplayText(String text) {
		displayText = text;
	}

	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}
}
