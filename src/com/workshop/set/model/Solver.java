package com.workshop.set.model;

import com.workshop.set.control.TempEnvironment;
import com.workshop.set.model.geometry.Equation;
import com.workshop.set.model.geometry.OperationalGeometry;
import com.workshop.set.model.geometry.VectorSpace;
import com.workshop.set.model.interfaces.*;

import com.workshop.set.model.lang.core.*;
import com.workshop.set.model.lang.environments.Evaluation;
import com.workshop.set.model.geometry.VectorSpace.*;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.model.ref.MDouble;
import com.workshop.set.model.ref.MappableList;
import com.workshop.set.view.SetScreen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Solver {
    public Solver( VectorSpace v, Gensym generator ) {

        _symbolTable            = new HashMap<>();
        _currentElements        = new HashSet<>();
        _currentSelections      = new HashSet<>();
        _pivoting               = new HashSet<>();

        this._space              = v;
        this._generator          = generator;
        this._environment        = new Evaluation( generator );
        this._renderer           = new TempEnvironment( new HashSet<Geometry>(), new HashSet<Geometry>() ); // rendering model

    }

    /* Basis Objects :
    *  These objects are the foundations of the solution space */

    private VectorSpace         _space;
    private Evaluation          _environment;
    private Gensym              _generator;
    private TempEnvironment     _renderer;

    /* Maps :
     * These objects are the mappings between terms and their vector representations */

    private Map<Symbol,Geometry> _symbolTable;
    private Set<Geometry> _currentSelections;
    private Set<Geometry> _currentElements;
    private Set<Geometry> _pivoting;


    /*
     * In order for the solver to function properly, we require a mapping from
     * Terms into geometries and geometries into terms. ( or some kind of symmetric map )
     */







    /**
     * the addGeometry adds a geometry to the set of render-able elements on the stage,
     * as well as adds the appropriate terms to the context.
     *  Add geometry needs to
     *      - map the geometry into its term representation.
     *      - ensure that the geometry is well-typed.
     *      - let the geometry enter the context.
     *
     * @param g a geometry to add to the context for rendering and evaluation
     * @throws com.workshop.set.model.lang.exceptions.TypecheckingException,
     * @throws com.workshop.set.model.lang.exceptions.ProofFailureException,
     */

    public synchronized void addGeometry( Geometry g ) throws
        ProofFailureException, TypecheckingException {
         // at this point we have a term representation,
         // with well-typed subterms, we can go ahead and type it.
         // its now well-typed an in the context

        Term t = geometryIntoTerm( g );
        _renderer.addGeometry(g);

    }


    /**
     * Add a named term to the Context.
     *
     * @param s the symbol that should represent t
     * @param t the term to be named by s
     * @throws ProofFailureException if a internal environment error occurs
     * @throws TypecheckingException if the term fails to type-check
     */
    public synchronized void addTerm( Symbol s, Term t )
        throws ProofFailureException, TypecheckingException {
        _environment.name( s, t );
    }

    /**
     * WARNING: This function lets you alter the reference space directly. This is very dangerous
     * ( consider removing this functionality ). This is the "set" equivalent of bit manipulation
     * of objects.
     *
     * add / overide a new symbolic value in the current reference context. this WILL change all the values of
     * of terms that depend of the symbol s, if it is already defined.
     *
     * @param s the symbol value of this reference.
     * @param val a double or MDouble to set as a the initial value of this symbol
     * @throws ProofFailureException
     */
    public synchronized void addSymbolicValue( Symbol s, MDouble val )
        throws ProofFailureException {
        _environment.set( s, val );
    }

    public synchronized void addSymbolicValue( Symbol s, double val )
        throws ProofFailureException {
        _environment.set( s, val );
    }


    /**
     * Add an assumption (an uninterpreted name) to the context. A legal assumption
     * means a well-formed type.
     *
     * @param s an uninterpreted name, assumed to be an element of type
     * @param type a type denoting a set to that s belongs to. This set must be a well-formed element of some universe
     * @throws ProofFailureException if a internal environment error occurs
     * @throws TypecheckingException if the term fails to type-check
     */
    public synchronized void assumeTerm( Symbol s, Term type )
        throws ProofFailureException, TypecheckingException {
        _environment.assume(s, type);
    }


    /**
     * the eval method runs a term in the solver - this function defines the semantics of terms
     * with respect to the solve instance.
     *
     * @param t a term to eval in this Solver
     * @return the normal form of T
     */
    public synchronized Term eval( Term t )
        throws ProofFailureException, TypecheckingException {
        Term evaluated = evaluateTerm( t );
        // if the normal form of the term t corresponds to a judgement, we attempt to impose that judgement.
        if ( evaluated instanceof TJudgement ) { constrain( (TJudgement) evaluated ); }
        return evaluated;
    }



    private synchronized void constrain( TJudgement judgement )
        throws ProofFailureException, TypecheckingException {
        Equation eq = new Equation( judgement.judging(), left( judgement.left ), right( judgement.right ) );
    }

    private synchronized OperationalGeometry left( Term l )
        throws ProofFailureException, TypecheckingException {
        try {

            if ( l instanceof TScalar ) {

                OperationalGeometry op = new OperationalGeometry( _environment );
                double[ ] arr = new double[ _space.dimension ];
                for ( int i = 0; i < _space.dimension; i++ ) { arr[ i ] = ((TScalar) l).getIndex(); }
                Point scalefactor = _space.point( _environment.freshname( "SCALE" ), arr );
                return op.geometry( scalefactor );

            } else if ( l instanceof TNameGenerator.TName ) {

            } else if ( l instanceof TMultiplicative ) {

            } else if ( l instanceof TAdditive ) {

            } else if ( l instanceof TVector ) {

            } else if ( l instanceof TSet ) {

            } else if ( l instanceof TTuple ) {

            } else {
                throw new ProofFailureException( "Illegal or Unreduced Term contained Within Judgement: " + l );
            }
        } catch ( GeometricFailure e ) {
            throw new ProofFailureException( "The specified geometry ( " + l + " ) is not a member of this vector space, R"+_space.dimension );
        }


        return null;
    }

    private synchronized OperationalGeometry right( Term r ) {
        return null;
    }

    /**
     * evaluate a named term in the current context.
     *
     * @param t the term to evaluate in the context
     * @return t's normal form
     * @throws ProofFailureException if a internal environment error occurs
     * @throws TypecheckingException if the term fails to type-check
     */
    public synchronized Term evaluateTerm( Term t )
        throws ProofFailureException, TypecheckingException {
        return _environment.eval( t );
    }

    /**
     * Evaluate the type of a given term in environment.
     *
     * @param t a term to type-check
     * @return the type of t
     * @throws ProofFailureException if a internal environment error occurs
     * @throws TypecheckingException if the term fails to type-check
     */
    public synchronized Term evaluateType( Term t )
        throws ProofFailureException, TypecheckingException {
        t.type( _environment );
        return _environment.proves( t );
    }


    /**
     * this removes the indicated geometry ONLY from the heap.
     * @param g the geometry to remove
     */

    public void removeGeometry( Geometry g )
        throws TypecheckingException, ProofFailureException {
        Term term = _environment.getValue( g.name() );


    }

    /**
     * this removes the indicated geometry and ALL OF ITS DESCENDENTS from the heap.
     * @param g the geometry to remove
     */

    public void removeGeometryAll(Geometry g ) {
        // this is actually very difficult. it required descending through the geometry, recursively removing all of
        // its atoms, and then checking to see that the heap's well-typing is still maintained.

    }



    public void deleteSelections() {

    }







    private Map<Symbol,Geometry> getSymbolTable() {
        return _symbolTable;
    }

    /**
     * This function converts a geometry into its term representation.
     * @param g a geometry to submit to the context
     * @return the Term representation of this geometry
     * @throws ProofFailureException just in case the resultant context is malformed
     * @throws TypecheckingException just in case typechecking fails.
     */
    private Term geometryIntoTerm( Geometry g )
        throws ProofFailureException,
            TypecheckingException {
        if ( g instanceof Relation ) {

            Term t = new TTuple(
                    geometryIntoTerm( ((Relation) g).domain() ),
                    geometryIntoTerm( ((Relation) g).codomain() )
            );
            _environment.name( g.name(), t );
            return t;

        } else if ( g instanceof Point ) {

            try {
                MappableList<Term> components = new MappableList<>();
                for ( int i = 1; i <= g.dimension(); i++ ) {
                    Symbol s = ((Point) g).getX_( i );
                    components.add( s );
                    _environment.set( s, ((Point) g).getN_( i ) );
                }
                return new TVector( components );
            } catch ( GeometricFailure exn ) {
                // not technically reachable, unless our vectorspace got fucked ^
                System.err.println( exn.getLocalizedMessage() );
                throw new ProofFailureException( "Internal Geometric Failure" );
            }

        } else throw new ProofFailureException( "Unrecognized Geometric Type" );
    }



    public Symbol name( String arg ) { return _environment.basename( arg ); }

    public synchronized void renderGeometries() { _renderer.renderGeometries(); }

    public synchronized void checkIntersections(Point elmt, boolean shift, boolean pivot) { _renderer.checkIntersections(elmt, shift, pivot); }

    public synchronized void setScreen(SetScreen main) { _renderer.setScreen( main ); }

    public void createConstraint(String type) {}

    public void update() {}


    @Override
    public String toString() {
        return _environment.toString();
    }

}
