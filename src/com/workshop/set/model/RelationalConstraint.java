package com.workshop.set.model;

import glfrontend.components.Vector4;

import java.util.ArrayList;
import java.util.Set;

import com.workshop.set.model.geometry.VectorSpace.GeometricFailure;
import com.workshop.set.model.geometry.VectorSpace.Point;

public class RelationalConstraint implements Constraint {

	ArrayList<LinearEquation> _equations;  
	ArrayList<SlopeEquation> _sEquations;  
	
	//gets the pivot and the orbit, indices of interest, and canonical type to create linear equation(s)
	public RelationalConstraint(ArrayList<Point> pivots, ArrayList<Point> orbits, Set<Integer> indices, String relation) throws GeometricFailure{
				
		_equations = new ArrayList<LinearEquation>();
		_sEquations = new ArrayList<SlopeEquation>();
		
		if(relation.equalsIgnoreCase("equality")){
			for (int i=0; i<pivots.size(); i++){
				Point pivot = pivots.get(i);
				Point orbit = orbits.get(i);
				
				for (int index : indices){
					_equations.add(new LinearEquation(pivot.getN_(index+1), orbit.getN_(index+1), index, relation, 1, pivots));
				}
				orbit.addConstraint(this);
			}
		}
		
		else if(relation.equalsIgnoreCase("slope_equality") || relation.equalsIgnoreCase("perpendicular")){

			Point o1 = orbits.get(0);
			Point o2 = orbits.get(1);	
			Point p1 = pivots.get(0);
			Point p2 = pivots.get(1);
			
			Point staticOrbit, dynamicOrbit; 
			
			staticOrbit=o1;
			dynamicOrbit=o2;
			
			if(o2.isLocked() && !o1.isLocked()){
				staticOrbit=o2;
				dynamicOrbit=o1;
			}
			
			_sEquations.add(new SlopeEquation(staticOrbit, dynamicOrbit, p1, p2, relation));
			
//			//pivot on o2 only if it is the only one locked
//			if(o2.isLocked() && !o1.isLocked()){
//				_equations.add(new LinearEquation(o2.getN_(2), o1.getN_(2), 2, relation, 1, pivots));
//				_equations.add(new LinearEquation(o2.getN_(1), o1.getN_(1), 1, relation, 1, pivots));
//				o1.addConstraint(this);
//			}
//			
//			else{
//				_equations.add(new LinearEquation(o1.getN_(2), o2.getN_(2), 2, relation, -1, pivots));
//				_equations.add(new LinearEquation(o1.getN_(1), o2.getN_(1), 1, relation, -1, pivots));
//				o2.addConstraint(this);
//			}
			
			dynamicOrbit.addConstraint(this);
		}
	}
	
	@Override
	public boolean apply() {
		//solve each linear equation and change corresponding values of orbit
		boolean noConflict=true;
		boolean toRet=true; 
		
		for(LinearEquation le : _equations){
			try {
				noConflict=le.solve();
			} catch (GeometricFailure e) {
				e.printStackTrace();
			}
			if(!noConflict)
				toRet=false;
		}
		
		for(SlopeEquation se : _sEquations){
			try {
				noConflict=se.solve();
			} catch (GeometricFailure e) {
				e.printStackTrace();
			}
			if(!noConflict)
				toRet=false;
		}
		
		
		
		return toRet;	//returns false if there were some issues
	}

}
