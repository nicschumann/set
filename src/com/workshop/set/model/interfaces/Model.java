package com.workshop.set.model.interfaces;

import glfrontend.components.GeometricElement;

public interface Model {
	
	public void drawGeometricElements();
	
	public boolean addElement(GeometricElement elmt);
	
	public boolean removeElement(GeometricElement elmt);

}
