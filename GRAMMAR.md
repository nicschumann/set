#LCONST(  )

The following is a short description of the grammar of LCONST, the intermediate language used by SET.


T1, T2      :=      lambda V : T1 . T2              ( TAbstraction )
            |       forall V : T1 . T2              ( TAll )
            |       sum V : T1 . T2                 ( TSum )
            |       T1 J T2                         ( TJudgement )
            |       T1 T2                           ( TApplication )
            |       Univ N                          ( TUniverse )
            |       Field                           ( TField )
            |       (V)                             ( TPattern )

V1, V2      :=      ( V1 V2 ... VN )                ( TVector )
            |       { V1, V2, ..., VN }             ( TSet )
            |       ( V1, V2 )                      ( TTuple )
            |       V1 O V2                         ( TOp )
            |       s                               ( TScalar )
            |       a                               ( TName )


##The statement of a constraint problem

A few key things allow us to admit theoretically problematic structures into our system,
most notably CYCLIC DEPENDENCIES. Imagine two geometric objects A and B in R2 (ie, 2D points ),
equipped with two projection maps each p1 : R2 -> R, and p2 : R2 -> R selecting the first and
second components of each vector respectively. Now let us impose a constraint pair that has a cyclic
definition -

    p1(A) = p2(B)               p1(A) - p2(B) = 0,          and conversely.

This gives us two horn-clause predicates encoding an equivalence.

    p1(A) = x <- p2(B) = x
    p2(B) = x <- p1(A) = x

We can prove, by induction on the values of p1(A) and p2(B) that this cyclic constraint will
always be maintained by the system. Assuming, as our basis, that p1(A) and p2(B) are introduced with values
that satisfy the constraint ( prove this later ), we can see only three possibilities of action on these
pairs. First, the values can be changed in tandem, by a uniform value. This happens when the user moves both points
simultaneously. In this case, the constraint is trivially satisfied, because both values are transformed by the same
factor - this is in fact the definition of simultaneous and equal motion. Otherwise, the user moves one point or the other
point. in this case, the moved point is clearly the predicating point, and the system simply rewrites the unmoved point in
terms of the moved point.