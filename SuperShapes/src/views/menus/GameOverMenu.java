package views.menus;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import controller.Constants;
import views.Window;

@SuppressWarnings("serial")
public class GameOverMenu extends JPanel {

	private int sizex = 200;
	private int sizey = 200;
	private JLabel gameOver = new JLabel("Game Over");
	private JButton load = new JButton("Load Game");
	private JButton menu = new JButton("Main Menu");
	private JButton exit = new JButton("Exit");
	private List<Component> buttons = new ArrayList<Component>();

	public GameOverMenu(Window w) {
		this.setLayout(null);
		gameOver.setBounds(50, 20, 100, 20);
		this.add(gameOver);
		buttons.add(load);
		buttons.add(menu);
		buttons.add(exit);
		for (Component c : buttons) {
			c.setBounds(50, (buttons.indexOf(c) + 1) * 30 + 20, 100, 20);
			this.add(c);
		}
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("yes");
				w.loadMenu();
			}
		});
		menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.mainMenu();
			}
		});
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.exit();
			}
		});
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
