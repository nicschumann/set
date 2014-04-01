package com.workshop.set.interfaces;

import com.workshop.set.lang.judgements.HasType;
import com.workshop.set.lang.judgements.HasValue;

/**
 * An environment maintains a mapping of variable names to values,
 * A mapping of values to to scalar coordinates, and a
 */
public interface Environment {
    public Context typing();

    public Environment extend( Context gamma );
    public Environment extend( Environment eta );

    public Environment extend( HasType a );
    public Environment extend( HasValue a );
}
