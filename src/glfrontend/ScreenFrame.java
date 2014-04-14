package glfrontend;

import org.lwjgl.util.vector.Vector2f;

public interface ScreenFrame {
	
	public enum MouseButton {
		LEFT,
		RIGHT;
	}
	
	public void setLocation(Vector2f loc);
	
	public Vector2f getLocation();
	
	public void setSize(Vector2f size);
	
	public Vector2f getSize();
	
	public void mousePressed(Vector2f p, MouseButton button);
	
	public void mouseReleased(Vector2f p, MouseButton button);
	
	public void mouseWheelScrolled(Vector2f p);
	
	public void keyPressed(int key);
	
	public void keyReleased(int key);
	
	public void render();
	
	public void resize(Vector2f newSize);
//	public void 
}
