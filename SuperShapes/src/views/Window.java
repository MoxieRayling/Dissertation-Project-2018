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

import controller.Controller;
import model.FileManager;
import model.Model;
import model.Room;
import model.entities.Entity;
import model.entities.Player;
import views.animation.Animation;
import views.menus.CreateEntityMenu;
import views.menus.CreateMenu;
import views.menus.CreateTileMenu;
import views.menus.Editor;
import views.menus.GameOverMenu;
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
	private PauseMenu pause;
	private LoadMenu load;
	private SaveMenu save;
	private NewGameMenu ngMenu;
	private JFrame frame;
	private Boolean input = true;
	private CreateMenu create;
	private GameOverMenu gameOver;
	private String mode = "game";
	private CreateEntityMenu ceMenu;
	private CreateTileMenu ctMenu;

	public Window() {
		frame = this;
		start = new StartMenu(this);
		game = new Animation(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.add(start);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
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
							if (e.getKeyCode() == 27) {
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

			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				game.setSize(frame.getWidth(), frame.getHeight());
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
			game.setEndGame(((Model) o).getEndGame());
			game.setWinner(((Model) o).isWinner());
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
		returnToGame();
	}

	public void gameOver(String title, String text) {
		gameOver = new GameOverMenu(this, title, text);
		this.setSize(gameOver.getPreferredSize());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(gameOver);
		this.validate();
		this.repaint();
		gameOver.requestFocusInWindow();
		setTitle("Game over - " + title);
	}

	public void mainMenu() {
		start = new StartMenu(this);
		this.setSize(start.getPreferredSize());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(start);
		this.validate();
		this.repaint();
		start.requestFocusInWindow();
		setTitle("Main Menu");
	}

	public void newGameMenu() {
		ngMenu = new NewGameMenu(this);
		this.setSize(ngMenu.getPreferredSize());
		this.getContentPane().removeAll();
		this.getContentPane().add(ngMenu);
		this.validate();
		this.repaint();
		ngMenu.requestFocusInWindow();
		setTitle("New Game Menu - Game \"" + FileManager.getGameDir().substring(6) + "\"");
	}

	public void createGame() {
		create = new CreateMenu(this);
		this.setSize(create.getPreferredSize());
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(create);
		this.validate();
		this.repaint();
		create.requestFocusInWindow();
		setTitle("Create Game Menu");
	}

	public void editor() {
		c.editor();
		editor = new Editor(this);
		c.runEditor();
		this.setSize(editor.getPreferredSize());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(editor);
		this.validate();
		this.repaint();
		editor.setMap();
		editor.requestFocusInWindow();
		setTitle("Editor - Editing \"" + FileManager.getGameDir().substring(6) + "\"");
	}

	public void createTileMenu() {
		ctMenu = new CreateTileMenu(this);
		c.runEditor();
		this.setSize(ctMenu.getPreferredSize());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(ctMenu);
		this.validate();
		this.repaint();
		ctMenu.requestFocusInWindow();
		setTitle("Editor - Tile Constructor");
	}

	public void createEntityMenu() {
		ceMenu = new CreateEntityMenu(this);
		c.runEditor();
		this.setSize(ceMenu.getPreferredSize());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(ceMenu);
		this.validate();
		this.repaint();
		ceMenu.requestFocusInWindow();
		setTitle("Editor - Entity Constructor");
	}

	public void pauseMenu() {
		pause = new PauseMenu(this, c.getX(), c.getY());
		pause.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (getContentPane().contains(pause.getLocation())) {
					if (e.getKeyCode() == 27 || e.getKeyCode() == 77 || e.getKeyCode() == 13) {
						returnToGame();
					} else if (e.getKeyCode() == 38) {
						pause.moveMap('N');
					} else if (e.getKeyCode() == 37) {
						pause.moveMap('W');
					} else if (e.getKeyCode() == 39) {
						pause.moveMap('E');
					} else if (e.getKeyCode() == 40) {
						pause.moveMap('S');
					}
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {

			}

		});
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setResizable(false);
		this.getContentPane().removeAll();
		this.getContentPane().add(pause);
		this.validate();
		this.repaint();
		pause.requestFocusInWindow();
		setTitle("\"" + FileManager.getGameDir().substring(6) + "\" - Game Paused");
	}

	public void returnToGame() {
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.getContentPane().removeAll();
		this.getContentPane().add(game);
		this.validate();
		this.repaint();
		game.requestFocusInWindow();
		setTitle("Game - Playing \"" + FileManager.getGameDir().substring(6) + "\"");
	}

	public void loadMenu() {
		load = new LoadMenu(this);
		load.updateSaves();
		this.setSize(load.getPreferredSize());
		this.setLocationRelativeTo(null);
		this.getContentPane().removeAll();
		this.getContentPane().add(load);
		this.validate();
		this.repaint();
		load.requestFocusInWindow();
		setTitle("Load Menu - Game \"" + FileManager.getGameDir().substring(6) + "\"");
	}

	public void saveMenu() {
		save = new SaveMenu(this);
		this.setSize(save.getPreferredSize());
		this.setLocationRelativeTo(null);
		this.getContentPane().removeAll();
		this.getContentPane().add(save);
		this.validate();
		this.repaint();
		save.requestFocusInWindow();
		setTitle("Save Menu - Game \"" + FileManager.getGameDir().substring(6) + "\"");
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
		c.saveGame(save);
	}

	public void makeNewDir() {
		c.makeNewDir();
	}

	public void makeNewSave() {
		c.makeNewSave();
	}

	public void endTurn() {
		if (c != null)
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

	public void refreshEditor() {
		editor.refresh();
	}

	public void createEntity(String entity) {
		c.createEntity(entity);
	}

	public List<String> getEntities() {
		return c.getEntities();
	}

	public void createTile(String tile) {
		c.createTile(tile);
	}

	public List<String> getTiles() {
		return c.getTiles();
	}

	public boolean gameExists(String game) {
		return c.gameExists(game);
	}

	public int getCoins() {
		return c.getCoins();
	}
}