package ng.tim.game;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import ng.tim.game.entities.Player;
import ng.tim.game.entities.PlayerMP;
import ng.tim.game.gfx.Camera;
import ng.tim.game.gfx.SpriteBatch;
import ng.tim.game.gfx.SpriteSheet;
import ng.tim.game.level.Level;
import ng.tim.game.net.GameClient;
import ng.tim.game.net.GameServer;
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
	public static SpriteBatch spriteBatch = new SpriteBatch();
	
	public boolean running = false;
	public int tickCount = 0;
	
	//For rendering
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);	
	private Camera cam = new Camera();
	int shaderProgram;
	int vertexShader;
	int fragmentShader;	
	
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
		
		glDeleteProgram(shaderProgram);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		
		Display.destroy();
		System.exit(0);
	}
	
	public void initGL() 
	{
		//Initialize the GLSL program
		shaderProgram = glCreateProgram();
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		
		StringBuilder vertexShaderSource = new StringBuilder();
		StringBuilder fragmentShaderSource = new StringBuilder();

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("res/Shaders/shader.vert"));
			String line;
			while((line = reader.readLine()) != null)
			{
				vertexShaderSource.append(line).append('\n');
			}
			reader.close();
			
		}
		catch(IOException e)
		{
			System.err.println("Vertex Shader was not loaded properly");
			Display.destroy();
			System.exit(1);
		}
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("res/Shaders/shader.frag"));
			String line;
			while((line = reader.readLine()) != null)
			{
				fragmentShaderSource.append(line).append('\n');
			}
			reader.close();
			
		}
		catch(IOException e)
		{
			System.err.println("Fragment Shader was not loaded properly");
			Display.destroy();
			System.exit(1);
		}
		
		//compile the vertex shader
		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);
		
		//throw an error ourselves because java wont throw one automatically
		if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Vertex shader wasn't able to be compiled correctly.");
		}
		
		//compile the fragment shader
		glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        
		//throw an error ourselves because java wont throw one automatically
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Fragment shader wasn't able to be compiled correctly.");
        }
        
        //attach the shader program
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);
		
		//initialization of OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 500, 500, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
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
		
		//Use the shader program
		glUseProgram(shaderProgram);
		
		/*
		
		//Render the level
		glBegin(GL_QUADS);
		
		level.renderTiles();
		level.renderEntities();
		
		glEnd();
		
		*/
		
		spriteBatch.begin();
		level.renderEntities();
		level.renderTiles();
		spriteBatch.end();
		
		//Disable the buffers
		glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
		
		//Stop using the shader program
		glUseProgram(0);
		
	}
}
