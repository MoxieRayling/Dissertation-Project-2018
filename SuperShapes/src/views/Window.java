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
	private MapView map;
	private JFrame frame;
	private Boolean input = true;
	private CreateGame create;
	private GameRules gameRules;

	public Window() {
		frame = this;
		start = new StartMenu(this);
		game = new Animation(this);
		editor = new Editor(this);
		create = new CreateGame(this);
		gameRules = new GameRules(this);
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
					if (getContentPane().contains(game.getLocation())) {
						if (e.getKeyChar() == 'm') {
							map();
						} else {
							c.Input(e.getKeyCode());
							input = false;
						}
					} 
				}
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
		// c.setMode("game");
		returnToGame();
	}

	public void loadGame() {
		c.loadGame();
		c.setMode("game");
		returnToGame();
	}

	public void mainMenu() {
		this.setSize(200, 190);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(start);
		this.validate();
		this.repaint();
		start.requestFocusInWindow();
	}

	public void createGame() {
		this.setSize(200, 190);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(create);
		this.validate();
		this.repaint();
		create.requestFocusInWindow();
	}

	public void editor(String game) {
		c.runEditor();
		editor.setGame(game);
		c.setMode("editor");
		this.setSize(1200, 600);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(editor);
		this.validate();
		this.repaint();
		editor.setMap();
		editor.requestFocusInWindow();
	}

	public void map() {
		this.map = new MapView(this, c.getX(), c.getY());
		map.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (getContentPane().contains(map.getLocation())) {
					System.out.println("yes");
					if (e.getKeyCode() == 27 || e.getKeyCode() == 77 || e.getKeyCode() == 13) {
						returnToGame();
					} else if (e.getKeyCode() == 38) {
						map.moveMap('N');
					} else if (e.getKeyCode() == 37) {
						map.moveMap('W');
					} else if (e.getKeyCode() == 39) {
						map.moveMap('E');
					} else if (e.getKeyCode() == 40) {
						map.moveMap('S');
					}
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {

			}

		});
		this.setSize(512, 512);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(map);
		this.validate();
		this.repaint();
		map.requestFocusInWindow();
	}

	public void returnToGame() {
		this.setSize(512, 512);
		this.getContentPane().removeAll();
		this.getContentPane().add(game);
		this.validate();
		this.repaint();
		game.requestFocusInWindow();
	}

	public void addToRoom(String[] lines, int x, int y) {
		c.addToRoom(lines, x, y);
	}

	public void changeRoom(int x, int y) {
		c.changeRoom(x, y);
	}

	public String[] getRooms() {
		return c.getRooms();
	}

	public void changeRoom(String id) {
		c.changeRoom(id);
	}

	public void addRoom() {
		c.addRoom();
	}

	public void export() {
		c.export();
	}

	public String[][] getMap(int x, int y) {
		return c.getMap(x, y);
	}

	public void gameRules(String dir) {
		gameRules.setGame(dir);
		this.setSize(650, 512);
		this.getContentPane().removeAll();
		this.getContentPane().add(gameRules);
		this.validate();
		this.repaint();
		gameRules.requestFocusInWindow();
	}
	
	public void setDirectory(String dir) {
		c.setDirectory(dir);
	}
	public void setExit(int index, int coord) {
		c.setExit(index,coord);
	}

	public void setRoomSize(int[] size) {
		c.setRoomSize(size);
	}

}