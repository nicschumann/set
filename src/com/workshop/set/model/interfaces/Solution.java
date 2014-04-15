package com.workshop.set.model.interfaces;

import java.util.Map;
import java.util.Set;

/**
 * Created by nicschumann on 4/14/14.
 */
public interface Solution {
    public Set<Symbol> variables();
    public Map<Symbol,Term> domains();
    public Map<Symbol,Constraint> constraints();







}
