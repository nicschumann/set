package glfrontend;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glViewport;
import glfrontend.components.GLPanel;

import java.awt.Color;
import java.awt.Dimension;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector2f;

public class GLFrontEnd implements FrontEnd {

	public static Dimension WINDOW_DIMENSIONS = new Dimension(500, 500);
	public static Vector2f SCALE = new Vector2f(1, 1);
	public static String TITLE = "";

	private GLPanel _panel;

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
		_panel = new GLPanel();
		_panel.setLocation(0, 0);
		_panel.setSize(WINDOW_DIMENSIONS.width, WINDOW_DIMENSIONS.height);
		_panel.setBackground(Color.DARK_GRAY);
	}

	public void setMainPanel(GLPanel panel) {
		this._panel = panel;
	}

	/**
	 * Renders the game objects and characters for each time step.
	 */
	@Override
	public void onRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glPushMatrix();
		_panel.render();
		glPopMatrix();
	}

	/**
	 * Handles the game logic for each time step.
	 */
	@Override
	public void onTick() {
		if (Display.wasResized()) {
			glViewport(0, 0, Display.getWidth(), Display.getHeight());
			glLoadIdentity();
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0.0f, Display.getWidth(), Display.getHeight(), 0.0f, 1.0f, -1.0f);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			_panel.resize(Display.getWidth(), Display.getHeight());
		}
	}

	/**
	 * Handles the keyboard and mouse input from each time step;
	 */
	@Override
	public void checkInput() {

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
		glEnable(GL_TEXTURE_RECTANGLE_ARB);

		// Only shows the image on the front of the surface.
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
	}

	/**
	 * Sets up openGL matrices for rendering objects. Top left: (0, 0) Bottom right:
	 * (window_dimensions)
	 */
	public void setUpMatrices() {
		glMatrixMode(GL_PROJECTION);
		glOrtho(0, Display.getWidth() / SCALE.x, Display.getHeight() / SCALE.y, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	@Override
	public void enterLoop() {
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

}
