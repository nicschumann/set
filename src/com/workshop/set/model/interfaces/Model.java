package com.workshop.set.model.interfaces;

import com.workshop.set.model.geometry.VectorSpace.Geometry;
import com.workshop.set.model.geometry.VectorSpace.Point;
import com.workshop.set.view.SetScreen;

public interface Model {
	
	public void addGeometry(Geometry g);

	public void removeGeometry(Geometry g);

	public void removeGeometryAll(Geometry g);
	
	public void renderGeometries();

	public void checkIntersections(Point elmt, boolean shift, boolean pivot);
	
	public void deleteSelections();

	public void setScreen(SetScreen main);
	
	public void createConstraint(String type);

	public void update();

}
