package glfrontend.components;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

public class GLButton extends GLComponent {

	private float[] _brightColor = new float[4];
	private float[] _midColor = new float[4];
	private float bs; // border size

	public GLButton() {
		super();
	}

	public GLButton(float x, float y) {
		super(x, y);
	}

	public GLButton(Vector2f dim) {
		super(dim);
	}

	@Override
	public void setSize(Vector2f dim) {
		Vector2f.add(ul, dim, lr);
		bs = (lr.y - ul.y) / 15f;
	}

	@Override
	public void setBackground(Color color) {
		Color temp = color.darker().darker();
		temp.getComponents(_color);

		temp = color.brighter().brighter();
		temp.getComponents(_brightColor);

		color.getComponents(_midColor);
	}

	@Override
	public void draw() {

		// set top border color
		glColor4f(_brightColor[0], _brightColor[1], _brightColor[2], _brightColor[3]);

		// draw top of border
		glBegin(GL_QUADS);
		glVertex2f(ul.x, ul.y);
		glVertex2f(ul.x, lr.y - bs);
		glVertex2f(lr.x - bs, lr.y - bs);
		glVertex2f(lr.x - bs, ul.y);
		glEnd();

		// set color
		glColor4f(_midColor[0], _midColor[1], _midColor[2], _midColor[3]);

		// draw button
		glBegin(GL_QUADS);
		glVertex2f(ul.x + bs, ul.y + bs);
		glVertex2f(ul.x + bs, lr.y - bs);
		glVertex2f(lr.x - bs, lr.y - bs);
		glVertex2f(lr.x - bs, ul.y + bs);
		glEnd();

	}

	@Override
	public void mousePressed(Vector2f p, MouseButton button) {}

	@Override
	public void mouseReleased(Vector2f p, MouseButton button) {}

	@Override
	public void mouseWheelScrolled(Vector2f p) {}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void resize(Vector2f newSize) {}

}
