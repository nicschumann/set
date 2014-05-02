
/** Abstract Geometric Elements */
sig Constraint {
	pivot : one Geometry,
	orbits : set Geometry
} {
	
}

sig Location {}

abstract sig Geometry {}
	/* A Point represents a location in space,
 
	        x : R      y : R      z : R
		----------------------------------
                ( x y z ) : Geometry
				  ^ ^ ^
				 location

     */
	sig Point extends Geometry {
		location : one Location
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
		no (left + right) & this
	}





