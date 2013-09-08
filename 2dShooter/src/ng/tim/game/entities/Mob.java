package ng.tim.game.entities;

import ng.tim.game.level.Level;
import ng.tim.game.level.tiles.Tile;

public abstract class Mob extends Entity
{

	protected String name;
	protected int speed;
	protected int numSteps = 0; //How far they have walked
	protected boolean isMoving;
	protected int movingDir = 1;
	protected int scale = 1;
	
	public Mob(Level level, String name, int x, int y, int speed)
	{
		super(level);
		this.name = name;
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
	public void move(int xa, int ya)
	{
		//Only moves one at a time
		if(xa != 0 && ya != 0)
		{
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}
		
		numSteps++;
		if(!hasCollided(xa,ya))
		{
			if(ya < 0)
			{
				movingDir = 0; //it is moving up
			}
			if(ya > 0)
			{
				movingDir = 1; //it is moving up
			}
			if(xa < 0)
			{
				movingDir = 2; //it is moving up
			}
			if(xa > 0)
			{
				movingDir = 3; //it is moving up
			}
			
			x += xa * speed;
			y += ya * speed;
		}
	}
	
	public abstract boolean hasCollided(int xa, int ya);
	
	public boolean isSolidTile(int xa, int ya, int x, int y)
	{
		if(level == null)
		{
			return false;
		}
		
		Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
		Tile newTile = level.getTile((this.x + x + xa) >> 3, (this.y + y + xa) >> 3);
		
		if(!lastTile.equals(newTile) && newTile.isSolid())
		{
			return true;
		}
		
		return false;
	}
	
	public String getName()
	{
		return name;
	}

	public int getNumSteps()
	{
		return numSteps;
	}

	public boolean isMoving()
	{
		return isMoving;
	}

	public int getMovingDir()
	{
		return movingDir;
	}

	public void setNumSteps(int numSteps)
	{
		this.numSteps = numSteps;
	}

	public void setMoving(boolean isMoving)
	{
		this.isMoving = isMoving;
	}

	public void setMovingDir(int movingDir)
	{
		this.movingDir = movingDir;
	}
}
