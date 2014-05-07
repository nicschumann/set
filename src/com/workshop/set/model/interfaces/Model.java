package com.workshop.set.model.interfaces;


import java.util.List;

import static org.lwjgl.input.Keyboard.*;
import org.lwjgl.util.vector.Vector3f;

import com.workshop.set.model.geometry.VectorSpace.GeometricFailure;
import com.workshop.set.model.geometry.VectorSpace.Geometry;
import com.workshop.set.model.geometry.VectorSpace.Point;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.view.SetScreen;

public interface Model {

	public enum Function {

		X_VAL_EQUAL("Set X Equal (X)", KEY_X, true),
		Y_VAL_EQUAL("Set Y Equal (Y)", KEY_Y, true),
		Z_VAL_EQUAL("Set Z Equal (Z)", KEY_Z, true),
		PARALLEL("Set Lines Parallel (L)", KEY_L, true),
		PERPENDICULAR("Set Lines Perpendicular (X)", KEY_X, true),
		
		SET_PIVOT("Set As Pivot (P)", KEY_P, false),
        CREATE_RELATION("Create Relation (R)", KEY_R, false),
        TERM( null, KEY_T, true );

		public String buttonText;
        private Term value;
        public final int key;
		public final boolean isConstraint;

		private Function(String buttonText, int key, boolean constraint) {
			this.buttonText = buttonText;
			this.key = key;
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
	
	public Geometry getIntersection(Point elmt);

	public void deleteSelections();

	public void setScreen(SetScreen main);

	public void createConstraint(Function type);
	
	public void executeFunction(Function type) throws GeometricFailure;

	public void update();

	public List<Function> getFunctions();
	
	public void executeRayCast(Vector3f A, Vector3f B, boolean shift, boolean pivot, Point p);

	public Geometry getGeometry(Vector3f A, Vector3f V, Point p);


}
