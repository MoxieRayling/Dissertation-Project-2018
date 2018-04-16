package model.entities;

public class SnakeBody extends Entity 
{		
	public SnakeBody(String roomId,int count, int x, int y) 
	{
		super(roomId,count, x, y);
		id += "sb";
		image = "snake.png";
	}

	@Override
	public String toString() {
		return null;
	}
}
