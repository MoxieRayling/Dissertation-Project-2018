package model;

import observers.Observer;

public class GameModel extends Model {

	public GameModel(Observer v) {
		super(v);
		super.player = new Player("0,0", 5, 10, 5);
		super.player.addObserver(v);
		super.room = loadRoom("0,0");
		super.room.addObserver(v);
		//super.room.updatePath(player.getX(), player.getY());
		super.room.notifyObserver();
	}
}
