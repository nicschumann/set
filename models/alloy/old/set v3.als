module SET/basis[ Symbol ] 
	open util/ordering[ Time ] 			-- ordered contexts



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
		symbols : set Symbol
	}



	sig Constraint {
		equation : one Equation,			-- each constraint attempts to satisfy one equation
		pivots : set Symbol,				-- each constraint is equipped with a fixed set of fixed-valued pivots,	KNOWNS.
	--	orbits : set Symbol					-- each constraint is equipped with a fixed set of variable-valued orbits, UNKNOWNS.
											-- any symbols in the context, not in the set of pivots, are orbits for this constraint.
	} {
		pivots in equation.symbols
--		no pivots & orbits					-- symbols cannot be both pivots and orbits in a constraint. (is this true?)
	}

	pred EquivalentConstraints[ c,c' : Constraint ] {
		c.equation = c'.equation			-- equivalent constraints have the same equation.
		c.pivots = c'.pivots				-- equivalent constraints have pivot sets P1 and P2 such that P1 contains P2 and P2 contains P1
		--c.orbits = c'.orbits				-- equivalent constraints have orbit sets O1 and O2 such that O1 contains O2 and O2 contains O1

		--and	

		--objects.c = objects.c'				-- equivalent constraints live in the same context.
	}



	/** [2] : Geometry */
	/* 	
		In SET, Geometry is defined as a simple, inductive relation:
		
		a Geometry may be either:

			a)				A "Point" consisting of Symbols x, y, and z.
			b)				A binary "Relation" between other two other geometries A and B

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
 
	      x : Symbol    y : Symbol   z : Symbol
		----------------------------------------	(PointIntro)
                  ( x y z ) : Geometry
				    ^ ^ ^
				  location

    */
	sig Point extends Geometry {
		x : one Symbol, y : one Symbol, z : one Symbol	
	} {
		x != y and x != z and y != z 					-- each symbol in a geometry is a unique identifier
		defines = x + y + z								-- a Point defines the symbols it contains.
	}

	fact noSharedSymbols {								-- each unique Point defines unique symbols
		all disj p1,p2 : Point |
			no (p1.x + p1.y + p1.z) & (p2.x + p2.y + p2.z)
	}
	
	-- it could be interesting to remove this constraint - then points would easily vary with one another...








	/*  A Relation relates two Geometries with equivalent structure.

		  A : Geometry      B : Geometry     depth( A ) = depth( B )
   	    -------------------------------------------------------------		(RelIntro)
		      				( A , B ) : Geometry 
                			  ^   ^
                          proj1   proj2                         
	*/
	sig Relation extends Geometry {
		left : one Geometry,
		right : one Geometry
	} {
		no (left + right) & this					-- a Relation cannot contain itself
		this not in left.^(@left + @right) 			-- "
		this not in right.^(@right + @left)			-- "
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
			p.defines in r.left.defines implies r not in Context.objects.t'
			p.defines in r.right.defines implies r not in Context.objects.t'
		}
		
		
		
		no Context.system.t' - Context.system.t
		all c : Context.system.t {
			p.defines in c.pivots implies c not in Context.system.t'
		}
		
	}

	pred deleteRelation[ t, t' : Time, r: Relation ] {
		
		r in Context.objects.t
		r not in Context.objects.t'

		no Context.objects.t' - Context.objects.t
		--is a relation within a relation necessarily also linked to by the context?
		--relations can share points without being the same relation
		all x : (Context.objects.t & Relation) {
			r in x.^left implies x not in Context.objects.t'
			r in x.^right implies x not in Context.objects.t'
		}

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
	[ ] ** deleting a point deletes the relations containing it, and constraints that contain it, and constraints w/ equations that contain it.
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
	assert wellFormedDeletion {
		all t,t' : Time {
			t' = t.next implies
				all p : Point | pointDeletion[ t,t',p ]
				and all r : Relation | relationDeletion[ t,t',r ]
		}
	}
	check wellFormedDeletion for 3 but 12 Symbol,
								exactly 2 Point,
								2 Relation,
								6 int,
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





pred constrainedContext {
	some t : Time |
		some Context.system.t
}

pred deleteRelationTest {
	some t : Time |
	some t' : t.next  | 
	some r : Relation | deleteRelation[t,t',r]
}


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




