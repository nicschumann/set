module GrammarDependent where
	{- An Implementation of System Lambda-PI, 
	   a candidate for the core language of Set -}

	data Variable 		= Var String Integer
	data Term 			= TLambda Variable Term Term
						| TPi Variable Term Term
						| TApp Term Term
						| TVar Variable
						| TKind Integer
						| TType

	data Judgment 		= J Term Term
	type Context 		= [Judgment]

	instance Show Variable where
		show (Var s i) = s ++ (show i)

	instance Eq Variable where
		(Var s i) == (Var s' i') = (s == s') && (i == i')

	instance Show Term where
		show (TLambda v ty te) = "\\" ++ (show v) ++ " : " ++(show ty) ++ " .[ " ++ (show te) ++ "]"
		show (TPi v ty te) = "||"++ (show v) ++" : "++ (show ty) ++" .( "++(show te)++")"
		show (TApp t t') = (show t)++" "++(show t')
		show (TVar v) = show v
		show TKind n = "Kind(" ++ show n ++ ")"
		show TType = "Type"

	instance Eq Term where
		(TLambda v t1 t2) == (TLambda v' t1' t2') = (v == v') && (t1 == t1') && (t2 == t2')
		(TPi v t1 t2) == (TPi v' t1' t2') = (v == v') && (t1 == t1') && (t2 == t2')
		(TApp t1 t2) == (TApp t1' t2') = (t1 == t1') && (t2 == t2')
		(TVar v) == (TVar v') = (v == v')
		(TKind n) == (TKind n') = (n == n')
		TType == TType = True
		_ == _ = False

	instance Show Judgment where
		show (J te ty) = (show te) ++" : "++ (show ty)

	instance Eq Judgment where
		(J a t) == (J a' t') = (a == a') && (t == t')


	wellformed 												:: Context -> Bool

	wellformed []											= True
	wellformed j@(Judge a@(TVar a') t@(TVar t')):c'			= j `notElem` c'
															  && (c' |- (J t TType)) || (c' |- (J t TKind))


	{-

	(|-)													:: Context -> Judgment -> Bool

	c |- (Judge TType TKind)								= wellformed c

	c |- j@(Judge a@(TVar a') t@(TVar t'))					= (wellformed c) && j `elem` c

	c |- (Judge (TPi x tA tB) TType)						= (c |- (J tA TType)) 
															  && ((J (TVar x) tA):c |- (J tB TType))

	c |- (Judge (TPi x tA tB) TKind)						= (c |- (J tA TType)) 
															  && ((J (TVar x) tA):c |- (J tB TKind))

	c |- (Judge (TLambda x tA t) (TPi x' tA' tB))			= (tA == tA')
															  && (c |- (J tA TType))
															  && ((J (TVar x') tA):c |- (J tB TType))
															  && ((J (TVar x) tA):c |- (J t tB))

	c |- (Judge (TApp t u) tB)								= (c |- (Judge t (TPi)))	





	-}

	-- enriched typechecking relation `|-`:
	(|-)													:: Context -> Term -> Either Term String
															{-
																           c wellformed
																	-------------------------- (T-Type)
																		c |- Type : Kind 0		
															-}
	c |- TType 												= case wellformed c of
																True 			-> Right (TKind 0)
																False 			-> Left "Kinding Error."	
															{-
																		      c wellformed
																	-------------------------------	(T-Sort)	
																	 	c |- Kind n : Kind (S n)
															-}
	c |- (TKind n) 											= case wellformed c of
																True 			-> Right (TKind $ succ n)
																False 			-> Left "Universe Inconsistency."
															{-
																	  c wellformed      x : A in c
																	-------------------------------- (T-Var)
																	           c |- x : A
															 -}
	c |- (TVar x)											= if wellformed c 
															  then case (lookup (TVar x) c) of
															  	   Just tA		-> Right tA
															  	   Nothing		-> Left "Unbound Identifier: " ++ (show x) ++ "."
															  else Left "Malformed Context."
															{-
																	 c |- A : Type   c, x : A |- B : Type        
																	-------------------------------------- ( T-Prod1 )
																	      c |- ( Pi x : A B ) : Type

																	 c |- A : Type   c, x : A |- B : Kind n       
																	-------------------------------------- ( T-Prod )
																	      c |- ( Pi x : A B ) : Kind n

															-}
	c |- (TPi x tA tB) 										= case ((c |- tA),((J (TVar x) tA):c |- tB)) of
																((Right Type),(Right Type))		-> Right Type
																((Right Type),(Right (Kind n)))	-> Right $ Kind n
																((Left s),(Left s')) 			-> Left s ++ "\n" ++ s'
																((Left s),_)					-> Left s
																(_,(Left s))					-> Left s

	c |- (TLambda x tA t)									= case (( c |- tA ),
																	((J (TVar x) tA):c |- t)) of 
																	((Right Type),(Right tB)) -> case ((J (TVar x) tA):c |- tB )
																									   Right Type -> Right (TPi x tA tB)
																									   Left s 	  -> Left s
																	((Left s),(Left s')) 	  -> Left s ++ "\n" ++ s'
																	((Left s),_)			  -> Left s
																	(_,(Left s))			  -> Left s	

	c |- (TApp t u)											= case ((c |- t),(c |- u)) of
																	((Right (TPi x tA tB)),(Right tA')) -> if (tA == tA') then Right $ subst u x tB
																										   else Left "Type Mismatch in application: "++ tA ++" not equal to "++ tA' ++" in "++ (TApp t u)
																	((Left s),(Left s')) 	  -> Left s ++ "\n" ++ s'
																	((Left s),_)			  -> Left s
																	(_,(Left s))			  -> Left s	



	subst :: Term -> Variable -> Term -> Term


	subst t x TType 											= TType
	subst t x (TKind n)											= TKind n
	subst t x y@(TVar z)										= if (x == z) then t else y
	subst t x app@(TApp s u)									= TApp $ (subst t x s) (subst t x u)
	subst t x pi@(TPi x tA tB)									= 























