package controller;
import model.Model;
import views.View;

public class Controller implements Constants
{
	private View v;
	private Model m;
	
	public Controller(View v,Model m) 
	{
		this.m = m;
		this.v = v;
	}
	
	public View GetV() 
	{
		return v;
	}

	public void Input(int i) 
	{
		m.input(i);
	}

	public void newGame() {
		m.eraseSaveData();
	}

	public void restart() {
		m.resetRoom();
	}

	public void loadGame() {
		m.loadGame();
	}

	public void notifyObservers() {
		m.notifyAllObservers();
	}

	public void setNext(Boolean next) {
		m.setNext(next);
	}

	public void addToRoom(String[] lines, int x, int y) {
		m.addToRoom(lines,x,y);
	}

	public void setMode(String mode) {
		m.setMode(mode);
	}
}
