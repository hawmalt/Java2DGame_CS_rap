package ng.tim.game.entities;

import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL13.*;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ng.tim.game.Game;
import ng.tim.game.gfx.Animation;
import ng.tim.game.level.Level;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public class Player extends Entity
{

	private String username;
	public static final int playerWidth = 16;
	public static final int playerHeight = 16;
	private Animation anim1 = new Animation(Game.mainSpriteSheet, 2, 0, "walking up", true, new Rectangle(0, 28*8, playerWidth, playerHeight), 20);
	
	
	public Player(Level level, int x, int y, String username)
	{
		super(level);
		this.username = username;
		
		//load the texture
		texture = loadTexture("Player");
	}

	public void tick()
	{
		
	}

	public void render()
	{	
		GL13.glActiveTexture(GL_TEXTURE0);
		texture.bind();
				
		glTexCoord2f(0, 0);
		glVertex2i(x, y);
		
		glTexCoord2f(1, 0);
		glVertex2i(x + playerWidth, y);
		
		glTexCoord2f(1, 1);
		glVertex2i(x + playerWidth, y + playerHeight);
		
		glTexCoord2f(0, 1);
		glVertex2i(x, y + playerHeight);
		
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
}
