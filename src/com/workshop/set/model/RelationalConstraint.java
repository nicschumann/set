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
		
		
		if(relation.equalsIgnoreCase("equality")){
		for (int i=0; i<pivots.size(); i++){
			Point pivot = pivots.get(i);
			Point orbit = orbits.get(i);
			
			for (int index : indices){
				//index of interest to be constrained between pivot and orbit in "relation" way
				_equations.add(new LinearEquation(pivot, orbit, index, relation));
			}
			orbit.addConstraint(this);
		}
		}
		
		else if(relation.equalsIgnoreCase("slope_equality")){
			System.out.println("relate the new lines by slope");
			
			
			//set up equality constraints for slope (decide the pivot based on which values are locked
			//in the second line 
			
			//get the pivot slope and use to set up equality equations 
			Point p1 = pivots.get(0);
			Point p2 = pivots.get(1);
			
			
			
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
