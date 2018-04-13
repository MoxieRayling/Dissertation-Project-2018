package views;

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

@SuppressWarnings("serial")
public class PauseMenu extends JPanel {

	private int sizex = 200;
	private int sizey = 200;
	private String game = "";
	private JButton resume = new JButton("Resume");
	private JButton save = new JButton("Save Game");
	private JButton load = new JButton("Load Game");
	private JButton controls = new JButton("Controls");
	private JButton exit = new JButton("Exit");
	private List<Component> menu = new ArrayList<Component>();

	PauseMenu(Window w) {
		this.setLayout(null);

		menu.add(resume);
		menu.add(save);
		menu.add(load);
		menu.add(controls);
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
		controls.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.controls();
			}
		});
	}

	private String[] getSaves() {
		File file = new File("games/" + game + "/saves");
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});

		return directories;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
