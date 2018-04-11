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
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Constants;
import model.Block;
import model.Entity;
import model.Ghost;
import model.Room;
import model.Snake;
import model.Turret;

@SuppressWarnings("serial")
public class Editor extends JPanel implements Constants, ItemListener {
	private Timer timer;
	private Window w;
	private int sizex = 512;
	private int sizey = 512;
	private int scale = sizex / 13;
	private int roomScale = sizex / 13;
	private List<Image> images = new ArrayList<Image>();
	private RoomImage room = null;
	private MapImage map = null;
	private List<JButton> tileButtons = new ArrayList<JButton>();
	private List<JButton> nExitButtons = new ArrayList<JButton>();
	private List<JButton> eExitButtons = new ArrayList<JButton>();
	private List<JButton> sExitButtons = new ArrayList<JButton>();
	private List<JButton> wExitButtons = new ArrayList<JButton>();
	private List<JButton> roomButtons = new ArrayList<JButton>();
	private List<Component> blockOptions = new ArrayList<Component>();
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
	private int selectedRoomX = 5;
	private int selectedRoomY = 5;
	private int mapCentreX = 0;
	private int mapCentreY = 0;
	private JButton mapUp;
	private JButton mapLeft;
	private JButton mapRight;
	private JButton mapDown;
	private List<List<Component>> entityComponents = new ArrayList<List<Component>>();
	private List<List<Component>> tileComponents = new ArrayList<List<Component>>();
	private JCheckBox paintEntity;
	private JCheckBox paintTile;
	private JButton export;
	private JTextField entityImage;
	private JTextField tileImage;
	private JSlider roomXSlider;
	private JSlider roomYSlider;
	private int[] size = { 11, 11 };
	private JButton newRoom;

	public Editor(Window w) {
		this.w = w;
		this.setLayout(null);
		initUI();
		this.setBackground(new Color(0x888888));
		start();
	}

	public void setMap() {
		map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
		images.add(map);
	}

	private void initUI() {
		newRoom = new JButton("Add New Room");
		newRoom.setBounds(150, 500, 100, 20);
		newRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.addRoom(selectedRoomX - 5, selectedRoomY - 5);
				map.setMap(w.getMap(mapCentreX, mapCentreY));
			}
		});
		this.add(newRoom);

		roomXSlider = new JSlider(3, 20, 11);
		roomXSlider.setBounds(750, 500, 100, 20);
		roomXSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				size[0] = roomXSlider.getValue();
				w.setRoomSize(size);
			}
		});
		this.add(roomXSlider);
		roomYSlider = new JSlider(3, 20, 11);
		roomYSlider.setBounds(950, 500, 100, 20);
		roomYSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				size[1] = roomYSlider.getValue();
				w.setRoomSize(size);
			}
		});
		this.add(roomYSlider);

		mapUp = new JButton();
		mapUp.setBounds(scale * 6, 0, scale, scale);
		mapUp.setOpaque(false);
		mapUp.setContentAreaFilled(false);
		mapUp.setBorderPainted(false);
		mapUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mapCentreY -= 1;
				selectedRoomY += 1;
				images.remove(map);
				map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
				images.add(map);
			}
		});
		this.add(mapUp);

		mapLeft = new JButton();
		mapLeft.setBounds(0, scale * 6, scale, scale);
		mapLeft.setOpaque(false);
		mapLeft.setContentAreaFilled(false);
		mapLeft.setBorderPainted(false);
		mapLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mapCentreX -= 1;
				selectedRoomX += 1;
				images.remove(map);
				map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
				images.add(map);
			}
		});
		this.add(mapLeft);

		mapDown = new JButton();
		mapDown.setBounds(scale * 6, scale * 12, scale, scale);
		mapDown.setOpaque(false);
		mapDown.setContentAreaFilled(false);
		mapDown.setBorderPainted(false);
		mapDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mapCentreY += 1;
				selectedRoomY -= 1;
				images.remove(map);
				map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
				images.add(map);
			}
		});
		this.add(mapDown);

		mapRight = new JButton();
		mapRight.setBounds(scale * 12, scale * 6, scale, scale);
		mapRight.setOpaque(false);
		mapRight.setContentAreaFilled(false);
		mapRight.setBorderPainted(false);
		mapRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mapCentreX += 1;
				selectedRoomX -= 1;
				images.remove(map);
				map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
				images.add(map);
			}
		});
		this.add(mapRight);

		export = new JButton("export");
		export.setBounds(scale * 14, scale * 13, scale * 2, 20);
		export.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.exportWorld();
			}
		});
		this.add(export);

		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				tileButtons.add(new JButton());
				roomButtons.add(new JButton());
			}
		}

		for (JButton b : tileButtons) {
			int x = tileButtons.indexOf(b) % 11;
			int y = tileButtons.indexOf(b) / 11;
			b.setBounds(scale * (x + 1) + 700, scale * (y + 1), scale, scale);
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
			b.setBounds(scale * (x + 1), scale * (y + 1), scale, scale);
			b.setOpaque(false);
			b.setContentAreaFilled(false);
			b.setBorderPainted(false);
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					w.exportWorld();
					selectedRoomX = x;
					selectedRoomY = y;
					w.changeRoom(x - 5 + mapCentreX, y - 5 + mapCentreY);
				}
			});
			this.add(b);
		}

		entityImage = new JTextField();
		entityImage.setBounds(scale * 14, scale * 2, 100, 20);
		this.add(entityImage);
		tileImage = new JTextField();
		tileImage.setBounds(scale * 14, scale * 8, 100, 20);
		this.add(tileImage);

		paintEntity = new JCheckBox("Change Entity");
		paintEntity.setContentAreaFilled(false);
		paintEntity.setBounds(scale * 14, scale / 3, scale * 3, 20);
		this.add(paintEntity);

		entities = new String[] { "None", "Block", "Snake", "Ghost", "Turret" };
		entitiesBox = new JComboBox<String>(entities);
		entitiesBox.setBounds(scale * 14, scale, scale * 2, 20);
		entitiesBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideEntityMenu();
					switch (e.getItem().toString()) {
					case "Block":
						entityImage.setVisible(true);
						break;
					case "Snake":
						for (Component c : snakeOptions) {
							c.setVisible(true);
						}
						entityImage.setBounds(scale * 14, scale * (2 + snakeOptions.size()), 100, 20);
						entityImage.setVisible(true);
						break;
					case "Turret":
						for (Component c : turretOptions) {
							c.setVisible(true);
						}
						entityImage.setBounds(scale * 14, scale * (2 + turretOptions.size()), 100, 20);
						entityImage.setVisible(true);
						break;
					case "Ghost":
						for (Component c : ghostOptions) {
							c.setVisible(true);
						}
						entityImage.setBounds(scale * 14, scale * (2 + ghostOptions.size()), 100, 20);
						entityImage.setVisible(true);
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
		paintTile.setBounds(scale * 14, scale * 6, scale * 3, 20);
		this.add(paintTile);

		tiles = new String[] { "Empty", "Wall", "Slide", "Teleport", "Hole" };
		tilesBox = new JComboBox<String>(tiles);
		tilesBox.setBounds(scale * 14, scale * 7, scale * 2, 20);
		tilesBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideTileMenu();
					switch (e.getItem().toString()) {
					case "Empty":
						tileImage.setBounds(scale * 14, scale * 8, 100, 20);
						break;
					case "Wall":
						tileImage.setBounds(scale * 14, scale * 8, 100, 20);
						break;
					case "Slide":
						slideDirectionBox.setVisible(true);
						tileImage.setBounds(scale * 14, scale * 9, 100, 20);
						break;
					case "Teleport":
						teleportX.setVisible(true);
						teleportY.setVisible(true);
						tileImage.setBounds(scale * 14, scale * 10, 100, 20);
						break;
					case "Hole":
						tileImage.setBounds(scale * 14, scale * 8, 100, 20);
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

		entityComponents.add(blockOptions);
		entityComponents.add(snakeOptions);
		entityComponents.add(turretOptions);
		entityComponents.add(ghostOptions);
		for (List<Component> l : entityComponents) {
			for (Component c : l) {
				c.setBounds(scale * 14, scale * (l.indexOf(c) + 2), scale * 2, 20);
				c.setVisible(false);
				this.add(c);
			}
		}
		entityImage.setVisible(false);

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
				c.setBounds(scale * 14, scale * (l.indexOf(c) + 8), scale * 2, 20);
				c.setVisible(false);
				this.add(c);
			}
		}
		initExits();
	}

	public void updateExits() {
		for (int i = 0; i < 20; i++) {
			if (i >= room.getxLength()) {
				nExitButtons.get(i).setVisible(false);
				sExitButtons.get(i).setVisible(false);
			} else {
				nExitButtons.get(i).setVisible(true);
				sExitButtons.get(i).setVisible(true);
				nExitButtons.get(i).setBounds(roomScale * i + 700 + scale, -roomScale / 2 + scale, roomScale,
						roomScale / 2);
				sExitButtons.get(i).setBounds(roomScale * i + 700 + scale, room.getyLength() * roomScale + scale,
						roomScale, roomScale / 2);
			}
			if (i >= room.getyLength()) {
				eExitButtons.get(i).setVisible(false);
				wExitButtons.get(i).setVisible(false);
			} else {
				eExitButtons.get(i).setVisible(true);
				wExitButtons.get(i).setVisible(true);
				wExitButtons.get(i).setBounds(700 + scale - roomScale / 2, roomScale * i + scale, roomScale / 2,
						roomScale);
				eExitButtons.get(i).setBounds(700 + scale + roomScale * room.getxLength(), roomScale * i + scale,
						roomScale / 2, roomScale);
			}
		}
	}

	public void initExits() {
		for (int i = 0; i < 20; i++) {
			int x = i;
			nExitButtons.add(new JButton());
			nExitButtons.get(i).setBounds(roomScale * i + 700 + scale, -roomScale / 2 + scale, roomScale,
					roomScale / 2);
			nExitButtons.get(i).setOpaque(false);
			nExitButtons.get(i).setContentAreaFilled(false);
			nExitButtons.get(i).setBorderPainted(false);
			nExitButtons.get(i).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					w.setExit(0, x);
				}
			});
			this.add(nExitButtons.get(i));

			sExitButtons.add(new JButton());
			sExitButtons.get(i).setBounds(roomScale * i + 700 + scale, 20 * roomScale + scale, roomScale,
					roomScale / 2);
			sExitButtons.get(i).setOpaque(false);
			sExitButtons.get(i).setContentAreaFilled(false);
			sExitButtons.get(i).setBorderPainted(false);
			sExitButtons.get(i).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					w.setExit(2, x);
				}
			});
			this.add(sExitButtons.get(i));

			wExitButtons.add(new JButton());
			wExitButtons.get(i).setBounds(700 + scale - roomScale / 2, roomScale * i + scale, roomScale / 2, roomScale);
			wExitButtons.get(i).setOpaque(false);
			wExitButtons.get(i).setContentAreaFilled(false);
			wExitButtons.get(i).setBorderPainted(false);
			wExitButtons.get(i).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					w.setExit(3, x);
				}
			});
			this.add(wExitButtons.get(i));

			eExitButtons.add(new JButton());
			eExitButtons.get(i).setBounds(700 + scale + roomScale * 20, roomScale * i + scale, roomScale / 2,
					roomScale);
			eExitButtons.get(i).setOpaque(false);
			eExitButtons.get(i).setContentAreaFilled(false);
			eExitButtons.get(i).setBorderPainted(false);
			eExitButtons.get(i).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					w.setExit(1, x);
				}
			});
			this.add(eExitButtons.get(i));
		}
	}

	public void setRoomScale(int size) {
		roomScale = Math.min(sizex, sizey) / (size + 3);
		for (Image i : images) {
			if (i != null && !(i instanceof MapImage)) {
				i.setScale(scale);
				i.update();
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
				result[0] = "block " + selectedX + "," + selectedY;
				break;
			case "Snake":
				result[0] = "snake " + selectedX + "," + selectedY + " " + snakeSize.getValue();
				break;
			case "Turret":
				result[0] = "turret " + selectedX + "," + selectedY + " " + turretRate.getValue() + " "
						+ turretDirectionBox.getSelectedItem().toString() + " " + turretDelay.getValue();
				break;
			case "Ghost":
				result[0] = "ghost " + selectedX + "," + selectedY + " " + ghostPower.getValue();
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
				result[1] = "empty " + selectedX + "," + selectedY;
				break;
			case "Wall":
				result[1] = "wall " + selectedX + "," + selectedY;
				break;
			case "Slide":
				result[1] = "slide " + selectedX + "," + selectedY + " "
						+ slideDirectionBox.getSelectedItem().toString();
				break;
			case "Teleport":
				result[1] = "tele " + selectedX + "," + selectedY + " " + teleportX.getValue() + ","
						+ teleportY.getValue();
				break;
			case "Hole":
				result[1] = "hole " + selectedX + "," + selectedY;
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
		setRoomScale(Math.max(r.getxLength(), r.getyLength()));
		images.clear();
		images.add(room);
		for (Entity e : r.getEnemies()) {
			createImage(e);
		}
		size[0] = r.getxLength();
		size[1] = r.getyLength();
		roomXSlider.setValue(r.getxLength());
		roomYSlider.setValue(r.getyLength());
		removeRooms();
		updateRoomButtons(r.getxLength(), r.getyLength());
		room = new RoomImage(r.getTiles(), roomScale, r.getxLength(), r.getyLength(), r.getId(), r.getExits());
		images.add(room);
		updateExits();
	}

	public void updateRoomButtons(int x, int y) {

		for (JButton b : tileButtons) {
			this.remove(b);
		}
		tileButtons.clear();
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				tileButtons.add(new JButton());
			}
		}

		for (JButton b : tileButtons) {
			int xTile = tileButtons.indexOf(b) % x;
			int yTile = tileButtons.indexOf(b) / x;
			b.setBounds(roomScale * xTile + 700 + scale, roomScale * yTile + scale, roomScale, roomScale);
			b.setOpaque(false);
			b.setContentAreaFilled(false);
			b.setBorderPainted(false);
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectedX = xTile;
					selectedY = yTile;
					w.addToRoom(generateTile(), xTile, yTile);
				}
			});
			this.add(b);
		}
	}

	public void createImage(Entity e) {
		if (e instanceof Block) {
			Block b = (Block) e;
			images.add(new BlockImage(b.getId(), b.getX(), b.getY(), roomScale, b.getRoomId()));
		} else if (e instanceof Turret) {
			Turret t = (Turret) e;
			images.add(new TurretImage(t.getId(), t.getX(), t.getY(), roomScale, t.getRoomId(), t.getDirection()));
		} else if (e instanceof Snake) {
			Snake sb = (Snake) e;
			images.add(new SnakeImage(sb.getId(), sb.getX(), sb.getY(), roomScale, sb.getRoomId()));
		} else if (e instanceof Ghost) {
			Ghost g = (Ghost) e;
			images.add(new GhostImage(g.getId(), g.getX(), g.getY(), roomScale, g.getRoomId()));
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
					g.drawRect(scale, scale, scale * 11, scale * 11);
					i.drawThis(g, scale, scale);
				} else {
					i.drawThis(g, scale + 700, scale);
				}
			}
		}
		g.setColor(Color.RED);
		g.drawRect(selectedX * roomScale + 700 + scale, selectedY * roomScale + scale, roomScale, roomScale);
		g.drawRect((selectedRoomX + 1) * scale, (selectedRoomY + 1) * scale, scale, scale);

		timer.start();
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {

	}

}
