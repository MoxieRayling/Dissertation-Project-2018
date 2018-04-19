package views.menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.FileManager;
import views.Window;

@SuppressWarnings("serial")
public class CreateTileMenu extends JPanel {
	private Window w;
	private int sizex = 400;
	private int sizey = 400;
	private int scale = 512 / 13;
	private JComboBox<String> tilesBox;
	private String[] tiles;
	private List<Component> emptyOptions = new ArrayList<Component>();
	private List<Component> wallOptions = new ArrayList<Component>();
	private List<Component> slideOptions = new ArrayList<Component>();
	private String[] directions = new String[] { "North", "East", "South", "West" };
	private JComboBox<String> slideDirectionBox;
	private List<Component> teleOptions = new ArrayList<Component>();
	private JSlider teleportX;
	private JSlider teleportY;
	private List<Component> holeOptions = new ArrayList<Component>();
	private List<Component> keyOptions = new ArrayList<Component>();
	private JTextField key;
	private List<Component> lockOptions = new ArrayList<Component>();
	private List<Component> coinOptions = new ArrayList<Component>();
	private List<List<Component>> tileComponents = new ArrayList<List<Component>>();
	private JComboBox<String> tileImage;
	private JTextArea tileText;
	private JTextField name;
	private JButton create;
	private JButton back;

	public CreateTileMenu(Window w) {
		this.w = w;
		this.setLayout(null);
		this.setBackground(new Color(0xbbbbbb));
		initUI();
		for (Component c : emptyOptions) {
			c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 3, 20);
			if (c instanceof JTextArea) {
				c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 3, 80);
			}
			c.setVisible(true);
		}
	}

	private void initUI() {
		back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				w.back();
				w.refreshEditor();
			}
		});
		back.setBounds(scale, 300, scale * 3, 20);
		this.add(back);

		tiles = new String[] { "Empty", "Wall", "Slide", "Teleport", "Hole", "Key", "Lock", "Coin" };
		tilesBox = new JComboBox<String>(tiles);
		tilesBox.setBounds(scale, scale, scale * 3, 20);
		tilesBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideTileMenu();
					switch (e.getItem().toString()) {
					case "Empty":
						for (Component c : emptyOptions) {
							c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 2, 20);
							if (c instanceof JTextArea) {
								c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 3, 80);
							}
							c.setVisible(true);
						}
						break;
					case "Wall":
						for (Component c : wallOptions) {
							c.setBounds(scale, scale * (wallOptions.indexOf(c) + 2), scale * 2, 20);
							if (c instanceof JTextArea) {
								c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 3, 80);
							}
							c.setVisible(true);
						}
						break;
					case "Slide":
						for (Component c : slideOptions) {
							c.setBounds(scale, scale * (slideOptions.indexOf(c) + 2), scale * 2, 20);
							if (c instanceof JTextArea) {
								c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 3, 80);
							}
							c.setVisible(true);
						}
						break;
					case "Teleport":
						for (Component c : teleOptions) {
							c.setBounds(scale, scale * (teleOptions.indexOf(c) + 2), scale * 2, 20);
							if (c instanceof JTextArea) {
								c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 3, 80);
							}
							c.setVisible(true);
						}
						break;
					case "Hole":
						for (Component c : holeOptions) {
							c.setBounds(scale, scale * (holeOptions.indexOf(c) + 2), scale * 2, 20);
							if (c instanceof JTextArea) {
								c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 3, 80);
							}
							c.setVisible(true);
						}
						break;
					case "Key":
						for (Component c : keyOptions) {
							c.setBounds(scale, scale * (keyOptions.indexOf(c) + 2), scale * 2, 20);
							if (c instanceof JTextArea) {
								c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 3, 80);
							}
							c.setVisible(true);
						}
						break;
					case "Lock":
						for (Component c : lockOptions) {
							c.setBounds(scale, scale * (lockOptions.indexOf(c) + 2), scale * 2, 20);
							if (c instanceof JTextArea) {
								c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 3, 80);
							}
							c.setVisible(true);
						}
						break;
					case "Coin":
						for (Component c : coinOptions) {
							c.setBounds(scale, scale * (coinOptions.indexOf(c) + 2), scale * 2, 20);
							if (c instanceof JTextArea) {
								c.setBounds(scale, scale * (emptyOptions.indexOf(c) + 2), scale * 3, 80);
							}
							c.setVisible(true);
						}
						break;
					default:
						break;
					}
				}
			}
		});
		this.add(tilesBox);

		name = new JTextField();
		create = new JButton("Create Tile");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (name.getText().trim().length() > 0)
					generateTile();
			}
		});
		create.setBounds(scale, 270, scale * 3, 20);
		this.add(create);
		tileImage = new JComboBox<String>(getImages());
		tileText = new JTextArea();
		tileText.setWrapStyleWord(true);
		tileText.setLineWrap(true);
		;

		emptyOptions.add(name);
		emptyOptions.add(tileImage);
		emptyOptions.add(tileText);

		wallOptions.add(name);
		wallOptions.add(tileImage);
		wallOptions.add(tileText);

		slideOptions.add(name);
		slideDirectionBox = new JComboBox<String>(directions);
		slideOptions.add(slideDirectionBox);
		slideOptions.add(tileImage);
		slideOptions.add(tileText);

		teleOptions.add(name);
		teleportX = new JSlider(0, 10, 0);
		teleportX.setOpaque(false);
		teleportY = new JSlider(0, 10, 0);
		teleportY.setOpaque(false);
		teleOptions.add(teleportX);
		teleOptions.add(teleportY);
		teleOptions.add(tileImage);
		teleOptions.add(tileText);

		holeOptions.add(name);
		holeOptions.add(tileImage);
		holeOptions.add(tileText);

		keyOptions.add(name);
		key = new JTextField();
		keyOptions.add(key);
		keyOptions.add(tileImage);
		keyOptions.add(tileText);

		lockOptions.add(name);
		lockOptions.add(key);
		lockOptions.add(tileImage);
		lockOptions.add(tileText);

		coinOptions.add(name);
		coinOptions.add(tileImage);
		coinOptions.add(tileText);

		tileComponents.add(emptyOptions);
		tileComponents.add(wallOptions);
		tileComponents.add(slideOptions);
		tileComponents.add(teleOptions);
		tileComponents.add(holeOptions);
		tileComponents.add(keyOptions);
		tileComponents.add(lockOptions);
		tileComponents.add(coinOptions);

		for (List<Component> l : tileComponents) {
			for (Component c : l) {
				c.setBounds(scale, scale * (l.indexOf(c) + 2), scale * 3, 20);
				if (c instanceof JTextArea) {
					c.setBounds(scale, scale * (l.indexOf(c) + 2), scale * 3, 80);
				}
				c.setVisible(false);
				this.add(c);
			}
		}
		tileImage.setVisible(false);

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
		for (List<Component> l : tileComponents) {
			for (Component c : l) {
				c.setVisible(false);
			}
		}
	}

	private void generateTile() {
		String result = "";
		switch (tilesBox.getSelectedItem().toString()) {
		case "Empty":
			result = name.getText().replaceAll(" ", "_") + " empty " + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
			break;
		case "Wall":
			result = name.getText().replaceAll(" ", "_") + " wall " + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
			break;
		case "Slide":
			result = name.getText().replaceAll(" ", "_") + " slide " + " "
					+ slideDirectionBox.getSelectedItem().toString() + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
			break;
		case "Teleport":
			result = name.getText().replaceAll(" ", "_") + " tele " + " " + teleportX.getValue() + ","
					+ teleportY.getValue() + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
			break;
		case "Hole":
			result = name.getText().replaceAll(" ", "_") + " hole " + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
			break;
		case "Key":
			result = name.getText().replaceAll(" ", "_") + " key " + " " + key.getText() + " "
					+ tileImage.getSelectedItem() + " \"" + tileText.getText().replaceAll(" ", "_") + " "
					+ String.valueOf(tileText.getText().isEmpty());
			break;
		case "Lock":
			result = name.getText().replaceAll(" ", "_") + " lock " + " " + key.getText() + " "
					+ tileImage.getSelectedItem() + " \"" + tileText.getText().replaceAll(" ", "_") + " "
					+ String.valueOf(tileText.getText().isEmpty());
			break;
		case "Coin":
			result = name.getText().replaceAll(" ", "_") + " coin " + " " + tileImage.getSelectedItem() + " \""
					+ tileText.getText().replaceAll(" ", "_") + " " + String.valueOf(tileText.getText().isEmpty());
			break;
		default:
			break;
		}
		String fileName = "parts/tiles.txt";
		List<String> tiles = FileManager.readFromFile(fileName);
		tiles.add(result);
		FileManager.writeToFile(tiles, fileName);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
