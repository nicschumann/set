package com.workshop.set.model;

import com.workshop.set.model.VectorSpace.GeometricFailure;
import com.workshop.set.model.VectorSpace.Point;
import com.workshop.set.model.ref.MDouble;

public class LinearEquation {
	
	//will this ever not be a pairwise relation?
	//constraints only exist between points which comes down to some sort of pairwise matching it seems
	
	MDouble _known, _unknown; 
	Point _pivot, _orbit; 
	String _relation; 
	
	//keeps track purely of numbers and variables in n space and solves the equation 

	public LinearEquation(Point pivot, Point orbit, int index, String relation) throws GeometricFailure{
		_pivot = pivot; 
		_orbit = orbit; 
		_relation = relation; 
		if(relation.equalsIgnoreCase("equality")){
			//System.out.println("set up to solve an equality relation");
			_known = pivot.getN_(index+1);
			_unknown = orbit.getN_(index+1);
		}
		
	}
	
	public boolean solve(){
		//always solving the equation with respect to 0
		
		//solve and modify the actual instance of this geometry. return true if successful
		//if geometry was already constrained, then this is overconstrained. return false
		
		if(_relation.equalsIgnoreCase("equality")){
			//update unknown to be the opposite of known (solving equality)
			
			System.out.println("Should be setting to this value: " + _known.get()*-1);
			_unknown.set(-1*_known.get());
			return true; 
		}
	
		
		return false; 

	}
}
