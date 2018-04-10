package views;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StartMenu extends JPanel {

	private int sizex = 200;
	private int sizey = 190;
	private JButton newGame = new JButton("New Game");
	private JButton continueGame = new JButton("Continue");
	private JButton createGame = new JButton("Create Game");
	private JButton options = new JButton("Options");
	private JButton exitGame = new JButton("Exit");
	private List<JButton> buttons = new ArrayList<JButton>();

	StartMenu(Window w) {
		this.setLayout(null);
		newGame.setSize(getButtonSize());
		continueGame.setSize(getButtonSize());
		options.setSize(getButtonSize());
		
		buttons.add(newGame);
		buttons.add(continueGame);
		buttons.add(createGame);
		buttons.add(options);
		buttons.add(exitGame);
		
		for(JButton b : buttons) {
			b.setBounds(50, buttons.indexOf(b)*30 + 20, 100, 20);
			this.add(b);
		}

		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.newGame();
			}
		});
		continueGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				w.loadGame();
			}
		});
		createGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//w.createGame();
				w.editor("");
			}
		});
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(sizex, sizey);
	}

	public Dimension getButtonSize() {
		return new Dimension(sizex, sizey / 4);
	}
}
