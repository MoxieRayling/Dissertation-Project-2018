package views.menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import model.FileManager;
import views.Window;

@SuppressWarnings("serial")
public class CreateTileMenu extends JPanel {
	private Window w;
	private int sizex = 400;
	private int sizey = 400;
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

	public CreateTileMenu(Window w) {
		this.w = w;
		this.setLayout(null);
		this.setBackground(new Color(0x990000));
		initUI();
		for (JComponent b : emptyOptions) {
			b.setBounds(sizex / 2 - buttonSizex / 2, emptyOptions.indexOf(b) * buttonSizey + 100, buttonSizex,
					buttonSizey);
			b.setFont(new Font("Arial", Font.PLAIN, 20));
			if (!(b instanceof JTextField) && !(b instanceof JTextArea)) {
				b.setBackground(new Color(0x660000));
				b.setForeground(new Color(0x000000).brighter());
				b.setBorder(null);
			}
			if (b instanceof MenuButton) {
				((MenuButton) b).setHoverBackgroundColor(new Color(0x990000).brighter());
				((MenuButton) b).setPressedBackgroundColor(new Color(0xff2222));
			}

			b.setVisible(true);
		}
	}

	private void initUI() {
		back = new MenuButton("Back");
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				w.back();
				w.refreshEditor();
			}
		});
		this.add(back);

		tiles = new String[] { "Empty", "Wall", "Slide", "Teleport", "Hole", "Key", "Lock", "Coin" };
		tilesBox = new JComboBox<String>(tiles);
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
					switch (e.getItem().toString()) {
					case "Empty":
						for (Component c : emptyOptions) {
							c.setBounds(sizex / 2 - buttonSizex / 2, emptyOptions.indexOf(c) * buttonSizey + 100,
									buttonSizex, buttonSizey);

							if (c instanceof JTextArea) {
								c.setBounds(sizex / 2 - buttonSizex / 2, emptyOptions.indexOf(c) * buttonSizey + 100,
										buttonSizex, buttonSizey);

							}
							c.setVisible(true);
						}
						break;
					case "Wall":
						for (Component c : wallOptions) {
							c.setBounds(sizex / 2 - buttonSizex / 2, wallOptions.indexOf(c) * buttonSizey + 100,
									buttonSizex, buttonSizey);

							if (c instanceof JTextArea) {
								c.setBounds(sizex / 2 - buttonSizex / 2, wallOptions.indexOf(c) * buttonSizey + 100,
										buttonSizex, buttonSizey);

							}
							c.setVisible(true);
						}
						break;
					case "Slide":
						for (Component c : slideOptions) {
							c.setBounds(sizex / 2 - buttonSizex / 2, slideOptions.indexOf(c) * buttonSizey + 100,
									buttonSizex, buttonSizey);

							if (c instanceof JTextArea) {
								c.setBounds(sizex / 2 - buttonSizex / 2, slideOptions.indexOf(c) * buttonSizey + 100,
										buttonSizex, buttonSizey);

							}
							c.setVisible(true);
						}
						break;
					case "Teleport":
						for (Component c : teleOptions) {
							c.setBounds(sizex / 2 - buttonSizex / 2, teleOptions.indexOf(c) * buttonSizey + 100,
									buttonSizex, buttonSizey);

							if (c instanceof JTextArea) {
								c.setBounds(sizex / 2 - buttonSizex / 2, teleOptions.indexOf(c) * buttonSizey + 100,
										buttonSizex, buttonSizey);

							}
							c.setVisible(true);
						}
						break;
					case "Hole":
						for (Component c : holeOptions) {
							c.setBounds(sizex / 2 - buttonSizex / 2, holeOptions.indexOf(c) * buttonSizey + 100,
									buttonSizex, buttonSizey);

							if (c instanceof JTextArea) {
								c.setBounds(sizex / 2 - buttonSizex / 2, holeOptions.indexOf(c) * buttonSizey + 100,
										buttonSizex, buttonSizey);

							}
							c.setVisible(true);
						}
						break;
					case "Key":
						for (Component c : keyOptions) {
							c.setBounds(sizex / 2 - buttonSizex / 2, keyOptions.indexOf(c) * buttonSizey + 100,
									buttonSizex, buttonSizey);

							if (c instanceof JTextArea) {
								c.setBounds(sizex / 2 - buttonSizex / 2, keyOptions.indexOf(c) * buttonSizey + 100,
										buttonSizex, buttonSizey);

							}
							c.setVisible(true);
						}
						break;
					case "Lock":
						for (Component c : lockOptions) {
							c.setBounds(sizex / 2 - buttonSizex / 2, lockOptions.indexOf(c) * buttonSizey + 100,
									buttonSizex, buttonSizey);

							if (c instanceof JTextArea) {
								c.setBounds(sizex / 2 - buttonSizex / 2, lockOptions.indexOf(c) * buttonSizey + 100,
										buttonSizex, buttonSizey);

							}
							c.setVisible(true);
						}
						break;
					case "Coin":
						for (Component c : coinOptions) {
							c.setBounds(sizex / 2 - buttonSizex / 2, coinOptions.indexOf(c) * buttonSizey + 100,
									buttonSizex, buttonSizey);

							if (c instanceof JTextArea) {
								c.setBounds(sizex / 2 - buttonSizex / 2, coinOptions.indexOf(c) * buttonSizey + 100,
										buttonSizex, buttonSizey);

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
		create = new MenuButton("Create Tile");
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
		for (int i = 0; i < tileImage.getComponentCount(); i++) {
			if (tileImage.getComponent(i) instanceof JComponent) {
				((JComponent) tileImage.getComponent(i)).setBorder(null);
			}
			if (tileImage.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) tileImage.getComponent(i)).setBorderPainted(false);
			}
		}
		tileText = new JTextArea();
		tileText.setWrapStyleWord(true);
		tileText.setLineWrap(true);

		emptyOptions.add(tilesBox);
		emptyOptions.add(name);
		emptyOptions.add(tileImage);
		emptyOptions.add(tileText);

		wallOptions.add(tilesBox);
		wallOptions.add(name);
		wallOptions.add(tileImage);
		wallOptions.add(tileText);

		slideOptions.add(tilesBox);
		slideOptions.add(name);
		slideDirectionBox = new JComboBox<String>(directions);
		slideOptions.add(slideDirectionBox);
		slideOptions.add(tileImage);
		slideOptions.add(tileText);

		teleOptions.add(tilesBox);
		teleOptions.add(name);
		teleportX = new JSlider(0, 10, 0);
		teleportX.setOpaque(false);
		teleportY = new JSlider(0, 10, 0);
		teleportY.setOpaque(false);
		teleOptions.add(teleportX);
		teleOptions.add(teleportY);
		teleOptions.add(tileImage);
		teleOptions.add(tileText);

		holeOptions.add(tilesBox);
		holeOptions.add(name);
		holeOptions.add(tileImage);
		holeOptions.add(tileText);

		keyOptions.add(tilesBox);
		keyOptions.add(name);
		key = new JTextField();
		keyOptions.add(key);
		keyOptions.add(tileImage);
		keyOptions.add(tileText);

		lockOptions.add(tilesBox);
		lockOptions.add(name);
		lockOptions.add(key);
		lockOptions.add(tileImage);
		lockOptions.add(tileText);

		coinOptions.add(tilesBox);
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

		for (List<JComponent> l : tileComponents) {
			l.add(this.create);
			l.add(this.back);
			for (JComponent b : l) {
				b.setBounds(sizex / 2 - buttonSizex / 2, l.indexOf(b) * buttonSizey + 100, buttonSizex, buttonSizey);
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
		tileText.setBorder(border);
		tileText.setBackground(new Color(0xffffff));
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
		w.createTile(result);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
