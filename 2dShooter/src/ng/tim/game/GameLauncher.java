package ng.tim.game;

import java.applet.Applet;
import java.awt.BorderLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameLauncher extends Applet
{
	private static Game game = new Game();
	public static final boolean DEBUG = false;
	
	
	@Override
	public void init()
	{
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
		setMaximumSize(Game.DIMENSIONS);
		setMinimumSize(Game.DIMENSIONS);
		setPreferredSize(Game.DIMENSIONS);
		game.debug = DEBUG;
		game.isApplet = true;
	}
	
	@Override
	public void start()
	{
		game.start();
	}
	
	@Override
	public void stop()
	{
		game.stop();
	}
	
	public static void main(String[] args)
	{
		//Sets the sizes of the screen
		game.setMinimumSize(Game.DIMENSIONS);
		game.setMaximumSize(Game.DIMENSIONS);
		game.setPreferredSize(Game.DIMENSIONS);
				
		game.frame = new JFrame(Game.NAME); //create the JFrame
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exits program when x button pressed
		game.frame.setLayout(new BorderLayout());
		
		game.frame.setIgnoreRepaint(true); // We are going to do it ourselves
		
		game.frame.add(game, BorderLayout.CENTER); //Add the Game to the JFrame
		game.frame.pack(); //keeps everything at or above preferred size
				
		game.frame.setResizable(false); //cannot resize window
		game.frame.setLocationRelativeTo(null); //want window to be centered
		game.frame.setVisible(true); //Make it so you can see the window
		game.windowHandler = new WindowHandler(game);
		game.debug = DEBUG;
		
		game.start();
	}
}
