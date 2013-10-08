package ng.tim.game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.opengl.*;

import javax.imageio.ImageIO;

import ng.tim.game.Game;
import ng.tim.game.entities.Entity;
import ng.tim.game.entities.PlayerMP;
import ng.tim.game.gfx.SpriteSheet;
import ng.tim.game.level.tiles.GrassTile;
import ng.tim.game.level.tiles.Tile;
import ng.tim.game.level.tiles.VoidTile;

public class Level
{
	private ArrayList<Tile> tiles; //array of ids for what tile is in that coordinate
	
	private int width; //width in tiles
	private int height; //height in tiles
	
	private List<Entity> entities = new ArrayList<Entity>(); //the objects in the level
	
	//image for level loading generating
	private String imagePath;
	private BufferedImage image;
	
	//image for spritesheet
	private SpriteSheet sheet;
	
	public SpriteSheet getSheet()
	{
		return sheet;
	}
	
	public Level(String imagePathsheet, String imagePath)
	{
		//if the sprite sheet path is null use the standard sheet
		if(imagePathsheet == null)
		{
			sheet = new SpriteSheet("/sprite_sheet.png");
		}
		
		//if level spritesheet path is null call the generate level for a premade level
		if(imagePath != null)
		{
			this.imagePath = imagePath;
			this.loadLevelFromFile();
		}
		else
		{
			this.width = 64;
			this.height = 64;
			tiles = new ArrayList<Tile>();
			this.generateLevel();
		}
	}
	
	private void loadLevelFromFile()
	{
		try
		{
			this.image = ImageIO.read(Level.class.getResourceAsStream(imagePath));
			this.width = image.getWidth();
			this.height = image.getHeight();
			tiles = new ArrayList<Tile>();
			this.loadTiles();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadTiles()
	{
		int[] tileColors = this.image.getRGB(0, 0, width, height, null, 0, width);
		
		//go through the tile colors array
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if(tileColors[x + y * width] == 0xff00ff00)
				{
					tiles.add(new GrassTile(2, 0xff00ff00, x * Tile.tileWidth, y * Tile.tileHeight, this));
				}
				
				
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void saveLevelToFile()
	{
		try
		{
			ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void generateLevel()
	{
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if(x * y % 10 < 7)
				{
					tiles.add(new GrassTile(2, 0xff00ff00, x * Tile.tileWidth, y * Tile.tileHeight, this));				}
				else
				{
					//tiles[x + y * width] = new StoneTile();
				}
			}
		}
	}
	
	//Solves concurrency error
	public synchronized List<Entity> getEntities()
	{
		return this.entities;
	}
	
	
	//update the level
	public void tick()
	{
		//update the level objects
		for(Entity e : getEntities())
		{
			e.tick();
		}
		
		for(Tile t : Tile.tiles)
		{
			if(t == null)
			{
				break;
			}
			t.tick();
		}
		
	}
	
	//renders the tiles
	public void renderTiles()
	{
		
		for(Tile t : tiles)
		{
			Game.spriteBatch.draw(t);
		}
	}

	//render the objects
	public void renderEntities()
	{
		for(Entity e : getEntities())
		{
			Game.spriteBatch.draw(e);
		}
	}
	
	//returns a tile at coordinates x,y
	public Tile getTile(int x, int y)
	{
		//return a void tile if the coordinates are out of bounds
		if(x < 0 || x > width || y < 0 || y > height)
		{
			return null;
		}
		return Tile.tiles[x + y * width];
	}
	
	//Adds an entity to the level
	public void addEntity(Entity entity)
	{
		this.getEntities().add(entity);
	}

	public void removePlayerMP(String username)
	{
		int index = 0;
		
		for(Entity e : getEntities())
		{
			if(e instanceof PlayerMP && ((PlayerMP)e).getUsername().equals(username))
			{
				break;
			}
			index++;
		}
		
		this.getEntities().remove(index);
	}
	
	private int getPlayerMPindex(String username)
	{
		int index = 0;
		for(Entity e : getEntities())
		{
			if(e instanceof PlayerMP && ((PlayerMP)e).getUsername().equals(username))
			{
				break;
			}
			index++;
		}
		
		return index;
	}
	
	public void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir)
	{
		int index = getPlayerMPindex(username);
		PlayerMP player = (PlayerMP)this.getEntities().get(index);
		player.x = x;
		player.y = y;
	}
}
