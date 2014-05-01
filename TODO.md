#####TODO List - Set

##Model
  - Finish Implementing the ConstraintMatrix class
      - finish the recursive solve procedure.

  - Update the ConstraintMap to allow constraint
    between single components.


##View

##Controller

in: Collection<g : Geometry>
  ->  Renderer
        new Structure<GLShape> l
      - for each g in Collection:
          ?type Geometry?
              - Relation?    -> add appropriate shape to l
              - Point?       -> add GLPoint to l.
  <-    return l
out: Collection<GLShape>
