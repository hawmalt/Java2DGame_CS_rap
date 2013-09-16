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
