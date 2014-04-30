package com.workshop.set.model;

import com.workshop.set.model.interfaces.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicschumann on 4/19/14.
 */
public class Equality implements Constraint<Symbol,Double> {

    private static double multiplicativeIdentity = 1.0D;
    private static double multiplicativeInverse = -1.0D;

    /**
     * @param relation the set of elements (a,b) for which we should like (a = b) to hold
     * @return a matrix row that imposes that this relation holds in the constraint set
     */
    @Override
    public List<Map<Symbol,Double>> constructRows( Map<Symbol,Symbol> relation ) {
        List<Map<Symbol,Double>> rows = new ArrayList<>();
        Map<Symbol,Double> map;

        for ( Map.Entry<Symbol,Symbol> pair : relation.entrySet() ) {
            map = new HashMap<>();
            map.put( pair.getKey(), multiplicativeIdentity );
            map.put( pair.getValue(), multiplicativeInverse );
            rows.add( new HashMap<>( map ) );
        }

        return rows;
    }

    public String toString() {
        return "=";
    }
}
