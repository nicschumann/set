package com.workshop.set.view;

import static org.lwjgl.opengl.GL11.glTranslatef;
import glfrontend.ScreenFrame;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class SetScreen implements ScreenFrame {
	
	private Vector2f ul, lr;
	List<ScreenFrame> frames = new ArrayList<>();
	
	public SetScreen(float x, float y) {
		init();
		setSize(new Vector2f(x, y));
	}

	private void init() {
		ul = new Vector2f(0f, 0f);
		lr = new Vector2f(50f, 50f);
	}
	
	public void add(ScreenFrame frame) {
		frames.add(frame);
	}
	
	@Override
	public void setLocation(Vector2f loc) {
		Vector2f.sub(lr, ul, lr);
		ul = loc;
		Vector2f.add(ul, lr, lr);
	}
	
	@Override
	public Vector2f getLocation() {
		return ul;
	}
	
	@Override
	public void setSize(Vector2f size) {
		Vector2f.add(ul, size, lr);
	}
	
	@Override
	public Vector2f getSize() {
		Vector2f result = new Vector2f();
		Vector2f.sub(lr, ul, result);
		return result;
	}

	@Override
	public void mousePressed(Vector2f p, MouseButton button) {
	}

	@Override
	public void mouseReleased(Vector2f p, MouseButton button) {
	}

	@Override
	public void mouseWheelScrolled(Vector2f p) {
	}

	@Override
	public void keyPressed(int key) {
	}

	@Override
	public void keyReleased(int key) {
	}

	@Override
	public void render() {
		glTranslatef(ul.x, ul.y, 0);
		for (ScreenFrame frame : frames)
			frame.render();
		glTranslatef(-ul.x, -ul.y, 0);
	}
	
	@Override
	public void resize(Vector2f newSize) {
		Vector2f size = new Vector2f();
		Vector2f.sub(lr, ul, size);

		for (ScreenFrame frame : frames) {
			Vector2f oldSize = frame.getSize();
			Vector2f oldLoc = frame.getLocation();
			System.out.println(frame + ", " + oldSize + ", " + newSize);

			frame.setLocation(newRatio(oldLoc, size, newSize));
			frame.resize(newRatio(oldSize, size, newSize));
		}

		Vector2f.add(ul, newSize, lr);
	}
	
	public static Vector2f newRatio(Vector2f oldTop, Vector2f oldBottom, Vector2f newBottom) {
		float x = (oldTop.x * newBottom.x) / oldBottom.x;
		float y = (oldTop.y * newBottom.y) / oldBottom.y;
		return new Vector2f(x, y);
	}

}
