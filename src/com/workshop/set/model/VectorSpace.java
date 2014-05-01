package com.workshop.set.model;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.ref.MDouble;

/**
 * Created by nicschumann on 4/17/14.
 */
public class VectorSpace {
    /**
     * A geometry set can be instantiated over a specific dimension. Instantiating a
     * geometry also determines equivalence classes over members of that geometry, and
     * constituted an inductive definition of the geometry on a specific dimension. This is
     * a way of introducing type level computation into the java hierarchy.
     *
     * @param dimension the dimension of the bases of this geometry; valid for dimension > 0;
     */
    public VectorSpace( int dimension, Gensym g ) { this.dimension = dimension; this.generator = g; }

    private int dimension;
    private Gensym generator;


    /**
     * the interface to objects that exist inside of this VectorSpace
     */
    public abstract class Geometry {
        /**
         * This method returns a the name of this geometric object. Each object
         * is required to have a unique name by the runtime system, so that the
         * CSP can track it, and so that the language can manipulate it. If the user does not
         * specify the name of the symbol, the runtime system generates one for it. This allows
         * equality on geometries to be decided using symbolic names.
         *
         *
         * @return a Symbol object representing this Geometry's name.
         */
        public abstract Symbol name();

        /**
         * this method returns the atomic names that are bound to values in this geometry.
         * for example, a vector X in R3 might have values x1 x2 x3. for X, values() returns the
         * map ( x1 -> n1, x2 -> n2, x3 -> n3 ), where the n_i are the numerical values of the
         * x_i.
         *
         * @return a mapping from component names to values.
         */
        public abstract Map<Symbol,MDouble> values();

        /**
         * this method returns the set of components that this geometry has named. for example,
         * a vector X in R3 might have the component set { x1, x2, x3 }.
         *
         * @return a set containing the components of this geometry
         */
        public abstract Set<Symbol> components();

        /**
         * Join creates a pointwise symbolic relation between this geometric element
         * and the supplied geometry. If the two geometries share the same structure,
         * then the relational join succeeds, if not, the join fails, and a GeometricFailure
         * exception is thrown.
         *
         * @param x the geometry to attempt to join.
         * @return a mapping from the symbols of this geometry into the corresponding symbols of x
         * @throws GeometricFailure in case the geometries have different internal structures
         */
        public abstract Map<Symbol,Symbol> join( Geometry x ) throws GeometricFailure;

        /**
         * This method returns the dimension of the VectorSpace in which this
         * geometry is defined.
         *
         * @return an integer representing the dimension of the vector
         */
        public int dimension() { return dimension; }
        
        public abstract Set<Geometry> getGeometries();
        
        public abstract double[] getPointArray();
        
        public abstract void setHighlight(boolean b);
        
        public abstract boolean getHighlight();

        public abstract String displayString();

    }

    /**
     * Basis of the geometric structure.
     * This class represents a location in n-dimensional space
     */
    public class Point extends Geometry {
        public Point( Symbol name, Map<Symbol,MDouble> components ) throws GeometricFailure {
            if ( components.size() != dimension ) throw new GeometricFailure( components.size() );

            this.name = name;
            this.namedComponents = new LinkedHashMap<>( components );

            int i = 0;
            for ( Map.Entry<Symbol,MDouble> e : namedComponents.entrySet() ) {
                this.components[ i ] = e.getValue();
                this.names[ i ] = e.getKey();
                i++;
            }
            this.init(); 
        }

        public Point( Symbol name, MDouble... components  ) throws GeometricFailure {
            if ( components.length != dimension ) throw new GeometricFailure( components.length );

            this.name = name;

            for ( int i = 0; i < components.length; i++ ) {

                Symbol s = generator.generate(name.toString() + "_" + String.valueOf(i));
                this.namedComponents.put( s, components[ i ] );
                this.components[ i ] = components[ i ];
                this.names[ i ] = s;

            }
            this.init(); 
        }

        public Point( Symbol name, List<MDouble> components  ) throws GeometricFailure {
            if ( components.size() != dimension ) throw new GeometricFailure( components.size() );

            this.name = name;

            for ( int i = 0; i < components.size(); i++ ) {

                Symbol s = generator.generate(name.toString() + "_" + String.valueOf(i));
                this.namedComponents.put( s, components.get( i ) );
                this.components[ i ] = components.get( i );
                this.names[ i ] = s;

            }
            this.init(); 
        }
        
        public void init(){
        	highlighted=false; 
        }

        private Symbol name;
        private Symbol[] names = new Symbol[ dimension ];
        private MDouble[] components = new MDouble[ dimension ];
        private Map<Symbol,MDouble> namedComponents = new LinkedHashMap<>();
        private boolean highlighted; 

        /**
         * getN_( i...n ) is defined.
         *
         * for an array of components in this vector, x1 ... xn,
         * select a component with subscript i and return it.
         *
         * @param i the subscript of the component to select
         * @return the double box represented by the corresponding X_i
         */
        public MDouble getN_( int i ) throws GeometricFailure {
            if ( i <= 0 || i > dimension ) throw new GeometricFailure( i );
            return components[ i - 1 ];
        }

        /**
         * getX_( i...n ) is defined.
         *
         * for an array of components in this vector, x1 ... xn,
         * select a component with subscript i and return it.
         *
         * @param i the subscript of the component to select
         * @return the Symbol represented by the corresponding X_i */
        public Symbol getX_( int i ) throws GeometricFailure {
            if ( i <= 0 || i > dimension ) throw new GeometricFailure( i );
            return names[ i-1 ];
        }

        /**
         * @return the name of this point
         */
        @Override
        public Symbol name() { return name; }

        /**
         * @return the ( component, value ) pairs contained in this point.
         */
        @Override
        public Map<Symbol,MDouble> values() { return namedComponents; }

        /**
         * @return the set of unknowns ( represented as symbols ) in this point
         */
        @Override
        public Set<Symbol> components() {
            HashSet<Symbol> set = new HashSet<>();
            for ( Map.Entry<Symbol,?> entry : namedComponents.entrySet() ) {
                set.add( entry.getKey() );
            }
            return set;
        }

        /**
         * given a geometry in this vector space, join attempts to map elements of this
         * geometry onto the supplied one. Given two structurally identical geometries, this
         * method returns a mapping from the symbols of the first into the symbols of the second,
         * ie, an image of the first in the second. If the two structures are not similar, no such
         * mapping exists, and the empty map is returned
         *
         * @param x a geometry in the vector space to attempt to join.
         * @return a mapping from this into x.
         */
        public Map<Symbol,Symbol> join( Geometry geom ) throws GeometricFailure {
            Map<Symbol,Symbol> m = new LinkedHashMap<>();
            try {
                if ( dimension != geom.dimension() ) throw new GeometricFailure( geom.dimension() );

                Point b = (Point)geom;
                for ( int i = 0; i < dimension; i++ ) {
                    m.put( getX_( i+1 ), b.getX_( i+1 ) );
                }

                return m;

            } catch ( ClassCastException _ ) {
                throw new GeometricFailure( geom.dimension() );
            }

        }

        /**
         * @return A string representation of this point.
         */
        @Override
        public String toString() {
            StringBuilder s = new StringBuilder( name.toString() ).append( " -> ( ");
            for ( MDouble X_i : components ) {
                s.append( X_i ).append(" ");
            }
            s.append( ")" );
            return s.toString();
        }
        
        @Override
        public Set<Geometry> getGeometries() {
        	return new HashSet<>(0);
        }
        
        @Override
        public double[] getPointArray() {
        	double[] array = new double[dimension];
        	try {
        		for (int i = 0; i < dimension; i++) {
        			array[i] = getN_(i+1).get();
        		}
        	} catch (GeometricFailure e) {
        		e.printStackTrace();
        	}
        	return array;
        }

		@Override
		public void setHighlight(boolean b) {
			highlighted = b;
		}

		@Override
		public boolean getHighlight() {
			return highlighted;
		}


		@Override
		public String displayString() {
			double[] pnts = getPointArray();
			String result = String.format("Point \"%s\": (%.2f, %.2f, %.2f)", name, pnts[0], pnts[1], pnts[2]);
			return result;
		}
	}

	public class Relation extends Geometry {
		public Relation(Symbol name, Geometry A, Geometry B) {
			this.name = name;
			this.A = A;
			this.B = B;
			this.init();
		}

		public Relation(Geometry A, Geometry B) {
			this.A = A;
			this.B = B;
			this.init();
		}

		public void init() {
			highlighted = false;
		}

		private Symbol name;
		private Geometry A;
		private Geometry B;
		private boolean highlighted;

		public Symbol name() {
			return name;
		}

		public Geometry domain() {
			return A;
		}

		public Geometry codomain() {
			return B;
		}

		@Override
		public Map<Symbol, MDouble> values() {
			Map<Symbol, MDouble> union = new LinkedHashMap<>();

			union.putAll(A.values());
			union.putAll(B.values());

			return union;
		}

		@Override
		public Set<Symbol> components() {
			Set<Symbol> set = A.components();
			set.addAll(B.components());
			return set;
		}

		@Override
		public Map<Symbol, Symbol> join(Geometry x) throws GeometricFailure {
			Map<Symbol, Symbol> m = new LinkedHashMap<>();
			try {
				if (dimension != x.dimension())
					throw new GeometricFailure(x.dimension());

				Relation b = (Relation) x;
				Map<Symbol, Symbol> ma = A.join(b.domain());
				Map<Symbol, Symbol> mb = B.join(b.codomain());

				m.putAll(ma);
				m.putAll(mb);

				return m;

			} catch (ClassCastException _) {
				throw new GeometricFailure(x.dimension());
			}
		}

		@Override
		public Set<Geometry> getGeometries() {
			Set<Geometry> geoms = new HashSet<>();
			geoms.add(A);
			geoms.add(B);
			return geoms;
		}

		@Override
		public double[] getPointArray() {
			double[] a = A.getPointArray();
			double[] b = B.getPointArray();
			double[] array = new double[a.length + b.length];
			System.arraycopy(a, 0, array, 0, a.length);
			System.arraycopy(b, 0, array, a.length, b.length);
			return array;
		}

		/**
		 * @return A string representation of this point.
		 */
		@Override
        public void setHighlight(boolean b) {
            highlighted = b;
        }

        @Override
        public boolean getHighlight() {
            return highlighted;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder(name.toString()).append(" -> ( ");
            s.append(A.toString()).append(", ").append(B.toString());
            s.append(" )");
            return s.toString();
        }

        @Override
        public String displayString() {
            String result = String.format("Relation \"%s\": {A: '%s', B: '%s'}", name, A.name(), B.name());
            return result;
        }
    }

	public class GeometricFailure extends Exception {
		public GeometricFailure(int i) {
			this.i = i;
		}

		private static final long serialVersionUID = 432234L;
		private int i;

		@Override
		public String getMessage() {
			return "GeometricFailure: subscripting error - dimension of vector space is " + dimension
					+ ", encountered a request for component " + i;
		}

		@Override
		public String getLocalizedMessage() {
			return getMessage();
		}
	}

	@Override
	public String toString() {
		return "Vector Space(" + dimension + ")";
	}

	// generators : Point
	public Point point(Symbol name, MDouble... comp) throws GeometricFailure {
		return new Point(name, comp);
	}

	public Point point(Symbol name, Map<Symbol, MDouble> comp) throws GeometricFailure {
		return new Point(name, comp);
	}

	public Point point(Symbol name, double... comp) throws GeometricFailure {
		List<MDouble> l = new LinkedList<>();
		for (double d : comp) {
			l.add(new MDouble(d));
		}
		return new Point(name, l);
	}

	public Relation relation(Symbol name, Geometry a, Geometry b) throws GeometricFailure {
		return new Relation(name, a, b);
	}
}
