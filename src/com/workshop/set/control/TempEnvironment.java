package com.workshop.set.control;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glColor3f;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.util.glu.Sphere;

import com.workshop.set.model.VectorSpace.GeometricFailure;
import com.workshop.set.model.VectorSpace.Geometry;
import com.workshop.set.model.VectorSpace.Point;
import com.workshop.set.model.VectorSpace.Relation;
import com.workshop.set.model.interfaces.Model;

public class TempEnvironment implements Model {

	private Set<Geometry> _currentElements;

	public TempEnvironment() {
		_currentElements = new HashSet<>();
	}
	
	@Override
	public void addGeometry(Geometry g) {
		_currentElements.add(g);
		Set<Geometry> geoms = g.getGeometries();
		for (Geometry geom : geoms) {
			_currentElements.remove(geom);
		}
	}

	@Override
	public void removeGeometry(Geometry g) {
		_currentElements.remove(g);
		Set<Geometry> geoms = g.getGeometries();
		for (Geometry geom : geoms) {
			_currentElements.add(geom);
		}
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
		for (Geometry g : geoms) {
			depth = drawGeometry(g) + 1;
		}
		drawRelation((Relation) geom, depth);
		return depth;
	}

	private void drawRelation(Relation geoms, int depth) {
		double[] pnts = geoms.getPointArray();
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

			if(p.getHighlight())
				glColor3f(0,1,0);
			else
				glColor3f(1,0,0);
			
			glTranslated(x, y, z);
			new Sphere().draw(.08f, 10, 10);
			glTranslated(-x, -y, -z);

		} catch (GeometricFailure e) {
			e.printStackTrace();
		}
	}

	@Override
	public void checkIntersections(Point elmt) {		
		//for now, just a naive iterative check. (improve later with bounding volumes)
		//if get an intersection, add the item to list of selected objects (to be used for constraints or deletions)
		boolean intersected;
		
		for (Geometry element : _currentElements){
			double[] pts = element.getPointArray();

			// point
			if (pts.length==3) {
				intersected = this.checkPtIntersection(elmt, pts);
				if(intersected){
					//highlight and add to selected items
					element.setHighlight(true);
				}
			}
			else if (pts.length==6){
				//check intersection with relation 
			}	
		}	
	}
	
	public boolean checkPtIntersection(Point toCheck, double[] oldLoc){
		double[] newLoc = toCheck.getPointArray(); 
		double dist = Math.sqrt((oldLoc[0]-newLoc[0])*(oldLoc[0]-newLoc[0]) + (oldLoc[1]-newLoc[1])*(oldLoc[1]-newLoc[1]) + (oldLoc[2]-newLoc[2])*(oldLoc[2]-newLoc[2]));
		return(dist<=.08);
	}
	
	public boolean checkLineIntersection(){
		return false; 
	}
	
	
	
}
