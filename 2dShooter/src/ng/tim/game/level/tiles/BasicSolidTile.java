package ng.tim.game.level.tiles;

import ng.tim.game.Game;
import ng.tim.game.level.Level;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class BasicSolidTile extends BasicTile
{

	public BasicSolidTile(int id, int x, int y, int levelColor, int xpos, int ypos, Level level) {
		super(id, x, y, levelColor, xpos, ypos, level);
		
		//body definition
				BodyDef bd = new BodyDef();
				bd.position.set(this.xpos, this.ypos);  
				bd.type = BodyType.STATIC;
				 
				//define shape of the body.
				PolygonShape cs = new PolygonShape();
				cs.setAsBox(Tile.width/2, Tile.height/2);
				 
				//define fixture of the body.
				FixtureDef fd = new FixtureDef();
				fd.shape = cs;
				fd.density = 0.5f;
				fd.friction = 0.3f;        
				fd.restitution = 0.5f;
				 
				//create the body and add fixture to it
				Body body =  Game.world.createBody(bd);
				body.createFixture(fd);
				
				this.xpos = (int) body.getPosition().x;
				this.ypos = (int) body.getPosition().y;
	}

}
