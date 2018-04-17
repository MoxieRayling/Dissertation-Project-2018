package views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Constants;
import controller.Controller;
import model.Model;
import model.Room;
import model.entities.Entity;
import model.entities.Player;
import views.animation.Animation;
import views.menus.CreateMenu;
import views.menus.Editor;
import views.menus.GameOverMenu;
import views.menus.GameRulesMenu;
import views.menus.LoadMenu;
import views.menus.MapView;
import views.menus.NewGameMenu;
import views.menus.PauseMenu;
import views.menus.SaveMenu;
import views.menus.StartMenu;

@SuppressWarnings("serial")
public class Window extends JFrame implements View {

	private Controller c;
	private StartMenu start;
	private Animation game;
	private Editor editor;
	private MapView map;
	private PauseMenu pause;
	private LoadMenu load;
	private SaveMenu save;
	private NewGameMenu ngMenu;
	private JFrame frame;
	private Boolean input = true;
	private CreateMenu create;
	private GameRulesMenu gameRules;
	private GameOverMenu gameOver;
	private String mode = "game";
	private List<JPanel> history = new ArrayList<JPanel>();

	public Window() {
		frame = this;
		start = new StartMenu(this);
		game = new Animation(this);
		create = new CreateMenu(this);
		gameRules = new GameRulesMenu(this);
		pause = new PauseMenu(this);
		load = new LoadMenu(this);
		save = new SaveMenu(this);
		ngMenu = new NewGameMenu(this);
		gameOver = new GameOverMenu(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.add(start);
		this.pack();
		this.setVisible(true);
		game.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (mode.equals("text")) {
					game.nextText();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (input) {
					if (getContentPane().contains(game.getLocation())) {
						if (mode.equals("game")) {
							if (e.getKeyChar() == 'm') {
								map();
							} else if (e.getKeyCode() == 27) {
								pauseMenu();
							} else {
								c.Input(e.getKeyCode());
								input = false;
							}
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
				if (c != null) {
					// c.notifyObservers();
				}
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		this.mainMenu();
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
		} else if (o instanceof Room) {
			game.roomUpdate((Room) o);
			if (editor != null)
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
		this.setResizable(true);
		c.loadGame();
		c.setMode("game");
		returnToGame();
	}

	public void gameOver() {
		history.add(gameOver);
		this.setSize(gameOver.getPreferredSize());
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(gameOver);
		this.validate();
		this.repaint();
		gameOver.requestFocusInWindow();
	}

	public void mainMenu() {
		history.add(start);
		this.setSize(start.getPreferredSize());
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(start);
		this.validate();
		this.repaint();
		start.requestFocusInWindow();
	}

	public void newGameMenu() {
		history.add(ngMenu);
		load.updateSaves();
		this.setSize(ngMenu.getPreferredSize());
		this.getContentPane().removeAll();
		this.getContentPane().add(ngMenu);
		this.validate();
		this.repaint();
		ngMenu.requestFocusInWindow();
	}

	public void createGame() {
		history.add(create);
		this.setSize(create.getPreferredSize());
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(create);
		this.validate();
		this.repaint();
		create.requestFocusInWindow();
	}

	public void gameRules() {
		history.add(gameRules);
		this.setSize(gameRules.getPreferredSize());
		this.getContentPane().removeAll();
		this.getContentPane().add(gameRules);
		this.validate();
		this.repaint();
		gameRules.requestFocusInWindow();
	}

	public void editor() {
		editor = new Editor(this);
		history.add(editor);
		c.runEditor();
		c.setMode("editor");
		this.setSize(editor.getPreferredSize());
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(editor);
		this.validate();
		this.repaint();
		editor.setMap();
		editor.requestFocusInWindow();
	}

	public void pauseMenu() {
		history.add(pause);
		this.setSize(pause.getPreferredSize());
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(pause);
		this.validate();
		this.repaint();
		pause.requestFocusInWindow();
	}

	public void map() {
		history.add(map);
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
		this.setSize(map.getPreferredSize());
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(map);
		this.validate();
		this.repaint();
		map.requestFocusInWindow();
	}

	public void returnToGame() {
		history.clear();
		history.add(game);
		this.setResizable(true);
		this.setSize(game.getPreferredSize());
		this.getContentPane().removeAll();
		this.getContentPane().add(game);
		this.validate();
		this.repaint();
		game.requestFocusInWindow();
	}

	public void loadMenu() {
		history.add(load);
		load.updateSaves();
		this.setSize(load.getPreferredSize());
		this.getContentPane().removeAll();
		this.getContentPane().add(load);
		this.validate();
		this.repaint();
		load.requestFocusInWindow();
	}

	public void saveMenu() {
		history.add(save);
		this.setSize(save.getPreferredSize());
		this.getContentPane().removeAll();
		this.getContentPane().add(save);
		this.validate();
		this.repaint();
		save.requestFocusInWindow();
	}

	public void controls() {

	}

	public void back() {
		history.remove(history.size() - 1);
		JPanel back = history.get(history.size() - 1);
		this.setSize(back.getPreferredSize());
		this.getContentPane().removeAll();
		this.getContentPane().add(back);
		this.validate();
		this.repaint();
		back.requestFocusInWindow();
	}

	public void addToRoom(String[] lines, int x, int y) {
		c.addToRoom(lines, x, y);
	}

	public void changeRoom(int x, int y) {
		c.changeRoom(x, y);
	}

	public void changeRoom(String id) {
		c.changeRoom(id);
	}

	public void exportRoom() {
		c.exportRoom();
	}

	public void exportWorld() {
		c.exportWorld();
	}

	public String[][] getMap(int x, int y) {
		return c.getMap(x, y);
	}

	public void setExit(int index, int coord) {
		c.setExit(index, coord);
	}

	public void setRoomSize(int[] size) {
		c.setRoomSize(size);
	}

	public void addRoom(int x, int y) {
		c.addRoom(x, y);
	}

	public void deleteRoom(int x, int y) {
		c.deleteRoom(x, y);
	}

	public void saveGame(String save) {
		c.saveGame();
	}

	public void makeNewDir() {
		c.makeNewDir();
	}

	public void makeNewSave() {
		c.makeNewSave();
	}

	public void endTurn() {
		c.endTurn();
	}

	public void exit() {
		System.exit(0);
	}

	public void setTextRead(boolean b) {
		this.mode = "game";
		c.setTextRead();
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void exportEvent(String event) {
		c.exportEvent(event);
	}
}