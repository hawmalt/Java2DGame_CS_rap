package ng.tim.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ng.tim.game.entities.Player;
import ng.tim.game.entities.PlayerMP;
import ng.tim.game.gfx.SpriteSheet;
import ng.tim.game.level.Level;
import ng.tim.game.net.GameClient;
import ng.tim.game.net.GameServer;
import ng.tim.game.net.packets.Packet00Login;

public class Game extends Canvas implements Runnable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 500; //Width of the window resolution
	public static final int HEIGHT = 500; //Height of the window resolution
	public static final String NAME = "Game"; //The games name
	public static Game game;
	public static final Dimension DIMENSIONS = new Dimension(WIDTH,HEIGHT);

	public static SpriteSheet mainSpriteSheet; //this is the spritesheet that will be used for the entire game
	
	public JFrame frame; //The Jframe of game
	private Thread thread;
	
	public boolean running = false;
	public int tickCount = 0;
	
	//For rendering
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);	
	
	public InputHandler input; //This handles all the key presses
	public Level level;
	public Player player;
	
	//Multiplier
	public GameClient socketClient;
	public GameServer socketServer;
	public WindowHandler windowHandler;
	
	public boolean debug = true; //make this false only when we are about to release the game
	public boolean isApplet = false;
	
	//initializing function
	public void init()
	{
		game = this;
		
		mainSpriteSheet = new SpriteSheet("/sprite_sheet.png");
		
		input = new InputHandler(this);
		
		level = new Level(null,null);
		player = new PlayerMP(level, 100, 100, input, JOptionPane.showInputDialog(this, "Please enter a username"), null, -1);
		level.addEntity(player);
		
		if(!isApplet)
		{
			Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);
			if(socketServer != null)
			{
				socketServer.addConnection((PlayerMP) player, loginPacket);
			}
			loginPacket.writeData(socketClient);
		}
	}
	
	//start the game
	public synchronized void start()
	{
		running = true; //run the game
		thread = new Thread(this, NAME + "_main"); //Create thread
		thread.start();
		if(!isApplet)
		{
			if(JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0)
			{
				socketServer = new GameServer(this);
				socketServer.start();
			}
			
			socketClient = new GameClient(this, "localhost"); //set up for MULTIPLAYER
			socketClient.start();
		}
	}
	
	//stop the game
	public synchronized void stop()
	{
		running = false; //the game is not running anymore
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	public void run()
	{
		long lastTime = System.nanoTime(); //Gets current time in nanoseconds since the epoch 1970s
		double nsPerTick = 1000000000D/60D; //How many nanoseconds in one tick
		
		int ticks = 0; //how many ticks happened
		int frames = 0; //how many frames happened
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0; //how many unproccesed nanoseconds passed so far
		
		init(); //This function is called once
		
		while(running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now; //update the lastTime
			boolean shouldRender = true; //Limits how many frames are rendered
			
			//this will run until it hits 60 then it will continue
			while(delta >= 1)
			{
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			
			//make the thread sleep to limit frames
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Only render if shouldRender is true
			if(shouldRender)
			{
				frames++;
				render();
			}
			
			//update if 1000 milliseconds passed since last update
			if(System.currentTimeMillis() - lastTimer >= 1000)
			{
				lastTimer += 1000;
				debug(DebugLevel.INFO, frames + " frames, " + ticks + " ticks");
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	private int x = 0, y = 0;
	
	//update logic in the game
	public void tick()
	{
		tickCount++;
		if(input.up.isPressed())
		{
			y--;
		}
		if(input.down.isPressed())
		{
			y++;
		}
		if(input.left.isPressed())
		{
			x--;
		}
		if(input.right.isPressed())
		{
			x++;
		}
		
		level.tick();
	}
	
	//draws to screen
	public void render()
	{
		BufferStrategy bs = getBufferStrategy(); //organize data on this canvas
		if(bs == null)
		{
			createBufferStrategy(3); //Triple buffering
			return;
		}
		
		int xOffset = player.x - (WIDTH / 2);
		int yOffset = player.y - (HEIGHT / 2);
		
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		
		//Blank out the screen
		g.setColor(Color.black);
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		
		level.renderTiles(g); //render the tiles
		level.renderEntities(g);//render the entities on top of tiles
		
		g.dispose(); //frees up space since not using g anymore
		bs.show(); //show contents of buffer
	}

	public void debug(DebugLevel level, String msg)
	{
		switch(level)
		{
		case INFO:
			if(debug)
			{
				System.out.println("["+ NAME +"] " + msg);
			}
			break;
		case WARNING:
			System.out.println("["+ NAME +"] [WARNING] " + msg);
			break;
		case SEVERE:
			System.out.println("["+ NAME +"] [SEVERE] " + msg);
			this.stop();
			break;
		}
	}
	
	public static enum DebugLevel
	{
		INFO,WARNING,SEVERE;	
	}
}
