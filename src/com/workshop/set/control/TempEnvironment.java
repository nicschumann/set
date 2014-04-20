package com.workshop.set.control;

import glfrontend.components.GeometricElement;

import java.util.HashSet;
import java.util.Set;

import com.workshop.set.model.interfaces.Model;

public class TempEnvironment implements Model {

	private Set<GeometricElement> _currentElements;
	
	public TempEnvironment() {
		_currentElements = new HashSet<>();
	}
	
	public boolean addElement(GeometricElement elmt) {
		return _currentElements.add(elmt);
	}
	
	public boolean removeElement(GeometricElement elmt) {
		return _currentElements.remove(elmt);
	}

	@Override
	public void drawGeometricElements() {
		for (GeometricElement elmt : _currentElements) {
			elmt.render();
		}
	}
}
