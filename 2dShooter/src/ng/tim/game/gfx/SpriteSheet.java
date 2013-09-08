package ng.tim.game.gfx;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	/*
	 * The Sprite sheet loads an image.
	 * It then stores all of the image into an array of pixels
	 * That array of pixels is what is used to draw to the screen
	 */
	
	public String path; //path to the image
	public int width, height;
	public BufferedImage image = null; //The spritesheet image
	
	public SpriteSheet(String path)
	{		
		//try to load the picture and store it to image
		try
		{
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//in case image is null return nothing
		if(image == null)
		{
			return;
		}
		
		//set up variables
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	public void render(Graphics g, Rectangle dest, Rectangle source)
	{
		g.drawImage(image, dest.x, dest.y, dest.x + dest.width, dest.y + dest.height, 
				source.x, source.y, source.x + source.width, source.y + source.height, null);
	}
}
