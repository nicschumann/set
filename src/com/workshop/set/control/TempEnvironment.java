package com.workshop.set.control;

import static com.workshop.set.SetMain.GENSYM;
import static com.workshop.set.SetMain.VEC_SPACE_3D;
import static com.workshop.set.model.interfaces.Model.Function.CREATE_RELATION;
import static com.workshop.set.model.interfaces.Model.Function.PARALLEL;
import static com.workshop.set.model.interfaces.Model.Function.SET_PIVOT;
import static com.workshop.set.model.interfaces.Model.Function.X_VAL_EQUAL;
import static com.workshop.set.model.interfaces.Model.Function.Y_VAL_EQUAL;
import static com.workshop.set.model.interfaces.Model.Function.Z_VAL_EQUAL;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.util.glu.Sphere;

import com.workshop.set.model.Constraint;
import com.workshop.set.model.RelationalConstraint;
import com.workshop.set.model.geometry.VectorSpace.GeometricFailure;
import com.workshop.set.model.geometry.VectorSpace.Geometry;
import com.workshop.set.model.geometry.VectorSpace.Point;
import com.workshop.set.model.geometry.VectorSpace.Relation;
import com.workshop.set.model.interfaces.Model;
import com.workshop.set.view.SetScreen;

public class TempEnvironment implements Model {

	private SetScreen _screen;

	private Set<Geometry> _currentElements;
	private List<Geometry> _currentSelections;

	private final float[] _color = { 0f, 0f, 0f };
	private final float[] _colorH = { 1f, 1f, 1f };
	private final float[] _pivotColor = { 1f, 0.5f, 0 };

	public TempEnvironment(HashSet<Geometry> _currentElements, List<Geometry> _currentSelections) {
		this._currentElements = _currentElements;
		this._currentSelections = _currentSelections;
	}

	public TempEnvironment() {
		this._currentElements = new HashSet<>();
		this._currentSelections = new ArrayList<>();
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
			drawGeometry(geom, false);
		}
	}

	private void drawGeometry(Geometry geom, boolean pivot) {
		Set<Geometry> geoms = geom.getGeometries();

		// point
		if (geoms.isEmpty()) {
			drawPoint((Point) geom, pivot);
			return;
		}

		drawRelation((Relation) geom, pivot);
		return;
	}

	private void drawRelation(Relation geoms, boolean pivot) {
		double[] pnts = geoms.getPointArray();

		if (geoms.isPivot())
			glColor3f(_pivotColor[0], _pivotColor[1], _pivotColor[2]);
		else if (geoms.getHighlight())
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

	private void drawPoint(Point p, boolean pivot) {

		p.applyConstraints();

		try {
			double x = p.getN_(1).get();
			double y = p.getN_(2).get();
			double z = p.getN_(3).get();

			if (p.isPivot())
				glColor3f(_pivotColor[0], _pivotColor[1], _pivotColor[2]);
			else if (p.getHighlight())
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
			elt.setPivot(false); // can only stop being a pivot if deselected without being used
		}
		_currentSelections.clear();
	}

	@Override
	public void deleteSelections() {
		for (Geometry elt : _currentSelections) {
			_currentElements.remove(elt);
		}
		_currentSelections.clear();
		_screen.removeSelections(true);
	}

	/**
	 * Given specification of type of constraint, applies it to the current selections It should be
	 * possible because only possible functions are offered
	 */
	public void createConstraint(Function type) {

		ArrayList<Point> pivots = new ArrayList<Point>();
		ArrayList<Point> orbits = new ArrayList<Point>();
		Set<Integer> indices = new HashSet<Integer>();
		String relation = "";

		// parse out the points of interest and send them
		for (Geometry g : _currentSelections) {

			Set<Geometry> geom = g.getGeometries();
			if (geom.isEmpty()) {
				// add the point to the correct list.
				if (g.isPivot())
					pivots.add((Point) g);
				else
					orbits.add((Point) g);
			} else {
				// for each element, to appropriate list
				for (Geometry elt : geom) {
					if (g.isPivot())
						pivots.add((Point) elt);
					else
						orbits.add((Point) elt);
				}
			}
		}

		switch (type) {
		case X_VAL_EQUAL:
			indices.add(0);
			relation = "equality";
			break;
		case Y_VAL_EQUAL:
			indices.add(1);
			relation = "equality";
			break;
		case Z_VAL_EQUAL:
			indices.add(2);
			relation = "equality";
			break;
		case PARALLEL:
			relation = "slope_equality";
			break;
		default:
			break;
		}

		try {
			Constraint c = new RelationalConstraint(pivots, orbits, indices, relation);

			// add to master list for bookkeeping?
		} catch (GeometricFailure e) {
			e.printStackTrace();
		}
	}

	@Override
	public void checkIntersections(Point elmt, boolean shift, boolean pivot) {

		boolean intersected = false;
		boolean selected = false;

		for (Geometry element : _currentElements) {
			double[] pts = element.getPointArray();

			if (pts.length == 3) // point
				intersected = this.checkPtIntersection(elmt, pts);
			else if (pts.length == 6) // line
				intersected = this.checkLineIntersection(elmt, pts);

			if (intersected) {
				if (!shift) {// if no shift, must first empty previous selections
					this.deselectAll();
					_screen.removeSelections(false);
				}
				if (pivot) {
					element.setPivot(true); // becomes a pivot if selected with pivot down
				}
				if (!_currentSelections.contains(element)) {
					element.setHighlight(true);
					_currentSelections.add(element);
					_screen.displaySelected(element);
				} else if (shift) {
//					element.setHighlight(false);
//					_currentSelections.remove(element);
//					_screen.removeSelection(element);
				}
				selected = true;

				// update the possible functions to apply display
			}
		}

		// if no object was selected deselect all items
		if (!selected) {
			this.deselectAll();
			_screen.removeSelections(true);
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
	public boolean equalsWithinEps(double val1, double val2, double eps) {
		return (Math.abs(val1 - val2)) < eps;
	}

	public boolean checkLineIntersection(Point check, double[] pts) {

		double[] toCheck = check.getPointArray();
		boolean inBounds = false;

		// line's direction vector
		Vector4 d = new Vector4((float) (pts[0] - pts[3]), (float) (pts[1] - pts[4]), (float) (pts[2] - pts[5]), 0f)
				.getNormalized();

		double waneX = .08; // radius of spheres rendering
		double waneY = .08;

		if (d.x < .12)
			waneX = -.03; // give selection leeway to vertical/horizontal lines
		if (d.y < .12)
			waneY = -.03;

		double maxX = Math.max(pts[0], pts[3]) - waneX;
		double minX = Math.min(pts[0], pts[3]) + waneX;
		double maxY = Math.max(pts[1], pts[4]) - waneY;
		double minY = Math.min(pts[1], pts[4]) + waneY;

		if (toCheck[0] <= maxX && toCheck[0] >= minX && toCheck[1] <= maxY && toCheck[1] >= minY)
			inBounds = true;

		if (equalsWithinEps(d.x, 0, .02)) // vertical line
			return (equalsWithinEps(toCheck[0], pts[0], .1) && inBounds);

		else if (equalsWithinEps(d.y, 0, .02)) // horizontal line
			return (equalsWithinEps(toCheck[1], pts[1], .1) && inBounds);

		else {
			// solve linear system for the results toCheck.x,toCheck.y (toCheck.z always 0)
			double t1 = (toCheck[0] - pts[0]) / d.x;
			double t2 = (toCheck[1] - pts[1]) / d.y;

			return (equalsWithinEps(t1, t2, .1) && inBounds);
		}

	}

	@Override
	public void update() {
		for (Geometry geom : _currentElements) {
			geom.applyConstraints();
		}
	}

	@Override
	public List<Function> getFunctions() {
		List<Function> functions = new ArrayList<>();
		if (_currentSelections.size() == 1) {
			functions.add(SET_PIVOT);
		} else if (_currentSelections.size() == 2) {
			int counter = 0;
			boolean hasPivot = false;
			for (Geometry geom : _currentSelections) {
				if (geom.isPivot())
					hasPivot = true;
				if (geom instanceof Point) {
					if (counter < 0)
						return functions;
					counter++;
				} else {
					if (counter > 0)
						return functions;
					counter--;
				}
			}
			if (!hasPivot) {
				functions.add(CREATE_RELATION);
			} else if (counter > 0) {
				functions.add(X_VAL_EQUAL);
				functions.add(Y_VAL_EQUAL);
				functions.add(Z_VAL_EQUAL);
			} else if (counter < 0) {
				functions.add(PARALLEL);
			}
		}
		return functions;
	}

	@Override
	public void executeFunction(Function function) throws GeometricFailure {
		switch (function) {
		case SET_PIVOT:
			_currentSelections.get(0).setPivot(true);
			break;
		case CREATE_RELATION:
			Relation r = VEC_SPACE_3D.relation(GENSYM.generate(), _currentSelections.get(0), _currentSelections.get(1));
			_currentElements.add(r);
			break;
		default:
			break;
		}
	}
}
