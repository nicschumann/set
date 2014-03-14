// a javascript implementation of a K-Dimensional Tree Structure


var KDTree = (function() {

	var DEBUG = true,
		fail = Error;
	/** @constructor
		@param k : int  -- an int representing the dimension of the KDTree
		@param expect : (a -> b) \/ undefined a function representing the type of object to parameterize over, 
						or else undefined for a heterogeneous tree
		@return a kdtree, with kvalue = k */
	return function( k ) {
		return function( kdps ) {
			var Node = function(value, left, right) {
				this.getValue = function() { return value; }
				this.getLeftChild = function() { return left; }
				this.getRightChild = function() { return right; }
				this.locates = function( c ) {
					if ( c.length != value.arity() ) return false;
					else {
						for (var i ;= 0; i < value.arity(); i ++ ) {
							if ( value.getK( i ) != c[ i ] ) return false;
						}
						return true;
					}
				}
				this.lookup = function( c, depth ) {
					if ( this.locates( c ) ) return value;
					else {
						if ( (c[i] < value.getK( depth%k )) ) {
							if ( left.getValue() == undefined ) return null;
							else return left.get( c, depth+1 );
						} else {
							if ( right.getValue() == undefined ) return null;
							else return right.get( c, depth+1 );
						}
					}
				} 
				this.insert = function( v, depth ) {
					if ( v.equals( this.value ) ) return false; // duplicate member

					if ( v.getK( depth%k ) < this.value.getK( depth%k ) ) {
						if ( left == null ) {
							left = v; return true;
						} else {
							return left.insert( v, depth+1 );
						}
					} else  {
						if ( right == null ) {
							right = v; return true;
						} else {
							return right.insert( v, depth+1 );
						}
					}
				}
				this.delete = function( c,depth ) {
					if ( this.locates( c ) ) { // found the point
						if ( right != null ) {

						} else if ( left!=null ) {

						} else {
							value = undefined;
						}
					} else {

					}
				} 
			}

			var construct = function( kdps,depth ) {
				if (kdps.length == 0) return new Node(undefined, null, null);

				kdps.sort( function( a,b ) {
					var aval = a.getK( depth % k ),
						bval = b.getK( depth % k );
					return aval - bval;
				});

				var m = median( kdps );

				var l = kdps.filter( function( a ) {
					return a.getK( depth%k ) < m.getK( depth%k );
				}),
				var g = kdps.filter( function( a ) {
					return a.getK( depth%k ) >= m.getK( depth%k )
						   && !(a.equals( m ));
				});

				return new Node(
					m, construct(l, depth+1), construct(r, depth+1)
				);
			}

			var median = function( kdps ) { return kdps.length / 2; }

			// lookup














			var root = construct( kdps,0 ); 

			return {
				lookup : function( c ) {
					if ( c.length != k ) fail("KDTree::lookup(): Incorrect K Value");
					else return root.lookup( c,0 );
				},
				insert : function( v ) {
					if ( v.arity() != k ) fail("KDTree::insert(): Incorrect K Value");
					else return root.insert( v,0 );
				}
			};
		}
	}
})();





var KDPoint = (function() {
	return function( value, ks ) {
		this.constructor = KDPoint;

		this.arity = function() {return ks.length;}
		this.getk = function( i ) {
			if ( i < ks.length && i >= 0 ) { return ks[ i ]; }
			else throw new Error( "No Such Coordinate: "+i+"." );
		};
		this.getValue = function() { return value; }
		this.equals = function( b ) {
			if ( this.arity() != b.arity() ) return false;
			else {
				for (var i ;= 0; i < this.arity(); i ++ ) {
					if ( this.getK( i ) != b.getK( i ) ) return false;
				}
				return true;
			}
		}
	};
})();
