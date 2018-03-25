package views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import controller.Constants;
import controller.Controller;
import model.Entity;
import model.Model;
import model.Player;
import model.Room;

@SuppressWarnings("serial")
public class Window extends JFrame implements View, Constants {

	private Controller c;
	private StartMenu start;
	private Animation game;
	private Editor editor;
	private JFrame frame;
	private Boolean input = true;

	public Window() {
		frame = this;
		start = new StartMenu(this);
		game = new Animation(this);
		editor = new Editor(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.add(start);
		this.pack();
		this.setVisible(true);
		game.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (input) {
					c.Input(e.getKeyCode());
				}
				input = false;
			}

			@Override
			public void keyTyped(KeyEvent e) {

			}

		});
		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				game.setSize(frame.getWidth(), frame.getHeight());
				if (c != null)
					c.notifyObservers();
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public Boolean getInput() {
		return input;
	}

	public void setInput(Boolean input) {
		this.input = input;
		if (c != null)
			c.setNext(input);
	}

	@Override
	public void Update(ActionEvent e) {
		Object o = e.getSource();
		if (o instanceof Model) {
			setInput(((Model) o).getInput());
			game.modelUpdate((Model) o);
		} else if (o instanceof Room) {
			game.roomUpdate((Room) o);
			editor.roomUpdate((Room) o);
		} else if (o instanceof Player) {
			game.playerUpdate((Player) o);
		} else if (o instanceof Entity) {
			game.entityUpdate((Entity) o);
		}

	}

	@Override
	public void SetCon(Controller controller) {
		c = controller;
	}

	public void restart() {
		c.restart();
	}

	public void newGame() {
		c.newGame();
		//c.setMode("game");
		this.setSize(512,512);
		this.getContentPane().removeAll();
		this.getContentPane().add(game);
		this.validate();
		this.repaint();
		game.requestFocusInWindow();
	}

	public void loadGame() {
		c.loadGame();
		c.setMode("game");
		this.setSize(512,512);
		this.getContentPane().removeAll();
		this.getContentPane().add(game);
		this.validate();
		this.repaint();
		game.requestFocusInWindow();
	}

	public void editor() {
		c.newGame();
		c.setMode("editor");
		this.setSize(770, 600);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(editor);
		this.validate();
		this.repaint();
		editor.requestFocusInWindow();
	}

	public void addToRoom(String[] lines, int x, int y) {
		c.addToRoom(lines,x,y);
	}

}