package glfrontend.components;

import static com.workshop.set.view.SetScreen.newRatio;
import static org.lwjgl.opengl.GL11.glTranslatef;
import glfrontend.ScreenFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

public class GLPanel extends GLComponent {

	private List<ScreenFrame> comps;
	private Map<ScreenFrame, Boolean> contained;

	public GLPanel() {
		super();
		comps = new ArrayList<>();
		contained = new HashMap<>();
	}

	public void add(GLComponent c) {
		comps.add(c);
		contained.put(c, false);
	}

	public void add(GLComponent c, boolean resizable) {
		c.setResizable(resizable);
		comps.add(c);
		contained.put(c, false);
	}

	@Override
	public void draw() {

		// draw sub components
//		glTranslatef(ul.x, ul.y, 0);
//		for (ScreenFrame comp : comps)
//			comp.render();
//		glTranslatef(-ul.x, -ul.y, 0);		
		
	}

	@Override
	public void mousePressed(Vector2f p, MouseButton button) {
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

	@Override
	public void mouseMoved(Vector2f p) {
		for (ScreenFrame frame : comps) {
			if (frame.contains(p)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);

				if (!contained.get(frame)) {
					frame.mouseEntered(relativePoint);
					contained.put(frame, true);
				}
				frame.mouseMoved(relativePoint);
			} else if (contained.get(frame)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);

				frame.mouseExited(relativePoint);
				contained.put(frame, false);
			}
		}
	}

	@Override
	public void mouseEntered(Vector2f p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(Vector2f p) {
		// TODO Auto-generated method stub

	}

}
