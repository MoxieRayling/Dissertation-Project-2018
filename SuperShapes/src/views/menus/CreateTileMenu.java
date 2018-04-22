package views.menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import model.FileManager;
import views.Window;

@SuppressWarnings("serial")
public class CreateTileMenu extends JPanel {
	private Window w;
	private int sizex = 400;
	private int sizey = 620;
	private int buttonSizex = 150;
	private int buttonSizey = 40;
	private JLabel title;
	private int scale = 512 / 13;
	private JComboBox<String> tilesBox;
	private String[] tiles;
	private List<JComponent> emptyOptions = new ArrayList<JComponent>();
	private List<JComponent> wallOptions = new ArrayList<JComponent>();
	private List<JComponent> slideOptions = new ArrayList<JComponent>();
	private String[] directions = new String[] { "North", "East", "South", "West" };
	private JComboBox<String> slideDirectionBox;
	private List<JComponent> teleOptions = new ArrayList<JComponent>();
	private JSlider teleportX;
	private JSlider teleportY;
	private List<JComponent> holeOptions = new ArrayList<JComponent>();
	private List<JComponent> keyOptions = new ArrayList<JComponent>();
	private JTextField key;
	private List<JComponent> lockOptions = new ArrayList<JComponent>();
	private List<JComponent> coinOptions = new ArrayList<JComponent>();
	private List<List<JComponent>> tileComponents = new ArrayList<List<JComponent>>();
	private JComboBox<String> tileImage;
	private JTextArea tileText;
	private JTextField name;
	private MenuButton create;
	private MenuButton back;
	private JScrollPane scroll;
	private JLabel presetName;
	private JLabel presetType;
	private JLabel presetImage;
	private JLabel presetText;
	private JLabel presetCommand;
	private JComboBox<String> tileCommand;
	private JLabel teleXLabel;
	private JLabel teleYLabel;
	private JLabel keyLabel;
	private JLabel slideLabel;

	public CreateTileMenu(Window w) {
		this.w = w;
		this.setLayout(null);
		this.setBackground(new Color(0x990000));
		initUI();
		hideTileMenu();
		int offset = 100;
		for (JComponent b : emptyOptions) {
			b.setBounds(sizex / 2 - 20, emptyOptions.indexOf(b) * buttonSizey + offset, buttonSizex, buttonSizey);
			if (b == scroll) {
				b.setBounds(sizex / 2 - 20, emptyOptions.indexOf(b) * buttonSizey + offset, buttonSizex,
						buttonSizey + offset);
				offset = 200;
			}
			b.setFont(new Font("Arial", Font.PLAIN, 20));
			if (b instanceof MenuButton) {
				((MenuButton) b).setHoverBackgroundColor(new Color(0x990000).brighter());
				((MenuButton) b).setPressedBackgroundColor(new Color(0xff2222));
			}

			b.setVisible(true);
		}
	}

	private void initUI() {

		title = new JLabel("<html><div style='text-align: center;'>Tile Constuctor");
		title.setFont(new Font("Arial", Font.BOLD, 40));
		title.setForeground(Color.BLACK);
		title.setBounds(40, 0, 400, 100);
		title.setBackground(new Color(0x660000));
		this.add(title);

		back = new MenuButton("Back");
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				w.editor();
			}
		});
		this.add(back);

		tiles = new String[] { "Empty", "Wall", "Slide", "Teleport", "Hole", "Key", "Lock", "Coin" };
		tilesBox = new JComboBox<String>(tiles);
		tilesBox.setToolTipText("<html>Choose a tile type:<br><br>"
				+ "Empty - an empty tile, useful for displaying images, text, or executing a command<br><br>"
				+ "Wall - the player will not be able to pass through this tile.<br><br>"
				+ "Hole - the player will die on contact with this tile.<br><br>"
				+ "Slide - the player will be moved one space in the specified location.<br><br>"
				+ "Teleport - teleports the player to a specified tile.<br><br>"
				+ "Key - a key for the player to collect, allows the player to open a lock.<br><br>"
				+ "Lock - a wall that can only be passed if the player has the specified key.<br><br>"
				+ "Coin - a coin that the player can collect.");
		for (int i = 0; i < tilesBox.getComponentCount(); i++) {
			if (tilesBox.getComponent(i) instanceof JComponent) {
				((JComponent) tilesBox.getComponent(i)).setBorder(null);
			}
			if (tilesBox.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) tilesBox.getComponent(i)).setBorderPainted(false);
			}
		}
		tilesBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideTileMenu();
					int offset = 100;
					switch (e.getItem().toString()) {
					case "Empty":
						for (Component c : emptyOptions) {
							c.setBounds(sizex / 2 - 20, emptyOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
									buttonSizey);
							if (c == scroll) {
								c.setBounds(sizex / 2 - 20, emptyOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
										buttonSizey + 100);
								presetText.setBounds(0, emptyOptions.indexOf(c) * buttonSizey + 70, 150, 100);
								offset = 200;
							}
							c.setVisible(true);
						}
						break;
					case "Wall":
						for (Component c : wallOptions) {
							c.setBounds(sizex / 2 - 20, wallOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
									buttonSizey);
							if (c == scroll) {
								c.setBounds(sizex / 2 - 20, wallOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
										buttonSizey + 100);
								presetText.setBounds(0, wallOptions.indexOf(c) * buttonSizey + 70, 150, 100);
								offset = 200;
							}
							c.setVisible(true);
						}
						break;
					case "Slide":
						for (Component c : slideOptions) {
							c.setBounds(sizex / 2 - 20, slideOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
									buttonSizey);
							if (c == scroll) {
								c.setBounds(sizex / 2 - 20, slideOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
										buttonSizey + 100);
								presetText.setBounds(0, slideOptions.indexOf(c) * buttonSizey + 70, 150, 100);
								offset = 200;
							}
							c.setVisible(true);
						}
						slideLabel.setVisible(true);
						break;
					case "Teleport":
						for (Component c : teleOptions) {
							c.setBounds(sizex / 2 - 20, teleOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
									buttonSizey);
							if (c == scroll) {
								c.setBounds(sizex / 2 - 20, teleOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
										buttonSizey + 100);
								presetText.setBounds(0, teleOptions.indexOf(c) * buttonSizey + 70, 150, 100);
								offset = 200;
							}
							c.setVisible(true);
						}
						teleXLabel.setVisible(true);
						teleYLabel.setVisible(true);
						break;
					case "Hole":
						for (Component c : holeOptions) {
							c.setBounds(sizex / 2 - 20, holeOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
									buttonSizey);
							if (c == scroll) {
								c.setBounds(sizex / 2 - 20, holeOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
										buttonSizey + 100);
								presetText.setBounds(0, holeOptions.indexOf(c) * buttonSizey + 70, 150, 100);
								offset = 200;
							}
							c.setVisible(true);
						}
						break;
					case "Key":
						for (Component c : keyOptions) {
							c.setBounds(sizex / 2 - 20, keyOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
									buttonSizey);
							if (c == scroll) {
								c.setBounds(sizex / 2 - 20, keyOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
										buttonSizey + 100);
								presetText.setBounds(0, keyOptions.indexOf(c) * buttonSizey + 70, 150, 100);
								offset = 200;
							}
							c.setVisible(true);
						}
						keyLabel.setVisible(true);
						break;
					case "Lock":
						for (Component c : lockOptions) {
							c.setBounds(sizex / 2 - 20, lockOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
									buttonSizey);
							if (c == scroll) {
								c.setBounds(sizex / 2 - 20, lockOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
										buttonSizey + 100);
								presetText.setBounds(0, lockOptions.indexOf(c) * buttonSizey + 70, 150, 100);
								offset = 200;
							}
							c.setVisible(true);
						}
						keyLabel.setVisible(true);
						break;
					case "Coin":
						for (Component c : coinOptions) {
							c.setBounds(sizex / 2 - 20, coinOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
									buttonSizey);
							if (c == scroll) {
								c.setBounds(sizex / 2 - 20, coinOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
										buttonSizey + 100);

								presetText.setBounds(0, coinOptions.indexOf(c) * buttonSizey + 70, 150, 100);
								offset = 200;
							}
							c.setVisible(true);
						}
						break;
					default:
						break;
					}
					Rectangle r = tilesBox.getBounds();
					r.translate(-175, 0);
					presetType.setBounds(r);
					r = name.getBounds();
					r.translate(-175, 0);
					presetName.setBounds(r);
					r = tileImage.getBounds();
					r.translate(-175, 0);
					presetImage.setBounds(r);
					r = tileCommand.getBounds();
					r.translate(-175, 0);
					presetCommand.setBounds(r);
					r = teleportX.getBounds();
					r.translate(-175, 0);
					teleXLabel.setBounds(r);
					r = teleportY.getBounds();
					r.translate(-175, 0);
					teleYLabel.setBounds(r);
					r = key.getBounds();
					r.translate(-175, 0);
					keyLabel.setBounds(r);
					r = slideDirectionBox.getBounds();
					r.translate(-175, 0);
					slideLabel.setBounds(r);
				}
			}
		});
		this.add(tilesBox);

		name = new JTextField();
		name.setToolTipText("Name your preset");
		String[] commands = { "none", "enableShield", "disableShield", "enableRewind", "disableRewind", "enablePause",
				"disablePause", "enableFlight", "disableFlight", "win", "lose" };
		tileCommand = new JComboBox<String>(commands);
		tileCommand.setToolTipText("<html>Set a command to be activated when the player steps on the tile.<br> "
				+ "Options include enabling/disabling abilities and winning/losing the game.");
		for (int i = 0; i < tileCommand.getComponentCount(); i++) {
			if (tilesBox.getComponent(i) instanceof JComponent) {
				((JComponent) tileCommand.getComponent(i)).setBorder(null);
			}
			if (tilesBox.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) tileCommand.getComponent(i)).setBorderPainted(false);
			}
		}
		create = new MenuButton("Create Tile");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (name.getText().trim().length() > 0)
					generateTile();
			}
		});
		create.setBounds(scale, 270, scale * 3, 20);
		create.setToolTipText("Add this tile to the list of tile presets");
		this.add(create);
		tileImage = new JComboBox<String>(getImages());
		tileImage.setToolTipText("Select an image for the tile or select 'none' to use the defaults for the tile type");
		for (int i = 0; i < tileImage.getComponentCount(); i++) {
			if (tileImage.getComponent(i) instanceof JComponent) {
				((JComponent) tileImage.getComponent(i)).setBorder(null);
			}
			if (tileImage.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) tileImage.getComponent(i)).setBorderPainted(false);
			}
		}

		presetType = new JLabel("Tile type:", SwingConstants.RIGHT);
		presetType.setFont(new Font("Arial", Font.PLAIN, 20));
		presetType.setForeground(Color.BLACK);
		presetType.setBounds(0, 70, 155, 100);
		presetType.setBackground(new Color(0x660000));
		this.add(presetType);
		presetName = new JLabel("Tile name:", SwingConstants.RIGHT);
		presetName.setFont(new Font("Arial", Font.PLAIN, 20));
		presetName.setForeground(Color.BLACK);
		presetName.setBounds(0, 110, 155, 100);
		presetName.setBackground(new Color(0x660000));
		this.add(presetName);
		presetImage = new JLabel("Tile image:", SwingConstants.RIGHT);
		presetImage.setFont(new Font("Arial", Font.PLAIN, 20));
		presetImage.setForeground(Color.BLACK);
		presetImage.setBounds(0, 150, 155, 100);
		presetImage.setBackground(new Color(0x660000));
		this.add(presetImage);
		presetText = new JLabel("Tile text:", SwingConstants.RIGHT);
		presetText.setFont(new Font("Arial", Font.PLAIN, 20));
		presetText.setForeground(Color.BLACK);
		presetText.setBounds(0, 190, 155, 100);
		presetText.setBackground(new Color(0x660000));
		this.add(presetText);
		presetCommand = new JLabel("Tile command:", SwingConstants.RIGHT);
		presetCommand.setFont(new Font("Arial", Font.PLAIN, 20));
		presetCommand.setForeground(Color.BLACK);
		presetCommand.setBounds(0, 330, 155, 100);
		presetCommand.setBackground(new Color(0x660000));
		this.add(presetCommand);
		teleXLabel = new JLabel("Destination x is 0", SwingConstants.RIGHT);
		teleXLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		teleXLabel.setForeground(Color.BLACK);
		teleXLabel.setBounds(0, 330, 150, 100);
		teleXLabel.setBackground(new Color(0x660000));
		teleXLabel.setVisible(false);
		this.add(teleXLabel);
		teleYLabel = new JLabel("Destination y is 0", SwingConstants.RIGHT);
		teleYLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		teleYLabel.setForeground(Color.BLACK);
		teleYLabel.setBounds(0, 330, 150, 100);
		teleYLabel.setBackground(new Color(0x660000));
		teleYLabel.setVisible(false);
		this.add(teleYLabel);
		keyLabel = new JLabel("Keycode:", SwingConstants.RIGHT);
		keyLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		keyLabel.setForeground(Color.BLACK);
		keyLabel.setBounds(0, 330, 150, 100);
		keyLabel.setBackground(new Color(0x660000));
		keyLabel.setVisible(false);
		this.add(keyLabel);
		slideLabel = new JLabel("Direction:", SwingConstants.RIGHT);
		slideLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		slideLabel.setForeground(Color.BLACK);
		slideLabel.setBounds(0, 330, 150, 100);
		slideLabel.setBackground(new Color(0x660000));
		slideLabel.setVisible(false);
		this.add(slideLabel);

		tileText = new JTextArea();
		tileText.setBounds(20, 600, 1160, 100);
		tileText.setWrapStyleWord(true);
		tileText.setLineWrap(true);
		tileText.setToolTipText("<html>Choose text to be displayed when player steps on the tile. <br>"
				+ "The characters ';' and ':' will be removed.");
		scroll = new JScrollPane(tileText);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(20, 600, 1160, 100);
		this.add(scroll);

		emptyOptions.add(tilesBox);
		emptyOptions.add(name);
		emptyOptions.add(tileImage);
		emptyOptions.add(scroll);

		wallOptions.add(tilesBox);
		wallOptions.add(name);
		wallOptions.add(tileImage);
		wallOptions.add(scroll);

		slideOptions.add(tilesBox);
		slideOptions.add(name);
		slideDirectionBox = new JComboBox<String>(directions);
		slideDirectionBox.setToolTipText("Choose a direction to move the player in.");
		slideOptions.add(slideDirectionBox);
		slideOptions.add(tileImage);
		slideOptions.add(scroll);

		teleOptions.add(tilesBox);
		teleOptions.add(name);
		teleportX = new JSlider(0, 10, 0);
		teleportX.setOpaque(false);
		teleportX.setToolTipText("Choose the x coordinate to teleport the player to.");
		teleportY = new JSlider(0, 10, 0);
		teleportY.setOpaque(false);
		teleportY.setToolTipText("Choose the y coordinate to teleport the player to.");
		teleOptions.add(teleportX);
		teleOptions.add(teleportY);
		teleOptions.add(tileImage);
		teleOptions.add(scroll);

		holeOptions.add(tilesBox);
		holeOptions.add(name);
		holeOptions.add(tileImage);
		holeOptions.add(scroll);

		keyOptions.add(tilesBox);
		keyOptions.add(name);
		key = new JTextField();
		key.setToolTipText("Type a keycode for the key/lock");
		keyOptions.add(key);
		keyOptions.add(tileImage);
		keyOptions.add(scroll);

		lockOptions.add(tilesBox);
		lockOptions.add(name);
		lockOptions.add(key);
		lockOptions.add(tileImage);
		lockOptions.add(scroll);

		coinOptions.add(tilesBox);
		coinOptions.add(name);
		coinOptions.add(tileImage);
		coinOptions.add(scroll);

		tileComponents.add(emptyOptions);
		tileComponents.add(wallOptions);
		tileComponents.add(slideOptions);
		tileComponents.add(teleOptions);
		tileComponents.add(holeOptions);
		tileComponents.add(keyOptions);
		tileComponents.add(lockOptions);
		tileComponents.add(coinOptions);

		for (List<JComponent> l : tileComponents) {
			l.add(tileCommand);
			l.add(this.create);
			l.add(this.back);
			for (JComponent b : l) {
				b.setBounds(sizex / 2 - 20, l.indexOf(b) * buttonSizey + 100, buttonSizex, buttonSizey);
				b.setFont(new Font("Arial", Font.PLAIN, 20));
				b.setBackground(new Color(0x660000));
				b.setForeground(new Color(0x000000).brighter());
				b.setBorder(null);
				if (b instanceof MenuButton) {
					((MenuButton) b).setHoverBackgroundColor(new Color(0x990000).brighter());
					((MenuButton) b).setPressedBackgroundColor(new Color(0xff2222));
				}

				this.add(b);
			}
		}
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		name.setBorder(border);
		name.setBackground(new Color(0xffffff));
		name.setText("tile " + (tilesBox.getItemCount() + 1));
		scroll.setBorder(border);
		scroll.setBackground(new Color(0xffffff));
		tileImage.setVisible(false);
		key.setBorder(border);
		key.setBackground(new Color(0xffffff));

	}

	private String[] getImages() {
		File file = new File(FileManager.getGameDir() + "/textures");
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

	private void hideTileMenu() {
		for (List<JComponent> l : tileComponents) {
			for (JComponent c : l) {
				c.setVisible(false);
			}
		}
		teleXLabel.setVisible(false);
		teleYLabel.setVisible(false);
		keyLabel.setVisible(false);
		slideLabel.setVisible(false);
	}

	private void generateTile() {
		String result = "";
		switch (tilesBox.getSelectedItem().toString()) {
		case "Empty":
			result = name.getText().replaceAll(" ", "_") + " empty " + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_").replaceAll(";", "").replaceAll(":", "") + " "
					+ String.valueOf(tileText.getText().isEmpty() + " " + tileCommand.getSelectedItem().toString());
			break;
		case "Wall":
			result = name.getText().replaceAll(" ", "_") + " wall " + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_").replaceAll(";", "").replaceAll(":", "") + " "
					+ String.valueOf(tileText.getText().isEmpty() + " " + tileCommand.getSelectedItem().toString());
			break;
		case "Slide":
			result = name.getText().replaceAll(" ", "_") + " slide " + " "
					+ slideDirectionBox.getSelectedItem().toString() + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_").replaceAll(";", "").replaceAll(":", "") + " "
					+ String.valueOf(tileText.getText().isEmpty() + " " + tileCommand.getSelectedItem().toString());
			break;
		case "Teleport":
			result = name.getText().replaceAll(" ", "_") + " tele " + " " + teleportX.getValue() + ","
					+ teleportY.getValue() + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_").replaceAll(";", "").replaceAll(":", "") + " "
					+ String.valueOf(tileText.getText().isEmpty() + " " + tileCommand.getSelectedItem().toString());
			break;
		case "Hole":
			result = name.getText().replaceAll(" ", "_") + " hole " + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_").replaceAll(";", "").replaceAll(":", "") + " "
					+ String.valueOf(tileText.getText().isEmpty() + " " + tileCommand.getSelectedItem().toString());
			break;
		case "Key":
			result = name.getText().replaceAll(" ", "_") + " key " + " " + key.getText() + " "
					+ tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_").replaceAll(";", "").replaceAll(":", "") + " "
					+ String.valueOf(tileText.getText().isEmpty() + " " + tileCommand.getSelectedItem().toString());
			break;
		case "Lock":
			result = name.getText().replaceAll(" ", "_") + " lock " + " " + key.getText() + " "
					+ tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_").replaceAll(";", "").replaceAll(":", "") + " "
					+ String.valueOf(tileText.getText().isEmpty() + " " + tileCommand.getSelectedItem().toString());
			break;
		case "Coin":
			result = name.getText().replaceAll(" ", "_") + " coin " + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_").replaceAll(";", "").replaceAll(":", "") + " "
					+ String.valueOf(tileText.getText().isEmpty() + " " + tileCommand.getSelectedItem().toString());
			break;
		default:
			break;
		}
		w.createTile(result);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
