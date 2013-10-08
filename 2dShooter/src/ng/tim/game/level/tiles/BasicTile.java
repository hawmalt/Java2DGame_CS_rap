package ng.tim.game.level.tiles;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import org.lwjgl.opengl.GL13;

import ng.tim.game.Game;
import ng.tim.game.level.Level;

public class BasicTile extends Tile
{
	
	protected int tileColor;

	public BasicTile(int id, int levelColor, int xpos, int ypos, Level level)
	{
		super(id, false, levelColor, level);
		x = xpos;
		y = ypos;
		this.tileColor = tileColor;
		
		
		texture = loadTexture("Grass");
	}

	public void tick()
	{
		
	}
	
	public void render()
	{		
		
	}

}
