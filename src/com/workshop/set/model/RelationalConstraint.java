package com.workshop.set.model;

import java.util.ArrayList;
import java.util.Set;

import com.workshop.set.model.VectorSpace.GeometricFailure;
import com.workshop.set.model.VectorSpace.Point;

public class RelationalConstraint implements Constraint {

	ArrayList<LinearEquation> _equations;  
	
	//gets the pivot and the orbit, indices of interest, and canonical type to create linear equation(s)
	public RelationalConstraint(ArrayList<Point> pivots, ArrayList<Point> orbits, Set<Integer> indices, String relation) throws GeometricFailure{
				
		_equations = new ArrayList<LinearEquation>();
		
		for (int i=0; i<pivots.size(); i++){
			Point pivot = pivots.get(i);
			Point orbit = orbits.get(i);
			
			//System.out.println("Pivot's x : " + pivot.getN_(2).get());
			
			for (int index : indices){
				
				//System.out.println("Index : " + index);
				//index of interest to be constrained between pivot and orbit in "relation" way
				_equations.add(new LinearEquation(pivot, orbit, index, relation));
			}
			
			//add this constraint to the orbit in question	
			
			System.out.println("adding this constraint");
			orbit.addConstraint(this);
		}
	}
	
	@Override
	public boolean apply() {
		//solve each linear equation and change corresponding values of orbit
		boolean noConflict=true;
		boolean toRet=true; 
		
		for(LinearEquation le : _equations){
			noConflict=le.solve();
			if(!noConflict)
				toRet=false;
		}
		
		return toRet;	//returns false if there were some issues
	}

}
