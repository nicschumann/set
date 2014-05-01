package com.workshop.set.view;

import static com.workshop.set.SetMain.GENSYM;
import static com.workshop.set.SetMain.VEC_SPACE_3D;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glLoadIdentity;

import com.workshop.set.model.MDouble;

import glfrontend.ScreenFrameAdapter;
import glfrontend.components.Camera;
import glfrontend.components.Vector4;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

import com.workshop.set.model.VectorSpace.GeometricFailure;
import com.workshop.set.model.VectorSpace.Point;
import com.workshop.set.model.interfaces.Model;

public class Viewport extends ScreenFrameAdapter {

	private Vector2f ul, lr;
	private Stage _stage;
	private Camera _currCamera;
	private ArrayList<Camera> _cameras;
	private int _camIndex = 0;
	private Model _model;

	private Vector2f _currPos;
	private boolean _shiftDown;
	private Point[] _linePoints = new Point[2];
	private int _toUpdate;

	private String _mode; // controls interaction changes for creation or selection mode

	public Viewport(Model model, float w, float h) {
		init();
		setSize(new Vector2f(w, h));

		_cameras = new ArrayList<Camera>();
		_cameras.add(0, new Camera("orthographic"));
		_cameras.add(1, new Camera("perspective"));
		_currCamera = _cameras.get(_camIndex % _cameras.size());
		_camIndex += 1;

		_model = model;
	}

	private void init() {
		ul = new Vector2f(0f, 0f);
		lr = new Vector2f(50f, 50f);
		_shiftDown = false;
		_toUpdate = 0;

		_mode = "creation";
	}

	public void setStage(Stage s) {
		_stage = s;
	}

	@Override
	public void setLocation(Vector2f loc) {
		Vector2f.sub(lr, ul, lr);
		ul = loc;
		Vector2f.add(ul, lr, lr);
	}

	@Override
	public Vector2f getLocation() {
		return ul;
	}

	@Override
	public void setSize(Vector2f size) {
		Vector2f.add(ul, size, lr);
	}

	@Override
	public Vector2f getSize() {
		Vector2f result = new Vector2f();
		Vector2f.sub(lr, ul, result);
		return result;
	}

	@Override
	public boolean contains(Vector2f p) {
		if (p.x <= ul.x)
			return false;
		else if (p.x >= lr.x - 1)
			return false;
		if (p.y <= ul.y)
			return false;
		else if (p.y >= lr.y - 1)
			return false;
		return true;
	}

	@Override
	public void render3D() {
		_currCamera.multMatrix();
		_stage.render3D();
		glColor3f(1, 0, 0);
		_model.renderGeometries();
	}

	@Override
	public void render2D() {}

	/**
	 * Given a mouse position, generates a 3d ray and intersects with the projection plane,
	 * returning the point of intersection
	 */
	public Point traceMouseClick(float x, float y) {

		glLoadIdentity();
		_currCamera.multMatrix(); // keep matrices up to date

		FloatBuffer nearPlanePos = BufferUtils.createFloatBuffer(4);
		FloatBuffer farPlanePos = BufferUtils.createFloatBuffer(4);
		FloatBuffer model = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		IntBuffer viewport = BufferUtils.createIntBuffer(16);

		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, model);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

		GLU.gluUnProject(x, y, 0.0f, model, projection, viewport, nearPlanePos);
		GLU.gluUnProject(x, y, 1.0f, model, projection, viewport, farPlanePos);

		// initial p will be near plane pos
		Vector4 p = new Vector4(nearPlanePos.get(0), nearPlanePos.get(1), nearPlanePos.get(2), 0);

		// direction vector:
		Vector4 d = new Vector4(farPlanePos.get(0) - p.x, farPlanePos.get(1) - p.y, farPlanePos.get(2) - p.z, 0);
		d = d.getNormalized();

		// solve for t
		float t = -(p.z) / d.z;

		Vector4 proj = new Vector4(p.x + d.x * t, p.y + d.y * t, p.z + d.z * t, 0);

		// points off by a factor of two

		Point point = null;
		try {
			point = VEC_SPACE_3D.point(GENSYM.generate(), new MDouble((double) proj.x), new MDouble(
					(double) proj.y), new MDouble((double) proj.z));
		} catch (GeometricFailure e) {
			e.printStackTrace();
		}
		return point;
	}

	/**
	 * Given an intersection point p, checks for intersections with any object in the scene
	 */

	public void checkIntersections(Point p) {
		// may do initial bounds checking here...
		_model.checkIntersections(p, _shiftDown);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		Point p = this.traceMouseClick(e.location.x, (this.getSize().y - e.location.y));

		// if in creation mode, add the element (will add a step to put in right bucket):
		if (_mode.equalsIgnoreCase("creation")) {

			_model.addGeometry(p);
			// if shift key down, take care of adding this point to the lines renderable queue and
			// creating a
			// new line as well
			if (_shiftDown) {
				_linePoints[_toUpdate] = p;

				if (_toUpdate == 0)
					_toUpdate = 1;
				else
					_toUpdate = 0;

				// if a point in both locations, make a new line
				if (_linePoints[0] != null && _linePoints[1] != null) {
					try {
						_model.addGeometry(VEC_SPACE_3D.relation(GENSYM.generate(), _linePoints[0], _linePoints[1]));
					} catch (GeometricFailure e1) {
						e1.printStackTrace();
					}
				}
			}
			// if in selection mode, run intersection tests to find out if anything was selected
		}

		else if (_mode.equalsIgnoreCase("selection")) {
			this.checkIntersections(p);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// when this called, set the current location as the old pos
		_currPos = e.location;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		_currPos = e.location;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// get difference between current location and old location and use for translate
		if (_currPos == null)
			return;

		float deltaX = _currPos.x - e.location.x;
		float deltaY = _currPos.y - e.location.y;
		_currPos = e.location;
		_currCamera.mouseMove(deltaX, deltaY, e);
	}

	@Override
	public void mouseWheelScrolled(Vector2f p, int amount) {
		_currCamera.mouseWheel(amount / 6f);
	}

	@Override
	public void keyPressed(int key) {
		if (key == Keyboard.KEY_SPACE) { // flip through list of cameras
			_currCamera = _cameras.get(_camIndex % _cameras.size());
			_camIndex += 1;
		}
		if (key == 42)
			_shiftDown = true;
		if (key == 31 && _mode.equalsIgnoreCase("creation"))
			_mode = "selection";
		if (key == 46 && _mode.equalsIgnoreCase("selection"))
			_mode = "creation";
		if (key == 14) // delete
			_model.deleteSelections();
	}

	@Override
	public void keyReleased(int key) {
		if (key == 42) { // shift
			_shiftDown = false;
			_linePoints[0] = null;
			_linePoints[1] = null;
			_toUpdate = 0;
		}
	}

	@Override
	public ResizeType getResizeType() {
		return ResizeType.RATIO;
	}

	@Override
	public void resize(Vector2f newSize) {
		Vector2f size = new Vector2f();
		Vector2f.sub(lr, ul, size);

		Vector2f.add(ul, newSize, lr);
	}

}
