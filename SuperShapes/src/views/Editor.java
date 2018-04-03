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
import javax.swing.JCheckBox;
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
public class Editor extends JPanel implements Constants, ItemListener {
	private Timer timer;
	private Window w;
	private int sizex = 512;
	private int sizey = 512;
	private int scalex = sizex / 13;
	private int scaley = sizey / 13;
	private List<Image> images = new ArrayList<Image>();
	private RoomImage room = null;
	private MapImage map = null;
	private List<JButton> tileButtons = new ArrayList<JButton>();
	private List<JButton> roomButtons = new ArrayList<JButton>();
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
	private int selectedX = 0;
	private int selectedY = 0;
	private int selectedRoomX = 0;
	private int selectedRoomY = 0;
	private List<List<Component>> entityComponents = new ArrayList<List<Component>>();
	private List<List<Component>> tileComponents = new ArrayList<List<Component>>();
	private JCheckBox paintEntity;
	private JCheckBox paintTile;
	private JButton addToList;
	private JSlider xRoomSlider;
	private JSlider yRoomSlider;
	private JButton loadRoom;
	private JButton export;
	private JComboBox<String> roomList;

	public Editor(Window w) {
		this.w = w;
		this.setLayout(null);
		initUI();
		this.setBackground(new Color(0x222222));
		start();
	}

	public void setMap() {
		int x = 0;
		int y = 0;
		map = new MapImage(w.getMap(x, y), 0, 0, scalex, scaley, null);
		images.add(map);
	}

	private void initUI() {

		loadRoom = new JButton("Load Room");
		loadRoom.setBounds(scalex * 13, scaley * 10, scalex * 3, scaley);
		loadRoom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.changeRoom(xRoomSlider.getValue(), yRoomSlider.getValue());
			}
		});
		this.add(loadRoom);

		xRoomSlider = new JSlider(-10, 10, 0);
		xRoomSlider.setBounds(scalex * 13, scaley * 8, scalex * 3, scaley);
		this.add(xRoomSlider);

		yRoomSlider = new JSlider(-10, 10, 0);
		yRoomSlider.setBounds(scalex * 13, scaley * 9, scalex * 3, scaley);
		this.add(yRoomSlider);

		addToList = new JButton("Add to List");
		addToList.setBounds(scalex * 13, scaley * 11, scalex * 3, scaley);
		addToList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.addRoom();
				roomList.removeAllItems();
				String[] roomIds = w.getRooms();
				for (int i = 0; i < roomIds.length; i++) {
					roomList.addItem(roomIds[i]);
				}
			}
		});
		this.add(addToList);

		export = new JButton("export");
		export.setBounds(scalex * 13, scaley * 12, scalex * 3, scaley);
		export.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.export();
			}
		});
		this.add(export);

		roomList = new JComboBox<String>();
		roomList.setBounds(scalex * 16, scaley * 10, scalex * 2, 20);
		roomList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					w.changeRoom(e.getItem().toString());
				}
			}
		});
		this.add(roomList);

		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				tileButtons.add(new JButton());
				roomButtons.add(new JButton());
			}
		}

		for (JButton b : tileButtons) {
			int x = tileButtons.indexOf(b) % 11;
			int y = tileButtons.indexOf(b) / 11;
			b.setBounds(scalex * (x + 1) + 700, scaley * (y + 1), scalex, scaley);
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
		for (JButton b : roomButtons) {
			int x = roomButtons.indexOf(b) % 11;
			int y = roomButtons.indexOf(b) / 11;
			b.setBounds(scalex * (x + 1), scaley * (y + 1), scalex, scaley);
			b.setOpaque(false);
			b.setContentAreaFilled(false);
			b.setBorderPainted(false);
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectedRoomX = x;
					selectedRoomY = y;
					w.changeRoom(x, y);
				}
			});
			this.add(b);
		}

		paintEntity = new JCheckBox("Change Entity");
		paintEntity.setContentAreaFilled(false);
		paintEntity.setBounds(scalex * 13, scaley / 3, scalex * 3, 20);
		this.add(paintEntity);

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

		paintTile = new JCheckBox("Change Tile");
		paintTile.setContentAreaFilled(false);
		paintTile.setBounds(scalex * 16, scaley / 3, scalex * 3, 20);
		this.add(paintTile);

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
		if (paintEntity.isSelected()) {
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
			case "None":
				result[0] = "None";
			default:
				break;
			}
		}
		if (paintTile.isSelected()) {
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
				result[1] = "T " + selectedX + "," + selectedY + " " + teleportX.getValue() + ","
						+ teleportY.getValue();
				break;
			case "Hole":
				result[1] = "H " + selectedX + "," + selectedY;
				break;
			default:
				break;
			}
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
		render.add(map);
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
				if (i instanceof MapImage) {
					g.setColor(new Color(0x000000));
					g.drawRect(scalex, scaley, scalex * 11, scaley * 11);
					i.drawThis(g, scalex, scaley);
				} else {
					i.drawThis(g, scalex + 700, scaley);
				}
			}
		}
		g.setColor(Color.RED);
		g.drawRect((selectedX + 1) * scalex + 700, (selectedY + 1) * scaley, scalex, scaley);
		g.drawRect((selectedRoomX + 1) * scalex, (selectedRoomY + 1) * scaley, scalex, scaley);

		timer.start();
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {

	}
}
