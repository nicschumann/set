package glfrontend.components;

import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class GLPanel extends GLComponent {

	List<GLComponent> comps = new ArrayList<>();

	public GLPanel() {
		super();
	}

	public GLPanel(float x, float y) {
		super(x, y);
	}

	public GLPanel(Vector2f dim) {
		super(dim);
	}

	public void add(GLComponent c) {
		comps.add(c);
	}

	public void add(GLComponent c, boolean resizable) {
		c.setResizable(resizable);
		comps.add(c);
	}

	@Override
	public void draw() {

		// draw sub components
		glTranslatef(ul.x, ul.y, 0);
		for (GLComponent comp : comps)
			comp.render();
		glTranslatef(-ul.x, -ul.y, 0);

	}

	@Override
	public void resize(Vector2f dim) {
		if (!isResizable())
			return;
		Vector2f size = new Vector2f();
		Vector2f.sub(lr, ul, size);

		for (GLComponent comp : comps) {
			Vector2f oldSize = comp.getSize();
			Vector2f oldLoc = comp.getLocation();

			comp.setLocation(newRatio(oldLoc, size, dim));
			comp.resize(newRatio(oldSize, size, dim));
		}

		Vector2f.add(ul, dim, lr);
	}

	public static Vector2f newRatio(Vector2f oldTop, Vector2f oldBottom, Vector2f newBottom) {
		float x = (oldTop.x * newBottom.x) / oldBottom.x;
		float y = (oldTop.y * newBottom.y) / oldBottom.y;
		return new Vector2f(x, y);
	}

}
