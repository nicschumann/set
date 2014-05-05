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
				
				//first check if this value is already locked, if so, notify user
//				if(orbit.getN_(index+1).getLocked()){
//					System.out.println("Element is already constrained");
//					break; 
//				}
//				else{
//					orbit.getN_(index+1).lock(); 
//				}
				
				//index of interest to be constrained between pivot and orbit in "relation" way
				_equations.add(new LinearEquation(pivot.getN_(index+1), orbit.getN_(index+1), index, relation, 1, pivots));
			}
			orbit.addConstraint(this);
		}
		}
		
		else if(relation.equalsIgnoreCase("slope_equality") || relation.equalsIgnoreCase("perpendicular")){

			Point o1 = orbits.get(0);
			Point o2 = orbits.get(1);			
			//pivot on o2 only if it is the only one locked
			if(o2.isLocked() && !o1.isLocked()){
				_equations.add(new LinearEquation(o2.getN_(2), o1.getN_(2), 2, relation, 1, pivots));
				_equations.add(new LinearEquation(o2.getN_(1), o1.getN_(1), 1, relation, 1, pivots));
				o1.addConstraint(this);
			}
			
			else{
				_equations.add(new LinearEquation(o1.getN_(2), o2.getN_(2), 2, relation, -1, pivots));
				_equations.add(new LinearEquation(o1.getN_(1), o2.getN_(1), 1, relation, -1, pivots));
				o2.addConstraint(this);
			}
		}

        else if(relation.equalsIgnoreCase("slope_inverse")){

            Point o1 = orbits.get(0);
            Point o2 = orbits.get(1);

            //pivot on o2 only if it is the only one unlocked
            if(!o2.isLocked() && o1.isLocked()){
                //o1.y = deltay+o2.y
                //o1.x = deltax + o2.x
                _equations.add(new LinearEquation(o2.getN_(2), o1.getN_(2), 2, relation, 1, pivots));
                _equations.add(new LinearEquation(o2.getN_(1), o1.getN_(1), 1, relation, 1, pivots));
                o1.addConstraint(this);
            }

            else{
                //o2.y = o1.y - deltaY;
                //o2.x = o1.x - deltaX;
                _equations.add(new LinearEquation(o1.getN_(2), o2.getN_(2), 2, relation, -1, pivots));
                _equations.add(new LinearEquation(o1.getN_(1), o2.getN_(1), 1, relation, -1, pivots));
                o2.addConstraint(this);
            }
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
		
		return toRet;	//returns false if there were some issues
	}

}
