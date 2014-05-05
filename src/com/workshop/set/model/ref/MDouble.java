package com.workshop.set.model.ref;

public class MDouble extends Mutable<Double> {
	
	private boolean _locked; 
    public MDouble( double varying ) {  
    	super( varying );
    	_locked=false;
    }
    public void lock(){
    	_locked = true; 
    }
    public void unlock(){
    	_locked = false;
    }
    public boolean getLocked(){
    	return _locked; 
    }
}
