package ng.tim.game.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import ng.tim.game.Game;
import ng.tim.game.entities.Entity;

import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;

public class SpriteBatch {

	private boolean isDrawing = false;
	private Texture texture;
	
	public void begin()
	{
		if(isDrawing)
		{
			System.err.println("Must not be drawing when you call begin");
			return;
		}
		
		isDrawing = true;
		

		glBegin(GL_QUADS);
	}
	
	public void end()
	{
		if(!isDrawing)
		{
			System.err.println("Must be drawing when you call end");
			return;
		}
		
		isDrawing = false;
		glEnd();
	}
	
	public void initOpenGL()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Game.WIDTH, Game.HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

	}
	
	
	public void draw(Entity ent)
	{
		if(!isDrawing)
		{
			System.err.println("Must be drawing");
			return;
		}
		
		if(ent.x > Game.WIDTH || ent.y > Game.HEIGHT || (ent.x + ent.width) < 0 || (ent.y + ent.height) < 0)
		{
			return;
		}
		
		checkFlush(ent);
		
		glTexCoord2f(0, 0);
		glVertex2i(ent.x, ent.y);
		
		glTexCoord2f(1, 0);
		glVertex2i(ent.x + ent.width, ent.y);
		
		glTexCoord2f(1, 1);
		glVertex2i(ent.x + ent.width, ent.y + ent.height);
		
		glTexCoord2f(0, 1);
		glVertex2i(ent.x, ent.y + ent.height);
	}
	
	public void checkFlush(Entity ent)
	{
		if(texture == null || ent.getTexture() != texture)
		{
			flush(ent);
		}
	}
	
	public void flush(Entity ent)
	{
		glEnd();
		
		//These two "unbind" the texture
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_TEXTURE_2D);
		
		texture = ent.getTexture(); //set the texture
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID()); //bind the texture
		glBegin(GL_QUADS);
		
	}
}
