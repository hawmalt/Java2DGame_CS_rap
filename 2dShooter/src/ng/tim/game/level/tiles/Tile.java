package ng.tim.game.level.tiles;

import java.awt.Graphics;

import ng.tim.game.level.Level;

public abstract class Tile {

	//The width and height of each tile
	public static final int width = 8;
	public static final int height = 8;
	
	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new BasicSolidTile(0,0,0);
	public static final Tile STONE = new BasicSolidTile(1,1,0);
	public static final Tile GRASS = new BasicTile(2,2,0);
	public static final Tile WATER = new AnimatedTile(3,new int[][] {{0,5},{1,5}, {2,5},{1,5}}, 100);
	
	protected int id; //Location in tiles array of where that tile is located
	protected boolean solid; //For collision detection
	protected boolean emitter; //For light

	public Tile(int id, boolean isSolid, boolean isEmitter)
	{
		this.id = (byte)id;
		
		//If there is already a tile that has this id ERROR
		if(tiles[id] != null)
		{
			throw new RuntimeException("Duplicate tile id on " + id);
		}
		
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
	

}
