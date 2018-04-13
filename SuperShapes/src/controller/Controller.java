package controller;

import model.EditorModel;
import model.GameModel;
import model.Model;
import views.View;

public class Controller {
	private View v;
	private Model m;

	public Controller(View v, Model m) {
		this.m = m;
		this.v = v;
	}

	public View GetV() {
		return v;
	}

	public void Input(int i) {
		m.input(i);
	}

	public void newGame() {
		m = new GameModel(v);
		m.newSaveData();
	}

	public void restart() {
		m.resetRoom();
	}

	public void loadGame() {
		m = new GameModel(v);
		m.loadGame();
	}

	public void notifyObservers() {
		m.notifyAllObservers();
	}

	public void addToRoom(String[] lines, int x, int y) {
		((EditorModel) m).addToRoom(lines, x, y);
	}

	public void setMode(String mode) {
		m.setMode(mode);
	}

	public void changeRoom(int x, int y) {
		((EditorModel) m).changeRoom(x + "," + y, false);
	}

	public void changeRoom(String id) {
		if (id != null)
			m.changeRoom(id, false);
	}

	public void runEditor() {
		m = new EditorModel(v);
	}

	public void exportWorld() {
		((EditorModel) m).exportWorld();
	}

	public String[][] getMap(int x, int y) {
		return m.getMap(x, y);
	}

	public int getX() {
		return m.getX();
	}

	public int getY() {
		return m.getY();
	}

	public void setExit(int index, int coord) {
		((EditorModel) m).setExit(index, coord);
	}

	public void setRoomSize(int[] size) {
		((EditorModel) m).setRoomSize(size);
	}

	public void exportRoom() {
		((EditorModel) m).exportRoom();
	}

	public void addRoom(int x, int y) {
		((EditorModel) m).addRoom(x, y);
	}

	public void deleteRoom(int x, int y) {

		((EditorModel) m).deleteRoom(x, y);
	}

	public void makeNewDir() {
		m.makeNewDir();
	}
}
