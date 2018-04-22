package views.menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import model.FileManager;
import views.Window;

@SuppressWarnings("serial")
public class StartMenu extends JPanel {

	private int sizex = 400;
	private int sizey = 400;
	private int buttonSizex = 150;
	private int buttonSizey = 40;
	private JLabel title;
	private JComboBox<String> games;
	private MenuButton selectGame = new MenuButton("Select Game");
	private MenuButton editGame = new MenuButton("Edit Game");
	private MenuButton createGame = new MenuButton("Create Game");
	private MenuButton exitGame = new MenuButton("Exit");
	private List<JComponent> components = new ArrayList<JComponent>();

	public StartMenu(Window w) {
		this.setLayout(null);
		this.setBackground(new Color(0x990000));

		title = new JLabel("<html><div style='text-align: center;'>Super Deluxe <br>GameMaker");
		title.setFont(new Font("Arial", Font.BOLD, 40));
		title.setForeground(Color.BLACK);
		title.setBounds(sizex / 2 - 125, 30, 400, 100);
		title.setBackground(new Color(0x660000));
		this.add(title);
		games = new JComboBox<String>(getDirectories());
		for (int i = 0; i < games.getComponentCount(); i++) {
			if (games.getComponent(i) instanceof JComponent) {
				((JComponent) games.getComponent(i)).setBorder(null);
			}
			if (games.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) games.getComponent(i)).setBorderPainted(false);
			}
		}
		components.add(games);
		components.add(selectGame);
		components.add(editGame);
		components.add(createGame);
		components.add(exitGame);

		for (JComponent b : components) {
			b.setBounds(sizex / 2 - buttonSizex / 2, components.indexOf(b) * buttonSizey + 150, buttonSizex,
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
		selectGame.setToolTipText("Play the game shown in the list above");
		selectGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (games.getSelectedItem() != null) {
					FileManager.setGameDir((String) games.getSelectedItem());
					w.newGameMenu();
				}
			}
		});

		editGame.setToolTipText("Modify the game shown in the list above");
		editGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (games.getSelectedItem() != null) {
					FileManager.setGameDir((String) games.getSelectedItem());
					FileManager.setSaveDir("games/" + (String) games.getSelectedItem() + "/saves/save1");
					w.editor();
				}
			}
		});
		createGame.setToolTipText("Create a new game");
		createGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.createGame();
			}
		});
		exitGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.exit();
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
