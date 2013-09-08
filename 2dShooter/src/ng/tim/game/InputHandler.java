package ng.tim.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener
{
	public InputHandler(Game game)
	{
		//when the input handler is created this key listener will be implemented
		game.addKeyListener(this);
	}
	
	//Define what a Key is
	public class Key
	{
		private int numTimesPressed = 0;
		private boolean pressed = false; //is the key pressed
		
		public int getNumTimesPressed()
		{
			return numTimesPressed;
		}
		
		//getter method for pressed
		public boolean isPressed()
		{
			return pressed;
		}
		
		//allows to change if key is pressed
		public void toggle(boolean isPressed)
		{
			pressed = isPressed;
			
			if(isPressed)
			{
				numTimesPressed++;
			}
		}
	}
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();

	public void keyPressed(KeyEvent e)
	{
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e)
	{
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e)
	{
		
	}

	public void toggleKey(int keyCode, boolean isPressed)
	{
		if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
		{
			up.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
		{
			down.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
		{
			left.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
		{
			right.toggle(isPressed);
		}
	}
}
