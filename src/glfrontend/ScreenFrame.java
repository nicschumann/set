package glfrontend;

import org.lwjgl.util.vector.Vector2f;

public interface ScreenFrame {

	/**
	 * Specifies the left, right, or mouse wheel buttons
	 * 
	 * @author ltbarnes
	 * 
	 */
	public enum MouseButton {
		LEFT, RIGHT, WHEEL;
	}

	/**
	 * Sets how a component should handle resizing
	 * 
	 * @author ltbarnes
	 * 
	 */
	public enum ResizeType {
		FIT_LEFT, FIT_RIGHT, FIT_BOTTOM, FIT_TOP, RATIO;
	}

	/**
	 * Sets the top left location of this frame relative to its parent
	 * 
	 * @param loc
	 */
	public void setLocation(Vector2f loc);

	/**
	 * Returns the location for this frame
	 * 
	 * @return the vector representing the top left of this frame
	 */
	public Vector2f getLocation();

	/**
	 * Sets the width and height of this frame
	 * 
	 * @param size
	 */
	public void setSize(Vector2f size);

	/**
	 * Returns the size of this frame
	 * 
	 * @return the vector representing the width and height of this frame
	 */
	public Vector2f getSize();

	/**
	 * Determines whether or not the given point is contained within the frame
	 * 
	 * @param p
	 *            - the point to check for
	 * @return true if the point is in the frame, false otherwise
	 */
	public boolean contains(Vector2f p);

	public void setResizeType(ResizeType type);

	public ResizeType getResizeType();

	/**
	 * Triggered if a mouse button is pressed and released quickly
	 * 
	 * @param e
	 *            - {@link MouseEvent}
	 */
	public void mouseClicked(MouseEvent e);

	/**
	 * Triggered once if a mouse button is pressed
	 * 
	 * @param e
	 *            - {@link MouseEvent}
	 */
	public void mousePressed(MouseEvent e);

	/**
	 * Triggered if a mouse button is released
	 * 
	 * @param e
	 *            - {@link MouseEvent}
	 */
	public void mouseReleased(MouseEvent e);

	/**
	 * Triggered if a mouse button is held and the mouse is moved
	 * 
	 * @param e
	 *            - {@link MouseEvent}
	 */
	public void mouseDragged(MouseEvent e);

	/**
	 * Triggered if the mouse wheel is used to scroll up or down
	 * 
	 * @param amount
	 *            - the positive or negative value of the scroll
	 */
	public void mouseWheelScrolled(Vector2f p, int amount);

	/**
	 * Triggered once when a key is pressed
	 * 
	 * Potential Usage:
	 * 
	 * switch (key) { case Keyboard.KEY_A: // do stuff for key 'a' break; case Keyboard.KEY_W: // do
	 * stuff for key 'w' break; case Keyboard.KEY_D: // do stuff for key 'd' break; . . . }
	 * 
	 * @param key
	 */
	public void keyPressed(int key);

	/**
	 * Triggered when a key is released
	 * 
	 * Potential Usage:
	 * 
	 * switch (key) { case Keyboard.KEY_A: // do stuff for key 'a' break; case Keyboard.KEY_W: // do
	 * stuff for key 'w' break; case Keyboard.KEY_D: // do stuff for key 'd' break; . . . }
	 * 
	 * @param key
	 *            - the int representing the key value
	 */
	public void keyReleased(int key);

	/**
	 * Triggered when the mouse is moved and no buttons are pressed
	 * 
	 * @param p
	 *            - the position of the mouse
	 */
	public void mouseMoved(Vector2f p);

	/**
	 * Triggered when the mouse enters the component
	 * 
	 * @param p
	 *            - the position of the mouse
	 */
	public void mouseEntered(Vector2f p);

	/**
	 * Triggered when the cursor exits the component
	 * 
	 * @param p
	 *            - the last position of the mouse in the component
	 */
	public void mouseExited(Vector2f p);

	/**
	 * Renders the 3D objects
	 */
	public void render3D();

	/**
	 * Renders the 2D objects
	 */
	public void render2D();

	/**
	 * Handles resizing the frame and objects contained within (if resize enabled)
	 * 
	 * @param newSize
	 *            - the new size of the frame
	 */
	public void resize(Vector2f newSize);

	/**
	 * Allows UI elements to animate themselves by calling this method on every iteration of the
	 * lwjgl loop.
	 * 
	 * @param millisSincePrev
	 *            - time since method was last called
	 */
	public void animate(long millisSincePrev);

	/**
	 * A class containing mouse button and location data stored in public variables
	 * 
	 * @author ltbarnes
	 * 
	 */
	public static class MouseEvent {

		public final MouseButton button;
		public final Vector2f location;

		public MouseEvent(Vector2f p, MouseButton button) {
			this.location = p;
			this.button = button;
		}

	}

}
