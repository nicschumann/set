package com.workshop.set.model.interfaces;

import com.workshop.set.model.VectorSpace.Geometry;
import com.workshop.set.model.VectorSpace.Point;
import com.workshop.set.view.SetScreen;

public interface Model {
	
	public void addGeometry(Geometry g);

	public void removeGeometry(Geometry g);

	public void removeGeometryAll(Geometry g);
	
	public void renderGeometries();

	public void checkIntersections(Point elmt, boolean shift, boolean pivot);
	
	public void deleteSelections();

	public void setScreen(SetScreen main);

}
