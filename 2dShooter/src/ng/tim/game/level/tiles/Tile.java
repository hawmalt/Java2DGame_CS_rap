package ng.tim.game.level.tiles;

import java.awt.Graphics;

import ng.tim.game.entities.Entity;
import ng.tim.game.level.Level;

public abstract class Tile extends Entity{

	//The width and height of each tile
	public static final int tileWidth = 8;
	public static final int tileHeight = 8;
	
	public static final Tile[] tiles = new Tile[256];
/*	public static final Tile VOID = new BasicTile(0,0,0, 0xff000000);
	public static final Tile STONE = new BasicSolidTile(1,1,0, 0xff555555);
	public static final Tile GRASS = new BasicSolidTile(2,2,0, 0xff00ff00);
	public static final Tile WATER = new AnimatedTile(3,new int[][] {{0,5},{1,5}, {2,5},{1,5}}, 100, 0xff0000ff);
	*/
	
	protected int id; //Location in tiles array of where that tile is located
	protected boolean emitter; //For light
	private int levelColor; //what color corresponds to this tile when loading a level
	
	//position of the tile in world space


	public Tile(int id, boolean isEmitter, int levelColor, Level level)
	{
		super(level);
		
		width = tileWidth;
		height = tileHeight;
		
		this.id = (byte)id;
		
		this.levelColor = levelColor;
		
		this.emitter = isEmitter;
		tiles[id] = this;
	}
	
	public int getId()
	{
		return id;
	}
	
	public boolean isEmitter()
	{
		return emitter;
	}
	
	public abstract void tick();
	
	public abstract void render();

	public int getLevelColor() {
		return levelColor;
	}

}
