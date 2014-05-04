package com.workshop.set.model;

import java.util.ArrayList;
import java.util.Set;

import com.workshop.set.model.geometry.VectorSpace.GeometricFailure;
import com.workshop.set.model.geometry.VectorSpace.Point;

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
				_equations.add(new LinearEquation(pivot.getN_(index+1), orbit.getN_(index+1), index, relation));
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
			
			double deltaY = p1.getN_(2).get() - p2.getN_(2).get();
			double deltaX = p1.getN_(1).get() - p2.getN_(1).get();
			
			Point o1 = orbits.get(0);
			Point o2 = orbits.get(1);
			
			Point internalOrbit; 
			
			//choose which part of the orbit is not locked and use that as internal pivot
			//if both locked, default internal pivot to o1
			//if locked...etc
			
			//if first orbit is locked and second one is not, use second 
			
			//otherwise first and try to see if it's possible
			
			
			
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
