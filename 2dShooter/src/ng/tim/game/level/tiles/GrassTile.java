package ng.tim.game.level.tiles;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertex2i;
import ng.tim.game.level.Level;

import org.lwjgl.opengl.GL11;

public class GrassTile extends BasicSolidTile
{

	public GrassTile(int id, int levelColor, int xpos, int ypos, Level level) {
		super(id, levelColor, xpos, ypos, level);
		
	}
	
	public void render()
	{
		
	}

}
