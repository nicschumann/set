package com.workshop.set.view;

//import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
//import java.nio.FloatBuffer;
import glfrontend.ScreenFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
//import glfrontend.components.GLComponent;
//import glfrontend.ScreenFrame;

//import org.lwjgl.util.vector.Vector2f;
//import org.lwjgl.util.glu.GLU;
//import org.lwjgl.BufferUtils;

public class SetScreen implements ScreenFrame {

	private Vector2f ul, lr;
	private Viewport _viewport;

	private List<ScreenFrame> frames;
	private Map<ScreenFrame, Boolean> contained;

	// private Set<GeometricElement> _currentElements;

	public SetScreen(float w, float h) {
		init();
		setSize(new Vector2f(w, h));
		_viewport = new Viewport(w, h);
	}

	private void init() {
		// _currentElements = new HashSet<GeometricElement>();
		ul = new Vector2f(0f, 0f);
		lr = new Vector2f(50f, 50f);
		frames = new ArrayList<>();
		contained = new HashMap<>();
	}

	public void setStage(Stage s) {
		_viewport.setStage(s);
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
	public void mouseClicked(MouseEvent e) {
		// System.out.println("MouseClicked: " + e.location + ", Button: " + e.button);
		boolean onViewport = true;
		for (ScreenFrame frame : frames) {
			if (frame.contains(e.location)) {
				onViewport = false;
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(e.location, frame.getLocation(), relativePoint);
				frame.mouseClicked(new MouseEvent(relativePoint, e.button));
			}
		}
		if (onViewport)
			_viewport.mouseClicked(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("MousePressed: " + e.location + ", Button: " + e.button);
		boolean onViewport = true;
		for (ScreenFrame frame : frames) {
			if (frame.contains(e.location)) {
				onViewport = false;
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(e.location, frame.getLocation(), relativePoint);
				frame.mousePressed(new MouseEvent(relativePoint, e.button));
			}
		}
		if (onViewport)
			_viewport.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// System.out.println("MouseReleased: " + e.location + ", Button: " + e.button);
		boolean onViewport = true;
		for (ScreenFrame frame : frames) {
			if (frame.contains(e.location)) {
				onViewport = false;
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(e.location, frame.getLocation(), relativePoint);
				frame.mouseReleased(new MouseEvent(relativePoint, e.button));
			}
		}
		if (onViewport)
			_viewport.mouseReleased(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// System.out.println("MouseDragged: " + e.location + ", Button: " + e.button);
		boolean onViewport = true;
		for (ScreenFrame frame : frames) {
			if (frame.contains(e.location)) {
				onViewport = false;
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(e.location, frame.getLocation(), relativePoint);

				if (!contained.get(frame)) {
					frame.mouseEntered(relativePoint);
					contained.put(frame, true);
				}
				frame.mouseDragged(new MouseEvent(relativePoint, e.button));
			} else if (contained.get(frame)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(e.location, frame.getLocation(), relativePoint);

				frame.mouseExited(relativePoint);
				contained.put(frame, false);
			}
		}
		if (onViewport)
			_viewport.mouseDragged(e);
	}

	@Override
	public void render3D() {
		_viewport.render3D();
	}

	@Override
	public void render2D() {
		glTranslatef(ul.x, ul.y, 0);
		for (ScreenFrame frame : frames)
			frame.render2D();
		glTranslatef(-ul.x, -ul.y, 0);
	}

	@Override
	public void mouseMoved(Vector2f p) {
		// System.out.println("MouseMoved: " + p);
		boolean onViewport = true;
		for (ScreenFrame frame : frames) {
			if (frame.contains(p)) {
				onViewport = false;
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
		if (onViewport)
			_viewport.mouseMoved(p);
	}

	@Override
	public void mouseWheelScrolled(Vector2f p, int amount) {
		// System.out.println("WheelScrolled: " + amount);
		boolean onViewport = true;
		for (ScreenFrame frame : frames) {
			if (frame.contains(p)) {
				onViewport = false;
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);
				frame.mouseWheelScrolled(relativePoint, amount);
			}
		}
		if (onViewport)
			_viewport.mouseWheelScrolled(p, amount);

	}

	@Override
	public void keyPressed(int key) {
		_viewport.keyPressed(key);

	}

	@Override
	public void keyReleased(int key) {
		_viewport.keyReleased(key);
	}

	@Override
	public void mouseEntered(Vector2f p) {
		// System.out.println("MouseEntered: " + p);
		boolean onViewport = true;
		for (ScreenFrame frame : frames) {
			if (frame.contains(p)) {
				onViewport = false;
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);
				frame.mouseEntered(relativePoint);
			}
		}
		if (onViewport)
			_viewport.mouseEntered(p);
	}

	@Override
	public void mouseExited(Vector2f p) {
		// System.out.println("MouseExited: " + p);
		for (ScreenFrame frame : frames) {
			Vector2f relativePoint = new Vector2f();
			Vector2f.sub(p, frame.getLocation(), relativePoint);
			frame.mouseExited(relativePoint);
		}
		_viewport.mouseExited(p);
	}

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

		for (ScreenFrame frame : frames) {
			Vector2f oldSize = frame.getSize();

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
