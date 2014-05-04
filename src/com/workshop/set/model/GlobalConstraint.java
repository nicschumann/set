package com.workshop.set.model;

import com.workshop.set.model.geometry.VectorSpace.Geometry;

public class GlobalConstraint implements Constraint{

	Geometry _g; 
	
	
	
	public boolean apply(){
		return false; 
	}
}
