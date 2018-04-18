package views.menus;

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
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Constants;
import model.Room;
import model.entities.Block;
import model.entities.Entity;
import model.entities.Ghost;
import model.entities.Shot;
import model.entities.Snake;
import model.entities.SnakeBody;
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
public class Editor extends JPanel implements ItemListener {
	private Timer timer;
	private Window w;
	private int sizex = 1200;
	private int sizey = 600;
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
	private JTextField key;
	private List<Component> keyOptions = new ArrayList<Component>();
	private List<Component> lockOptions = new ArrayList<Component>();
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
	private JComboBox<String> entityImage;
	private JComboBox<String> tileImage;
	private JTextArea tileText;
	private JSlider roomXSlider;
	private JSlider roomYSlider;
	private int[] size = { 11, 11 };
	private JCheckBox newRoom;
	private JCheckBox deleteRoom;
	private JButton finish;
	private JButton back;
	private JTextField eventName;
	private JButton makeEvent;

	public Editor(Window w) {
		this.w = w;
		this.setLayout(null);
		initUI();
		start();
	}

	public void setMap() {
		map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
		images.add(map);
	}

	private void initUI() {
		eventName = new JTextField();
		eventName.setBounds(800,530,100,20);
		this.add(eventName);
		
		makeEvent = new JButton("Make Event");
		makeEvent.setBounds(900,530,100,20);
		makeEvent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!eventName.getText().equals("")) {
					w.eventEditor(eventName.getText());
				}
			}
		});
		this.add(makeEvent);
		
		finish = new JButton("Finish");
		finish.setBounds(1000, 530, 100, 20);
		finish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.exportWorld();
				w.mainMenu();
			}
		});
		this.add(finish);
		back = new JButton("Back");
		back.setBounds(100, 530, 100, 20);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.back();
			}
		});
		this.add(back);

		newRoom = new JCheckBox("Add New Room");
		newRoom.setBounds(100, 500, 150, 20);
		newRoom.setOpaque(false);
		this.add(newRoom);

		deleteRoom = new JCheckBox("Delete Room");
		deleteRoom.setBounds(300, 500, 150, 20);
		deleteRoom.setOpaque(false);
		this.add(deleteRoom);

		roomXSlider = new JSlider(3, 20, 11);
		roomXSlider.setOpaque(false);
		roomXSlider.setBounds(750, 500, 100, 20);
		roomXSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				size[0] = roomXSlider.getValue();
				if (selectedX > size[0])
					selectedX = size[0] - 1;
				w.setRoomSize(size);
			}
		});
		this.add(roomXSlider);
		roomYSlider = new JSlider(3, 20, 11);
		roomYSlider.setOpaque(false);
		roomYSlider.setBounds(950, 500, 100, 20);
		roomYSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				size[1] = roomYSlider.getValue();
				if (selectedY > size[1])
					selectedY = size[1] - 1;
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

		entityImage = new JComboBox<String>(getImages());
		entityImage.setBounds(scale * 14, scale * 2, 100, 20);
		this.add(entityImage);
		tileImage = new JComboBox<String>(getImages());
		tileImage.setBounds(scale * 14, scale * 8, 100, 20);
		this.add(tileImage);
		tileText = new JTextArea();
		tileText.setBounds(scale * 14, scale * 9, 180, 60);
		tileText.setLineWrap(true);
		tileText.setWrapStyleWord(true);
		this.add(tileText);

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

		key = new JTextField();
		key.setBounds(scale * 14, scale * 8, 100, 20);
		this.add(key);
		this.keyOptions.add(key);
		this.lockOptions.add(key);

		tiles = new String[] { "Empty", "Wall", "Slide", "Teleport", "Hole", "Key", "Lock", "Coin" };
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
						tileText.setBounds(scale * 14, scale * 9, 180, 60);
						break;
					case "Wall":
						tileImage.setBounds(scale * 14, scale * 8, 100, 20);
						tileText.setBounds(scale * 14, scale * 9, 180, 60);
						break;
					case "Slide":
						slideDirectionBox.setVisible(true);
						tileImage.setBounds(scale * 14, scale * 9, 100, 20);
						tileText.setBounds(scale * 14, scale * 10, 180, 60);
						break;
					case "Teleport":
						teleportX.setVisible(true);
						teleportY.setVisible(true);
						tileImage.setBounds(scale * 14, scale * 10, 100, 20);
						tileText.setBounds(scale * 14, scale * 11, 180, 60);
						break;
					case "Hole":
						tileImage.setBounds(scale * 14, scale * 8, 100, 20);
						tileText.setBounds(scale * 14, scale * 9, 180, 60);
						break;
					case "Key":
						key.setVisible(true);
						tileImage.setBounds(scale * 14, scale * 9, 100, 20);
						tileText.setBounds(scale * 14, scale * 10, 180, 60);
						break;
					case "Lock":
						key.setVisible(true);
						tileImage.setBounds(scale * 14, scale * 9, 100, 20);
						tileText.setBounds(scale * 14, scale * 10, 180, 60);
						break;
					case "Coin":
						tileImage.setBounds(scale * 14, scale * 8, 100, 20);
						tileText.setBounds(scale * 14, scale * 9, 180, 60);
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
		turretRate.setOpaque(false);
		turretOptions.add(turretRate);
		turretDelay = new JSlider(0, 122, 0);
		turretDelay.setOpaque(false);
		turretOptions.add(turretDelay);

		snakeSize = new JSlider(1, 122, 5);
		snakeSize.setOpaque(false);
		snakeOptions.add(snakeSize);

		ghostPower = new JSlider(1, 122, 3);
		ghostPower.setOpaque(false);
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
		teleportX.setOpaque(false);
		teleportY = new JSlider(0, 10, 0);
		teleportY.setOpaque(false);
		teleportOptions.add(teleportX);
		teleportOptions.add(teleportY);
		slideDirectionBox = new JComboBox<String>(directions);
		slideOptions.add(slideDirectionBox);
		tileComponents.add(teleportOptions);
		tileComponents.add(slideOptions);
		tileComponents.add(keyOptions);
		tileComponents.add(lockOptions);
		for (List<Component> l : tileComponents) {
			for (Component c : l) {
				c.setBounds(scale * 14, scale * (l.indexOf(c) + 8), scale * 2, 20);
				c.setVisible(false);
				this.add(c);
			}
		}
		initExits();
	}

	private String[] getImages() {
		File file = new File(Constants.gameDir + "/textures");
		String[] images = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});
		String[] result = new String[images.length + 1];
		result[0] = "none";
		for (int i = 1; i < images.length + 1; i++) {
			result[i] = images[i - 1];
		}
		return result;
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
		roomScale = Math.min(512, 512) / (size + 3);
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
				result[0] = "block " + selectedX + "," + selectedY + " " + entityImage.getSelectedItem();
				break;
			case "Snake":
				result[0] = "snake " + selectedX + "," + selectedY + " " + snakeSize.getValue() + " "
						+ entityImage.getSelectedItem();
				break;
			case "Turret":
				result[0] = "turret " + selectedX + "," + selectedY + " " + turretRate.getValue() + " "
						+ turretDirectionBox.getSelectedItem().toString() + " " + turretDelay.getValue() + " "
						+ entityImage.getSelectedItem();
				break;
			case "Ghost":
				result[0] = "ghost " + selectedX + "," + selectedY + " " + ghostPower.getValue() + " "
						+ entityImage.getSelectedItem();
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
				result[1] = "empty " + selectedX + "," + selectedY + " " + tileImage.getSelectedItem() + " \""
						+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
				break;
			case "Wall":
				result[1] = "wall " + selectedX + "," + selectedY + " " + tileImage.getSelectedItem() + " \""
						+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
				break;
			case "Slide":
				result[1] = "slide " + selectedX + "," + selectedY + " "
						+ slideDirectionBox.getSelectedItem().toString() + " " + tileImage.getSelectedItem() + " \""
						+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
				break;
			case "Teleport":
				result[1] = "tele " + selectedX + "," + selectedY + " " + teleportX.getValue() + ","
						+ teleportY.getValue() + " " + tileImage.getSelectedItem() + " \""
						+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
				break;
			case "Hole":
				result[1] = "hole " + selectedX + "," + selectedY + " " + tileImage.getSelectedItem() + " \""
						+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
				break;
			case "Key":
				result[1] = "key " + selectedX + "," + selectedY + " " + key.getText() + " "
						+ tileImage.getSelectedItem() + " \"" + tileText.getText().replaceAll(" ", "_") + " "
						+ String.valueOf(tileText.getText().isEmpty());
				break;
			case "Lock":
				result[1] = "lock " + selectedX + "," + selectedY + " " + key.getText() + " "
						+ tileImage.getSelectedItem() + " \"" + tileText.getText().replaceAll(" ", "_") + " "
						+ String.valueOf(tileText.getText().isEmpty());
				break;
			case "Coin":
				result[1] = "coin " + selectedX + "," + selectedY + " " + tileImage.getSelectedItem() + " \""
						+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
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
