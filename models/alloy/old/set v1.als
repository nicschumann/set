
/** Abstract Geometric Elements */

sig Symbol {}
sig Equation {}
sig Constraint {
	equation : one Equation,
	pivots : set Symbol,
	orbits : set Symbol
} {
	no pivots & orbits
}

sig Context {
	system : set Constraint,
	objects : set Geometry
} {
	all o : objects {
		all c : system {
			o.defines in (c.pivots + c.orbits)
		}
	}		
}


/* 	
	Inductive Definition of Geometric Elements
 */
abstract sig Geometry {
	defines : set Symbol
}

	/* 	A Point represents a location in 3-space;
		each component of a point is represented by a UNIQUE symbol,
		which refers to that components current position in 3-space.
 
	        x : R      y : R      z : R
		----------------------------------
                ( x y z ) : Geometry
				  ^ ^ ^
				 location

     */
	sig Point extends Geometry {
		x : one Symbol, y : one Symbol, z : one Symbol	
	} {
		x != y and x != z and y != z 
		defines = x + y + z
	}

	/*  A Relation relates two Geometries,

		  A : Geometry      B : Geometry
   	    ----------------------------------
		 ( A , B ) : Geometry x Geometry 
           ^   ^
           l   r                         */
	sig Relation extends Geometry {
		left : one Geometry,
		right : one Geometry
	} {
		no (left + right) & this			-- a Relation cannot contain itself
		this not in left.^@left 			-- "
		this not in right.^@right 			-- "
		defines = left.@defines + right.@defines
	}

pred RelationalArity[ r : Relation ] {
	r.left in Point iff r.right in Point
		r.left in Relation iff r.right in Relation
		r.left in Relation implies RelationalArity[ r.left ]
		and r.right in Relation implies RelationalArity[ r.right ]		
}



assert NoGeometricCycles {
	all r : Relation {
		r not in r.^left
		r not in r.^right
	}
}

assert NoConstraintCycles {

}



assert NoSharedSymbols {
}

assert inGeometryIFFinConstraint {
}

/** assertions... 
	[x] ** no cycles in a given relation 
	[] ** left & right must have the same arity / structure.
	[x] ** points cannot share symbols.
	[x] ** all symbols in geometries must be in the constraints
*/

fact noSharedSymbols {
	all disj p1,p2 : Point |
		no (p1.x + p1.y + p1.z) & (p2.x + p2.y + p2.z)
}






// functions





/*

	Tomorrow Goals:
	+	Prove a Statement about the relationship between the number of symbols,
		and the number of disjoint constraints such that the system is still satisfiable.
	+	Develop state model for the application...


*/














run { some Relation } for 3 but 6 Symbol, 1 Context




