package views;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CreateGame extends JPanel {

	private int sizex = 200;
	private int sizey = 190;
	private JComboBox<String> games;
	private JTextField newGame;
	private JButton edit = new JButton("Edit Game");
	private JButton make = new JButton("Make New Game");
	private JButton exit = new JButton("Exit");

	CreateGame(Window w) {

		this.setLayout(null);
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String dir = (String) games.getSelectedItem();
				w.setDirectory(dir);
				w.gameRules(dir);
			}
		});
		edit.setBounds(20, 40, 100, 20);
		this.add(edit);

		make.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String dir = "games/" + newGame.getText();
				File file = new File(dir);
				if (file.isDirectory()) {
					// warning
				} else {
					new File(dir).mkdirs();
					w.setDirectory(dir);
					w.gameRules(dir);
				}
			}
		});
		make.setBounds(20, 100, 100, 20);
		this.add(make);

		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.mainMenu();
			}
		});
		exit.setBounds(20, 130, 100, 20);
		this.add(exit);

		games = new JComboBox<String>(getDirectories());
		games.setBounds(20, 10, 100, 20);
		this.add(games);

		newGame = new JTextField();
		newGame.setBounds(20, 70, 100, 20);
		this.add(newGame);
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
