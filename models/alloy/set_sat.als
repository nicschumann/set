module SET/basis[ Symbol ]
	open util/ordering[ Time ]         	-- ordered contexts



	/** [0] : Prelude */

	sig Time {}

	/** [1] : Equations and Constraints: */
	/*
   	an equation represents the unique problem that a given system
   	of constraint is constructed to solve. As such, it helps to define an
   	equivalence relation on constraints: Two constraints are equivalent IFF
   	they represent the same equation, contain the same set of pivots,
   	contain the same set of orbits, and exist in the same context.
	*/
	sig Equation {
    	symbols : set Symbol				-- the set of symbols used in this equation
	}



	sig Constraint {
    	equation : one Equation,        	-- each constraint attempts to satisfy one equation
    	pivots : set Symbol,            	-- each constraint is equipped with a fixed set of fixed-valued pivots,	KNOWNS.
                                        	-- any symbols in the context, not in the set of pivots, are orbits for this constraint.
		satisfied : set Time 				-- the set of times at which this specific constraint is satisfied.
	} {
    	pivots in equation.symbols
	}

	pred EquivalentConstraints[ c,c' : Constraint ] {
    	c.equation = c'.equation        	-- equivalent constraints have the same equation.
    	c.pivots = c'.pivots            	-- equivalent constraints have pivot sets P1 and P2 such that P1 contains P2 and P2 contains P1
    	--c.orbits = c'.orbits            	-- equivalent constraints have orbit sets O1 and O2 such that O1 contains O2 and O2 contains O1

    	--and    

    	--objects.c = objects.c'            	-- equivalent constraints live in the same context.
	}



	/** [2] : Geometry */
	/*	 
    	In SET, Geometry is defined as a simple, inductive relation:
   	 
    	a Geometry may be either:

        	a)            	A "Point" consisting of Symbols x, y, and z.
        	b)            	A binary "Relation" between other two other geometries A and B

    	In essence, a Geometry is a simple binary tree structure. In addition to this,
    	we impose the constraint that Geometries are perfectly balanced, ie, the subtrees of
    	relations must have the same depth, given by the usual depth measure on binary trees.    	 

    	In terms of visualization, a relation between points may be (is) visualized as a straight
    	line between those point's locations in 3 space, a relation between lines is the plane contained
    	between the lines, relation between planes as the volume contained between them, a relation between
    	volumes as a path space mapping the elements of the left volume into the right volume ( an animation, perhaps? ).
       	 
       	 
 	*/
	abstract sig Geometry { defines : set Symbol }

	/* 	A Point represents a location in 3-space;
    	each component of a point is represented by a UNIQUE symbol,
    	which refers to that components current position in 3-space.
    	THis could be generalized into n arbitrary dimensions, but higher dimensional
    	geometry is not implemented in SET, so we limit ourselves to the space of R3.
 
      	x : Symbol	y : Symbol   z : Symbol
    	----------------------------------------	(PointIntro)
              	( x y z ) : Geometry
                	^ ^ ^
              	location

	*/
	sig Point extends Geometry {
    	x : one Symbol, y : one Symbol, z : one Symbol    
	} {
    	x != y and x != z and y != z                 	-- each symbol in a geometry is a unique identifier
    	defines = x + y + z                            	-- a Point defines the symbols it contains.
	}

	fact noSharedSymbols {                            	-- each unique Point defines unique symbols
    	all disj p1,p2 : Point |
        	no (p1.x + p1.y + p1.z) & (p2.x + p2.y + p2.z)
	}
    
	-- it could be interesting to remove this constraint - then points would easily vary with one another...








	/*  A Relation relates two Geometries with equivalent structure.

      	A : Geometry  	B : Geometry 	depth( A ) = depth( B )
       	-------------------------------------------------------------    	(RelIntro)
                          	( A , B ) : Geometry
                          	^   ^
                      	proj1   proj2                    	 
	*/
	sig Relation extends Geometry {
    	left : one Geometry,
    	right : one Geometry
	} {
    	no (left + right) & this                	-- a Relation cannot contain itself
    	this not in left.^(@left + @right)         	-- "
    	this not in right.^(@right + @left)        	-- "
    	defines = left.@defines + right.@defines	-- a Relation defines what its left and right sides define.
	}
	-- left  and right maintainence?


	fact RelationalArity {
    	all r : Relation {
        	r.left in Point iff r.right in Point
        	r.left in Relation iff r.right in Relation
        	#(r.left.defines) = #(r.right.defines)
    	}
	}

	assert NoGeometricCycles {
    	all r : Relation {
        	r not in r.^left
        	r not in r.^right
    	}
	}

	assert NoConstraintCycles {}

	assert NoSharedSymbols {}

	assert inGeometryIFFinConstraint {}


	/** [3] : Contexts: */
	/*
    	a Context represents the current state of the constraint problem

 	*/

	one sig Context {
    	system : set Constraint -> Time,
    	objects : set Geometry -> Time    
	} {
    	all t : Time {

    	--	all o : objects.t  | all c : system.t | o.defines in (c.pivots + c.orbits)
       	 
        	all r : Relation | r in objects.t implies (r.left in objects.t and r.right in objects.t)

    	--	all g : Geometry - objects.t | no s : g.defines | s in (objects.t).defines
        	all e : system.t.equation | e.symbols in objects.t.defines

    	}
       	 

	}

	pred initialCondition[ t : Time ] {
    	no Context.system.t
    	no Context.objects.t    
	}

	pred addPoint[ t,t' : Time, p : Point ] {
    	p not in Context.objects.t    
    	Context.objects.t' = Context.objects.t + p
    	Context.system.t' = Context.system.t
	}

	pred addRelation[ t,t' : Time, r : Relation ] {
    	r not in Context.objects.t
    	some lft, rht : Context.objects.t {
        	Context.system.t' = Context.system.t
        	Context.objects.t' = Context.objects.t + r
        	r.left = lft and r.right = rht
    	}    
	}

	pred addConstraint[ t,t' : Time, c : Constraint, symb : set Symbol ] {
	--	no (Context.objects.t).defines - (c.pivots + c.orbits)
    	c not in Context.system.t

    	symb in Context.objects.t.defines
    	symb = c.equation.symbols

    	Context.system.t' = Context.system.t + c
    	Context.objects.t' = Context.objects.t    
	}

	pred deletePoint[ t,t' : Time, p : Point ] {

    	p in Context.objects.t
    	p not in Context.objects.t'

    	no Context.objects.t' - Context.objects.t

    	all x : (Context.objects.t & Point) - p | x in Context.objects.t'
    	all r : (Context.objects.t & Relation) {
        	p.defines in r.left.defines iff r not in Context.objects.t'
        	p.defines in r.right.defines iff r not in Context.objects.t'
    	}
   	 
    	no Context.system.t' - Context.system.t
    	all c : Context.system.t {
        	p.defines in c.pivots implies c not in Context.system.t'
   		 c in Context.system.t' implies p.defines not in c.pivots
    	}
   	 
	}

	pred deleteRelation[ t, t' : Time, r: Relation ] {
   	 
    	r in Context.objects.t
    	r not in Context.objects.t'

    	no Context.objects.t' - Context.objects.t
    	--is a relation within a relation necessarily also linked to by the context?
    	--relations can share points without being the same relation
    	all x : (Context.objects.t & Relation) {
        	r in x.^left iff x not in Context.objects.t'
        	r in x.^right iff x not in Context.objects.t'
    	}
   	 (Context.objects.t & Point) = (Context.objects.t' & Point)
    	Context.system.t' = Context.system.t
	}

	pred deleteConstraint[ t, t' : Time, c: Constraint ] {    
    	c in Context.system.t
    	Context.system.t' = Context.system.t - c
    	Context.objects.t = Context.objects.t'
	}

	/**
    	?? applyConstraint ??
	*/

	assert noDoubleInit {
    	no disj t,t' : Time {
        	t' = t.next and initialCondition[t] and initialCondition[t']
    	}   	 
	}
	check noDoubleInit for 3 but 12 Symbol,
                            	exactly 4 Point,
                            	4 Relation,
                            	6 int,
                            	6 Time

	-- Can't represent movement!

	/** [4] : Trace: */

	fact Traces {
    	first.initialCondition
    	all t : Time - last |
        	let t' = t.next {
            	some c : Constraint, p : Point, r : Relation, pvts : set Symbol {
                   	addPoint[ t, t', p ]
                	or addRelation[ t, t', r ]
                	or addConstraint[ t, t', c, pvts ]
                	or deletePoint[ t, t', p]
                	or deleteRelation[ t, t', r ]
                	or deleteConstraint[ t, t', c ]
            	}           	 
        	}
	}

/** assertions...
	[x] ** no cycles in a given relation
	[X] ** left & right must have the same arity / structure.
	[x] ** points cannot share symbols.
	[x] ** all symbols in geometries must be in the constraints

	[ ] ** deleting a relation preserves points
	[x] ** deleting a point deletes the relations containing it, and constraints that contain it, and constraints w/ equations that contain it.
*/


	/** [5] : Assertions : */


	pred pointDeletion[ t,t' : Time, p : Point ] {
    	deletePoint[ t,t',p ]
    	implies
    	p in Context.objects.t
    	and no (Context.objects.t' & Relation).defines & p.defines
    	and no Context.system.t'.equation.symbols & p.defines
	}
	pred relationDeletion[ t,t' : Time, r : Relation ] {
    	deleteRelation[ t,t',r ]
    	implies
    	r in Context.objects.t
    	and all rel : Context.objects.t' & Relation | no rel.(^left + ^right) & r
	}
     pred relationDeletionPreservesPoints[ t,t' : Time, r : Relation ] {
    	deleteRelation[ t,t',r ]
    	implies
   	 (Context.objects.t & Point) = (Context.objects.t' & Point)
    }
    pred constraintDeletion[ t, t' : Time, c : Constraint ] {
   	 deleteConstraint[t, t', c]
   	 implies
   	 c in Context.system.t and no c & Context.system.t'
    }
	assert wellFormedDeletion {
    	all t,t' : Time {
        	t' = t.next implies
            	all p : Point | pointDeletion[ t,t',p ]
            	and all r : Relation | relationDeletion[ t,t',r ] and relationDeletionPreservesPoints[ t,t',r ]
   			 and all c : Constraint | constraintDeletion[ t,t',c]
    	}
	}
	check wellFormedDeletion for 3 but 12 Symbol,
                            	exactly 2 Point,
                            	2 Relation,
                            	6 int,
                            	6 Time
    
    assert relationDepth {
   	 all r : Relation | #(r.^left) = #(r.^right)
    }
    check relationDepth for 3 but 12 Symbol,
                            	exactly 2 Point,
                            	2 Relation,
                            	10 int,
                            	6 Time
/*

	(Today) Goals:
	+	Develop state model for the application...
	+ 	Make a drawing
	+	No Geometries / Constraints not in Context
	+ 	Make a pred that shows a more interesting relations

	+ 	Add constraints that have no pivots?

	+ Constraints indexed by time?


*/

pred wellconstrainedContext[ t : Time ] {
	no con : Context.system.t | t not in con.satisfied
}

-- pred underconstrainedContext[] {}

pred overconstrainedContext[ t : Time ] { not wellconstrainedContext[ t ] }

pred unsatisfiableAddition[ t,t' : Time, c : Constraint ] {
	t not in c.satisfied
	c not in Context.system.t
	c in Context.system.t'
	all s : Context.objects.t'.defines {
		s
	
	}

}

-- context transitions

/*
	when you add a constraint to the system, the constraint can be satisfiable or unsatisfiable
	( in this model, this is an arbitrary choice that affects the remaining run of the trace )
	
	if the added constraint is UNSATISFIABLE:
	S1 ->	A: constraint(t,s,u) sat

	S2 ->	A: constraint(t,s,u) sat	
		  	B: constraint(x,y,z) sat
	
	S3 ->	A: constraint(t,s,u) sat	
		  	B: constraint(x,y,z) sat
			C: constraint(a,b,z) sat

	S4 -> 	A: constraint(t,s,u) sat	
		  	B: constraint(x,y,z) sat
			C: constraint(a,b,z) sat
			D: constraint(z) unsat  

	S5 ->	A: constraint(t,s,u) sat	
		  	B: constraint(x,y,z) unsat
			C: constraint(a,b,z) unsat
			D: constraint(z) unsat 	

	S6 ->		





*/



pred constrainedContext {
	some t : Time |
    	some Context.system.t
}

pred deleteRelationTest {
	some t : Time |
	some t' : t.next  |
	some r : Relation | deleteRelation[t,t',r]
}

pred pointDeletionCausesAllDeletion {
    some t: Time {
   	 t = first
   	 initialCondition[t]
   	 some p: Point {
   		 addPoint[t, t.next, p]
   		 some r : Relation {
   			 r.left = p
   			 r.right = p
   			 addRelation[t.next, t.next.next, r]
   			 some c : Constraint {
   				 some c.equation.symbols & p.defines
   				 addConstraint[t.next.next, t.next.next.next, c, c.equation.symbols]
   			 }
   		 }
   		 deletePoint[t.next.next.next, t.next.next.next.next, p]
   	 }
    }
}

pred planeExample {
    some t: Time {
   	 t = first
   	 initialCondition[t]
   	 some disj p1, p2, p3, p4: Point {
   		 addPoint[t, t.next, p1]
   		 addPoint[t.next, t.next.next, p2]
   		 addPoint[t.next.next, t.next.next.next, p3]
   		 addPoint[t.next.next.next, t.next.next.next.next, p4]
   		 some disj r1, r2, r3 : Relation {
   			 r1.left = p1
   			 r1.right = p2
   			 r2.left = p3
   			 r2.right = p4
   			 r3.left = r1
   			 r3.right = r2
   			 addRelation[t.next.next.next.next, t.next.next.next.next.next, r1]
   			 addRelation[t.next.next.next.next.next, t.next.next.next.next.next.next, r2]
   			 addRelation[t.next.next.next.next.next.next, t.next.next.next.next.next.next.next, r3]
   		 }
   	 }
    }
}

run planeExample for 3 but 12 Symbol,
                            	exactly 4 Point,
                            	3 Relation,
                            	6 int,
                            	8 Time


run pointDeletionCausesAllDeletion for 3 but 12 Symbol,
                            	exactly 2 Point,
                            	2 Relation,
                            	6 int,
                            	6 Time

run deleteRelationTest for 3 but 12 Symbol,
                            	exactly 2 Point,
                            	2 Relation,
                            	6 int,
                            	6 Time

run constrainedContext for 3 but 12 Symbol,
                            	exactly 2 Point,
                            	2 Relation,
                            	6 int,
                            	6 Time

run { some Relation } for 3 but 12 Symbol,
                            	exactly 2 Point,
                            	2 Relation,
                            	6 int,
                            	6 Time

run addRelation for 3 but 12 Symbol,
                            	4 Point,
                            	2 Relation,
                            	10 int,
                            	6 Time




