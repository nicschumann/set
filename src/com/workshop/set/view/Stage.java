package com.workshop.set.view;

import glfrontend.components.GLComponent;
import org.lwjgl.util.vector.Vector2f;
import static org.lwjgl.opengl.GL11.*;

public class Stage extends GLComponent {


	public Stage() {
		super();
	}

	public Stage(float x, float y) {
		super(x, y);
	}

	public Stage(Vector2f size) {
		super(size);
	}

	@Override
	public void draw() {
//		be.getRenderObjects();
		
		//render the grid, axes and renderable items from the stage
		
		
		//1. render the grid
		
		glBegin(GL_LINES);
		
		
		glEnd();
		
//		glBegin(GL_QUADS);
//		glVertex2f(ul.x, ul.y);
//		glVertex2f(ul.x, lr.y);
//		glVertex2f(lr.x, lr.y);
//		glVertex2f(lr.x, ul.y);
//		glEnd();
		
		
	}

	@Override
	public void resize(Vector2f dim) {
		if (!isResizable())
			return;
		Vector2f size = new Vector2f();
		Vector2f.sub(lr, ul, size);

		Vector2f.add(ul, dim, lr);
	}

}
