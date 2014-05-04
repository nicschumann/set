package com.workshop.set.model.interfaces;


import com.workshop.set.model.geometry.VectorSpace.Geometry;
import com.workshop.set.model.geometry.VectorSpace.Point;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.view.SetScreen;

import java.util.List;

public interface Model {

	public enum Function {
		X_VAL_EQUAL("Set X Equal"),
		Y_VAL_EQUAL("Set Y Equal"),
		Z_VAL_EQUAL("Set Z Equal"),
		PARALLEL("Set Lines Parallel");

		public final String buttonText;

		private Function(String buttonText) {
			this.buttonText = buttonText;
		}
	}

    public void addGeometry(Geometry g) throws ProofFailureException, TypecheckingException;

	public void removeGeometry(Geometry g) throws ProofFailureException, TypecheckingException;

	public void removeGeometryAll(Geometry g) throws ProofFailureException, TypecheckingException;

	public void renderGeometries();

	public void checkIntersections(Point elmt, boolean shift, boolean pivot);

	public void deleteSelections();

	public void setScreen(SetScreen main);

	public void createConstraint(Function type);

	public void update();

	public List<Function> getFunctions();

}
