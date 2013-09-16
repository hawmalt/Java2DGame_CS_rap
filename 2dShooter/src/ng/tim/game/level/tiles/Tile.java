package ng.tim.game.level.tiles;

import java.awt.Graphics;

import ng.tim.game.entities.Entity;
import ng.tim.game.level.Level;

public abstract class Tile{

	//The width and height of each tile
	public static final int width = 8;
	public static final int height = 8;
	
	public static final Tile[] tiles = new Tile[256];
/*	public static final Tile VOID = new BasicTile(0,0,0, 0xff000000);
	public static final Tile STONE = new BasicSolidTile(1,1,0, 0xff555555);
	public static final Tile GRASS = new BasicSolidTile(2,2,0, 0xff00ff00);
	public static final Tile WATER = new AnimatedTile(3,new int[][] {{0,5},{1,5}, {2,5},{1,5}}, 100, 0xff0000ff);
	*/
	
	protected int id; //Location in tiles array of where that tile is located
	protected boolean solid; //For collision detection
	protected boolean emitter; //For light
	private int levelColor; //what color corresponds to this tile when loading a level
	
	//position of the tile in world space
	public int xpos;
	public int ypos;

	public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor)
	{
		this.id = (byte)id;
		
		this.levelColor = levelColor;
		
		this.solid = isSolid;
		this.emitter = isEmitter;
		tiles[id] = this;
	}
	
	public int getId()
	{
		return id;
	}
	
	public boolean isSolid()
	{
		return solid;
	}
	
	public boolean isEmitter()
	{
		return emitter;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g, Level level, int x, int y);

	public int getLevelColor() {
		return levelColor;
	}

}
