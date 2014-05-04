package com.workshop.set.model;

import com.workshop.set.model.interfaces.*;

import com.workshop.set.model.lang.core.*;
import com.workshop.set.model.lang.environments.Evaluation;
import com.workshop.set.model.VectorSpace.*;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.model.ref.MDouble;
import com.workshop.set.model.ref.MappableList;
import com.workshop.set.view.SetScreen;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Solver implements Model {
    public Solver( VectorSpace v, Gensym generator ) {

        _currentElements        = new HashSet<>();
        _currentSelections      = new HashSet<>();

        this._space              = v;
        this._generator          = generator;
        this._environment        = new Evaluation( generator );
        this._renderer           = new Renderer( _currentElements, _currentSelections );

    }

    /* Basis Objects :
    *  These objects are the foundations of the solution space */

    private VectorSpace         _space;
    private Evaluation          _environment;
    private Gensym              _generator;
    private Renderer            _renderer;

    /* Maps :
     * These objects are the mappings between terms and their vector representations */

    private Set<Geometry> _currentSelections;
    private Set<Geometry> _currentElements;


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

    @Override
    public synchronized void addGeometry( Geometry g ) throws
        ProofFailureException, TypecheckingException {
         // at this point we have a term representation,
         // with well-typed subterms, we can go ahead and type it.
         // its now well-typed an in the context

        Term t = geometryIntoTerm( g );
        _renderer.addGeometry(g);

    }



    public synchronized void addTerm( Term t ) {


    }


    /**
     * this removes the indicated geometry ONLY from the heap.
     * @param g the geometry to remove
     */
    @Override
    public void removeGeometry( Geometry g )
        throws ProofFailureException, TypecheckingException {
        Term term = _environment.getValue( g.name() );


    }

    /**
     * this removes the indicated geometry and ALL OF ITS DESCENDENTS from the heap.
     * @param g the geometry to remove
     */
    @Override
    public void removeGeometryAll(Geometry g ) {
        // this is actually very difficult. it required descending through the geometry, recursively removing all of
        // its atoms, and then checking to see that the heap's well-typing is still maintained.

    }


    @Override
    public void deleteSelections() {

    }







    private Map<Symbol,MDouble> getSymbolTable() {
        return null;
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





    public synchronized void renderGeometries() { _renderer.renderGeometries(); }

    public synchronized void checkIntersections(Point elmt, boolean shift) { _renderer.checkIntersections(elmt, shift); }

    public synchronized void setScreen(SetScreen main) { _renderer.setScreen( main ); }






  }
