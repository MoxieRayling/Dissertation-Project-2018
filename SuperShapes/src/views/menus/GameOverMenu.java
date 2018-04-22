package views.menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import views.Window;

@SuppressWarnings("serial")
public class GameOverMenu extends JPanel {

	private int sizex = 400;
	private int sizey = 400;
	private int buttonSizex = 150;
	private int buttonSizey = 40;
	private JLabel title = new JLabel("Game Over");
	private MenuButton load = new MenuButton("Load Game");
	private MenuButton menu = new MenuButton("Main Menu");
	private MenuButton exit = new MenuButton("Exit");
	private List<MenuButton> buttons = new ArrayList<MenuButton>();

	public GameOverMenu(Window w, String titleText, String text) {
		this.setLayout(null);
		this.setBackground(new Color(0x990000));

		title = new JLabel("<html><div style='text-align: center;'>" + titleText, SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 40));
		title.setForeground(Color.BLACK);
		title.setBounds(sizex / 2 - 200, 10, 400, 100);
		title.setBackground(new Color(0x660000));
		this.add(title);
		title = new JLabel("<html><div style='text-align: center;'>" + text, SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.PLAIN, 20));
		title.setForeground(Color.BLACK);
		title.setBounds(sizex / 2 - 175, 100, 350, 100);
		title.setBackground(new Color(0x660000));
		this.add(title);
		buttons.add(load);
		buttons.add(menu);
		buttons.add(exit);
		for (MenuButton b : buttons) {
			b.setBounds(sizex / 2 - buttonSizex / 2, buttons.indexOf(b) * buttonSizey + 200, buttonSizex, buttonSizey);
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
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
