package com.workshop.set.model;

import com.workshop.set.model.geometry.VectorSpace.GeometricFailure;
import com.workshop.set.model.geometry.VectorSpace.Point;

import glfrontend.components.Vector4;

public class SlopeEquation {
	
	private Point _static, _dynamic, _p1, _p2; 
	private String _relation;
	
	public SlopeEquation(Point orbitStatic, Point orbitDynamic, Point p1, Point p2, String relation){
		//the direction these points will move, the point to stay in place, the one to move
		
		_relation = relation;
		_p1 = p1; 
		_p2 = p2; 
		
		_static = orbitStatic; 
		_dynamic = orbitDynamic; 
	}
	
	public boolean solve() throws GeometricFailure{
		
		//get the magnitude of the vector to match and the direction vector
		Vector4 dir = new Vector4((float)(_p2.getN_(1).get()-_p1.getN_(1).get()), (float)(_p2.getN_(2).get()-_p1.getN_(2).get()), (float)(_p2.getN_(3).get()-_p1.getN_(3).get()),0f);
		//System.out.println("The direction vector 1: " + dir.x + " " + dir.y + " " + dir.z);
		
		if(_relation.equalsIgnoreCase("perpendicular")){
			//make a new vector out of a component of pivot and orbit and take cross product
			Vector4 toCross = new Vector4(0,0,1,0);
			dir = dir.getCrossProd(toCross);
		}
		
		dir = dir.getNormalized();
		
		Vector4 otherDir = new Vector4((float)(_static.getN_(1).get()-_dynamic.getN_(1).get()), (float)(_static.getN_(2).get()-_dynamic.getN_(2).get()), (float)(_static.getN_(3).get()-_dynamic.getN_(3).get()),0f);
		float mag = otherDir.getMagnitude(otherDir); 
		
		//System.out.println("Magnitude: " + mag);
		//get magnitude of the orbit and multiply by dir
		dir = dir.uniformScale(mag);
		
		//to get the direction we should move the dynamic
		//orbit with respect to the static
		
		//System.out.println("The direction vector 4: " + dir.x + " " + dir.y + " " + dir.z);
		
		_dynamic.getN_(1).set(_static.getN_(1).get()+dir.x);	//set the reference to the x position to the static's x plus dir
		_dynamic.getN_(2).set(_static.getN_(2).get()+dir.y);
		_dynamic.getN_(3).set(_static.getN_(3).get()+dir.z);
		
		return true;  
	}

}
