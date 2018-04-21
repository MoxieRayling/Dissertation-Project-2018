package views.menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.FileManager;
import model.Room;
import model.entities.Block;
import model.entities.Entity;
import model.entities.Ghost;
import model.entities.Snake;
import model.entities.Turret;
import views.Window;
import views.animation.BlockImage;
import views.animation.GhostImage;
import views.animation.Image;
import views.animation.MapImage;
import views.animation.RoomImage;
import views.animation.SnakeImage;
import views.animation.TurretImage;

@SuppressWarnings("serial")
public class Editor extends JPanel {
	private Timer timer;
	private Window w;
	private int sizex = 1200;
	private int sizey = 750;
	private int buttonSizex = 150;
	private int buttonSizey = 40;
	private JLabel mapTitle;
	private JLabel roomTitle;
	private int scale = 512 / 13;
	private int roomScale = 512 / 13;
	private List<Image> images = new ArrayList<Image>();
	private RoomImage room = null;
	private MapImage map = null;
	private List<JButton> tileButtons = new ArrayList<JButton>();
	private List<JButton> nExitButtons = new ArrayList<JButton>();
	private List<JButton> eExitButtons = new ArrayList<JButton>();
	private List<JButton> sExitButtons = new ArrayList<JButton>();
	private List<JButton> wExitButtons = new ArrayList<JButton>();
	private List<JButton> roomButtons = new ArrayList<JButton>();
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
	private JCheckBox paintEntity;
	private JCheckBox paintTile;
	private JSlider roomXSlider;
	private JSlider roomYSlider;
	private int[] size = { 11, 11 };
	private JCheckBox newRoom;
	private JCheckBox deleteRoom;
	private MenuButton finish;
	private MenuButton back;
	private List<String> entities = new ArrayList<String>();
	private List<String> tiles = new ArrayList<String>();
	private JComboBox<String> entityList;
	private JComboBox<String> tileList;
	private MenuButton makeEntity;
	private MenuButton makeTile;
	private JTextArea mapLines;
	private JScrollPane scroll;
	private List<JComponent> menu = new ArrayList<JComponent>();

	public Editor(Window w) {
		this.w = w;
		this.setLayout(null);
		this.setBackground(new Color(0x990000));
		initUI();
		start();
	}

	public void setMap() {
		map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
		images.add(map);
	}

	private void initUI() {
		refresh();

		mapTitle = new JLabel("<html><div style='text-align: center;'>Map Editor");
		mapTitle.setFont(new Font("Arial", Font.BOLD, 40));
		mapTitle.setForeground(Color.BLACK);
		mapTitle.setBounds(40, -20, 400, 100);
		mapTitle.setBackground(new Color(0x660000));
		this.add(mapTitle);

		roomTitle = new JLabel("<html><div style='text-align: center;'>Room Editor");
		roomTitle.setFont(new Font("Arial", Font.BOLD, 40));
		roomTitle.setForeground(Color.BLACK);
		roomTitle.setBounds(sizex / 2 + 125, -20, 400, 100);
		roomTitle.setBackground(new Color(0x660000));
		this.add(roomTitle);

		mapLines = new JTextArea();
		mapLines.setEditable(false);
		mapLines.setBounds(20, 600, 1160, 100);
		scroll = new JScrollPane(mapLines);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(20, 600, 1160, 100);
		this.add(scroll);

		paintEntity = new JCheckBox("Add Entity");
		paintEntity.setContentAreaFilled(false);
		this.add(paintEntity);

		paintTile = new JCheckBox("Add Tile");
		paintTile.setContentAreaFilled(false);
		this.add(paintTile);

		makeEntity = new MenuButton("Construct Entity");
		makeEntity.setToolTipText("Opens the entity constructor menu");
		makeEntity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.createEntityMenu();
			}
		});
		this.add(makeEntity);

		makeTile = new MenuButton("Construct Tile");
		makeTile.setToolTipText("Opens the tile constructor menu");
		makeTile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.createTileMenu();
			}
		});
		this.add(makeTile);

		menu.add(makeTile);
		menu.add(tileList);
		menu.add(paintTile);
		menu.add(makeEntity);
		menu.add(entityList);
		menu.add(paintEntity);

		for (JComponent b : menu) {
			if (menu.indexOf(b) > 2)
				b.setBounds(sizex / 2 - buttonSizex / 2, (menu.indexOf(b) + 1) * (buttonSizey + 20) + scale * 2,
						buttonSizex, buttonSizey);
			else
				b.setBounds(sizex / 2 - buttonSizex / 2, menu.indexOf(b) * (buttonSizey + 20) + scale * 2, buttonSizex,
						buttonSizey);
			b.setFont(new Font("Arial", Font.PLAIN, 20));
			b.setBackground(new Color(0x660000));
			b.setForeground(new Color(0x000000).brighter());
			b.setBorder(null);
			if (b instanceof MenuButton) {
				((MenuButton) b).setHoverBackgroundColor(new Color(0x990000).brighter());
				((MenuButton) b).setPressedBackgroundColor(new Color(0xff2222));
			}
		}

		finish = new MenuButton("Finish");
		finish.setToolTipText("Exports game");
		finish.setBounds(1000, 550, buttonSizex, buttonSizey);
		finish.setFont(new Font("Arial", Font.PLAIN, 20));
		finish.setBackground(new Color(0x660000));
		finish.setForeground(new Color(0x000000).brighter());
		finish.setBorder(null);
		finish.setHoverBackgroundColor(new Color(0x990000).brighter());
		finish.setPressedBackgroundColor(new Color(0xff2222));
		finish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.exportWorld();
				w.mainMenu();
			}
		});
		this.add(finish);
		back = new MenuButton("Back");
		back.setBounds(50, 550, buttonSizex, buttonSizey);
		back.setFont(new Font("Arial", Font.PLAIN, 20));
		back.setBackground(new Color(0x660000));
		back.setForeground(new Color(0x000000).brighter());
		back.setBorder(null);
		back.setHoverBackgroundColor(new Color(0x990000).brighter());
		back.setPressedBackgroundColor(new Color(0xff2222));
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.back();
			}
		});
		this.add(back);

		newRoom = new JCheckBox("Add New Room");
		newRoom.setBounds(50, 500, buttonSizex, buttonSizey);
		newRoom.setFont(new Font("Arial", Font.PLAIN, 18));
		newRoom.setBackground(new Color(0x660000));
		newRoom.setForeground(new Color(0x000000).brighter());
		newRoom.setBorder(null);
		newRoom.setOpaque(false);
		this.add(newRoom);

		deleteRoom = new JCheckBox("Delete Room");
		deleteRoom.setBounds(300, 500, buttonSizex, buttonSizey);
		deleteRoom.setFont(new Font("Arial", Font.PLAIN, 18));
		deleteRoom.setBackground(new Color(0x660000));
		deleteRoom.setForeground(new Color(0x000000).brighter());
		deleteRoom.setBorder(null);
		deleteRoom.setOpaque(false);
		this.add(deleteRoom);

		JLabel roomX = new JLabel("Room width: 11");
		roomX.setBounds(810, 490, buttonSizex, buttonSizey);
		roomX.setFont(new Font("Arial", Font.PLAIN, 18));
		roomX.setForeground(Color.BLACK);
		this.add(roomX);

		JLabel roomY = new JLabel("Room height: 11");
		roomY.setBounds(1050, 490, buttonSizex, buttonSizey);
		roomY.setFont(new Font("Arial", Font.PLAIN, 18));
		roomY.setForeground(Color.BLACK);
		this.add(roomY);

		roomXSlider = new JSlider(3, 20, 11);
		roomXSlider.setToolTipText("Changes the width of the room");
		roomXSlider.setOpaque(false);
		roomXSlider.setBounds(710, 500, 100, 20);
		roomXSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				size[0] = roomXSlider.getValue();
				if (selectedX > size[0])
					selectedX = size[0] - 1;
				w.setRoomSize(size);
				roomX.setText("Room width: " + roomXSlider.getValue());
			}
		});
		this.add(roomXSlider);
		roomYSlider = new JSlider(3, 20, 11);
		roomYSlider.setToolTipText("Changes the height of the room");
		roomYSlider.setOpaque(false);
		roomYSlider.setBounds(950, 500, 100, 20);
		roomYSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				size[1] = roomYSlider.getValue();
				if (selectedY > size[1])
					selectedY = size[1] - 1;
				w.setRoomSize(size);
				roomY.setText("Room height: " + roomYSlider.getValue());
			}
		});
		this.add(roomYSlider);

		mapUp = new JButton();
		mapUp.setBounds(scale * 6, scale, scale, scale);
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
		mapLeft.setBounds(0, scale * 7, scale, scale);
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
		mapDown.setBounds(scale * 6, scale * 13, scale, scale);
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
		mapRight.setBounds(scale * 12, scale * 7, scale, scale);
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

		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				tileButtons.add(new JButton());
				roomButtons.add(new JButton());
			}
		}

		for (JButton b : tileButtons) {
			b.setToolTipText("<html>This image represents the room selected in the map on the left.<br><br>"
					+ "Clicking the edges of the room will create an exit in that space<br><br>"
					+ "Clicking a tile while either \"add tile\" or \"add entity\" are <br>"
					+ "selected will add a tile or entity respectively.<br><br>"
					+ "The sliders at the bottom will change the size of the selected room.");
			int x = tileButtons.indexOf(b) % 11;
			int y = tileButtons.indexOf(b) / 11;
			b.setBounds(scale * (x + 1) + 700, scale * (y + 1), scale, scale);
			b.setOpaque(false);
			b.setContentAreaFilled(false);
			b.setBorderPainted(false);
			b.setToolTipText("<html>");

			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectedX = x;
					selectedY = y;
					w.addToRoom(generateTile(x, y), x, y);
				}
			});
			this.add(b);
		}
		for (JButton b : roomButtons) {
			b.setToolTipText("<html>Use the white arrow buttons to navigate the map. <br><br>"
					+ "Clicking on a square will change to room on the <br>"
					+ "right hand side to the selected room on the map.<br><br>"
					+ "Clicking while the \"add room\" checkbox is selected <br>"
					+ "will create a new room in the selected space. <br><br>"
					+ "Similarly the \"delete room\" checkbox will delete <br>" + "a room in the selected space.");
			int x = roomButtons.indexOf(b) % 11;
			int y = roomButtons.indexOf(b) / 11;
			b.setBounds(scale * (x + 1), scale * (y + 2), scale, scale);
			b.setOpaque(false);
			b.setContentAreaFilled(false);
			b.setBorderPainted(false);
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					w.exportWorld();
					selectedRoomX = x;
					selectedRoomY = y;
					if (newRoom.isSelected()) {
						w.addRoom(selectedRoomX - 5, selectedRoomY - 5);
						map.setMap(w.getMap(mapCentreX, mapCentreY));
						w.changeRoom(x - 5 + mapCentreX, y - 5 + mapCentreY);
					} else if (deleteRoom.isSelected()) {
						w.deleteRoom(selectedRoomX - 5, selectedRoomY - 5);
						map.setMap(w.getMap(mapCentreX, mapCentreY));
					} else {
						w.changeRoom(x - 5 + mapCentreX, y - 5 + mapCentreY);
					}
					w.exportWorld();
				}
			});
			this.add(b);
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
				nExitButtons.get(i).setBounds(roomScale * i + 700 + scale, -roomScale / 2 + scale * 2, roomScale,
						roomScale / 2);
				sExitButtons.get(i).setBounds(roomScale * i + 700 + scale, room.getyLength() * roomScale + scale * 2,
						roomScale, roomScale / 2);
			}
			if (i >= room.getyLength()) {
				eExitButtons.get(i).setVisible(false);
				wExitButtons.get(i).setVisible(false);
			} else {
				eExitButtons.get(i).setVisible(true);
				wExitButtons.get(i).setVisible(true);
				wExitButtons.get(i).setBounds(700 + scale - roomScale / 2, roomScale * i + scale * 2, roomScale / 2,
						roomScale);
				eExitButtons.get(i).setBounds(700 + scale + roomScale * room.getxLength(), roomScale * i + scale * 2,
						roomScale / 2, roomScale);
			}
		}
	}

	public void initExits() {
		for (int i = 0; i < 20; i++) {
			int x = i;
			nExitButtons.add(new JButton());
			nExitButtons.get(i).setBounds(roomScale * i + 700 + scale, -roomScale / 2 + scale * 2, roomScale,
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
			sExitButtons.get(i).setBounds(roomScale * i + 700 + scale, 20 * roomScale + scale * 2, roomScale,
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
			wExitButtons.get(i).setBounds(700 + scale - roomScale / 2, roomScale * i + scale * 2, roomScale / 2,
					roomScale);
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
			eExitButtons.get(i).setBounds(700 + scale + roomScale * 20, roomScale * i + scale * 2, roomScale / 2,
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
		roomScale = Math.min(512, 512) / (size + 3);
		for (Image i : images) {
			if (i != null && !(i instanceof MapImage)) {
				i.setScale(scale);
				i.update();
			}
		}
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
			b.setToolTipText("<html>This image represents the room selected in the map on the left.<br><br>"
					+ "Clicking the edges of the room will create an exit in that space<br><br>"
					+ "Clicking a tile while either \"add tile\" or \"add entity\" are <br>"
					+ "selected will add a tile or entity respectively.<br><br>"
					+ "The sliders at the bottom will change the size of the selected room.");
			int xTile = tileButtons.indexOf(b) % x;
			int yTile = tileButtons.indexOf(b) / x;
			b.setBounds(roomScale * xTile + 700 + scale, roomScale * yTile + scale * 2, roomScale, roomScale);
			b.setOpaque(false);
			b.setContentAreaFilled(false);
			b.setBorderPainted(false);
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectedX = xTile;
					selectedY = yTile;
					String[] tile = generateTile(xTile, yTile);
					w.addToRoom(generateTile(xTile, yTile), xTile, yTile);
					if (tile[0] != null) {
						mapLines.append(entityToString(tile[0], xTile, yTile));
					}
					if (tile[1] != null) {
						mapLines.append(tileToString(tile[1]));
					}
				}
			});
			this.add(b);
		}
	}

	private String entityToString(String data, int x, int y) {
		String[] entity = data.split(" ");
		switch (entity[0]) {
		case "remove":
			return "Removed entity at tile: " + x + "," + y + "\n";
		case "block":
			return "Added Chase-entity at tile: " + x + "," + y + ", with image: " + entity[2] + "\n";
		case "snake":
			return "Added Chain-entity with length: " + entity[2] + ", at tile: " + x + "," + y + ", with image: "
					+ entity[3] + "\n";
		case "ghost":
			return "Added Sticky-entity with power: " + entity[2] + ", at tile: " + x + "," + y + ", with image: "
					+ entity[3] + "\n";
		case "turret":
			return "Added Turret-entity with fire-rate: " + entity[2] + ", delay: " + entity[4] + ", facing: "
					+ entity[3] + ", at tile: " + x + "," + y + ", with image: " + entity[5] + "\n";
		}
		return "";
	}

	private String tileToString(String data) {
		String[] tile = data.split(" ");
		String result = "";
		switch (tile[0]) {
		case "empty":
			result += "Added Empty-tile at tile: " + tile[1] + ", with image: " + tile[2] + ", with command: "
					+ tile[5];
			if (!tile[3].equals("\"")) {
				result += ", with text: \"" + tile[3].replaceAll("_", " ") + "\"";
			}
			break;
		case "wall":
			result += "Added Wall-tile at tile: " + tile[1] + ", with image: " + tile[2] + ", with command: " + tile[5];
			if (!tile[3].equals("\"")) {
				result += ", with text: \"" + tile[3].replaceAll("_", " ") + "\"";
			}
			break;
		case "hole":
			result += "Added Hole-tile at tile: " + tile[1] + ", with image: " + tile[2] + ", with command: " + tile[5];
			if (!tile[3].equals("\"")) {
				result += ", with text: \"" + tile[3].replaceAll("_", " ") + "\"";
			}
			break;
		case "slide":
			result += "Added Slide-tile at tile: " + tile[1] + ", facing: " + tile[2] + ", with image: " + tile[3]
					+ ", with command: " + tile[6];
			if (!tile[4].equals("\"")) {
				result += ", with text: " + tile[4].replaceAll("_", " ") + "\"";
			}
			break;
		case "tele":
			result += "Added Teleport-tile at tile: " + tile[1] + ", with destination: " + tile[2] + "with image: "
					+ tile[3] + ", with command: " + tile[6];
			if (!tile[4].equals("\"")) {
				result += ", with text: \"" + tile[4].replaceAll("_", " ") + "\"";
			}
			break;
		case "key":
			result += "Added Key-tile at tile: " + tile[1] + ",with keycode: " + tile[2] + " with image: " + tile[3]
					+ ", with command: " + tile[6];
			if (!tile[4].equals("\"")) {
				result += ", with text: \"" + tile[4].replaceAll("_", " ") + "\"";
			}
			break;
		case "lock":
			result += "Added lock-tile at tile: " + tile[1] + ",with keycode: " + tile[2] + " with image: " + tile[3]
					+ ", with command: " + tile[6];
			if (!tile[4].equals("\"")) {
				result += ", with text: \"" + tile[4].replaceAll("_", " ") + "\"";
			}
			break;
		case "coin":
			result += "Added Coin-tile at tile: " + tile[1] + ", with image: " + tile[2] + ", with command: " + tile[5];
			if (!tile[3].equals("\"")) {
				result += ", with text: \"" + tile[3].replaceAll("_", " ") + "\"";
			}
			break;
		}
		result += "\n";
		return result;
	}

	private String[] generateTile(int x, int y) {
		String[] result = new String[2];
		for (String s : entities) {
			if (paintEntity.isSelected()) {
				if (entityList.getSelectedItem().toString().equals("remove")) {
					result[0] = "remove";
				} else if (s.startsWith(entityList.getSelectedItem().toString())) {
					result[0] = s.substring(s.indexOf(" ") + 1);
					result[0] = result[0].substring(0, result[0].indexOf(" ")) + " " + x + "," + y + " "
							+ result[0].substring(result[0].indexOf(" ") + 1);
				}
			}
		}
		System.out.println("entity " + result[0]);
		for (String s : tiles) {
			if (paintTile.isSelected() && s.startsWith(tileList.getSelectedItem().toString())) {
				result[1] = s.substring(s.indexOf(" ") + 1);
				result[1] = result[1].substring(0, result[1].indexOf(" ")) + " " + x + "," + y
						+ result[1].substring(result[1].indexOf(" ") + 1);
			}
		}
		System.out.println("tile " + result[1]);
		return result;
	}

	public void createImage(Entity e) {
		if (e instanceof Block) {
			Block b = (Block) e;
			images.add(new BlockImage(b.getId(), b.getX(), b.getY(), roomScale, b.getRoomId(), e.getImage(), 'N'));
		} else if (e instanceof Turret) {
			Turret t = (Turret) e;
			images.add(new TurretImage(t.getId(), t.getX(), t.getY(), roomScale, t.getRoomId(), t.getDirection(),
					e.getImage()));
		} else if (e instanceof Snake) {
			Snake s = (Snake) e;
			images.add(new SnakeImage(s.getId(), s.getX(), s.getY(), roomScale, s.getRoomId(), e.getImage(), 'N'));
		} else if (e instanceof Ghost) {
			Ghost g = (Ghost) e;
			images.add(new GhostImage(g.getId(), g.getX(), g.getY(), roomScale, g.getRoomId(), e.getImage(), 'N'));
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
					g.drawRect(scale, scale * 2, scale * 11, scale * 11);
					i.drawThis(g, scale, scale * 2);
				} else {
					i.drawThis(g, scale + 700, scale * 2);
				}
			}
		}
		g.setColor(Color.RED);
		g.drawRect(selectedX * roomScale + 700 + scale, selectedY * roomScale + scale * 2, roomScale, roomScale);
		g.drawRect((selectedRoomX + 1) * scale, (selectedRoomY + 2) * scale, scale, scale);

		timer.start();
	}

	public void refresh() {
		entities = w.getEntities();
		String[] entityNames = new String[entities.size() + 1];
		entityNames[0] = "remove";
		for (String e : entities) {
			entityNames[entities.indexOf(e) + 1] = e.substring(0, e.indexOf(" "));
		}
		entityList = new JComboBox<String>(entityNames);
		for (int i = 0; i < entityList.getComponentCount(); i++) {
			if (entityList.getComponent(i) instanceof JComponent) {
				((JComponent) entityList.getComponent(i)).setBorder(null);
			}
			if (entityList.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) entityList.getComponent(i)).setBorderPainted(false);
			}
		}
		entityList.setToolTipText("<html>This list contains preset tiles, some have been made for you:<br><br>" 
				+ "Chase - this preset will create an entity that chases the player <br>"
				+ "and kills them on contact. It uses chade.png as its default image.<br><br>"
				+ "Chain - this preset is an entity that chain of entities 5 long. <br>"
				+ "Touching any part of this chain will kill the player. <br>"
				+ "It uses chain.png and its default image.<br><br>"
				+ "Sticky - this preset creates an entity the holds the player still <br>"
				+ "for 3 turns but doesn't kill them. It uses sticky.png as its default image.<br><br>"
				+ "Turret - this creates an entity which fires a projectile north at<br>"
				+ "a rate of 1 every 3 turns. Touching these projectiles will kill the player<br>"
				+ "It uses turret.png as its default image while the projectiles use shot.pg<br><br>"
				+ "Selcting \"Remove\" will remove an entity from the remove when selected");
		this.add(entityList);

		tiles = w.getTiles();
		String[] tileNames = new String[tiles.size()];
		for (String e : tiles) {
			tileNames[tiles.indexOf(e)] = e.substring(0, e.indexOf(" "));
		}
		tileList = new JComboBox<String>(tileNames);
		for (int i = 0; i < tileList.getComponentCount(); i++) {
			if (tileList.getComponent(i) instanceof JComponent) {
				((JComponent) tileList.getComponent(i)).setBorder(null);
			}
			if (tileList.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) tileList.getComponent(i)).setBorderPainted(false);
			}
		}
		tileList.setToolTipText("<html>This list contains preset tiles, some have been made for you:<br><br>" 
				+ "Empty - creates an empty tile in the selected space. It uses empty.png<br>"
				+ "as its default image.<br><br>"
				+ "Wall - creates an wall that the player can't move through. It uses <br>"
				+ "wall.png as it default image.<br><br>"
				+ "Hole - creates a tile that will kill the player on contact. It uses <br>"
				+ "hole.png as its default image.<br><br>"
				+ "Slide - creates a tile that moves the player one space north. It uses<br>"
				+ "slide.png as its default image.<br><br>"
				+ "Teleport - creates a tile that teleports the player to the space '5,5'.<br>"
				+ "It uses tele.png as its default image.<br><br>"
				+ "Key - creates a key that the player can collect on contact. It uses <br>"
				+ "key.png as its default iamge.<br><br>"
				+ "Lock - creates a wall that can only be passed if the player has the <br>"
				+ "corresponding key. It uses lock.png as its default image.<br><br>"
				+ "Coin - creates a coin which the player can collect on contact. It uses<br>"
				+ "coin.png as its default image."
				);
		this.add(tileList);
	}
}
