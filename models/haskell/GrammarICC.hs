module GrammarICC where
	import Data.Set

	data Variable 		= V String Integer
	data Sort			= Prop | Type Integer
	data Term			= TVar Variable
						| TSort Sort
						| EPi Variable Term Term
						| ELambda Variable Term Term
						| EApp Term Term
						| IPi Variable Term Term
						| ILambda Variable Term Term
						| IApp Term Term
	type Type 			= Term

	data Judgement 		= J Term Type
	type Context 		= [Judgement]

	instance Show Variable where
		show (V s i) = s ++ (show i)

	instance Eq Variable where
		(V s i) == (V s' i') = (s == s') && (i == i')

	instance Show Sort where
		show Prop = "Prop"
		show (Type i) = "Type("++ (show i) ++")"

	instance Eq Sort where
		Prop == Prop = True
		(Type i) == (Type i') = i == i'
		_ == _ = False

	instance Show Term where
	 	show (TVar v) = show v
	 	show (TSort s) = show s
	 	show (EPi v t1 t2) = "Pi("++(show v)++" : "++(show t1)++")." ++ (show t2)
	 	show (IPi v t1 t2) = "Pi["++(show v)++" : "++(show t1)++"]." ++ (show t2)
	 	show (ELambda v t1 t2) = "Pi("++(show v)++" : "++(show t1)++")." ++ (show t2)
	 	show (ILambda v t1 t2) = "Pi["++(show v)++" : "++(show t1)++"]." ++ (show t2)
	 	show (EApp t1 t2) = (show t1) ++" "++ (show t2)
	 	show (IApp t1 t2) = (show t1) ++" [ "++ (show t2) ++" ]"

	instance Eq Term where
	 	(TVar v) == (TVar v') 				= v == v'
	 	(TSort s) == (TSort s') 			= s == s'
	 	(EPi v t1 t2) == (EPi v' t1' t2')	= (v == v') && (t1 == t1') && (t2 ==t2') 
	 	(IPi v t1 t2) == (IPi v' t1' t2')	= (v == v') && (t1 == t1') && (t2 ==t2') 
	 	(ELambda v t1 t2) == (ELambda v' t1' t2')	= (v == v') && (t1 == t1') && (t2 ==t2') 
	 	(ILambda v t1 t2) == (ILambda v' t1' t2')	= (v == v') && (t1 == t1') && (t2 ==t2') 
	 	(EApp t1 t2) == (EApp t1' t2') 		= (t1 == t1') && (t2 == t2')
	 	(IApp t1 t2) == (IApp t1' t2') 		= (t1 == t1') && (t2 == t2')

	instance Show Judgement where
	 	show (J te ty) = (show te) ++" : "++ (show ty)

	instance Eq Judgement where
	 	(J te ty) == (J te' ty') = (te == te') && (ty == ty')

	fv 						:: Term -> Set Variable 
	fv (TSort s)			= empty
	fv (TVar v)				= singleton v
	fv (EPi v t1 t2)		= (union (fv t1) (fv t1)) \\ (singleton v)
	fv (IPi v t1 t2)		= (union (fv t1) (fv t1)) \\ (singleton v)
	fv (ELambda v t1 t2)	= (union (fv t1) (fv t1)) \\ (singleton v)
	fv (ILambda v t1 t2)	= (union (fv t1) (fv t1)) \\ (singleton v)
	fv (EApp t1 t2)			= union (fv t1) (fv t2)
	fv (IApp t1 t2)			= union (fv t1) (fv t2)

	dv 						:: Context -> Set Variable
	dv []					= empty
	dv (J te ty):c 			= insert te (dv c)




	-- the wellformed relation
	(-|)					:: 	Context -> Bool
	-| []					= 	True
	-| (J te ty):c			= 	(notMember te (dv c))
















