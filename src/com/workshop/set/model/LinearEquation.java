package com.workshop.set.model;

import java.util.ArrayList;

import com.workshop.set.model.geometry.VectorSpace.GeometricFailure;
import com.workshop.set.model.geometry.VectorSpace.Point;
import com.workshop.set.model.ref.MDouble;

public class LinearEquation {
		
	MDouble _known, _unknown; 
	String _relation;
	double _constant;	//constant in linear equation
	ArrayList<Point> _pivots;
	int _index; 
	
	//keeps track purely of numbers and variables in n space and solves the equation 

	public LinearEquation(MDouble known, MDouble unknown, int index, String relation, double c, ArrayList<Point> p) throws GeometricFailure{
		
		
		_constant = c; 
		_pivots = p;
		_index = index; 
		_relation = relation; 
		_known = known;
		_unknown = unknown;
	}
	
	public boolean solve() throws GeometricFailure{
		
		//dynamically get constant
		double constant=0; 
		
		//if one pivot, it's a line with no constant 
		if(_pivots.size()==1){
			constant=0; 
			//check if value is locked before making changes
			//if(!_unknown.getLocked()){
				_unknown.set(_known.get()+constant);
				_unknown.lock(); 
				return true;  
			//}
		}
		else if(_pivots.size()==2){	//if two pivots, take delta of the corresponding index 
			
			//depending on perpendicular or parallel, may be this value or inverse			
			Point p1 = _pivots.get(0);
			Point p2 = _pivots.get(1);
			
			if(_relation.equalsIgnoreCase("perpendicular")){
				//assign inverse slopes
				double slope = (p1.getN_(2).get() - p2.getN_(2).get())/(p1.getN_(1).get() - p2.getN_(1).get());
				if(_index==2)
					constant=-1; 
				else	//set equal to slope 
					constant = slope; 
				constant *= _constant;
				//if(!_unknown.getLocked()){
					_unknown.set(_known.get()+constant); 
					return true;  
				//}
			}
			
			else{	//parallel
				double delta = p1.getN_(_index).get() - p2.getN_(_index).get();
				constant = delta; 
				constant *= _constant; 
				//if(!_unknown.getLocked()){
					_unknown.set(_known.get()+constant); 
					return true;  
				//}
			}
		}
		
		//System.out.println("cannot reset locked value");
		return false; 
	}
}
