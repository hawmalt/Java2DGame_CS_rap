package ng.tim.game.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL13.*;
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
		//GL13.glActiveTexture(GL_TEXTURE0);
		//texture.bind();
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
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_TEXTURE_2D);
	}
	
	public void draw(Entity ent)
	{
		if(!isDrawing)
		{
			System.err.println("Must be drawing");
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
		
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_TEXTURE_2D);
		
		//glDeleteTextures(texture.getTextureID());
		
		texture = ent.getTexture(); //set the texture
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		glBegin(GL_QUADS);
		
	}
	
	public void setTexture(Texture t)
	{
		texture = t;
	}
}
