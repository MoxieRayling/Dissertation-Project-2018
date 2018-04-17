package views.menus;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Constants;
import views.Window;

@SuppressWarnings("serial")
public class CreateMenu extends JPanel {

	private int sizex = 200;
	private int sizey = 190;
	private JTextField newGame;
	private JButton make = new JButton("Make New Game");
	private JButton exit = new JButton("Exit");

	public CreateMenu(Window w) {

		this.setLayout(null);

		make.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!newGame.getText().isEmpty()) {
					Constants.setGameDir(newGame.getText());
					Constants.setSaveDir("save");
					w.makeNewDir();
					//w.gameRules();
					w.editor();
				}
			}
		});
		make.setBounds(20, 50, 100, 20);
		this.add(make);

		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.mainMenu();
			}
		});
		exit.setBounds(20, 80, 100, 20);
		this.add(exit);

		newGame = new JTextField();
		newGame.setBounds(20, 20, 100, 20);
		this.add(newGame);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}
}
