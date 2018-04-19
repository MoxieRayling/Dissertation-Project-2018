package views.menus;

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
import javax.swing.JTextField;

import model.FileManager;
import views.Window;

@SuppressWarnings("serial")
public class CreateEntityMenu extends JPanel {
	private Window w;
	private int sizex = 400;
	private int sizey = 400;
	private int scale = 512 / 13;
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
	private List<List<Component>> entityComponents = new ArrayList<List<Component>>();
	private JComboBox<String> entityImage;
	private JTextField name;
	private JButton create;
	private JButton back;

	public CreateEntityMenu(Window w) {
		this.w = w;
		this.setLayout(null);
		initUI();
		for (Component c : blockOptions) {
			c.setBounds(scale, scale * (blockOptions.indexOf(c) + 2), scale * 2, 20);
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
		back.setBounds(scale, 300, scale * 2, 20);
		this.add(back);

		entities = new String[] { "Block", "Snake", "Ghost", "Turret" };
		entitiesBox = new JComboBox<String>(entities);
		entitiesBox.setBounds(scale, scale, scale * 2, 20);
		entitiesBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideEntityMenu();
					switch (e.getItem().toString()) {
					case "Block":
						for (Component c : blockOptions) {
							c.setBounds(scale, scale * (blockOptions.indexOf(c) + 2), scale * 2, 20);
							c.setVisible(true);
						}
						break;
					case "Snake":
						for (Component c : snakeOptions) {
							c.setBounds(scale, scale * (snakeOptions.indexOf(c) + 2), scale * 2, 20);
							c.setVisible(true);
						}
						break;
					case "Turret":
						for (Component c : turretOptions) {
							c.setBounds(scale, scale * (turretOptions.indexOf(c) + 2), scale * 2, 20);
							c.setVisible(true);
						}
						break;
					case "Ghost":
						for (Component c : ghostOptions) {
							c.setBounds(scale, scale * (ghostOptions.indexOf(c) + 2), scale * 2, 20);
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

		name = new JTextField();
		create = new JButton("Create Entity");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (name.getText().trim().length() > 0)
					generateEntity();
			}
		});
		entityImage = new JComboBox<String>(getImages());

		blockOptions.add(name);
		blockOptions.add(entityImage);
		blockOptions.add(create);

		turretOptions.add(name);
		directions = new String[] { "North", "East", "South", "West" };
		turretDirectionBox = new JComboBox<String>(directions);
		turretOptions.add(turretDirectionBox);
		turretRate = new JSlider(1, 11, 3);
		turretRate.setOpaque(false);
		turretOptions.add(turretRate);
		turretDelay = new JSlider(0, 122, 0);
		turretDelay.setOpaque(false);
		turretOptions.add(turretDelay);
		turretOptions.add(entityImage);
		turretOptions.add(create);

		snakeOptions.add(name);
		snakeSize = new JSlider(1, 122, 5);
		snakeSize.setOpaque(false);
		snakeOptions.add(snakeSize);
		snakeOptions.add(entityImage);
		snakeOptions.add(create);

		ghostOptions.add(name);
		ghostPower = new JSlider(1, 122, 3);
		ghostPower.setOpaque(false);
		ghostOptions.add(ghostPower);
		ghostOptions.add(entityImage);
		ghostOptions.add(create);

		entityComponents.add(blockOptions);
		entityComponents.add(snakeOptions);
		entityComponents.add(turretOptions);
		entityComponents.add(ghostOptions);

		for (List<Component> l : entityComponents) {
			for (Component c : l) {
				c.setBounds(scale, scale * (l.indexOf(c) + 2), scale * 2, 20);
				c.setVisible(false);
				this.add(c);
			}
		}
		entityImage.setVisible(false);

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

	private void hideEntityMenu() {
		for (List<Component> l : entityComponents) {
			for (Component c : l) {
				c.setVisible(false);
			}
		}
	}

	private void generateEntity() {
		String result = "";
		switch (entitiesBox.getSelectedItem().toString()) {
		case "Block":
			result = name.getText().replaceAll(" ", "_") + " block " + entityImage.getSelectedItem();
			break;
		case "Snake":
			result = name.getText().replaceAll(" ", "_") + " snake " + snakeSize.getValue() + " " + entityImage.getSelectedItem();
			break;
		case "Turret":
			result = name.getText().replaceAll(" ", "_") + " turret " + turretRate.getValue() + " "
					+ turretDirectionBox.getSelectedItem().toString() + " " + turretDelay.getValue() + " "
					+ entityImage.getSelectedItem();
			break;
		case "Ghost":
			result = name.getText().replaceAll(" ", "_") + " ghost " + ghostPower.getValue() + " " + entityImage.getSelectedItem();
			break;
		default:
			break;

		}
		String fileName = "parts/entities.txt";
		List<String> tiles = FileManager.readFromFile(fileName);
		tiles.add(result);
		FileManager.writeToFile(tiles, fileName);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
