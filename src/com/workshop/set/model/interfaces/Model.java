package com.workshop.set.model.interfaces;

import com.workshop.set.model.VectorSpace.Geometry;

public interface Model {
	
	public void addGeometry(Geometry g);

	public void removeGeometry(Geometry g);

	public void removeGeometryAll(Geometry g);
	
	public void renderGeometries();

}
