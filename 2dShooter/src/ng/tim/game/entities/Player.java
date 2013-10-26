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

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public class Player extends Entity
{

	private String username;
	private Animation anim1 = new Animation(Game.mainSpriteSheet, 2, 0, "walking up", true, new Rectangle(0, 28*8, width, height), 20);
	private Body body;
	private int speed = 100;
	
	public Player(Level level, int x, int y, String username)
	{
		super(level);
		this.username = username;
		
		width = 16;
		height = 16;
		
		//load the texture
		texture = loadTexture("Player");
	}
	
	public void setBody(Body b)
	{
		body = b;
	}
	/*
	 * known bugs 
	 * The program currently uses the absolute value of the y velocity to approximate when the player is in the air.  
	 * This makes the program think that the player is on the ground at the peek of its jumps.
	 * 
	 * The player ends up burried part way into the ground
	 */
	public void tick()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			moveLeft();
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			moveRight();
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			jump();
		
		//sets the sideways velocity to zero if no sideways keys are pressed and player is on the ground
		if(!(Keyboard.isKeyDown(Keyboard.KEY_SPACE)||Keyboard.isKeyDown(Keyboard.KEY_RIGHT)||Keyboard.isKeyDown(Keyboard.KEY_LEFT))&&
				Math.abs(body.getLinearVelocity().y)<2)
			body.setLinearVelocity(new Vec2(0, body.getLinearVelocity().y));
		
		
		body.setLinearVelocity(new Vec2(body.getLinearVelocity().x, body.getLinearVelocity().y+body.getGravityScale()));
	}
	
	public void moveLeft()
	{
		if(Math.abs(body.getLinearVelocity().y)<2)// used as a substitute to seeing if the player is on the ground.  
													//Cannot change direction in air
			body.setLinearVelocity(new Vec2(-speed, body.getLinearVelocity().y));
	}
	
	public void moveRight()
	{
		if(Math.abs(body.getLinearVelocity().y)<2)// used as a substitute to seeing if the player is on the ground.  
													//Cannot change direction in air
			body.setLinearVelocity(new Vec2(speed, body.getLinearVelocity().y));
	}
	
	public void jump()
	{
		body.setLinearVelocity(new Vec2(body.getLinearVelocity().x, -speed));
		
	}

	public void render()
	{	
		GL13.glActiveTexture(GL_TEXTURE0);
		texture.bind();
				
		glTexCoord2f(0, 0);
		glVertex2i(x, y);
		
		glTexCoord2f(1, 0);
		glVertex2i(x + width, y);
		
		glTexCoord2f(1, 1);
		glVertex2i(x + width, y + height);
		
		glTexCoord2f(0, 1);
		glVertex2i(x, y + height);
		
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
}
