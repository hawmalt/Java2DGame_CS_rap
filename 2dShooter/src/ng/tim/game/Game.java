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
import ng.tim.game.gfx.Camera;
import ng.tim.game.gfx.SpriteSheet;
import ng.tim.game.level.Level;
import ng.tim.game.net.GameClient;
import ng.tim.game.net.GameServer;
import ng.tim.game.net.packets.Packet00Login;
import ng.tim.game.sound.Sound;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Game extends Canvas implements Runnable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 500; //Width of the window
	public static final int HEIGHT = 500; //Height of the window
	public static final String NAME = "Game"; //The games name
	public static Game game;
	public static final Dimension DIMENSIONS = new Dimension(WIDTH,HEIGHT);

	public static SpriteSheet mainSpriteSheet; //this is the spritesheet that will be used for the entire game
	
	public JFrame frame; //The Jframe of game
	private Thread thread;
	
	public boolean running = false;
	public int tickCount = 0;
	
	//For rendering
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);	
	private Camera cam = new Camera();
	
	public InputHandler input; //This handles all the key presses
	public Level level;
	public Player player;
	
	//Multiplier
	public GameClient socketClient;
	public GameServer socketServer;
	public WindowHandler windowHandler;
	
	public boolean debug = true; //make this false only when we are about to release the game
	public boolean isApplet = false;
	
	//SoundTest
	public Sound sound = new Sound("/Sound/Randomize.wav");
	
	//World class
	public static World world;
	public Body body;
	
	//initializing function
	public void init()
	{
		game = this;
		
		world = new World(new Vec2(0, 9.8f)); // set up world
		
		mainSpriteSheet = new SpriteSheet("/sprite_sheet.png");
		
		input = new InputHandler(this);
		
		//body definition
		BodyDef bd = new BodyDef();
		bd.position.set(50, -200);  
		bd.type = BodyType.DYNAMIC;
		 
		//define shape of the body.
		PolygonShape cs = new PolygonShape();
		cs.setAsBox(8, 8);
		 
		//define fixture of the body.
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 0.5f;
		fd.friction = 0.3f;        
		fd.restitution = 0.0f;
		 
		//create the body and add fixture to it
		body =  world.createBody(bd);
		body.createFixture(fd);
		
		level = new Level(null,"/Levels/platform_test.png");
		player = new PlayerMP(level, 100, 100, input, JOptionPane.showInputDialog(this, "Please enter a username"), null, -1);
		level.addEntity(player);
		
		sound.play();
		
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
				debug(DebugLevel.WARNING, frames + " frames, " + ticks + " ticks");
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
		
		player.y = (int)body.getPosition().y; // test
		player.x = (int)body.getPosition().x; // test
		
		float timeStep = 1.0f / 60.f;
		int velocityIterations = 6;
		int positionIterations = 2;
		
		level.tick();
		
		world.step(timeStep, velocityIterations, positionIterations);
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
		
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		
		//Blank out the screen
		g.setColor(Color.black);
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		cam.setX(player.x - WIDTH/2);
		cam.setY(player.y - HEIGHT/2);
		
		g.transform(cam.getTransformation());
		
		level.renderTiles(g, cam); //render the tiles
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
