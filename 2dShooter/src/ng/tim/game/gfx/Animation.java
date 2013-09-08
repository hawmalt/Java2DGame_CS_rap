package ng.tim.game.gfx;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Animation
{
	private Rectangle[] r;
	private SpriteSheet spritesheet;
	private int frame = 0;
	private boolean repeat = false;
	
	public Animation(SpriteSheet spritesheet, int frames, int startIndex, boolean repeat)
	{
		this.spritesheet = spritesheet;
		this.frame = startIndex;
		this.repeat = repeat;
		r = new Rectangle[frames];
	}
	
	public void render(Graphics g)
	{
		
	}
}
