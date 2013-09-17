package ng.tim.game.entities;

import java.awt.Rectangle;

import ng.tim.game.Game;
import ng.tim.game.gfx.Animation;
import ng.tim.game.level.Level;

import static org.lwjgl.opengl.GL11.glVertex2i;


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
	}

	public void tick()
	{
		
	}

	public void render()
	{
		glVertex2i(x, y);
		glVertex2i(x + playerWidth, y);
		glVertex2i(x + playerWidth, y + playerHeight);
		glVertex2i(x, y + playerHeight);
	}
	
	public String getUsername()
	{
		return this.username;
	}
}
