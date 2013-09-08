package ng.tim.game.level.tiles;

import java.awt.Graphics;

import ng.tim.game.level.Level;

public class BasicTile extends Tile
{
	protected int x, y;
	protected int tileColor;

	public BasicTile(int id, int x, int y)
	{
		super(id, false, false);
		this.x = x;
		this.y = y;
		this.tileColor = tileColor;
		
	}

	public void tick()
	{
		
	}
	
	public void render(Graphics g, Level level, int xPos, int yPos)
	{

	}

}
