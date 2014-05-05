package com.workshop.set.model.interfaces;


import com.workshop.set.model.geometry.VectorSpace.GeometricFailure;
import com.workshop.set.model.geometry.VectorSpace.Geometry;
import com.workshop.set.model.geometry.VectorSpace.Point;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.view.SetScreen;

import java.util.List;

public interface Model {

	public enum Function {
<<<<<<< HEAD
		X_VAL_EQUAL("Set X Equal"),
		Y_VAL_EQUAL("Set Y Equal"),
		Z_VAL_EQUAL("Set Z Equal"),
		PARALLEL("Set Lines Parallel"),
        TERM( null );

		public String buttonText;
        private Term value;
=======
		X_VAL_EQUAL("Set X Equal", true),
		Y_VAL_EQUAL("Set Y Equal", true),
		Z_VAL_EQUAL("Set Z Equal", true),
		PARALLEL("Set Lines Parallel", true),
		
		SET_PIVOT("Set As Pivot", false),
		CREATE_RELATION("Create Relation", false);

		public final String buttonText;
		public final boolean isConstraint;
>>>>>>> master

		private Function(String buttonText, boolean constraint) {
			this.buttonText = buttonText;
			this.isConstraint = constraint;
		}

        public void setTerm( Term t ) { value = t; }
        public Term getTerm( ) { return value; }
        public void setString( String name ) { buttonText = name; }
	}

    public void addGeometry(Geometry g) throws ProofFailureException, TypecheckingException;

	public void removeGeometry(Geometry g) throws ProofFailureException, TypecheckingException;

	public void removeGeometryAll(Geometry g) throws ProofFailureException, TypecheckingException;

	public void renderGeometries();

	public void checkIntersections(Point elmt, boolean shift, boolean pivot);

	public void deleteSelections();

	public void setScreen(SetScreen main);

	public void createConstraint(Function type);
	
	public void executeFunction(Function type) throws GeometricFailure;

	public void update();

	public List<Function> getFunctions();

}
