package ng.tim.game.entities;

import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import ng.tim.game.level.Level;

public abstract class Entity
{
	public int x,y;
	protected Level level;
	protected Texture texture;

	
	public Entity(Level level)
	{
		init(level);
	}
	
	public final void init(Level level)
	{
		this.level = level;
	}
	
	protected Texture loadTexture(String key)
	{
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + key + ".png")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract void tick();
	
	public abstract void render();
}
