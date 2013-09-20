package ng.tim.game.level.tiles;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertex2i;
import ng.tim.game.level.Level;

import org.lwjgl.opengl.GL11;

public class GrassTile extends BasicSolidTile
{

	public GrassTile(int id, int x, int y, int levelColor, int xpos, int ypos, Level level) {
		super(id, x, y, levelColor, xpos, ypos, level);
				
		x = 2;
		y = 0;
	}
	
	public void render()
	{
		//texture.bind();
		glTexCoord2f(0, 0);
		glVertex2i(xpos, ypos);
		
		glTexCoord2f(1, 0);
		glVertex2i(xpos + 8, ypos);
		
		glTexCoord2f(1, 1);
		glVertex2i(xpos + 8, ypos + 8);
		
		glTexCoord2f(0, 1);
		glVertex2i(xpos, ypos + 8);
	}

}
