package ng.tim.game.level.tiles;

public class BasicSolidTile extends BasicTile
{

	public BasicSolidTile(int id, int x, int y) {
		super(id, x, y);
		this.solid = true;
	}

}
