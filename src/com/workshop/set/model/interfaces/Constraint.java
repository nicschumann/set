package com.workshop.set.model.interfaces;

import com.workshop.set.model.Mutable;

import java.util.List;
import java.util.Map;

public interface Constraint<S,V> {
    public List<Map<S,V>> constructRows( Map<S,S> relation );
}
