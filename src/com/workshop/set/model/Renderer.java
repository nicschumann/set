package com.workshop.set.model;

import com.workshop.set.model.interfaces.Model;
import com.workshop.set.view.SetScreen;
import glfrontend.components.Vector4;
import org.lwjgl.util.glu.Sphere;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glTranslated;


public class Renderer implements Model {
    public Renderer(
        Set<VectorSpace.Geometry> _currentElements,
        Set<VectorSpace.Geometry> _currentSelections
    ) {
        this._currentElements = _currentElements;
        this._currentSelections = _currentSelections;
    }

    private SetScreen _screen;

    private Set<VectorSpace.Geometry> _currentElements;
    private Set<VectorSpace.Geometry> _currentSelections;

    private final float[] _color = { 0f, 0f, 0f };
    private final float[] _colorH = { 1f, 1f, 1f };

    public void setScreen(SetScreen screen) {
        _screen = screen;
    }

    @Override
    public void addGeometry(VectorSpace.Geometry g) {
        _currentElements.add(g);
    }

    @Override
    public void removeGeometry(VectorSpace.Geometry g) {
        _currentElements.remove(g);
    }

    @Override
    public void removeGeometryAll(VectorSpace.Geometry g) {
        _currentElements.remove(g);
        Set<VectorSpace.Geometry> geoms = g.getGeometries();
        for (VectorSpace.Geometry geom : geoms) {
            removeGeometryAll(geom);
        }
    }

    @Override
    public void renderGeometries() {
        for (VectorSpace.Geometry geom : _currentElements) {
            drawGeometry(geom, false);
        }
    }

    private void drawGeometry(VectorSpace.Geometry geom, boolean pivot) {
        Set<VectorSpace.Geometry> geoms = geom.getGeometries();

        // point
        if (geoms.isEmpty()) {
            drawPoint((VectorSpace.Point) geom, pivot);
            return;
        }

        drawRelation((VectorSpace.Relation) geom, pivot);
        return;
    }

    private void drawRelation(VectorSpace.Relation geoms, boolean pivot) {
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

    private void drawPoint(VectorSpace.Point p, boolean pivot) {
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

        } catch (VectorSpace.GeometricFailure e) {
            e.printStackTrace();
        }
    }

    public void deselectAll() {
        for (VectorSpace.Geometry elt : _currentSelections) {
            elt.setHighlight(false);
        }
        _currentSelections.clear();
    }

    @Override
    public void deleteSelections() {
        for (VectorSpace.Geometry elt : _currentSelections) {
            _currentElements.remove(elt);
        }
        _currentSelections.clear();
    }

    @Override
    public void checkIntersections(VectorSpace.Point elmt, boolean shift) {

        boolean intersected = false;
        boolean selected = false;

        for (VectorSpace.Geometry element : _currentElements) {
            double[] pts = element.getPointArray();

            if (pts.length == 3) // point
                intersected = this.checkPtIntersection(elmt, pts);
            else if (pts.length == 6) // line
                intersected = this.checkLineIntersection(elmt, pts);

            if (intersected) {
                if (!shift) {// if no shift, must first empty previous selections
                    this.deselectAll();
                }
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

    public boolean checkPtIntersection(VectorSpace.Point toCheck, double[] oldLoc) {
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

    public boolean checkLineIntersection(VectorSpace.Point check, double[] pts) {

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
