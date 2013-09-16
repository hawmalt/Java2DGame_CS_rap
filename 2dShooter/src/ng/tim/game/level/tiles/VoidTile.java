package ng.tim.game.level.tiles;

public class VoidTile extends BasicTile
{

	public VoidTile(int id, int x, int y, int levelColor, int xpos, int ypos) {
		super(id, x, y, levelColor, xpos, ypos);
		x = 0;
		y = 0;
	}

}
