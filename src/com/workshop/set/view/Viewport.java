package com.workshop.set.view;

import glfrontend.ScreenFrame;
import glfrontend.components.GLCamera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

public class Viewport implements ScreenFrame {

	private Vector2f ul, lr;
	private Stage _stage;
	private GLCamera _camera;

	public Viewport(float w, float h) {
		init();
		setSize(new Vector2f(w, h));
		_camera = new GLCamera();
		_camera.setOrthographicView();
	}

	private void init() {
		ul = new Vector2f(0f, 0f);
		lr = new Vector2f(50f, 50f);
	}

	public void setStage(Stage s) {
		_stage = s;
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
	public boolean contains(Vector2f p) {
		if (p.x <= ul.x)
			return false;
		else if (p.x >= lr.x - 1)
			return false;
		if (p.y <= ul.y)
			return false;
		else if (p.y >= lr.y - 1)
			return false;
		return true;
	}

	@Override
	public void render3D() {
		_camera.multMatrix();
		_stage.render3D();
	}

	@Override
	public void render2D() {}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseWheelScrolled(Vector2f p, int amount) {
		_camera.mouseWheel(amount / 6f);
	}

	@Override
	public void keyPressed(int key) {
		if (key == Keyboard.KEY_SPACE) { // toggle on space bar
			String currMode = _camera.getMode();
			if (currMode.equalsIgnoreCase("orthographic")) {
				_camera.setPerspView();
			} else if (currMode.equalsIgnoreCase("perspective")) {
				_camera.setOrthographicView();
			}
		}
	}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void mouseMoved(Vector2f p) {}

	@Override
	public void mouseEntered(Vector2f p) {}

	@Override
	public void mouseExited(Vector2f p) {}

	@Override
	public void setResizeType(ResizeType type) {}

	@Override
	public ResizeType getResizeType() {
		return ResizeType.RATIO;
	}

	@Override
	public void resize(Vector2f newSize) {
		Vector2f size = new Vector2f();
		Vector2f.sub(lr, ul, size);

		Vector2f.add(ul, newSize, lr);
	}

}
