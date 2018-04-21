package model;

import model.entities.Player;
import observers.Observer;

public class GameModel extends Model {

	public GameModel(Observer v) {
		super(v);
		super.player = new Player("0,0", 5, 10, 5);
		super.coinsHistory = 0;
		super.keysHistory = player.getKeys();
		super.player.addObserver(v);
		super.room = loadRoom("0,0");
		super.room.addObserver(v);
		super.room.notifyObserver();
	}
}
