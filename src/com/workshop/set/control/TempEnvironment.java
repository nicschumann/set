package com.workshop.set.control;

import static com.workshop.set.SetMain.GENSYM;
import static com.workshop.set.SetMain.VEC_SPACE_3D;
import static com.workshop.set.model.interfaces.Model.Function.CREATE_RELATION;
import static com.workshop.set.model.interfaces.Model.Function.PARALLEL;
import static com.workshop.set.model.interfaces.Model.Function.PERPENDICULAR;
import static com.workshop.set.model.interfaces.Model.Function.SET_PIVOT;
import static com.workshop.set.model.interfaces.Model.Function.X_VAL_EQUAL;
import static com.workshop.set.model.interfaces.Model.Function.Y_VAL_EQUAL;
import static com.workshop.set.model.interfaces.Model.Function.Z_VAL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_QUADS;
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
import org.lwjgl.util.vector.Vector3f;

import com.workshop.set.model.Constraint;
import com.workshop.set.model.RelationalConstraint;
import com.workshop.set.model.geometry.VectorSpace.GeometricFailure;
import com.workshop.set.model.geometry.VectorSpace.Geometry;
import com.workshop.set.model.geometry.VectorSpace.Point;
import com.workshop.set.model.geometry.VectorSpace.Relation;
import com.workshop.set.model.interfaces.Model;
import com.workshop.set.view.SetScreen;

public class TempEnvironment implements Model {

	public static final float SELECTION_RADIUS = 0.1f;

	private SetScreen _screen;

	private Set<Geometry> _currentElements;
	private List<Geometry> _currentSelections;
	
//	private Map<Geometry, Constraint> _constraints;

	private final float[] _color = { 0f, 0f, 0f };
	private final float[] _colorH = { 1f, 1f, 1f };
	private final float[] _pivotColor = { 1f, 0.5f, 0 };

	public TempEnvironment(Set<Geometry> _currentElements, List<Geometry> _currentSelections) {
		this._currentElements = _currentElements;
		this._currentSelections = _currentSelections;
//		this._constraints = 
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
        for ( Geometry anc : g.getAncestors() ) {
            ((Relation) anc).domain().setParent( null );
            ((Relation) anc).codomain().setParent( null );
            anc.setParent( null );
            _currentElements.remove( anc );
        }
        g.setParent( null );
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
			glColor3f(0.25f, 0.25f, 0.25f);
			glBegin(GL_QUADS);
			glVertex3d(pnts[0], pnts[1], pnts[2]);
			glVertex3d(pnts[3], pnts[4], pnts[5]);
			glVertex3d(pnts[9], pnts[10], pnts[11]);
			glVertex3d(pnts[6], pnts[7], pnts[8]);
			glEnd();
		}
	}

	private void drawPoint(Point p, boolean pivot) {

		// p.applyConstraints();

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
			removeGeometry( elt );
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
		case PERPENDICULAR:
			relation = "perpendicular";
			break;
		default:
			break;
		}

		try {
			Constraint c = new RelationalConstraint(pivots, orbits, indices, relation);
			
			
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
					// element.setHighlight(false);
					// _currentSelections.remove(element);
					// _screen.removeSelection(element);
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

	@Override
	public Geometry getIntersection(Point elmt) {
		for (Geometry element : _currentElements) {
			double[] pts = element.getPointArray();
			boolean intersected = false;

			if (pts.length == 3) // point
				intersected = this.checkPtIntersection(elmt, pts);
			else if (pts.length == 6) // line
				intersected = this.checkLineIntersection(elmt, pts);

			if (intersected)
				return element;
		}
		return null;
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
		if (_currentSelections.size() == 1 && !_currentSelections.get(0).isPivot()) {
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
				functions.add(PERPENDICULAR);
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
		case TERM:
			break;
		default:
			break;
		}
	}

	@Override
	public void executeRayCast(Vector3f A, Vector3f B, boolean shift, boolean pivot, Point p) {

		boolean selected = false;

		Geometry element;
		if ((element = getGeometry(A, B, p)) != null) {
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
				// element.setHighlight(false);
				// _currentSelections.remove(element);
				// _screen.removeSelection(element);
			}
			selected = true;

			// update the possible functions to apply display
		}

		// if no object was selected deselect all items
		if (!selected) {
			this.deselectAll();
			_screen.removeSelections(true);
		}
	}

	@Override
	public Geometry getGeometry(Vector3f A, Vector3f B, Point p) {

		Geometry selected = null;

		for (Geometry element : _currentElements) {
			if (element instanceof Point) {

				double[] pnts = element.getPointArray();
				Vector3f C = new Vector3f((float) pnts[0], (float) pnts[1], (float) pnts[2]);

				// a
				Vector3f bMinA = new Vector3f();
				Vector3f.sub(B, A, bMinA);
				float a = (bMinA.x * bMinA.x) + (bMinA.y * bMinA.y) + (bMinA.z * bMinA.z);

				// b
				Vector3f aMinC = new Vector3f();
				Vector3f.sub(A, C, aMinC);
				float b = 2 * ((bMinA.x * aMinC.x) + (bMinA.y * aMinC.y) + (bMinA.z * aMinC.z));

				// c
				float c = (aMinC.x * aMinC.x) + (aMinC.y * aMinC.y) + (aMinC.z * aMinC.z) - SELECTION_RADIUS
						* SELECTION_RADIUS;

				if ((b * b - 4 * a * c) >= 0)
					selected = element;
			} else {
				double[] pnts = element.getPointArray();
				if (pnts.length == 6) { // line

					// if (this.checkLineIntersection(p, pnts))
					// return element;
					Vector3f L = new Vector3f((float) pnts[0], (float) pnts[1], (float) pnts[2]);
					Vector3f L2 = new Vector3f((float) pnts[3], (float) pnts[4], (float) pnts[5]);

					Vector3f[] pAndQ = getPQ(L, L2, A, B);
					if (!betweenPoint(L, L2, pAndQ[0])) {
						continue;
					}

					Vector3f PQ = new Vector3f();
					Vector3f.sub(pAndQ[1], pAndQ[0], PQ);

					double d = Math.sqrt(Vector3f.dot(PQ, PQ));
					if (d < SELECTION_RADIUS - 0.02f && selected == null)
						selected = element;

				}
			}
		}
		return selected;
	}

	public static Vector3f[] getPQ(Vector3f L1a, Vector3f L1b, Vector3f L2a, Vector3f L2b) {

		Vector3f dir1 = new Vector3f();
		Vector3f dir2 = new Vector3f();
		Vector3f.sub(L1b, L1a, dir1);
		Vector3f.sub(L2b, L2a, dir2);
		if (L1a.equals(L1b))
			return null;
		dir1.normalise();
		dir2.normalise();

		Vector3f aMinL = new Vector3f();
		Vector3f.sub(L2a, L1a, aMinL);

		float c1 = Vector3f.dot(aMinL, dir1);
		float c2 = Vector3f.dot(dir1, dir2);
		float c3 = Vector3f.dot(dir1, dir1);
		float c4 = Vector3f.dot(aMinL, dir2);
		float c5 = Vector3f.dot(dir2, dir2);
		float c6 = c2;

		float s = (c1 * c6 - c4 * c3) / (c5 * c3 - c6 * c2);
		float t = (s * c2 + c1) / c3;

		Vector3f[] PandQ = { new Vector3f(L1a.x + t * dir1.x, L1a.y + t * dir1.y, L1a.z + t * dir1.z),
				new Vector3f(L2a.x + s * dir2.x, L2a.y + s * dir2.y, L2a.z + s * dir2.z) };
		return PandQ;
	}

	public static boolean betweenPoint(Vector3f p1, Vector3f p2, Vector3f p) {
		if (p1.x < p2.x) {
			if (p.x < p1.x || p.x > p2.x) {
				return false;
			}
		} else {
			if (p.x < p2.x || p.x > p1.x) {
				return false;
			}
		}
		if (p1.y < p2.y) {
			if (p.y < p1.y || p.y > p2.y) {
				return false;
			}
		} else {
			if (p.y < p2.y || p.y > p1.y) {
				return false;
			}
		}
		if (p1.z < p2.z) {
			if (p.z < p1.z || p.z > p2.z) {
				return false;
			}
		} else {
			if (p.z < p2.z || p.z > p1.z) {
				return false;
			}
		}

		return true;
	}

}
