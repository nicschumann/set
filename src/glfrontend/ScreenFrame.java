package glfrontend;

import org.lwjgl.util.vector.Vector2f;

public interface ScreenFrame {
	
	public enum MouseButton {
		LEFT,
		RIGHT;
	}
	
	public enum ResizeType {
		FIT_LEFT,
		FIT_RIGHT,
		FIT_BOTTOM,
		FIT_TOP,
		RATIO
	}
	
	public void setLocation(Vector2f loc);
	
	public Vector2f getLocation();
	
	public void setSize(Vector2f size);
	
	public Vector2f getSize();
	
	public boolean contains(Vector2f p);
	
	public void setResizeType(ResizeType type);
	
	public ResizeType getResizeType();
	
	public void mousePressed(Vector2f p, MouseButton button);
	
	public void mouseReleased(Vector2f p, MouseButton button);
	
	public void mouseWheelScrolled(int amount);
	
	public void keyPressed(int key);
	
	public void keyReleased(int key);
	
	public void mouseMoved(Vector2f p);
	
	public void mouseEntered(Vector2f p);
	
	public void mouseExited(Vector2f p);
	
	public void render();
	
	public void resize(Vector2f newSize);

}
