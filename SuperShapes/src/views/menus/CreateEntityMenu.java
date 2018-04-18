package views.menus;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import controller.Constants;
import views.Window;

@SuppressWarnings("serial")
public class CreateEntityMenu extends JPanel implements ItemListener {
	private Window w;
	private int sizex = 1200;
	private int sizey = 600;
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

	public CreateEntityMenu(Window w) {
		this.w = w;
		this.setLayout(null);
		initUI();
	}

	private void initUI() {

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

	private void hideEntityMenu() {
		for (List<Component> l : entityComponents) {
			for (Component c : l) {
				c.setVisible(false);
			}
		}
	}

	private String generateTile() {
		String result = "";
		switch (entitiesBox.getSelectedItem().toString()) {
		case "Block":
			result= "block " + " " + entityImage.getSelectedItem();
			break;
		case "Snake":
			result = "snake " + " " + snakeSize.getValue() + " "
					+ entityImage.getSelectedItem();
			break;
		case "Turret":
			result = "turret " + " " + turretRate.getValue() + " "
					+ turretDirectionBox.getSelectedItem().toString() + " " + turretDelay.getValue() + " "
					+ entityImage.getSelectedItem();
			break;
		case "Ghost":
			result = "ghost " + " " + ghostPower.getValue() + " "
					+ entityImage.getSelectedItem();
			break;
		case "None":
			result = "None";
		default:
			break;

		}
		return result;
	}


	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {

	}

}
