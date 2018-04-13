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

import controller.Constants;

@SuppressWarnings("serial")
public class StartMenu extends JPanel {

	private int sizex = 200;
	private int sizey = 250;
	private JComboBox<String> games;
	private JButton selectGame = new JButton("Select Game");
	private JButton editGame = new JButton("Edit Game");
	private JButton createGame = new JButton("Create Game");
	private JButton options = new JButton("Options");
	private JButton exitGame = new JButton("Exit");
	private List<Component> components = new ArrayList<Component>();

	StartMenu(Window w) {
		this.setLayout(null);

		games = new JComboBox<String>(getDirectories());
		components.add(games);
		components.add(selectGame);
		components.add(editGame);
		components.add(createGame);
		components.add(options);
		components.add(exitGame);

		for (Component b : components) {
			b.setBounds(50, components.indexOf(b) * 30 + 20, 100, 20);
			this.add(b);
		}

		selectGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Constants.setGameDir((String) games.getSelectedItem());
				w.loadMenu();
			}
		});

		editGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Constants.setGameDir((String) games.getSelectedItem());
				w.gameRules();
			}
		});
		createGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.createGame();
			}
		});
	}

	private String[] getDirectories() {
		File file = new File("games");
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		return directories;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
