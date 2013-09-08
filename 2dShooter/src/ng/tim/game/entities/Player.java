package ng.tim.game.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import ng.tim.game.Game;
import ng.tim.game.InputHandler;
import ng.tim.game.gfx.Animation;
import ng.tim.game.gfx.Font;
import ng.tim.game.level.Level;
import ng.tim.game.net.packets.Packet02Move;

public class Player extends Mob
{

	private InputHandler input;
	private int scale = 1;
	private String username;
	private Rectangle sourceRec;
	public static final int playerWidth = 16;
	public static final int playerHeight = 16;
	private Animation anim1 = new Animation(Game.mainSpriteSheet, 2, 0, "walking up", true, new Rectangle(0, 28*8, playerWidth, playerHeight), 20);
	
	public Player(Level level, int x, int y, InputHandler input, String username)
	{
		super(level, "Player", x, y, 1);
		this.input = input;
		this.username = username;
	}

	public void tick()
	{
		int xa = 0;
		int ya = 0;
		
		if(input != null)
		{
			if(input.up.isPressed())
			{
				ya--;
				anim1.updateAnimation();
			}
			if(input.down.isPressed())
			{
				ya++;
			}
			if(input.left.isPressed())
			{
				xa--;
			}
			if(input.right.isPressed())
			{
				xa++;
			}
		}
		
		if(xa != 0 || ya != 0)
		{
			move(xa, ya);
			isMoving = true;
			
			if(!Game.game.isApplet)
			{
				Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving, this.movingDir);
				packet.writeData(Game.game.socketClient);
			}
		}
		else
		{
			isMoving = false;
		}
		
	}

	public void render(Graphics g)
	{	
		int modifier = 8 * scale; // size of the player
		int xOffset = x - modifier/2;
		int yOffset = y - modifier/2 - 4;

		//Make rectangles for rendering
		
		//get destination rectangle
		Rectangle destRect = new Rectangle(x, y, playerWidth, playerHeight);
		//get source rectangle
		Rectangle sourceRect = anim1.getFrame();
		
		
		//render the player
		Game.mainSpriteSheet.render(g, destRect, sourceRect);
		
		if(username != null)
		{
			Font.render(username, g, x - ((username.length() - 1) / 2 * 8), y - 10, 1);
		}
	}
	
	public boolean hasCollided(int xa, int ya)
	{
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;

		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMin)) {
				return true;
			}
		}
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMax)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMin, y)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMax, y)) {
				return true;
			}
		}

		return false;
	}
	
	public String getUsername()
	{
		return this.username;
	}
}
