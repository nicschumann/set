package com.workshop.set.model;

import java.util.List;
import java.util.Map;

public interface Constraint<S,V> {
    public List<Map<S,V>> constructRows( Map<S,S> relation );
}
