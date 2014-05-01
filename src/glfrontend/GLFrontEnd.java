package glfrontend;

import static glfrontend.ScreenFrame.MouseButton.LEFT;
import static glfrontend.ScreenFrame.MouseButton.RIGHT;
import static glfrontend.ScreenFrame.MouseButton.WHEEL;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION_MATRIX;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRANSFORM_BIT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glLoadMatrix;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glViewport;
import glfrontend.ScreenFrame.KeyEvent;
import glfrontend.ScreenFrame.MouseEvent;
import glfrontend.components.GLPanel;

import java.awt.Dimension;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

public class GLFrontEnd implements FrontEnd {

	public static Dimension WINDOW_DIMENSIONS = new Dimension(500, 500);
	public static Vector2f SCALE = new Vector2f(1, 1);
	public static String TITLE = "";

	private static final FloatBuffer perspectiveProjectionMatrix = BufferUtils.createFloatBuffer(16);
	private static final FloatBuffer orthographicProjectionMatrix = BufferUtils.createFloatBuffer(16);

	private boolean leftPressed, rightPressed, wheelPressed;
	private boolean clickLeft, clickRight, clickWheel;
	private Vector2f prevMouse;
	private boolean contained;
	
	private long animationTime;

	private ScreenFrame _frame;

	public GLFrontEnd() {
		init();
	}

	public GLFrontEnd(String title) {
		TITLE = title;
		init();
	}

	public GLFrontEnd(String title, Dimension dim) {
		TITLE = title;
		WINDOW_DIMENSIONS = dim;
		init();
	}

	private void init() {
		setUpDisplay();
		setUpStates();
		setUpMatrices();
		_frame = new GLPanel();
		_frame.setLocation(new Vector2f(0, 0));
		_frame.setSize(new Vector2f(WINDOW_DIMENSIONS.width, WINDOW_DIMENSIONS.height));
		prevMouse = new Vector2f(Mouse.getX(), mouseY());
		leftPressed = false;
		rightPressed = false;
		wheelPressed = false;
		contained = false;
	}

	public void setMainScreen(ScreenFrame frame) {
		this._frame = frame;
	}

	/**
	 * Renders the game objects and characters for each time step.
	 */
	@Override
	public void onRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		_frame.render3D();

		// load 2D ortho matrix, render 2D, then load perspective matrix
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(orthographicProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
		glBindTexture(GL_TEXTURE_2D, 0);
		_frame.render2D();
		glPopMatrix();
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
	}

	/**
	 * Handles the game logic for each time step.
	 */
	@Override
	public void onTick() {
		if (Display.wasResized()) {
			float w = Display.getWidth();
			float h = Display.getHeight();
			glViewport(0, 0, Display.getWidth(), Display.getHeight());
			glLoadIdentity();

			float defaultHeight = 600f;
			float defaultFov = 1.0f; // Approximately 60 degrees
			// float fov = (float) Math.atan(h / defaultHeight * Math.tan(defaultFov * 0.5f)) *
			// 2.0f;
			float fov = (float) Math.atan(h / defaultHeight * Math.tan(defaultFov * 0.5f)) * 2.0f;

			glPushAttrib(GL_TRANSFORM_BIT);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			GLU.gluPerspective((float) Math.toDegrees(fov), w / h, 0.01f, 1000f);
			glPopAttrib();

			glGetFloat(GL_PROJECTION_MATRIX, perspectiveProjectionMatrix);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, Display.getWidth() / SCALE.x, Display.getHeight() / SCALE.y, 0, 1, -1);
			glGetFloat(GL_PROJECTION_MATRIX, orthographicProjectionMatrix);
			glLoadMatrix(perspectiveProjectionMatrix);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			_frame.resize(new Vector2f(Display.getWidth(), Display.getHeight()));
		}
		long currentTime = System.currentTimeMillis();
		_frame.animate(currentTime - animationTime);
		animationTime = currentTime;
	}

	/**
	 * Handles the keyboard and mouse input from each time step;
	 */
	@Override
	public void checkInput() {

		Vector2f mouseLoc = new Vector2f(Mouse.getX(), mouseY());
		boolean buttonPressed = false;
		boolean mouseMoved = mouseLoc.x - prevMouse.x != 0 || mouseLoc.y - prevMouse.y != 0;
		boolean contains = _frame.contains(mouseLoc);

		if (contained && !contains) {
			_frame.mouseExited(mouseLoc);
			contained = contains;
		} else if (contains && !contained)
			_frame.mouseEntered(mouseLoc);
		if (!contains)
			return;

		// Left button
		if (Mouse.isButtonDown(0)) {
			buttonPressed = true;
			if (!leftPressed) {
				_frame.mousePressed(new MouseEvent(mouseLoc, LEFT));
				leftPressed = true;
				clickLeft = true;
			}
			if (mouseMoved) {
				_frame.mouseDragged(new MouseEvent(mouseLoc, LEFT));
				clickLeft = false;
			}
		} else if (leftPressed) {
			_frame.mouseReleased(new MouseEvent(mouseLoc, LEFT));
			if (contains && clickLeft) {
				_frame.mouseClicked(new MouseEvent(mouseLoc, LEFT));
				clickLeft = false;
			}
			leftPressed = false;
		}

		// Right button
		if (Mouse.isButtonDown(1)) {
			buttonPressed = true;
			if (!rightPressed) {
				_frame.mousePressed(new MouseEvent(mouseLoc, RIGHT));
				rightPressed = true;
				clickRight = true;
			}
			if (mouseMoved) {
				_frame.mouseDragged(new MouseEvent(mouseLoc, RIGHT));
				clickRight = false;
			}
		} else if (rightPressed) {
			_frame.mouseReleased(new MouseEvent(mouseLoc, RIGHT));
			if (contains && clickRight) {
				_frame.mouseClicked(new MouseEvent(mouseLoc, RIGHT));
				clickRight = false;
			}
			rightPressed = false;
		}

		// Wheel button
		if (Mouse.isButtonDown(2)) {
			buttonPressed = true;
			if (!wheelPressed) {
				_frame.mousePressed(new MouseEvent(mouseLoc, WHEEL));
				wheelPressed = true;
				clickWheel = true;
			}
			if (mouseMoved) {
				_frame.mouseDragged(new MouseEvent(mouseLoc, WHEEL));
				clickWheel = false;
			}
		} else if (wheelPressed) {
			_frame.mouseReleased(new MouseEvent(mouseLoc, WHEEL));
			if (contains && clickWheel) {
				_frame.mouseClicked(new MouseEvent(mouseLoc, WHEEL));
				clickWheel = false;
			}
			wheelPressed = false;
		}

		if (!buttonPressed && mouseMoved) {
			_frame.mouseMoved(mouseLoc);
		}
		int dWheel = Mouse.getDWheel();
		if (dWheel != 0)
			_frame.mouseWheelScrolled(mouseLoc, dWheel);

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				_frame.keyPressed(new KeyEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter()));
			} else {
				_frame.keyReleased(new KeyEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter()));
			}
		}
		prevMouse = mouseLoc;
		contained = contains;
	}

	/**
	 * Updates the display at 60 frames per second.
	 */
	private void update() {
		Display.update();
		Display.sync(60);
	}

	/**
	 * Initialize a new display with size determined by the variable WINDOW_DIMENSIONS.
	 */
	public void setUpDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WINDOW_DIMENSIONS.width, WINDOW_DIMENSIONS.height));
			Display.setTitle(TITLE);
			Display.setVSyncEnabled(true); // Prevents flickering frames.
			Display.setResizable(true);
			Display.setInitialBackground(0.3f, 0.3f, 0.3f);
			Display.create();
		} catch (LWJGLException e) {
			System.err.println("ERROR: " + e.getMessage());
			cleanUp(true);
		}
	}

	/**
	 * Sets up the openGL settings
	 */
	public void setUpStates() {
		// Allows for textures(images) to be drawn on shape surfaces.
		// glEnable(GL_TEXTURE_RECTANGLE_ARB);

		glEnable(GL_TEXTURE_2D);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// Only shows the image on the front of the surface.
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}

	/**
	 * Sets up openGL matrices for rendering objects. Top left: (0, 0) Bottom right:
	 * (window_dimensions)
	 */
	public void setUpMatrices() {
		float w = Display.getWidth();
		float h = Display.getHeight();
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glLoadIdentity();

		float defaultHeight = 600f;
		float defaultFov = 1.0f; // Approximately 60 degrees
		float fov = (float) Math.atan(h / defaultHeight * Math.tan(defaultFov * 0.5f)) * 2.0f;

		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective((float) Math.toDegrees(fov), w / h, 0.01f, 1000f);
		glPopAttrib();

		glGetFloat(GL_PROJECTION_MATRIX, perspectiveProjectionMatrix);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth() / SCALE.x, Display.getHeight() / SCALE.y, 0, 1, -1);
		glGetFloat(GL_PROJECTION_MATRIX, orthographicProjectionMatrix);
		glLoadMatrix(perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
	}

	@Override
	public void enterLoop() {
		animationTime = System.currentTimeMillis();
		while (!Display.isCloseRequested()) {
			onRender();
			onTick();
			checkInput();
			update();
		}
	}

	@Override
	public void cleanUp(boolean asCrash) {
		Display.destroy();
		System.exit(asCrash ? 1 : 0);
	}

	public static float mouseY() {
		return Display.getHeight() - Mouse.getY() - 1;
	}

}
