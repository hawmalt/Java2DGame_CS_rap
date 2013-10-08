package ng.tim.game.level.tiles;

import ng.tim.game.level.Level;

public class VoidTile extends BasicTile
{

	public VoidTile(int id, int levelColor, int xpos, int ypos, Level level) {
		super(id, levelColor, xpos, ypos, level);
		x = 0;
		y = 0;
	}

}
