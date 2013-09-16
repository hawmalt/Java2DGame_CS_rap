package ng.tim.game.level.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;

import ng.tim.game.level.Level;

public class BasicTile extends Tile
{
	
	protected int x, y;
	protected int tileColor;

	public BasicTile(int id, int x, int y, int levelColor, int xpos, int ypos)
	{
		super(id, false, false, levelColor);
		this.x = x;
		this.y = y;
		this.tileColor = tileColor;
		this.xpos = xpos;
		this.ypos = ypos;
		
	}

	public void tick()
	{
		
	}
	
	public void render(Graphics g, Level level, int xPos, int yPos)
	{
		Rectangle destRect = new Rectangle(xpos, ypos, Tile.width, Tile.height);
		//get source rectangle
		Rectangle sourceRect = new Rectangle(x * Tile.width, y * Tile.height, Tile.width, Tile.height);
		
		//draw the tile to screen
		level.getSheet().render(g, destRect, sourceRect);
	}

}
