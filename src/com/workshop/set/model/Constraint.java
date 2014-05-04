package com.workshop.set.model;

import java.util.List;
import java.util.Map;

//public interface Constraint<S,V> {
//    public List<Map<S,V>> constructRows( Map<S,S> relation );
//}

// models abstract constraint as applied to a geometry and able to solve itself or report unable
public interface Constraint{
	
	public boolean apply();
}