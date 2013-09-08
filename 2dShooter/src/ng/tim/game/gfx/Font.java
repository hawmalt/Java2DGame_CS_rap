package ng.tim.game.gfx;

import java.awt.Graphics;
import java.awt.Rectangle;

import ng.tim.game.Game;

public class Font
{
	private static String chars =""+
	"ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ>< "+
	"abcdefghijklmnopqrstuvwxyzäöü   " +
	"1234567890+-/*^!.,?#()=ßÇ$&%\"':;";
	
	public static void render(String msg, Graphics g, int x, int y, int scale)
	{
		//loop through the characters
		for(int i = 0; i < msg.length(); i++)
		{
			int charIndex = chars.indexOf(msg.charAt(i)); //where we are in the chars string. If it does not exist it will be -1
			
			//If it is a valid character call the screen render function
			if(charIndex >= 0)
			{
				//Make rectangles for rendering
				
				//get destination rectangle
				Rectangle destRect = new Rectangle(x + (i*8), y, 8, 8);
				//get source rectangle
				Rectangle sourceRect = new Rectangle((charIndex*8) % Game.mainSpriteSheet.getWidth(), (23*8) + (charIndex/(Game.mainSpriteSheet.getWidth()>>3))*8, 8, 8);
				
				Game.mainSpriteSheet.render(g, destRect, sourceRect);
			}
			
		}
	}
}
