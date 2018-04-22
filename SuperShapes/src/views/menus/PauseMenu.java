package views.menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import views.Window;
import views.animation.MapImage;

@SuppressWarnings("serial")
public class PauseMenu extends JPanel implements ItemListener {
	private Timer timer;
	private Window w;
	private int sizex = 700;
	private int sizey = 400;
	private int buttonSizex = 300;
	private int buttonSizey = 80;
	private int scale = sizex / 13;
	private MapImage map = null;
	private int selectedRoomX = 5;
	private int selectedRoomY = 5;
	private int mapCentreX = 0;
	private int mapCentreY = 0;
	private JButton mapUp;
	private JButton mapLeft;
	private JButton mapRight;
	private JButton mapDown;
	private JLabel title;
	private MenuButton resume = new MenuButton("Resume");
	private MenuButton save = new MenuButton("Save Game");
	private MenuButton load = new MenuButton("Load Game");
	private MenuButton exit = new MenuButton("Exit");
	private List<JComponent> menu = new ArrayList<JComponent>();

	public PauseMenu(Window w, int x, int y) {
		map = new MapImage(w.getMap(x, y), 0, 0, scale);
		this.setLayout(null);
		this.w = w;
		this.setBackground(new Color(0x990000));

		title = new JLabel("<html><div style='text-align: center;'>Game Paused");
		title.setFont(new Font("Arial", Font.BOLD, 60));
		title.setForeground(Color.BLACK);
		title.setBounds(800, 100, 400, 100);
		title.setBackground(new Color(0x660000));
		this.add(title);

		menu.add(resume);
		menu.add(save);
		menu.add(load);
		menu.add(exit);

		for (JComponent b : menu) {
			b.setBounds(850, menu.indexOf(b) * buttonSizey + 220, buttonSizex, buttonSizey);
			b.setFont(new Font("Arial", Font.PLAIN, 40));
			b.setBackground(new Color(0x660000));
			b.setForeground(new Color(0x000000).brighter());
			b.setBorder(null);
			if (b instanceof MenuButton) {
				((MenuButton) b).setHoverBackgroundColor(new Color(0x990000).brighter());
				((MenuButton) b).setPressedBackgroundColor(new Color(0xff2222));
			}

			this.add(b);
		}

		resume.setToolTipText("Go back to the game");
		resume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.returnToGame();
			}
		});

		save.setToolTipText("Opens the save-game menu");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.saveMenu();
			}
		});
		load.setToolTipText("Opens the load-game menu");
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.loadMenu();
			}
		});
		exit.setToolTipText("Quits the game (be sure to save first)");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.mainMenu();
			}
		});
		initMap();
		start();
	}

	private void initMap() {

		mapUp = new JButton();
		mapUp.setBounds(scale * 6, 25, scale, scale);
		mapUp.setOpaque(false);
		mapUp.setContentAreaFilled(false);
		mapUp.setBorderPainted(false);
		mapUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveMap('N');
			}
		});
		this.add(mapUp);

		mapLeft = new JButton();
		mapLeft.setBounds(0, scale * 6 + 25, scale, scale);
		mapLeft.setOpaque(false);
		mapLeft.setContentAreaFilled(false);
		mapLeft.setBorderPainted(false);
		mapLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveMap('W');
			}
		});
		this.add(mapLeft);

		mapDown = new JButton();
		mapDown.setBounds(scale * 6, scale * 12 + 25, scale, scale);
		mapDown.setOpaque(false);
		mapDown.setContentAreaFilled(false);
		mapDown.setBorderPainted(false);
		mapDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveMap('S');
			}
		});
		this.add(mapDown);

		mapRight = new JButton();
		mapRight.setBounds(scale * 12, scale * 6 + 25, scale, scale);
		mapRight.setOpaque(false);
		mapRight.setContentAreaFilled(false);
		mapRight.setBorderPainted(false);
		mapRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveMap('E');
			}
		});
		this.add(mapRight);

		JButton mapHelp = new JButton();
		mapHelp.setEnabled(false);
		mapHelp.setOpaque(false);
		mapHelp.setContentAreaFilled(false);
		mapHelp.setBorderPainted(false);
		mapHelp.setBounds(0, 0, scale * 13, scale * 13);
		mapHelp.setToolTipText("<html>Use the arrow keys or the white arrow buttons to navigate the map.<br>"
				+ "The map shows if a room contains a key, lock, coin, or goal, as well as were the player is.");
		this.add(mapHelp);
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

	public void moveMap(char direction) {
		switch (direction) {
		case 'N':
			mapCentreY -= 1;
			map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
			break;
		case 'E':
			mapCentreX += 1;
			map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
			break;
		case 'S':
			mapCentreY += 1;
			map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
			break;
		case 'W':
			mapCentreX -= 1;
			map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scale);
			break;
		default:
			break;
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}

	@Override
	protected synchronized void paintComponent(Graphics g1) {
		timer.stop();
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g);
		g.setColor(new Color(0x000000));
		g.drawRect(scale, scale + 25, scale * 11, scale * 11);
		map.drawThis(g, scale, scale + 25);
		g.setColor(Color.RED);
		g.drawRect((selectedRoomX + 1) * scale, (selectedRoomY + 1) * scale + 25, scale, scale);

		timer.start();

	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {

	}
}
