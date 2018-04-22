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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.FileManager;
import views.Window;

@SuppressWarnings("serial")
public class CreateEntityMenu extends JPanel {
	private Window w;
	private int sizex = 400;
	private int sizey = 500;
	private int buttonSizex = 150;
	private int buttonSizey = 40;
	private JLabel title;
	private int scale = 512 / 13;
	private List<JComponent> blockOptions = new ArrayList<JComponent>();
	private List<JComponent> snakeOptions = new ArrayList<JComponent>();
	private JSlider snakeSize;
	private List<JComponent> ghostOptions = new ArrayList<JComponent>();
	private JSlider ghostPower;
	private List<JComponent> turretOptions = new ArrayList<JComponent>();
	private JSlider turretRate;
	private JSlider turretDelay;
	private String[] directions;
	private JComboBox<String> turretDirectionBox;
	private String[] entities;
	private JComboBox<String> entitiesBox;
	private List<List<JComponent>> entityComponents = new ArrayList<List<JComponent>>();
	private JComboBox<String> entityImage;
	private JTextField name;
	private MenuButton create;
	private MenuButton back;
	private JScrollPane scroll;
	private JLabel entityTypeLabel;
	private JLabel entityNameLabel;
	private JLabel entityImageLabel;
	private JLabel snakeLengthLabel;
	private JLabel ghostPowerLabel;
	private JLabel turretDirectionLabel;
	private JLabel turretRateLabel;
	private JLabel turretDelayLabel;

	public CreateEntityMenu(Window w) {
		this.w = w;
		this.setBackground(new Color(0x990000));
		this.setLayout(null);
		initUI();
		hideEntityMenu();
		int offset = 100;
		for (JComponent c : blockOptions) {
			c.setBounds(sizex / 2 - 20, blockOptions.indexOf(c) * buttonSizey + offset, buttonSizex, buttonSizey);
			if (c == scroll) {
				c.setBounds(sizex / 2 - 20, blockOptions.indexOf(c) * buttonSizey + offset, buttonSizex,
						buttonSizey + offset);
				offset = 200;
			}
			c.setFont(new Font("Arial", Font.PLAIN, 20));
			if (c instanceof MenuButton) {
				((MenuButton) c).setHoverBackgroundColor(new Color(0x990000).brighter());
				((MenuButton) c).setPressedBackgroundColor(new Color(0xff2222));
			}
			c.setVisible(true);
		}
	}

	private void initUI() {
		title = new JLabel("<html><div style='text-align: center;'>Entity Constuctor");
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
				w.refreshEditor();
			}
		});
		back.setBounds(scale, 300, scale * 2, 20);
		this.add(back);

		entities = new String[] { "Block", "Snake", "Ghost", "Turret" };
		entitiesBox = new JComboBox<String>(entities);
		entitiesBox.setToolTipText("<html>Choose a entity type:<br><br>"
				+ "Chase - this entity will chase the player and kill them on contact.<br><br>"
				+ "Chain - this entity follows the player in a chain of entities.<br>"
				+ "It will kill the player on contact.<br><br>"
				+ "Sticky - this entity will hold the player still for a certain number of turns.<br><br>"
				+ "turret - this entity will shoot projectiles in a straight line.<br>"
				+ "The rate and direction of the projectiles can be specified.<br>"
				+ "Projectiles will kill the player.");
		for (int i = 0; i < entitiesBox.getComponentCount(); i++) {
			if (entitiesBox.getComponent(i) instanceof JComponent) {
				((JComponent) entitiesBox.getComponent(i)).setBorder(null);
			}
			if (entitiesBox.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) entitiesBox.getComponent(i)).setBorderPainted(false);
			}
		}
		entitiesBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					hideEntityMenu();
					switch (e.getItem().toString()) {
					case "Block":
						for (JComponent c : blockOptions) {
							c.setBounds(sizex / 2 - 20, blockOptions.indexOf(c) * buttonSizey + 100, buttonSizex,
									buttonSizey);
							c.setVisible(true);
						}
						break;
					case "Snake":
						for (JComponent c : snakeOptions) {
							c.setBounds(sizex / 2 - 20, snakeOptions.indexOf(c) * buttonSizey + 100, buttonSizex,
									buttonSizey);
							c.setVisible(true);
						}
						snakeLengthLabel.setVisible(true);
						break;
					case "Turret":
						for (JComponent c : turretOptions) {
							c.setBounds(sizex / 2 - 20, turretOptions.indexOf(c) * buttonSizey + 100, buttonSizex,
									buttonSizey);
							c.setVisible(true);
						}
						turretDirectionLabel.setVisible(true);
						turretRateLabel.setVisible(true);
						turretDelayLabel.setVisible(true);
						break;
					case "Ghost":
						for (JComponent c : ghostOptions) {
							c.setBounds(sizex / 2 - 20, ghostOptions.indexOf(c) * buttonSizey + 100, buttonSizex,
									buttonSizey);
							c.setVisible(true);
						}
						ghostPowerLabel.setVisible(true);
						break;
					case "None":
						break;
					default:
						break;
					}

					Rectangle r = entityImage.getBounds();
					r.translate(-160, 0);
					entityImageLabel.setBounds(r);
				}
			}
		});
		this.add(entitiesBox);

		name = new JTextField();
		name.setToolTipText("Name your entity");
		create = new MenuButton("Create Entity");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (name.getText().trim().length() > 0)
					generateEntity();
			}
		});
		create.setToolTipText("Add this tile to the list of entity presets");
		entityImage = new JComboBox<String>(getImages());
		entityImage.setToolTipText("Select an image for the entity or select 'none' to use the defaults for the entity type");
		for (int i = 0; i < entityImage.getComponentCount(); i++) {
			if (entityImage.getComponent(i) instanceof JComponent) {
				((JComponent) entityImage.getComponent(i)).setBorder(null);
			}
			if (entityImage.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) entityImage.getComponent(i)).setBorderPainted(false);
			}
		}

		entityTypeLabel = new JLabel("Entity type:", SwingConstants.RIGHT);
		entityTypeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		entityTypeLabel.setForeground(Color.BLACK);
		entityTypeLabel.setBounds(0, 70, 170, 100);
		entityTypeLabel.setBackground(new Color(0x660000));
		this.add(entityTypeLabel);

		entityNameLabel = new JLabel("Entity name:", SwingConstants.RIGHT);
		entityNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		entityNameLabel.setForeground(Color.BLACK);
		entityNameLabel.setBounds(0, 110, 170, 100);
		entityNameLabel.setBackground(new Color(0x660000));
		this.add(entityNameLabel);

		entityImageLabel = new JLabel("Entity image:", SwingConstants.RIGHT);
		entityImageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		entityImageLabel.setForeground(Color.BLACK);
		entityImageLabel.setBounds(0, 150, 170, 100);
		entityImageLabel.setBackground(new Color(0x660000));
		this.add(entityImageLabel);

		snakeLengthLabel = new JLabel("Chain length is 5", SwingConstants.RIGHT);
		snakeLengthLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		snakeLengthLabel.setForeground(Color.BLACK);
		snakeLengthLabel.setBounds(0, 150, 170, 100);
		snakeLengthLabel.setBackground(new Color(0x660000));
		snakeLengthLabel.setVisible(false);
		this.add(snakeLengthLabel);

		ghostPowerLabel = new JLabel("Hold Duration is 3", SwingConstants.RIGHT);
		ghostPowerLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		ghostPowerLabel.setForeground(Color.BLACK);
		ghostPowerLabel.setBounds(0, 150, 170, 100);
		ghostPowerLabel.setBackground(new Color(0x660000));
		ghostPowerLabel.setVisible(false);
		this.add(ghostPowerLabel);

		turretDirectionLabel = new JLabel("Turret direction:", SwingConstants.RIGHT);
		turretDirectionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		turretDirectionLabel.setForeground(Color.BLACK);
		turretDirectionLabel.setBounds(0, 150, 170, 100);
		turretDirectionLabel.setBackground(new Color(0x660000));
		turretDirectionLabel.setVisible(false);
		this.add(turretDirectionLabel);

		turretRateLabel = new JLabel("Turret firerate is 3", SwingConstants.RIGHT);
		turretRateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		turretRateLabel.setForeground(Color.BLACK);
		turretRateLabel.setBounds(0, 190, 170, 100);
		turretRateLabel.setBackground(new Color(0x660000));
		turretRateLabel.setVisible(false);
		this.add(turretRateLabel);

		turretDelayLabel = new JLabel("Turret delay is 0", SwingConstants.RIGHT);
		turretDelayLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		turretDelayLabel.setForeground(Color.BLACK);
		turretDelayLabel.setBounds(0, 230, 170, 100);
		turretDelayLabel.setBackground(new Color(0x660000));
		turretDelayLabel.setVisible(false);
		this.add(turretDelayLabel);

		blockOptions.add(entitiesBox);
		blockOptions.add(name);
		blockOptions.add(entityImage);
		blockOptions.add(create);
		blockOptions.add(back);

		turretOptions.add(entitiesBox);
		turretOptions.add(name);
		directions = new String[] { "North", "East", "South", "West" };
		turretDirectionBox = new JComboBox<String>(directions);
		turretDirectionBox.setToolTipText("Choose a direction for the turret to shoot.");
		
		for (int i = 0; i < turretDirectionBox.getComponentCount(); i++) {
			if (turretDirectionBox.getComponent(i) instanceof JComponent) {
				((JComponent) turretDirectionBox.getComponent(i)).setBorder(null);
			}
			if (turretDirectionBox.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) turretDirectionBox.getComponent(i)).setBorderPainted(false);
			}
		}
		turretOptions.add(turretDirectionBox);
		turretRate = new JSlider(1, 20, 3);
		turretRate.setOpaque(false);
		turretRate.setToolTipText("Select the rate at which the turret will shoot. Turret will shoot 1 time per x turns.");
		turretRate.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				turretRateLabel.setText("Turret firerate is " + turretRate.getValue());
			}
		});
		turretOptions.add(turretRate);
		turretDelay = new JSlider(0, 20, 0);
		turretDelay.setOpaque(false);
		turretDelay.setToolTipText("Select an amount of turns for the turret to wait but shooting.");
		turretDelay.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				turretDelayLabel.setText("Turret delay is " + turretDelay.getValue());
			}
		});
		turretOptions.add(turretDelay);
		turretOptions.add(entityImage);
		turretOptions.add(create);
		turretOptions.add(back);

		snakeOptions.add(entitiesBox);
		snakeOptions.add(name);
		snakeSize = new JSlider(1, 40, 5);
		snakeSize.setOpaque(false);
		snakeSize.setToolTipText("Select the length of the chain. This is measured in tiles.");
		snakeSize.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				snakeLengthLabel.setText("Chain length is " + snakeSize.getValue());
			}
		});
		snakeOptions.add(snakeSize);
		snakeOptions.add(entityImage);
		snakeOptions.add(create);
		snakeOptions.add(back);

		ghostOptions.add(entitiesBox);
		ghostOptions.add(name);
		ghostPower = new JSlider(1, 20, 3);
		ghostPower.setOpaque(false);
		ghostPower.setToolTipText("Select the amount of turns that the entity holds the player still for.");
		ghostPower.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				ghostPowerLabel.setText("Hold duration is " + ghostPower.getValue());
			}
		});
		ghostOptions.add(ghostPower);
		ghostOptions.add(entityImage);
		ghostOptions.add(create);
		ghostOptions.add(back);

		entityComponents.add(blockOptions);
		entityComponents.add(snakeOptions);
		entityComponents.add(turretOptions);
		entityComponents.add(ghostOptions);

		for (List<JComponent> l : entityComponents) {
			for (JComponent b : l) {
				if (l.indexOf(b) > 2)
					b.setBounds(sizex / 2 - buttonSizex / 2, (l.indexOf(b) + 1) * (buttonSizey + 20) + scale * 2,
							buttonSizex, buttonSizey);
				else
					b.setBounds(sizex / 2 - buttonSizex / 2, l.indexOf(b) * (buttonSizey + 20) + scale * 2, buttonSizex,
							buttonSizey);
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
		name.setText("entity " + (entitiesBox.getItemCount() + 1));
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
		for (List<JComponent> l : entityComponents) {
			for (JComponent c : l) {
				c.setVisible(false);
			}
		}
		snakeLengthLabel.setVisible(false);
		ghostPowerLabel.setVisible(false);
		turretDirectionLabel.setVisible(false);
		turretRateLabel.setVisible(false);
		turretDelayLabel.setVisible(false);
	}

	private void generateEntity() {
		String result = "";
		switch (entitiesBox.getSelectedItem().toString()) {
		case "Block":
			result = name.getText().replaceAll(" ", "_") + " block " + entityImage.getSelectedItem();
			break;
		case "Snake":
			result = name.getText().replaceAll(" ", "_") + " snake " + snakeSize.getValue() + " "
					+ entityImage.getSelectedItem();
			break;
		case "Turret":
			result = name.getText().replaceAll(" ", "_") + " turret " + turretRate.getValue() + " "
					+ turretDirectionBox.getSelectedItem().toString() + " " + turretDelay.getValue() + " "
					+ entityImage.getSelectedItem();
			break;
		case "Ghost":
			result = name.getText().replaceAll(" ", "_") + " ghost " + ghostPower.getValue() + " "
					+ entityImage.getSelectedItem();
			break;
		default:
			break;

		}
		w.createEntity(result);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
