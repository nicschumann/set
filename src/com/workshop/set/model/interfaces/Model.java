package com.workshop.set.model.interfaces;

import com.workshop.set.model.VectorSpace.Geometry;
import com.workshop.set.model.VectorSpace.Point;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.view.SetScreen;

public interface Model {
	
	public void addGeometry(Geometry g) throws ProofFailureException, TypecheckingException;

	public void removeGeometry(Geometry g);

	public void removeGeometryAll(Geometry g);
	
	public void renderGeometries();

	public void checkIntersections(Point elmt, boolean shift);
	
	public void deleteSelections();

	public void setScreen(SetScreen main);

}
