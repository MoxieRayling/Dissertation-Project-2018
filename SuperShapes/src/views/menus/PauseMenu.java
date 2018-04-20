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
import javax.swing.JPanel;
import javax.swing.JTextField;

import views.Window;

@SuppressWarnings("serial")
public class PauseMenu extends JPanel {

	private int sizex = 400;
	private int sizey = 400;
	private JButton resume = new JButton("Resume");
	private JButton save = new JButton("Save Game");
	private JButton load = new JButton("Load Game");
	private JButton exit = new JButton("Exit");
	private List<Component> menu = new ArrayList<Component>();

	public PauseMenu(Window w) {
		this.setLayout(null);

		menu.add(resume);
		menu.add(save);
		menu.add(load);
		menu.add(exit);

		for (Component c : menu) {
			c.setBounds(50, menu.indexOf(c) * 30 + 20, 100, 20);
			this.add(c);
		}

		resume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.returnToGame();
			}
		});
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.saveMenu();
			}
		});
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.loadMenu();
			}
		});
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
