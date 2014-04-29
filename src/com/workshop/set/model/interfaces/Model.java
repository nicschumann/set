package com.workshop.set.model.interfaces;

import java.util.Set;

import glfrontend.components.GeometricElement;

import com.workshop.set.model.VectorSpace.Geometry;
import com.workshop.set.model.VectorSpace.Point;

public interface Model {
	
	public void drawGeometricElements();
	
	public boolean addElement(GeometricElement elmt);
	
	public boolean removeElement(GeometricElement elmt);
	
	public void addGeometry(Geometry g);

	public void removeGeometry(Geometry g);

	public void removeGeometryAll(Geometry g);
	
	public void renderGeometries();

	public void checkIntersections(glfrontend.components.Point elmt);

}
