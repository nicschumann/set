package com.workshop.set.view;

import static org.lwjgl.opengl.GL11.glTranslatef;
import glfrontend.ScreenFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

public class SetScreen implements ScreenFrame {

	private Vector2f ul, lr;
	private List<ScreenFrame> frames;
	private Map<ScreenFrame, Boolean> contained;

	public SetScreen(float w, float h) {
		init();
		setSize(new Vector2f(w, h));
	}

	private void init() {
		ul = new Vector2f(0f, 0f);
		lr = new Vector2f(50f, 50f);
		frames = new ArrayList<>();
		contained = new HashMap<>();
	}

	public void add(ScreenFrame frame) {
		frames.add(frame);
		contained.put(frame, false);
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
		return false;
	}

	@Override
	public void mousePressed(Vector2f p, MouseButton button) {
		for (ScreenFrame frame : frames) {
			if (frame.contains(p)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);
				frame.mousePressed(relativePoint, button);
			}
		}
	}

	@Override
	public void mouseReleased(Vector2f p, MouseButton button) {
		for (ScreenFrame frame : frames) {
			if (frame.contains(p)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);
				frame.mouseReleased(relativePoint, button);
			}
		}
	}

	@Override
	public void mouseWheelScrolled(int amount) {}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

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

	@Override
	public void mouseMoved(Vector2f p) {
		for (ScreenFrame frame : frames) {
			if (frame.contains(p)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);

				if (!contained.get(frame)) {
					frame.mouseEntered(relativePoint);
					contained.put(frame, true);
				}
				frame.mouseMoved(relativePoint);
			} else if (contained.get(frame)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);

				frame.mouseExited(relativePoint);
				contained.put(frame, false);
			}
		}
	}

	@Override
	public void mouseEntered(Vector2f p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(Vector2f p) {
		// TODO Auto-generated method stub

	}

}
