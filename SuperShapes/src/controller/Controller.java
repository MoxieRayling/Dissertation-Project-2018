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
		switch(i) 
		{
		case 37:
			m.step('W');
			break;
		case 38:
			m.step('N');
			break;
		case 39:
			m.step('E');
			break;
		case 40:
			m.step('S');
			break;
		case 70:
			m.fly();
			break;
		case 82:
			m.rewind();
			break;
		case 80:
			m.pauseTime();
			break;
		case 83:
			m.shield();
			break;
		default:
			break;
		}
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
}
