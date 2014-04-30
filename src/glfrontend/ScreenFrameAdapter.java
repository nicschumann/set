package glfrontend;

import org.lwjgl.util.vector.Vector2f;

public class ScreenFrameAdapter implements ScreenFrame {

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
	public boolean contains(Vector2f p) {
		return false;
	}

	@Override
	public void setResizeType(ResizeType type) {}

	@Override
	public ResizeType getResizeType() {
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseWheelScrolled(Vector2f p, int amount) {}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void mouseMoved(Vector2f p) {}

	@Override
	public void mouseEntered(Vector2f p) {}

	@Override
	public void mouseExited(Vector2f p) {}

	@Override
	public void render3D() {}

	@Override
	public void render2D() {}

	@Override
	public void resize(Vector2f newSize) {}

	@Override
	public void animate(long millisSincePrev) {}

}
