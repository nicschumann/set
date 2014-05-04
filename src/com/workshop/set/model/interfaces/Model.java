package com.workshop.set.model.interfaces;

import com.workshop.set.model.geometry.VectorSpace.Geometry;
import com.workshop.set.model.geometry.VectorSpace.Point;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.view.SetScreen;

public interface Model {
	
	public void addGeometry(Geometry g) throws ProofFailureException, TypecheckingException;

	public void removeGeometry(Geometry g) throws ProofFailureException, TypecheckingException;

	public void removeGeometryAll(Geometry g) throws ProofFailureException, TypecheckingException;
	
	public void renderGeometries();

	public void checkIntersections(Point elmt, boolean shift, boolean pivot);
	
	public void deleteSelections();

	public void setScreen(SetScreen main);
	
	public void createConstraint(String type);

	public void update();

}
