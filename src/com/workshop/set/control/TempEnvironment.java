package com.workshop.set.control;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex3d;
import glfrontend.components.Vector4;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.util.glu.Sphere;

import com.workshop.set.model.VectorSpace.GeometricFailure;
import com.workshop.set.model.VectorSpace.Geometry;
import com.workshop.set.model.VectorSpace.Point;
import com.workshop.set.model.VectorSpace.Relation;
import com.workshop.set.model.interfaces.Model;
import com.workshop.set.view.SetScreen;

public class TempEnvironment implements Model {

	private SetScreen _screen;

	private Set<Geometry> _currentElements;
	private Set<Geometry> _currentSelections;

	private final float[] _color = { 0f, 0f, 0f };
	private final float[] _colorH = { 1f, 1f, 1f };

	public TempEnvironment() {
		_currentElements = new HashSet<>();
		_currentSelections = new HashSet<>();
	}

	public void setScreen(SetScreen screen) {
		_screen = screen;
	}

	@Override
	public void addGeometry(Geometry g) {
		_currentElements.add(g);
	}

	@Override
	public void removeGeometry(Geometry g) {
		_currentElements.remove(g);
	}

	@Override
	public void removeGeometryAll(Geometry g) {
		_currentElements.remove(g);
		Set<Geometry> geoms = g.getGeometries();
		for (Geometry geom : geoms) {
			removeGeometryAll(geom);
		}
	}

	@Override
	public void renderGeometries() {
		for (Geometry geom : _currentElements) {
			drawGeometry(geom);
		}
	}

	private int drawGeometry(Geometry geom) {
		Set<Geometry> geoms = geom.getGeometries();
		int depth = 0;

		// point
		if (geoms.isEmpty()) {
			drawPoint((Point) geom);
			return depth; // 0
		}
		
		drawRelation((Relation) geom, depth);
		return depth;
	}

	private void drawRelation(Relation geoms, int depth) {
		double[] pnts = geoms.getPointArray();
		if (geoms.getHighlight())
			glColor3f(_colorH[0], _colorH[1], _colorH[2]);
		else
			glColor3f(_color[0], _color[1], _color[2]);
		if (pnts.length == 6) {
			glLineWidth(1.2f);
			glEnable(GL_LINE_SMOOTH);
			glBegin(GL_LINES);
			glVertex3d(pnts[0], pnts[1], pnts[2]);
			glVertex3d(pnts[3], pnts[4], pnts[5]);
			glEnd();
		} else if (pnts.length == 12) {
			// TODO: draw plane
		}
	}

	private void drawPoint(Point p) {
		try {
			double x = p.getN_(1).get();
			double y = p.getN_(2).get();
			double z = p.getN_(3).get();

			if (p.getHighlight())
				glColor3f(_colorH[0], _colorH[1], _colorH[2]);
			else
				glColor3f(_color[0], _color[1], _color[2]);

			glTranslated(x, y, z);
			new Sphere().draw(.08f, 10, 10);
			glTranslated(-x, -y, -z);

		} catch (GeometricFailure e) {
			e.printStackTrace();
		}
	}

	public void deselectAll() {
		for (Geometry elt : _currentSelections) {
			elt.setHighlight(false);
		}
		_currentSelections.clear();
	}

	@Override
	public void deleteSelections() {
		for (Geometry elt : _currentSelections) {
			_currentElements.remove(elt);
		}
		_currentSelections.clear();
	}

	@Override
	public void checkIntersections(Point elmt, boolean shift) {

		boolean intersected = false;
		boolean selected = false;

		for (Geometry element : _currentElements) {
			double[] pts = element.getPointArray();

			if (pts.length == 3) // point
				intersected = this.checkPtIntersection(elmt, pts);
			else if (pts.length == 6) // line
				intersected = this.checkLineIntersection(elmt, pts);

			if (intersected) {
				if (!shift) // if no shift, must first empty previous selections
					this.deselectAll();
				element.setHighlight(true);
				_currentSelections.add(element);
				_screen.displaySelected(element);
				selected = true;
			}
		}

		// if no object was selected deselect all items
		if (!selected) {
			this.deselectAll();
			_screen.removeSelection();
		}
	}

	public boolean checkPtIntersection(Point toCheck, double[] oldLoc) {
		double[] newLoc = toCheck.getPointArray();
		double dist = Math.sqrt((oldLoc[0] - newLoc[0]) * (oldLoc[0] - newLoc[0]) + (oldLoc[1] - newLoc[1])
				* (oldLoc[1] - newLoc[1]) + (oldLoc[2] - newLoc[2]) * (oldLoc[2] - newLoc[2]));
		return (dist <= .08);
	}

	/**
	 * returns whether two values are "close enough" or equal within epsilon
	 */
	public boolean equalsWithinEps(double val1, double val2) {
		return (Math.abs(val1 - val2)) < .15;
	}

	public boolean checkLineIntersection(Point check, double[] pts) {

		double[] toCheck = check.getPointArray();
		// direction of line
		Vector4 d = new Vector4((float) (pts[0] - pts[3]), (float) (pts[1] - pts[4]), (float) (pts[2] - pts[5]), 0f)
				.getNormalized();

		// solve linear system for the results toCheck.x,toCheck.y (toCheck.z always 0)
		double t1 = (toCheck[0] - pts[0]) / d.x;
		double t2 = (toCheck[1] - pts[1]) / d.y;

		double maxX = Math.max(pts[0], pts[3]) - .08;
		double minX = Math.min(pts[0], pts[3]) + .08;
		double maxY = Math.max(pts[1], pts[4]) - .08;
		double minY = Math.min(pts[1], pts[4]) + .08;

		// if t values are "close enough, and between clipping bounds then point is on the line
		return (equalsWithinEps(t1, t2) && toCheck[0] <= maxX && toCheck[0] >= minX && toCheck[1] <= maxY && toCheck[1] >= minY);
	}
}
