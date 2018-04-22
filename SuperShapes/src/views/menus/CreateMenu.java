package views.menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import model.FileManager;
import views.Window;

@SuppressWarnings("serial")
public class CreateMenu extends JPanel {

	private int sizex = 400;
	private int sizey = 400;
	private int buttonSizex = 200;
	private int buttonSizey = 40;
	private JLabel title;
	private JTextField newGame;
	private MenuButton make = new MenuButton("Make New Game");
	private MenuButton exit = new MenuButton("Back");

	public CreateMenu(Window w) {

		this.setLayout(null);
		this.setBackground(new Color(0x990000));

		title = new JLabel("<html><div style='text-align: center;'>Name your game");
		title.setFont(new Font("Arial", Font.BOLD, 40));
		title.setForeground(Color.BLACK);
		title.setBounds(sizex / 2 - 160, 30, 400, 100);
		title.setBackground(new Color(0x660000));
		this.add(title);

		make.setBounds(sizex / 2 - buttonSizex / 2, 2 * buttonSizey + 150, buttonSizex, buttonSizey);
		make.setFont(new Font("Arial", Font.PLAIN, 20));
		make.setBackground(new Color(0x660000));
		make.setForeground(new Color(0x000000).brighter());
		make.setBorder(null);
		make.setHoverBackgroundColor(new Color(0x990000).brighter());
		make.setPressedBackgroundColor(new Color(0xff2222));
		make.setToolTipText("Creates a directory for a new game with the name given in the text field above");
		make.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!newGame.getText().isEmpty() && !FileManager.gameExists(newGame.getText())) {
					FileManager.setGameDir(newGame.getText().trim());
					FileManager.makeNewDir();
					FileManager.setSaveDir(newGame.getText().trim() + "/saves/save1");
					w.makeNewDir();
					w.editor();
				}
			}
		});
		this.add(make);

		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.mainMenu();
			}
		});
		exit.setBounds(sizex / 2 - buttonSizex / 2, 3 * buttonSizey + 150, buttonSizex, buttonSizey);
		exit.setFont(new Font("Arial", Font.PLAIN, 20));
		exit.setBackground(new Color(0x660000));
		exit.setForeground(new Color(0x000000).brighter());
		exit.setBorder(null);
		exit.setHoverBackgroundColor(new Color(0x990000).brighter());
		exit.setPressedBackgroundColor(new Color(0xff2222));
		this.add(exit);

		newGame = new JTextField();
		newGame.setBounds(sizex / 2 - buttonSizex / 2, buttonSizey + 150, buttonSizex, buttonSizey);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		newGame.setBorder(border);
		newGame.setBackground(new Color(0xffffff));
		this.add(newGame);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
