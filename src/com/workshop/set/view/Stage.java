package com.workshop.set.view;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex3i;
import glfrontend.ScreenFrameAdapter;

import org.lwjgl.util.vector.Vector2f;

public class Stage extends ScreenFrameAdapter {

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

	public void drawGrid() {
		glColor3f(0.5f, 0.5f, 0.5f);

		glLineWidth(1);
		glDepthMask(false);
		glEnable(GL_LINE_SMOOTH);
		glBegin(GL_LINES);
		for (int i = -10; i <= 10; i++) {
			glVertex3i(i, -10, 0);
			glVertex3i(i, 10, 0);
			glVertex3i(-10, i, 0);
			glVertex3i(10, i, 0);
		}
		glEnd();
		glDepthMask(true);
	}

	@Override
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
	public void render3D() {
		this.drawGrid();
	}

}
