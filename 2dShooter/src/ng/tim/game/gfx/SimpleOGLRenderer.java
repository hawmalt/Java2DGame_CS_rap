package ng.tim.game.gfx;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.*;
import org.lwjgl.*;

public class SimpleOGLRenderer {

	public SimpleOGLRenderer()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(500, 500));
			Display.setTitle("Our Game");
			Display.create();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//initialization of OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 500, 500, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		while(!Display.isCloseRequested())
		{
			//render
			
			glBegin(GL_LINES);
				glVertex2i(100, 100);
				glVertex2i(200, 200);
			glEnd();
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
}
