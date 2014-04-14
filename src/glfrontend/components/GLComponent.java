package glfrontend.components;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

public abstract class GLComponent {

	protected float[] _color;
	protected Vector2f ul;
	protected Vector2f lr;
	protected boolean resizable;

	public GLComponent() {
		init();
	}

	public GLComponent(float x, float y) {
		init();
		setSize(x, y);
	}

	public GLComponent(Vector2f dim) {
		init();
		setSize(dim);
	}

	private void init() {
		_color = new float[4];
		ul = new Vector2f(0f, 0f);
		lr = new Vector2f(50f, 50f);
		resizable = true;
	}

	public void setBackground(Color color) {
		color.getComponents(_color);
	}

	public void setSize(float width, float height) {
		setSize(new Vector2f(width, height));
	}

	public void setSize(Vector2f dim) {
		Vector2f.add(ul, dim, lr);
	}

	public Vector2f getSize() {
		Vector2f result = new Vector2f();
		Vector2f.sub(lr, ul, result);
		return result;
	}

	public void setLocation(float x, float y) {
		Vector2f.sub(lr, ul, lr);
		ul = new Vector2f(x, y);
		Vector2f.add(ul, lr, lr);
	}

	public void setLocation(Vector2f p) {
		Vector2f.sub(lr, ul, lr);
		ul = p;
		Vector2f.add(ul, lr, lr);
	}

	public Vector2f getLocation() {
		return ul;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public boolean isResizable() {
		return resizable;
	}

	public void resize(float width, float height) {
		resize(new Vector2f(width, height));
	}
	
	public void render() {
		// set color
		glColor4f(_color[0], _color[1], _color[2], _color[3]);

		// draw quad
		glBegin(GL_QUADS);
		glVertex2f(ul.x, ul.y);
		glVertex2f(ul.x, lr.y);
		glVertex2f(lr.x, lr.y);
		glVertex2f(lr.x, ul.y);
		glEnd();
		
		draw();
	}

	public abstract void draw();

	public abstract void resize(Vector2f dim);

}
