module GrammarReflected where

	{-# LANGUAGE XConstraintKinds #-}

	{- DEFINITIONS -}

	{- This definition models T class syntactic items -}
	type Field 			= 		Double
	data Variable 		= 		Var String Integer

	data TExpr 			= 		TAbs PExpr TExpr TExpr 								-- Expressions of the form : lambda P : T1 . T2
			   			| 		TAppD TExpr TExpr									-- Expressions of the form : T1 T2	
			   			| 		TAppU TExpr TExpr									-- Expressions of the form : T1 [ T2 ]
			   			| 		TArr TExpr TExpr 									-- Expressions of the form : T1 -> T2	
			   			| 		TConj TExpr TExpr 									-- Expressions of the form : T1 /\ T2
			   			|		TDisj TExpr TExpr 									-- Expressions of the form : T1 \/ T2
			   			|		TEq TExpr TExpr 									-- Expressions of the form : T1 = T2
			   			|		TPat PExpr 											-- Expressions of the form : P
			   			|		TScalar Field
			   			| 		TUniv Integer
			   			|		TField
			   			| 		TBot

	data PExpr 			=		PVar Variable 										-- Expressions of the form : a
						| 		PRec [PExpr]										-- Expressions of the form : ( P1, ..., PN )
						|		PCon TExpr 											-- Constant patterns in the language

	data Context b c 	=  		CContains b c (Context b c)
						|  		CBot
						deriving (Eq,Show,Ord)

	instance Show TExpr where
		show (TAppD t1 t2) 		= (show t1) ++" "++ (show t2)
		show (TAppU t1 t2)		= (show t1) ++" [ "++ (show t2) ++" ]"
		show (TArr t1 t2) 		= (show t1) ++" -> "++ (show t2) 
		show (TConj t1 t2) 		= (show t1) ++" /\\ "++ (show t2)
		show (TDisj t1 t2) 		= (show t1) ++" \\/ "++ (show t2) 
		show (TEq t1 t2) 		= (show t1) ++" = "++ (show t2)
		show (TPat p) 			= show p
		show (TAbs p t1 t2) 	= "\\ "++ (show p) ++" : "++ (show t1) ++ " . ( "++ (show t2)++" )"
		show (TScalar s) 			= show s
		show (TUniv i)			= "Univ("++(show i)++")"
		show TField  			= "F"
		show TBot 				= "_|_"
			
	instance Show PExpr where
		show (PVar v) = show v
		show (PRec l) = "("++ (foldr (\x -> \y -> x ++ ", " ++ y ) "" (map show l) ) ++")"
		show (PCon c) = show c

	instance Show Variable where
		show (Var s i) = (s ++ show i)

	instance Eq TExpr where
		(TAbs p a b) == (TAbs p' a' b')	= (p == p') && (a == a') && (b == b')
		(TAppD a b) == (TAppD a' b')	= (a == a') && (b == b')
		(TAppU a b) == (TAppU a' b')	= (a == a') &&  (b == b')
		(TArr a b) == (TArr a' b')		= (a == a') &&  (b == b')
		(TConj a b) == (TConj a' b')	= (a == a') &&  (b == b')
		(TDisj a b) == (TDisj a' b')	= (a == a') &&  (b == b')
		(TEq a b) == (TEq a' b')		= (a == a') &&  (b == b')
		(TPat a) == (TPat a')			= (a == a')
		(TScalar s) == (TScalar s')		= (s == s')
		(TUniv i) == (TUniv i') 		= (i == i')
		(TField) == (TField) 			= True
		(TBot) == (TBot)				= True
		_ == _							= False	

	instance Eq PExpr where
		(PVar v) == (PVar v') 			= v == v'
		(PRec l) == (PRec l')  			= l == l'
		_ == _ 							= False

	instance Eq Variable where
		(Var s i) == (Var s' i') 		= (i == i')	&& (s == s')		

	{- TYPING Context -}

	typed :: Context PExpr TExpr -> PExpr -> Maybe TExpr
	typed (CContains a b c') v = if v == a then Just b else typed c' v
	typed CBot _ = Nothing					




	subst								:: Variable -> TExpr -> TExpr -> TExpr

	subst p t (TPat p') 				= TPat (subsubst p t p')

	subst p t (TAbs pa ty te) 			= TAbs (subsubst p t pa) ty (subst p t te)

	subst p t (TAppD t1 t2)				= TAppD (subst p t t1) (subst p t t2)

	subst p t (TAppU t1 t2)				= TAppU (subst p t t1) (subst p t t2)

	subst p t (TArr a b)				= TArr (subst p t a) (subst p t b)

	subst p t (TConj a b)				= TConj (subst p t a) (subst p t b)

	subst p t (TDisj a b)				= TDisj (subst p t a) (subst p t b)

	subst p t (TEq a b)					= TEq (subst p t a) (subst p t b)

	subst p t (TScalar s)				= TScalar s

	subst p t (TUniv n)					= TUniv n

	subst p t TField					= TField

	subst p t TBot						= TBot


	subsubst							:: Variable -> TExpr -> PExpr -> PExpr
										{- The substitution relation on PExprs -}
										{- p transistions to t in p' -}

	subsubst v t (PRec l)				= PRec (map (subsubst v t) l)

	subsubst v t (PVar v')				= if v == v' then (PCon t) else (PVar v')

	subsubst v t (PCon t')				= PCon t'


	x = (Var "x" 0)

	id_T = TAbs (PVar (Var "x" 0)) TField (TPat (PVar (Var "x" 0)))




	






	(|=)								:: Context PExpr TExpr -> TExpr -> Maybe TExpr  
										{- The |= (typable) relation implements a decision procedure
										which, given a specific context and a term to attempt to derive
										a type for, returns either the type of that term, or Nothing. -}

										{-
														 c, p1 : t1 |= t : t2
												   -------------------------------------			( T-Abs -- Discharge )
												   	c |= (\ p1 : t1 . t) : t1 -> t2

										-}
	c |= (TAbs p1 t1 t)					= case ((CContains p1 t1 c) |= t ) of 
												Just t2 					-> Just (TArr t1 t2) 
												Nothing 					-> Nothing
										{-
													c |= t1 : a -> b 		c |= t2 : a
												   -------------------------------------			( T-App1 -- Modus Ponens )
												              c |= t1 t2 : b
													
										-}
	c |= (TAppD t1 t2)					= case ( (c |= t1),(c |= t2) ) of
											   ((Just (TArr a b)), (Just a')) 	-> if ( a == a' ) then Just b else Nothing
											   _								-> Nothing
										{-
													c |= t1 : a -> b 
												    c |= a : a' -> b'	   c |= t2 : a'
												   -------------------------------------			( T-App2 -- Term Lifting )
												              c |= t1 [t2] : (a a') -> b
													
													It's possible we need to actually do an eval, here, to eliminate the epression.

										-}
	c |= (TAppU t1 t2)					= case ( (c |= t1),(c |= t2) ) of
											   (Just (TArr a b), Just c') 		-> case ( c |= a ) of 
											   								   			(TArr c d) 	-> if ( c == c' ) then Just (TArr d b) else Nothing
											   								   			_ 		 	-> Nothing
											   _ 							-> Nothing
									 	{-
													c |= t1 : Prop n 		c |= t2 : Prop n
												   ------------------------------------------ 		( T-Eq -- Term Equality )
												              c |= t1 = t2 : Prop n 				

									 	-}
	c |= (TEq t1 t2)					= case ((c |= t1),(c |= t1)) of 
											   (Just (TUniv i), Just (TUniv i')) 	-> if (i == i') then Just (Prop i) else Nothing
											   _ 									-> Nothing
									 	{-
													c |= t1 : Prop n 		c |= t2 : Prop n
												   ------------------------------------------ 		( T-Eq -- Term Conjunction )
												              c |= t1 /\ t2 : Prop n 				

									 	-}											   
	c |= (TConj t1 t2)					= case ((c |= t1),(c |= t1)) of 
											   (Just (TUniv i), Just (TUniv i'))  	-> if (i == i') then Just (TUniv i) else Nothing
											   _ 									-> Nothing
									 	{-
													c |= t1 : Prop n 		c |= t2 : Prop n
												   ------------------------------------------ 		( T-Eq -- Term Disjunction )
												  q            c |= t1 \/ t2 : Prop n 				

									 	-}											   
	c |= (TDisj t1 t2)					= case ((c |= t1),(c |= t1)) of 
											   (Just (TUniv i), Just (TUniv i'))  	-> if (i == i') then Just (TUniv i) else Nothing
											   _ 									-> Nothing
										{-
											
											       ------------										( T-Scal -- Scalar Typing )		
													c |= s : F				

									 	-}
	c |= (TScalar _)					= Just TField
										{-

													-------------------------- 						( T-Univ -- Universe Typing )
													 c |= Prop n : Prop (S n)

										-}
	c |= (TUniv n)						= Just (TUniv (succ n))
										{-

													-------------------------- 						( T-Univ -- Field Typing )
													 c |= TField : Prop 0

										-}
	c |= TField							= Just (TUniv 0)
										{-

													 c |- P
													--------										( T-Pat -- Pattern Typing )
													 c |= P	

										-}
	c |= (TPat p) 						= (c |- p)





	(|-) 								:: Context Variable TExpr -> PExpr -> Maybe TExpr
										{- The |- (pattern typing) relation implements a decision procedure
										which, given a specific context and avariable pattern to attempt to type
										, returns either the type of that term, or Nothing. -}

										{-
												
												----------------- 									( P-Var  -- Polymorphic Binders )
												 c |- a : \x . x

										-}
	c |- p@(PVar v)						= case (typed p) of 
		                                       Nothing 							-> let x = (freshname c) in Just (TAbs (PVar x) (TUniv 0) (TPat (PVar x)))
		                                       a 								-> a
										{-

												

										-}
	c |- (PRec l)						= let x = (freshname c) ;
											  body = 



										  in case body of 
										  	Just b' -> Just (TAbs (PVar x) (TUniv 0) b')
										  	_ -> Nothing


										  



	


											case (map (c |-) l) of 
											    p:ps -> case p of 
											    			Just x -> 


											    [] -> Nothing


















