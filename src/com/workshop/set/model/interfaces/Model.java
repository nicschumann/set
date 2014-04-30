package com.workshop.set.model.interfaces;

import java.util.Set;
import com.workshop.set.model.VectorSpace.Geometry;
import com.workshop.set.model.VectorSpace.Point;

public interface Model {
	
	public void addGeometry(Geometry g);

	public void removeGeometry(Geometry g);

	public void removeGeometryAll(Geometry g);
	
	public void renderGeometries();

	public void checkIntersections(Point elmt, boolean shift);
	
	public void deleteSelections();

}
