package glfrontend.components;

import static com.workshop.set.view.SetScreen.newRatio;
import static org.lwjgl.opengl.GL11.glTranslatef;
import glfrontend.ScreenFrame;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class GLPanel extends GLComponent {

	List<ScreenFrame> comps = new ArrayList<>();

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
		for (ScreenFrame comp : comps)
			comp.render();
		glTranslatef(-ul.x, -ul.y, 0);

	}

	@Override
	public void mousePressed(Vector2f p, MouseButton button) {
//		System.out.println("Panel : " + p);
		for (ScreenFrame frame : comps) {
			if (frame.contains(p)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);
				frame.mousePressed(relativePoint, button);
			}
		}
	}

	@Override
	public void mouseReleased(Vector2f p, MouseButton button) {}

	@Override
	public void mouseWheelScrolled(int amount) {}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void resize(Vector2f dim) {
		if (!isResizable())
			return;
		Vector2f size = new Vector2f();
		Vector2f.sub(lr, ul, size);

		for (ScreenFrame comp : comps) {
			Vector2f oldSize = comp.getSize();
			Vector2f oldLoc = comp.getLocation();

			comp.setLocation(newRatio(oldLoc, size, dim));
			comp.resize(newRatio(oldSize, size, dim));
		}

		Vector2f.add(ul, dim, lr);
	}

}
