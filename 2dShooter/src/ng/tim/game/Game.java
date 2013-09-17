package ng.tim.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;

import java.awt.image.BufferedImage;

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
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Game
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 500; //Width of the window
	public static final int HEIGHT = 500; //Height of the window
	public static final String NAME = "Our Game"; //The games name
	public static Game game;
	
	long lastFrame; // Time at last frame
	int fps; //frames per second
	long lastFPS; //last fps time

	public static SpriteSheet mainSpriteSheet; //this is the spritesheet that will be used for the entire game
		
	public boolean running = false;
	public int tickCount = 0;
	
	//For rendering
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);	
	private Camera cam = new Camera();
	
	public Level level;
	public Player player;
	
	//Multiplier
	public GameClient socketClient;
	public GameServer socketServer;
	
	public boolean debug = true; //make this false only when we are about to release the game
	public boolean isApplet = false;
	
	//SoundTest
	public Sound sound = new Sound("/Sound/Randomize.wav");
	
	//World class
	public static World world;
	public Body body;
	
	public Game()
	{
		
		try
		{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle(NAME);
			Display.create();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		initGL(); // initialize OpenGL
		getDelta(); // call once before loop to initialize lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
		
		init(); //initialize game objects
		
		while(!Display.isCloseRequested())
		{
			int delta = getDelta();
			
			update();
			renderGL();
			
			//Exit if the escape key is pressed
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			{
				Display.destroy(); //get rid of the display
				System.exit(0);
			}
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
	
	public void initGL() 
	{
		//initialization of OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 500, 500, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
	}

	// how many milliseconds passed since last frame
	public int getDelta()
	{
		long time = getTime();
		int delta = (int)(time - lastFrame);
		lastFrame = time;
		
		return delta;
	}
	
	//get the system time in milliseconds
	public long getTime()
	{
		return (Sys.getTime() * 1000 / Sys.getTimerResolution());
	}
	
	//update game logic
	public void update()
	{
		//put update code here
		player.y = (int)body.getPosition().y; // test
		player.x = (int)body.getPosition().x; // test
		
		float timeStep = 1.0f / 60.f;
		int velocityIterations = 6;
		int positionIterations = 2;
		
		level.tick();
		
		world.step(timeStep, velocityIterations, positionIterations);
		
		updateFPS();
	}
	
	//calculate fps and set it as the title bar
	public void updateFPS()
	{
		if(getTime() - lastFPS > 1000)
		{
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	//initializing function
	public void init()
	{
		game = this;
		
		world = new World(new Vec2(0, 9.8f)); // set up world
		
		mainSpriteSheet = new SpriteSheet("/sprite_sheet.png");
				
		//body definition
		BodyDef bd = new BodyDef();
		bd.position.set(200, 0);  
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
		
		level = new Level(null,"/Levels/platform_test_high.png");
		player = new PlayerMP(level, 100, 100, JOptionPane.showInputDialog(this, "Please enter a username"), null, -1);
		level.addEntity(player);		
	}
	
	
	//draws to screen
	public void renderGL()
	{
		//Clear the screen
		glClear(GL_COLOR_BUFFER_BIT);
		
		//Render the level
		glBegin(GL_QUADS);
		
		level.renderTiles();
		level.renderEntities();
		
		glEnd();
	}
}
