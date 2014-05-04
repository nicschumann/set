package com.workshop.set.model;

import com.workshop.set.model.geometry.VectorSpace.GeometricFailure;
import com.workshop.set.model.geometry.VectorSpace.Point;
import com.workshop.set.model.ref.MDouble;

public class LinearEquation {
		
	MDouble _known, _unknown; 
	String _relation; 
	
	//keeps track purely of numbers and variables in n space and solves the equation 

	public LinearEquation(MDouble known, MDouble unknown, int index, String relation) throws GeometricFailure{
		_relation = relation; 
		//if(relation.equalsIgnoreCase("equality")){
			_known = known;
			_unknown = unknown;
		//}
	}
	
	public boolean solve(){
		//always solving the equation with respect to 0
		
		//solve and modify the actual instance of this geometry. return true if successful
		//if geometry was already constrained, then this is overconstrained. return false
		
		if(_relation.equalsIgnoreCase("equality")){
			_unknown.set(_known.get());
			return true; 
		}
		return false; 
	}
}
