// a javascript implementation of a K-Dimensional Tree Structure


var KDTree = (function() {

	var DEBUG = true,
		fail = Error;
	/** @constructor
		@param k : int  -- an int representing the dimension of the KDTree
		@return a kdtree constructor, with kvalue = k,
				expecting a collection of KDPoint elements */
	return function( k ) {
		return function( kdps ) {
			var Node = function(value, left, right) {

				this.getValue = function() { return value; }
				this.setValue = function( val ) { value = val; }
				this.getLeftChild = function() { return left; }
				this.setLeftChild = function( val ) { left = val; }
				this.getRightChild = function() { return right; }
				this.setRightChild = function( val ) { right = val; }

				this.vector = function() {
					var vect = [];
					for ( var i = 0; i < value.arity(); i++ ) {
						vect[ i ] = value.getK( i );
					}
					return vect;
				}

				this.locates = function( c ) {
					if ( c.length != value.arity() ) return false;
					else {
						for (var i = 0; i < value.arity(); i ++ ) {
							if ( value.getK( i ) != c[ i ] ) return false;
						}
						return true;
					}
				}
				this.lookup = function( c, depth ) {
					if ( this.locates( c ) ) return value;
					else {
						if ( (c[depth] < value.getK( depth%k )) ) {
							if ( left.getValue() == undefined ) return null;
							else return left.lookup( c, depth+1 );
						} else {
							if ( right.getValue() == undefined ) return null;
							else return right.lookup( c, depth+1 );
						}
					}
				} 
				this.insert = function( v, depth ) {
					if ( DEBUG ) console.log( "invoked" );

					if ( v.equals( value ) ) return false; // duplicate member

					if ( DEBUG ) console.log( "non-duplicate" );

					if ( v.getK( depth%k ) < value.getK( depth%k ) ) {
						if ( DEBUG ) console.log( "less" );
						if ( left.getValue() == undefined ) {
							if ( DEBUG ) console.log( "left null" );
							left = new Node( v, new Node(undefined,null,null), new Node(undefined,null,null) ); return true;
						} else {
							return left.insert( v, depth+1 );
						}
					} else  {
						if ( right.getValue() == undefined ) {
							if ( DEBUG ) console.log( "right null" );
							right = new Node(v,new Node(undefined,null,null),new Node(undefined,null,null)); return true;
						} else {
							return right.insert( v, depth+1 );
						}
					}
				}
				this.print = function( tabbing ) {
					var str = tabbing;

					if ( value != undefined ) {
						str += value.print()+"\n";
						str += tabbing+"[L]: " +left.print( tabbing + "\t" )+"\n"; 
						str += tabbing+"[R]: " +right.print( tabbing + "\t" )+"\n"; 
						return str;
					}
					else return str + "Leaf"
						

				} 
			}

			var construct = function( kdps,depth ) {
				if (kdps.length == 0) return new Node(undefined, null, null);

				if (DEBUG) console.log("passed guard");

				kdps.sort( function( a,b ) {
					var aval = a.getK( depth % k ),
						bval = b.getK( depth % k );
					return ( aval - bval );
				});

				if (DEBUG) console.log("passed sorting");

				var m = median( kdps );

				if (DEBUG) console.log("computed median: "+m );

				var l = kdps.filter( function( a ) {
					return a.getK( depth%k ) < kdps[m].getK( depth%k );
				});
				if (DEBUG) console.log( l );
				var g = kdps.filter( function( a ) {
					return a.getK( depth%k ) >= kdps[m].getK( depth%k )
						   && !(a.equals( kdps[m] ));
				});
				if (DEBUG) console.log( g );

				if (DEBUG) console.log("filtered lists");

				return new Node(
					kdps[m], construct(l, depth+1), construct(g, depth+1)
				);
			}

			var remove = function( c, node, depth ) {
				if ( node.getValue() == undefined ) fail("KDTree::remove(): Deletion of non-existent Node");
				else if ( node.locates( c ) ) {
					if ( node.getRightChild() != null ) {
						node.setValue( minimum( node.getRightChild(), (depth%k), (depth+1)%k ) );
						node.setRightChild( remove( node.vector(), node.getRightChild(), depth+1 ) );
					} else if ( node.getLeftChild() != null ) {
						node.setValue( minimum( node.getLeftChild(), (depth%k), (depth+1)%k ) );
						node.setRightChild( remove( node.vector(), node.getLeftChild(), depth+1 ) );
						node.setLeftChild( new Node( undefined, null, null ) );
					} else {
						node = new Node( undefined, null, null );
					}
				} else if ( c[ depth%k ] < node.getValue().getK( depth%k ) ) {
					t.setLeftChild( remove( c, node.getLeftChild(), depth+1 ) );
				} else {
					t.setRightChild( remove( c, node.getRightChild(), depth+1 ) );
				}
				return node;
			}

			var minimum = function( node, dim, cd ) {
				if ( node.getValue() == undefined ) return undefined;
				if ( cd == dim ) {
					if ( node.getLeftChild().getValue() == undefined ) return node.getValue();
					else return minimum(node.getLeftChild(), dim, (cd+1)%k );
				} else {
					return least(
						minimum( node.getLeftChild(), dim, (cd+1)%k ),
						least( 
							minimum( node.getRightChild(), dim, (cd+1)%k ),
						    node.getValue(), dim
						), dim
					);
				}
			}

			var least = function( val1, val2, dim ) {
				return ( val1.getK( dim ) < val2.getK( dim ) ) ? val1 : val2;

			}

			var median = function( kdps ) { return Math.floor( kdps.length / 2 ); }


			var root = construct( kdps,0 ); 

			return {
				lookup : function( c ) {
					if ( c.length != k ) fail("KDTree::lookup(): Incorrect K Value");
					else return root.lookup( c,0 );
				},
				insert : function( v ) {
					if ( DEBUG ) console.log( "invoked" );

					if ( v.arity() != k ) fail("KDTree::insert(): Incorrect K Value");
					else return root.insert( v,0 );
				},
				remove : function( c ) {
					if ( c.length != k ) fail("KDTree::remove(): Incorrect K Value");
					else return remove(c, root, 0);
				},
				print : function() {
					return "\n"+root.print("");
				}
			};
		}
	}
})();




/**
 * Given a value to contain, and an array of coordinates, the KDPoint
 * function returns a point that encapsulates the data, can be located
 * in K-Dimensional space, and can be compared for equality.
 */
var KDPoint = function( value, ks ) {
		this.constructor = KDPoint;

		var local_arity = function() { return ks.length; };
		var local_getValue = function() { return value; }
		var local_getK = function( i ) {
			if ( i < ks.length && i >= 0 ) { return ks[ i ]; }
			else throw new Error( "No Such Coordinate: "+i+"." );
		};
		var local_equality = function( b ) {
			if ( local_arity() != b.arity() ) return false;
			else {
				for (var i = 0; i < local_arity(); i ++ ) {
					if ( local_getK( i ) != b.getK( i ) ) return false;
				}
				return true;
			}
		}
		var local_print = function() {
			var ret = value + ": ( ";
			for ( var i = 0; i < local_arity(); i++ ) {
				ret += local_getK( i ) + " "; 
			}
			return ret + ")";
		}

		this.arity = local_arity;
		this.getK = local_getK;
		this.getValue = local_getValue;
		this.equals = local_equality;
		this.print = local_print;
};


var make2DPoint = function( name, x, y ) { return new KDPoint( name, [x,y] ); }
var make3DPoint = function( name, x, y, z ) { return new KDPoint( name, [x,y,z] ); }






