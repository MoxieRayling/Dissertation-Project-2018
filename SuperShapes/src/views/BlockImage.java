package views;

import java.awt.Color;
import java.awt.Graphics2D;

public class BlockImage extends Image
{	
	public BlockImage(String id, int x, int y, int scale, String room) 
	{
		super(x, y, scale,room);
		setId(id);
	}
	
	@Override
	public void drawThis(Graphics2D g, int x, int y) 
	{
    	g.setColor(new Color(0,0,100));
    	g.fillRect(getXPos()+x, getYPos()+y, scale, scale);
    	g.setColor(Color.BLACK);
    	g.drawRect(getXPos()+x, getYPos()+y, scale, scale);
	}
}
