package model;

public class SnakeBody extends Entity 
{		
	public SnakeBody(String roomId,int count, int x, int y) 
	{
		super(roomId,count, x, y);
		id += "sb";
	}

	@Override
	public String toString() {
		return null;
	}
}
