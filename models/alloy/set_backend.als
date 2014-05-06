module SET/backend[ Symbol ]

	sig Time {}

	sig Equation {
    	symbols : set Symbol			
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
    	c.equation = c'.equation        	
    	c.pivots = c'.pivots
		c.satisfied = c'.satisfied            
	}

	one sig Context {
    	system : set Constraint -> Time, 
	} {
    	all t : Time {       	 
        	all e : system.t.equation | e.symbols in objects.t.defines
    	}
	}


	
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
