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
                                        	-- any symbols in the context, not in the set of pivots, are considered orbits for this constraint.
	} {
    	pivots in equation.symbols			-- all the pivots in the constraint must be involved in the equations symbols.
	}

	pred EquivalentConstraints[ c,c' : Constraint ] {
    	c.equation = c'.equation        	-- equivalent constraints have the same equation.
    	c.pivots = c'.pivots            	-- equivalent constraints have pivot sets P1 and P2 such that P1 contains P2 and P2 contains P1
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
       	 
		the defines relation is a book-keeping relation that contains the set of symbols contained in this geometry
       	 
 	*/
	abstract sig Geometry { defines : set Symbol }

	/* 	A Point represents a location in 3-space;
    	each component of a point is represented by a UNIQUE symbol,
    	which refers to that components current position in 3-space.
    	This could be generalized into n arbitrary dimensions, but higher-dimensional
    	geometry is not implemented in SET, so we limit ourselves to the space of R3.
 
      	x : Symbol	y : Symbol   z : Symbol
    	----------------------------------------	(PointIntro)
              	( x y z ) : Geometry
                  ^ ^ ^
              	locations in R

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
    
	-- NB. it could be interesting to remove this constraint - then points would easily vary with one another... definitionally.


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
    	this not in left.^(@left + @right)         	-- a Relation cannot contain itself
    	this not in right.^(@right + @left)        	-- a Relation cannot contain itself
    	defines = left.@defines + right.@defines	-- a Relation defines what its left and right sides define.
	}

	/*
		A relation must relate elements with equivalent structure.
	*/
	fact RelationalArity {
    	all r : Relation {
        	r.left in Point iff r.right in Point
        	r.left in Relation iff r.right in Relation
        	#(r.left.defines) = #(r.right.defines)
    	}
	}



	/** [3] : Contexts: */
	/*
    	a Context represents the current state of the program. That is, it contains the set of currently-rendered Geometries,
		as well as the set of constraints currently imposed on those geometries. In order to model runs of the program,
		the set of constrains and the set of geometries are indexed by the ordered set Time.

 	*/

	one sig Context {
    	system : set Constraint -> Time,			-- the set of constrains imposed at a given time
    	objects : set Geometry -> Time    			-- the set of geometries defined at a given time
	} {
    	all t : Time {
       	 
        	all r : Relation | r in objects.t implies (r.left in objects.t and r.right in objects.t) -- relations must be well-defined in a context
        	all e : system.t.equation | e.symbols in objects.t.defines	-- equations may not mention undefined symbols.

    	}
       	 

	}


	/** [4] : Trace: */
	/*
		In order to model a run of the system, we used a trace fact, paired with predicates
		that model an initial empty condition, as well as transitions between times in the trace.
	*/

	/*  [4.1] : The Initial condition is an empty context */
	pred initialCondition[ t : Time ] {
    	no Context.system.t
    	no Context.objects.t    
	}

	/*  [4.2] : Adding a point to the context at time t augments the set of objects with that point in t'
				and leaves the constraint system as it was. That point must also be a new point; you can't add a 
				point that already exists! */
	pred addPoint[ t,t' : Time, p : Point ] {
    	p not in Context.objects.t    
    	Context.objects.t' = Context.objects.t + p
    	Context.system.t' = Context.system.t
	}

	/*  [4.3] : Adding a relation means selecting two Geometries and relating them. As such, the selected geometries must
				already be in the context - no adding relations on things that don't exist. Adding a relation doesn't add
				any new constraints or geometry to the context, just the specified relation. */
	pred addRelation[ t,t' : Time, r : Relation ] {
    	r not in Context.objects.t
    	some lft, rht : Context.objects.t {
        	Context.system.t' = Context.system.t
        	Context.objects.t' = Context.objects.t + r
        	r.left = lft and r.right = rht
    	}    
	}

	/*  [4.4] : a constraint may be added between objects that already exist in the context; that is
				the symbols involved in a constraint must be defined by objects already in the context at time t.
				Adding a constraint doesn't change the set of defined Geometries. */
	pred addConstraint[ t,t' : Time, c : Constraint, symb : set Symbol ] {
    	c not in Context.system.t

    	symb in Context.objects.t.defines
    	symb = c.equation.symbols

    	Context.system.t' = Context.system.t + c
    	Context.objects.t' = Context.objects.t    
	}

	/*  [4.5] : Deleting a point means deleting all of the relations that contain it in their subtrees, and
				all the constrains that mention its symbols. Deletion deletes strictly, although more than just
				the given point may vanish, nothing will ever be added; a transition via deletion results in a
				strictly smaller context. */
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

	/*  [4.6] : Deleting a relation means deleting all relations that mention it in their subtrees. Because the set of symbols doesn't change,
				the set of constraints is maintained as-is. Likewise, deleting a relation does not delete any points.  */
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
	
	/*  [4.7] : Deleting a constraint is simple - the constraint is removed from the context; nothing else is changed. */
	pred deleteConstraint[ t, t' : Time, c: Constraint ] {    
    	c in Context.system.t
    	Context.system.t' = Context.system.t - c
    	Context.objects.t = Context.objects.t'
	}


	

	/*  [4.8] : The traces specify the set of legal transitions of the system. */	
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


	/** [5] : Assertions : */
	/* 
		We were especially interested in checking that deletion maintained a well-formed context; in order
		to do this we had to check some assertions about what it means for a context to be well-formed.
	*/
	
	/* [5.1] : no relation should be able to contain itself as a child. */
	assert NoGeometricCycles {
    	all r : Relation {
        	r not in r.^left
        	r not in r.^right
    	}
	}
	check NoGeometricCycles for 3 but 12 Symbol,
                            	exactly 4 Point,
                            	4 Relation,
                            	6 int,
                            	6 Time	

	/* [5.2] : deleting a point should mean that that point was in the context, that it is no longer in the context,
			   and that no relation or constraint mentions its symbols. Note this is NOT true conversely. */
	pred pointDeletion[ t,t' : Time, p : Point ] {
    	deletePoint[ t,t',p ]
    	implies
    	p in Context.objects.t
    	and no (Context.objects.t' & Relation).defines & p.defines
    	and no Context.system.t'.equation.symbols & p.defines
	}
	/* [5.3.1] : deleting a relation should mean that that relation is no longer reachable from any other relation in the context. */
	pred relationDeletion[ t,t' : Time, r : Relation ] {
    	deleteRelation[ t,t',r ]
    	implies
    	r in Context.objects.t
    	and all rel : Context.objects.t' & Relation | no rel.(^left + ^right) & r
	}
	/* [5.3.2] : deleting a relation should not affect the points in the context. */
    pred relationDeletionPreservesPoints[ t,t' : Time, r : Relation ] {
    	deleteRelation[ t,t',r ]
    	implies
   	 	(Context.objects.t & Point) = (Context.objects.t' & Point)
    }
	/* [5.4] : deleting a constraint removes that constraint and does nothing else. */
    pred constraintDeletion[ t, t' : Time, c : Constraint ] {
   	 deleteConstraint[t, t', c]
   	 implies
   	 c in Context.system.t and no c & Context.system.t'
	 and Context.objects.t = Context.objects.t'
    }
	/* [5.5] : if the above predicates hold for every transition, then deletion maps well-formed contexts into well-formed context */
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
    
	/* [5.6] : relations are symmetric : ie, relational trees are perfectly balanced. */
    assert relationDepth {
   	 all r : Relation | #(r.^left) = #(r.^right)
    }
    check relationDepth for 3 but 12 Symbol,
                            	exactly 2 Point,
                            	2 Relation,
                            	10 int,
                            	6 Time
	/* [5.7] : no transition maps empty contexts into empty contexts - each transition does something. */
	assert noConsecutiveInit {
    	no disj t,t' : Time {
        	t' = t.next and initialCondition[t] and initialCondition[t']
    	}   	 
	}
	check noConsecutiveInit for 3 but 12 Symbol,
                            	exactly 4 Point,
                            	4 Relation,
                            	6 int,
                            	6 Time

	/** [6] : Instance Generator Predicates */
	/*  the following predicates make for some interesting instances... */

	pred extantRelationI { some Relation } -- there's some relation in the model.

	pred constrainedContext {
		some t : Time |
    		some Context.system.t		  -- the context is constrained at some point
	}

	pred deleteRelationTest {
		some t : Time |
		some t' : t.next  |
		some r : Relation | deleteRelation[t,t',r]	-- a relation is deleted
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
}	-- deleting a point clears the context.

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
}	-- a non-trivial plane is constructed

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

run extantRelationI for 3 but 12 Symbol,
                            	exactly 2 Point,
                            	2 Relation,
                            	6 int,
                            	6 Time

run addRelation for 3 but 12 Symbol,
                            	4 Point,
                            	2 Relation,
                            	10 int,
                            	6 Time




