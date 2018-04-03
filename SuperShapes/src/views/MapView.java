
package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import controller.Constants;

@SuppressWarnings("serial")
public class MapView extends JPanel implements Constants, ItemListener {
	private Timer timer;
	private Window w;
	private int sizex = 512;
	private int sizey = 512;
	private int scalex = sizex / 13;
	private int scaley = sizey / 13;
	private MapImage map = null;
	private int selectedRoomX = 5;
	private int selectedRoomY = 5;
	private int mapCentreX = 0;
	private int mapCentreY = 0;
	private JButton mapUp;
	private JButton mapLeft;
	private JButton mapRight;
	private JButton mapDown;

	public MapView(Window w, int x, int y) {
		map = new MapImage(w.getMap(x, y), 0, 0, scalex, scaley);
		this.w = w;
		this.setLayout(null);
		initUI();
		this.setBackground(new Color(0x888888));
		start();
	}

	private void initUI() {

		mapUp = new JButton();
		mapUp.setBounds(scalex * 6, 0, scalex, scaley);
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
		mapLeft.setBounds(0, scaley * 6, scalex, scaley);
		mapLeft.setOpaque(false);
		mapLeft.setContentAreaFilled(false);
		mapLeft.setBorderPainted(false);
		mapLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveMap('E');
			}
		});
		this.add(mapLeft);

		mapDown = new JButton();
		mapDown.setBounds(scalex * 6, scaley * 12, scalex, scaley);
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
		mapRight.setBounds(scalex * 12, scaley * 6, scalex, scaley);
		mapRight.setOpaque(false);
		mapRight.setContentAreaFilled(false);
		mapRight.setBorderPainted(false);
		mapRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveMap('W');
			}
		});
		this.add(mapRight);
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
			map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scalex, scaley);
			break;
		case 'E':
			mapCentreX += 1;
			map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scalex, scaley);
			break;
		case 'S':
			mapCentreY += 1;
			map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scalex, scaley);
			break;
		case 'W':
			mapCentreX -= 1;
			map = new MapImage(w.getMap(mapCentreX, mapCentreY), 0, 0, scalex, scaley);
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
		g.drawRect(scalex, scaley, scalex * 11, scaley * 11);
		map.drawThis(g, scalex, scaley);
		g.setColor(Color.RED);
		g.drawRect((selectedRoomX + 1) * scalex, (selectedRoomY + 1) * scaley, scalex, scaley);

		timer.start();

	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {

	}
}
