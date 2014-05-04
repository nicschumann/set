package com.workshop.set.model.interfaces;

import java.util.List;

import com.workshop.set.model.VectorSpace.Geometry;
import com.workshop.set.model.VectorSpace.Point;
import com.workshop.set.view.SetScreen;

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

	public void addGeometry(Geometry g);

	public void removeGeometry(Geometry g);

	public void removeGeometryAll(Geometry g);

	public void renderGeometries();

	public void checkIntersections(Point elmt, boolean shift, boolean pivot);

	public void deleteSelections();

	public void setScreen(SetScreen main);

	public void createConstraint(Function type);

	public void update();

	public List<Function> getFunctions();

}
