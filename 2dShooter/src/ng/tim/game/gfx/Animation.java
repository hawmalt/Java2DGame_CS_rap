package ng.tim.game.gfx;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Animation
{
	private Rectangle[] r;
	private SpriteSheet spritesheet;
	private int frame = 0;
	private boolean repeat = false;
	private boolean isRunning = true;
	private String animationName;
	
	private int animationDelay = 0; //how many frames should pass before updating
	private int framesPassed = 0; //how many frames passed since last frame change
	
	//Call this to have animations loaded automatically
	//THIS IS PRONE TO HAVE ERRORS SO USE THE OTHER CONSTRUCTOR IF YOU CAN
	public Animation(SpriteSheet spritesheet, int frames, int startIndex, String animationName, boolean repeat, Rectangle firstRectangle, int animationDelay)
	{
		this.spritesheet = spritesheet;
		this.frame = startIndex;
		this.animationName = animationName;
		this.repeat = repeat;
		this.animationDelay = animationDelay;
		r = new Rectangle[frames];
		
		//Fill the rectangle array
		//going in a strait horizontal line
		for(int i = 0; i < r.length; i++)
		{
			Rectangle rect = new Rectangle((firstRectangle.x + firstRectangle.width * i),
											firstRectangle.y,
											firstRectangle.width,
											firstRectangle.height);
			r[i] = rect;
		}
	}
	
	//Call this to load in custom animations
	public Animation(SpriteSheet spritesheet, int startIndex, String animationName, boolean repeat)
	{
		this.spritesheet = spritesheet;
		this.frame = startIndex;
		this.animationName = animationName;
		this.repeat = repeat;
		loadAnimationFromFile();
	}
	
	//I will leave this function for someone else to work on
	public void loadAnimationFromFile()
	{
		
	}
	
	public void updateAnimation()
	{
		//if is running is false do nothing
		if(!isRunning)
		{
			return;
		}
		
		//update the frame if enough frames passed
		if(framesPassed >= animationDelay)
		{
			frame++;
			framesPassed = 0;
		}
		framesPassed++;
		
		if(frame >= r.length)
		{
			if(repeat)
			{
				frame = 0;
			}
			else
			{
				stopAnimation();
			}
		}
	}
	
	public void startAnimation()
	{
		isRunning = true;
	}
	
	public void stopAnimation()
	{
		isRunning = false;
	}
	
	public Rectangle getFrame()
	{
		return r[frame];
	}

	public void setFrameIndex(int frame)
	{
		if(frame >= 0 && frame < r.length)
		{
			this.frame = frame;
		}
	}
	
	public String getAnimationName() {
		return animationName;
	}
}
