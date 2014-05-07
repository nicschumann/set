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


##Using Set

Set has two principle interfaces. First, it provides a stage on which geometric objects are rendered, and can be interacted with.
This stage can be used to create points, lines, and planes, move these objects around in space, apply defined constraints between them, and
delete them.

Set also provides direct access to the *&lambda;const*, a small, interpreted programming language used as an intermediate representation in
Set. If the graphical interface is Set's Java source code, then *&lambda;const* is Set's JVM byte-code. Modifying the stage affects Set's
heap of geometry, constraints, and their applications - the runtime enforces a well-typed heap - and modifying the heap directly through the *&lambda;const*
interpreter should affect Set's stage (although much of this dialog is not connected as of now). Set exposes its intermediate representation through a
Command Line based Read-Evaluate-Print loop. The following subsections detail the functionality in the stage and in the REPL.

#####Stage

The stage has two modes, a **create** mode, and a **select** mode. The current mode is displayed in the stage's bottom right hand corner, as a flag. These modes can
be toggled between using the **s** keyboard key for select and the **c** keyboard key for create.




##Build

Set depends on Google's guava libraries for several collection classes, and the LWJGL game library - a java interface to OpenGL - for a low level rendering API.
As such, after being .jar'ed, Set must be run with the JVM flag -Djava.library.path={native.file.path.here}, where the native file path points to a directory of LWJGL's
native-code openGL implementations.

##Additional Resources

See resources in the /docs folder for more information.
See models in the /models folder for a formal specification of Set's behavior in the Alloy modeling language.