#Set

**"Formal Reasoning Informs Reasoning About Form"**

Set is an implementation sketch of a Constraint-Based, Functional, Dependently-Typed, 3D Modeling program.
Instead of using a traditional representation of geometric objects by locating spacial boundaries and volumes in space -
as clouds of points that are only differentiated by their current locations in space - Set takes a notion of logical constraint
between geometric elements as primitive. That is to say, instead of representing a cube as a set of points { x1, x2, .., x8 },
that happen to be located in a cubic configuration at the time of viewing, Set represents a cube as a series of invariants
between geometries - ie. a set of parallel, perpindicular, and coincident lines. In this way, Set encodes the "logic" of what
it means to be Cube into the represented geometry, rather than relying on an incidental configuration of random points.
Being able to bake these invariants into a drawing allows the user to make sound logical judgements about their form - a key ability
when it comes to automated manufacturing, systems integration, and reasoning about form.

##Geometric Formalism

Instead of encoding points as locations, Set encodes points as ordered *n*-tuples of symbols, each of which varies over
the real numbers. The symbols in a point never change once the point is defined; their values may, as constraints are
imposed and satisfied by the solver.

Set uses a lightweight, inductive definition to introduce Geometry, as follows:



	x : Symbol    y : Symbol   z : Symbol
	------------------------------------- ( PointIntro )
	         ( x y z ) : Geometry



	    A : Geometry    B : Geometry
	------------------------------------- ( RelationIntro )
	        ( A, B ) : Geometry



All more complicated geometric structures are encoded as relations between points. For examples, a line is a simple
relation between points, a plane is a relation between lines, and a volume a relation between planes. Relations of
greater depth than this have more than 3 dimensions; one might visualize a relation between volumes ( ie, a relation
between relations between relations between relations between points ) as a transition function from the "A" volume into
the "B" volume. *NB*, Set does not implement this representation.

##Using Set

Set has two principle interfaces. First, it provides a stage on which geometric objects are rendered, and can be interacted
with. This stage can be used to create points, lines, and planes, move these objects around in space, apply defined
constraints between them, and delete them.

Set also provides direct access to the *&lambda;const*, a small, interpreted programming language used as an intermediate
representation in Set. If the graphical interface is Set's Java source code, then *&lambda;const* is Set's JVM byte-code.
Modifying the stage affects Set's heap of geometry, constraints, and their applications - the runtime enforces a well-typed
heap - and modifying the heap directly through the *&lambda;const* interpreter should affect Set's stage (although much
of this dialog is not connected as of now). Set exposes its intermediate representation through a Command Line based
Read-Evaluate-Print loop. The following subsections detail the functionality in the stage and in the REPL.

#####Stage

The stage has two modes, a **create** mode, and a **select** mode. The current mode is displayed in the stage's bottom
right hand corner, as a flag. These modes can be toggled between using the **s** keyboard key for **select** and the
**c** keyboard key for **create**. Set also currently contains two camera modes, which can be toggled between using the
**spacebar**. It provides an orthographic camera pointing down on the XY construction plane, and and a perspective camera
that orbits the Y axis. Both of these cameras can can be repositioned by clicking and dragging anywhere on the stage.
In perspective mode, the camera can also be made to orbit the Y axis, by right-clicking and dragging.

In **create** mode, the user can create points and lines. Clicking anywhere on the stage creates a point on the current
construction plane at that location (currently the XY Plane, with Z zero, is the only accessible construction plane).
Holding down shift and clicking twice creates two points with a relation (ie, a line) between them.

In **select** mode, points and lines can be selected by the user. Selecting a geometric object populates a menu in the
upper left hand corner of the stage, which displays vital statistics about that geometry. These data include whether the
geometry is a Point or a Relation, the name of that geometry, and, if it is a Point, its X, Y, and Z coordinate symbols,
otherwise its related sub-geometries. Coordinates and sub-geometry fields are text-fields which can be edited, points can
be relocated by changing their coordinates in these fields; relations can be re-related to other geometries by changing
the names of sub-geometries. The name field of each geometry can also be edited.

In general, geometries can be relocated by clicking and dragging them to a new location. Geometries move in a plane that
is coplanar with the XY plane, offset by their Z coordinate.

As geometries are selected a menu at the top left of the stage is populated with the constraints that are applicable to
the current selection. In order for a constraint to be applied, a pivot object must be set. This pivot acts as the fixed
point for the constraint, while the other constrained objects are allowed to vary with the constraint. When a constraint
appears on in the contextual menu, it can be applied with a click. Currently implemented constraints include:

-   Setting equal X coordinates between points


-   Setting equal Y coordinates between points


-   Setting equal Z coordinates between points


-   Constraining two lines to be parallel.


-   Constraining two lines to be perpendicular.


#####REPL

The REPL is a command-line based interface to Set's intermediate representation. It allows the heap to be accessed and
manipulated directly. The following is the list of recognized terms.

```shell set> :context```

Typing ":context" at the prompt instructs set to traverse and print the current heap. The heap is printed
as a mapping from symbolic names, into the types ascribed to those names, mapped to the values affixed to those names. It
is possible that a heap object may have no associated value if it is an assumption introduced into the heap by the user.
this




##Build

Set depends on Google's guava libraries for several collection classes, and the LWJGL game library - a java interface to
OpenGL - for a low level rendering API. As such, after being .jar'ed, Set must be run with the JVM flag
-Djava.library.path={native.file.path.here}, where the native file path points to a directory of LWJGL's native-code
openGL implementations.

##Additional Resources

-   See resources in the **/docs** folder for more information.

-   See models in the **/models** folder for a formal specification of Set's behavior in the Alloy modeling language.