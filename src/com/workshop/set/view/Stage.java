package com.workshop.set.view;

import glfrontend.components.GLComponent;
import glfrontend.ScreenFrame;
import org.lwjgl.util.vector.Vector2f;
import static org.lwjgl.opengl.GL11.*;

public class Stage implements ScreenFrame {

	private Vector2f ul, lr;

	public Stage(float x, float y) {
		init();
		setSize(new Vector2f(x, y));
	}

	public Stage(Vector2f size) {
		init();
		setSize(size);
	}

	public void init() {
		ul = new Vector2f(0f, 0f);
		lr = new Vector2f(50f, 50f);
	}
	

	public void draw() {}
	
	public void setLocation(Vector2f p) {
		Vector2f.sub(lr, ul, lr);
		ul = p;
		Vector2f.add(ul, lr, lr);
	}
	
	@Override
	public Vector2f getLocation() {
		return ul;
	}

	@Override
	public void setSize(Vector2f dim) {
		Vector2f.add(ul, dim, lr);
	}

	@Override
	public Vector2f getSize() {
		Vector2f result = new Vector2f();
		Vector2f.sub(lr, ul, result);
		return result;
	}

	@Override
	public boolean contains(Vector2f p) {
		return false;
	}

	@Override
	public void mousePressed(Vector2f p, MouseButton button) {}

	@Override
	public void mouseReleased(Vector2f p, MouseButton button) {}

	@Override
	public void mouseWheelScrolled(int amount) {}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void render() {
		
		//render the grid, axes and renderable items from the stage
		//1. render the grid
		
		//glBegin(GL_LINES);
		//glEnd();
		
		//System.out.println("UL: " + ul + "LR: " +lr);
		glColor3f(1, 0, 0);
		
		//glBegin(GL_QUADS);
		//glBegin(GL_LINES);
		//glVertex2f(ul.x/2, ul.y/2);
		//glVertex2f(ul.x/2, lr.y/2);
		//glVertex2f(lr.x/2, lr.y/2);
		//glVertex2f(lr.x/2, ul.y/2);
		//glEnd();
		
		glBegin(GL_TRIANGLES);
		glVertex3f(230,180, 1);
		glVertex3f(200,300,1);
		glVertex3f(300,300,1);
		glEnd();
		
//		glLineWidth(1);
//	    glDepthMask(false);
//	    glEnable(GL_LINE_SMOOTH);
//	    glEnable(GL_BLEND);
//	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//	    glBegin(GL_LINES);
//	    for (int i = -10; i <= 10; i++)
//	    {
//	        glVertex3i(i, 0, -10);
//	        glVertex3i(i, 0, 10);
//	        glVertex3i(-10, 0, i);
//	        glVertex3i(10, 0, i);
//	    }
//	    glEnd();
//	    glDisable(GL_BLEND);
//	    glDisable(GL_LINE_SMOOTH);
//	    glDepthMask(true);
		
	}

	@Override
	public void resize(Vector2f newSize) {}

}
