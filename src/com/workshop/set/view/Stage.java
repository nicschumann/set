package com.workshop.set.view;

import glfrontend.ScreenFrame;

import org.lwjgl.util.vector.Vector2f;

public class Stage implements ScreenFrame {

	public Stage() {}

	public Stage(float x, float y) {}

	public Stage(Vector2f size) {}

	@Override
	public void setLocation(Vector2f loc) {}

	@Override
	public Vector2f getLocation() {
		return null;
	}

	@Override
	public void setSize(Vector2f size) {}

	@Override
	public Vector2f getSize() {
		return null;
	}

	@Override
	public void mousePressed(Vector2f p, MouseButton button) {}

	@Override
	public void mouseReleased(Vector2f p, MouseButton button) {}

	@Override
	public void mouseWheelScrolled(Vector2f p) {}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void render() {}

	@Override
	public void resize(Vector2f newSize) {}

}
