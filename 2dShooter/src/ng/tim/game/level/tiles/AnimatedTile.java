package ng.tim.game.level.tiles;


public class AnimatedTile extends BasicTile
{
	private int[][] animationTileCoords;
	private int currentAnimationIndex;
	private long lastIterationTime; //milliseceonds since last update
	private int animationSwitchDelay;

	public AnimatedTile(int id, int[][] animationCoords, int animationSwitchDelay)
	{
		super(id, animationCoords[0][0], animationCoords[0][1]);
		this.animationTileCoords = animationCoords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationSwitchDelay = animationSwitchDelay;
	}

	public void tick()
	{
		//Check if the amount of miliseconds past since the last update is greater than the animation delay
		if((System.currentTimeMillis() - lastIterationTime) >= (animationSwitchDelay))
		{
			lastIterationTime = System.currentTimeMillis();
			currentAnimationIndex = (currentAnimationIndex + 1) % animationTileCoords.length; //move to the next frame and check if it is out of bounds
		}
	}
}
