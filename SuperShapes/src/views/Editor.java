package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import controller.Constants;
import model.Block;
import model.Entity;
import model.Ghost;
import model.Room;
import model.SnakeBody;
import model.Turret;

@SuppressWarnings("serial")
public class Editor extends JPanel implements Constants {
	private Timer timer;
	private Window w;
	private int sizex = 512;
	private int sizey = 512;
	private int scalex = sizex / 13;
	private int scaley = sizey / 13;
	private List<Image> images = new ArrayList<Image>();
	private RoomImage room = null;
	private List<JButton> buttons = new ArrayList<JButton>();
	private List<Component> snakeOptions = new ArrayList<Component>();
	private JSlider snakeSize;
	private List<Component> ghostOptions = new ArrayList<Component>();
	private JSlider ghostPower;
	private List<Component> turretOptions = new ArrayList<Component>();
	private JSlider turretRate;
	private JSlider turretDelay;
	private String[] directions;
	private JComboBox<String> turretDirectionBox;
	private String[] entities;
	private JComboBox<String> entitiesBox;
	private String[] tiles;
	private JSlider teleportX;
	private JSlider teleportY;
	private List<Component> teleportOptions = new ArrayList<Component>();
	private JComboBox<String> slideDirectionBox;
	private List<Component> slideOptions = new ArrayList<Component>();
	private JComboBox<String> tilesBox;
	private int selectedX = 1;
	private int selectedY = 1;
	private List<List<Component>> entityComponents = new ArrayList<List<Component>>();
	private List<List<Component>> tileComponents = new ArrayList<List<Component>>();

	public Editor(Window w) {
		this.w = w;
		this.setLayout(null);
		initUI();
		this.setBackground(new Color(0x222222));
		start();
	}

	private void initUI() {
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				buttons.add(new JButton());
			}
		}
		for (JButton b : buttons) {
			int x = buttons.indexOf(b) % 11;
			int y = buttons.indexOf(b) / 11;
			b.setBounds(scalex * (x + 1), scaley * (y + 1), scalex, scaley);
			b.setOpaque(false);
			b.setContentAreaFilled(false);
			b.setBorderPainted(false);
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectedX = x;
					selectedY = y;
					w.addToRoom(generateTile(), x, y);
				}
			});
			this.add(b);
		}

		entities = new String[] { "None", "Block", "Snake", "Ghost", "Turret" };
		entitiesBox = new JComboBox<String>(entities);
		entitiesBox.setBounds(scalex * 13, scaley, scalex * 2, 20);
		entitiesBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideEntityMenu();
					switch (e.getItem().toString()) {
					case "Block":
						break;
					case "Snake":
						for (Component c : snakeOptions) {
							c.setVisible(true);
						}
						break;
					case "Turret":
						for (Component c : turretOptions) {
							c.setVisible(true);
						}
						break;
					case "Ghost":
						for (Component c : ghostOptions) {
							c.setVisible(true);
						}
						break;
					case "None":
						break;
					default:
						break;
					}
				}
			}
		});
		this.add(entitiesBox);

		tiles = new String[] { "Empty", "Wall", "Slide", "Teleport", "Hole" };
		tilesBox = new JComboBox<String>(tiles);
		tilesBox.setBounds(scalex * 16, scaley, scalex * 2, 20);
		tilesBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideTileMenu();
					switch (e.getItem().toString()) {
					case "Empty":
						break;
					case "Wall":
						break;
					case "Slide":
						slideDirectionBox.setVisible(true);
						break;
					case "Teleport":
						teleportX.setVisible(true);
						teleportY.setVisible(true);
						break;
					case "Hole":
						break;
					default:
						break;
					}
				}
			}
		});
		this.add(tilesBox);

		directions = new String[] { "North", "East", "South", "West" };
		turretDirectionBox = new JComboBox<String>(directions);
		turretOptions.add(turretDirectionBox);
		turretRate = new JSlider(1, 11, 3);
		turretOptions.add(turretRate);
		turretDelay = new JSlider(0, 122, 0);
		turretOptions.add(turretDelay);

		snakeSize = new JSlider(1, 122, 5);
		snakeOptions.add(snakeSize);

		ghostPower = new JSlider(1, 122, 3);
		ghostOptions.add(ghostPower);

		entityComponents.add(snakeOptions);
		entityComponents.add(turretOptions);
		entityComponents.add(ghostOptions);
		for (List<Component> l : entityComponents) {
			for (Component c : l) {
				c.setBounds(scalex * 13, scaley * (l.indexOf(c) + 2), scalex * 2, 20);
				c.setVisible(false);
				this.add(c);
			}
		}

		teleportX = new JSlider(0, 10, 0);
		teleportY = new JSlider(0, 10, 0);
		teleportOptions.add(teleportX);
		teleportOptions.add(teleportY);
		slideDirectionBox = new JComboBox<String>(directions);
		slideOptions.add(slideDirectionBox);
		tileComponents.add(teleportOptions);
		tileComponents.add(slideOptions);
		for (List<Component> l : tileComponents) {
			for (Component c : l) {
				c.setBounds(scalex * 16, scaley * (l.indexOf(c) + 2), scalex * 2, 20);
				c.setVisible(false);
				this.add(c);
			}
		}

	}

	private void hideEntityMenu() {
		for (List<Component> l : entityComponents) {
			for (Component c : l) {
				c.setVisible(false);
			}
		}
	}

	private void hideTileMenu() {
		for (List<Component> l : tileComponents) {
			for (Component c : l) {
				c.setVisible(false);
			}
		}
	}

	private String[] generateTile() {
		String[] result = new String[2];
		switch (entitiesBox.getSelectedItem().toString()) {
		case "Block":
			result[0] = "b " + selectedX + "," + selectedY;
			break;
		case "Snake":
			result[0] = "s " + selectedX + "," + selectedY + " " + snakeSize.getValue();
			break;
		case "Turret":
			result[0] = "t " + selectedX + "," + selectedY + " " + turretRate.getValue() + " "
					+ turretDirectionBox.getSelectedItem().toString() + " " + turretDelay.getValue();
			break;
		case "Ghost":
			result[0] = "g " + selectedX + "," + selectedY + " " + ghostPower.getValue();
			break;
		default:
			break;
		}
		switch (tilesBox.getSelectedItem().toString()) {
		case "Empty":
			result[1] = "E " + selectedX + "," + selectedY;
			break;
		case "Wall":
			result[1] = "W " + selectedX + "," + selectedY;
			break;
		case "Slide":
			result[1] = "S " + selectedX + "," + selectedY + " " + slideDirectionBox.getSelectedItem().toString();
			break;
		case "Teleport":
			result[1] = "T " + selectedX + "," + selectedY + " " + teleportX.getValue() + "," + teleportY.getValue();
			break;
		case "Hole":
			result[1] = "H " + selectedX + "," + selectedY;
			break;
		default:
			break;
		}
		return result;
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
		images.clear();
		images.add(room);
		for (Entity e : r.getEnemies()) {
			createImage(e);
		}

		removeRooms();
		room = new RoomImage(r.getTiles(), scalex, scaley, r.getId(), r.getExits());
		images.add(room);
	}

	public void createImage(Entity e) {
		if (e instanceof Block) {
			Block b = (Block) e;
			images.add(new BlockImage(b.getId(), b.getX(), b.getY(), scalex, scaley, b.getRoomId()));
		} else if (e instanceof Turret) {
			Turret t = (Turret) e;
			images.add(new TurretImage(t.getId(), t.getX(), t.getY(), scalex, scaley, t.getRoomId(), t.getDirection()));
		} else if (e instanceof SnakeBody) {
			SnakeBody sb = (SnakeBody) e;
			images.add(new SnakeImage(sb.getId(), sb.getX(), sb.getY(), scalex, scaley, sb.getRoomId()));
		} else if (e instanceof Ghost) {
			Ghost g = (Ghost) e;
			images.add(new GhostImage(g.getId(), g.getX(), g.getY(), scalex, scaley, g.getRoomId()));
		}
	}

	private void start() {
		timer = new Timer(1, new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				repaint();
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
			if (i instanceof GhostImage) {
				render.add(i);
			}
		}
		for (Image i : images) {
			if (i instanceof TurretImage) {
				render.add(i);
			}
		}
		images = render;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}

	@Override
	protected synchronized void paintComponent(Graphics g1) {
		timer.stop();
		renderOrder();
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g);
		for (Image i : images) {
			if (i != null) {
				i.drawThis(g, scalex, scaley);
			}
		}
		g.setColor(Color.RED);
		g.drawRect((selectedX + 1) * scalex, (selectedY + 1) * scaley, scalex, scaley);

		timer.start();
	}
}
