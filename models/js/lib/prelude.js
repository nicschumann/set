// file prelude.js
// author nic schumann


// match : A, Array[A->[(Unit -> B) Option]] -> B
/* a simple case matching framework that extends
 * javascript's switch statement */
function match( testvalue, cases ) {
    if ( cases.length>0 ) {
        var matchfunction = cases[0]( testvalue ); cases.shift();
        if ( matchfunction!=false ) { return matchfunction( testvalue ); }
        else return match( testvalue, cases );
    } else throw new Error( "Match Failure: "+testvalue+" failed to match any supplied case." );
}

// matchcase : A -> -> (A->C->Boolean) -> (unit->B) -> A->(unit->B)
function matchcase( casevalue, predicate, truthvalue) {
    return function( testvalue ) {
        if ( predicate( testvalue, casevalue ) ) return truthvalue;
        else return false;
    };
}




// dispatcher class
// manage multiple event threads with namespaces in MD?
function Dispatcher() {

    // registerEvent : String -> Boolean
    this.registerEvent = function( evt ) {
        if ( this.listeners===undefined ) this.listeners = {};
        if ( this.listeners[ evt ]===undefined ) {
            this.listeners[ evt ] = [];
        }
        return true;
    };

    // unregisterEvent : String -> Boolean
    this.unregisterEvent = function( evt ) {
        if ( this.listeners!=undefined&&
             this.listeners[ evt ]!=undefined ) {
            this.listeners[ evt ] = undefined;
            return true;
        } else return false;
    };

    this.dispatchEvent = function( evt, args ) {
        if ( this.listeners===undefined||
             this.listeners[ evt ]===undefined ) return false;
        else {
            var eventChain = this.listeners[ evt ];
            for ( var i=0;i<eventChain.length;i++ ) { eventChain[ i ]( args ); }
        }
    }
    this.addEventListener = function( evt, continuation ) {
        if ( this.listeners===undefined||
             this.listeners[ evt ]===undefined ) return false;
        else {
            this.listeners[ evt ].push( continuation );
            return true;
        }
    }
    this.hasEventListener = function( evt, continuation ) {
        if ( this.listeners===undefined||
             this.listeners[ evt ]===undefined ) return false;
        else {
            for ( var i=0;i<this.listeners[ evt ].length;i++ ) {
                if ( this.listeners[ evt ][ i ]==continuation ) return true;
            }
        }
    }
    this.removeEventListener = function( evt, continuation ) {
        if ( this.listeners===undefined||
             this.listeners[ evt ]===undefined ) return false;
        else {
            for ( var i=0;i<this.listeners[ evt ].length;i++ ) {
                if ( this.listeners[ evt ][ i ]==continuation ) {
                    delete this.listeners[ evt ][ i ];
                    return true;
                }
            }
        }
    }
}



