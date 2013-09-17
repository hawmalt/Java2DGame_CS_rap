package ng.tim.game.level.tiles;

import static org.lwjgl.opengl.GL11.glVertex2i;

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
	
	public void render()
	{
		glVertex2i(xpos,ypos);
		glVertex2i(xpos + Tile.width, ypos);
		glVertex2i(xpos + Tile.width, ypos + Tile.height);
		glVertex2i(xpos, ypos + Tile.height);
	}

}
