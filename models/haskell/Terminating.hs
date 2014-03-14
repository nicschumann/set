{-# LANGUAGE GADTs #-}
{-# LANGUAGE RankNTypes #-}

module Terminating where
	data Term t where
		Var :: Nat n -> Term (Var n)
		Lambda :: Term t -> Term (Lambda t)
		Apply :: Term t1 -> Term t2 -> Term (Apply t1 t2)

	data Nat n where
		Zero :: Nat Z
		Succ :: Nat n -> Nat (S n)

	data Z
	data S n

	data Var t
	data Lambda t
	data Apply t1 t2

	data Less n m where
		LessZero :: Less Z (S n)
		LessSucc :: Less n m -> Less (S n) (S m)

	data Equal a b where
		Equal :: Equal a a

	data Plus a b c where
		PlusZero :: Plus Z b b
		PlusSucc :: Plus a b c -> Plus (S a) b (S c)

	data Reduce t1 t2 where
		ReduceSimple :: Replace Z t1 t2 t3 -> Reduce (Apply (Lambda t1) t2) t3
		ReduceLambda :: Reduce t1 t2 -> Reduce (Lambda t1) (Lambda t2)
		ReduceApplyLeft :: Reduce t1 t2 -> Reduce (Apply t1 t3) (Apply t2 t3)
		ReduceApplyRight :: Reduce t1 t2 -> Reduce (Apply t3 t1) (Apply t3 t2)

	data Lift n k t1 t2 where
		LiftVarLess :: Less i k -> Lift n k (Var i) (Var i)
		LiftVarGTE :: Either (Equal i k) (Less k i) -> Plus i n r -> Lift n k (Var i) (Var r)
		LiftApply :: Lift n k t1 t1' -> Lift n k t2 t2' -> Lift n k (Apply t1 t2) (Apply t1' t2')
		LiftLambda :: Lift n (S k) t1 t2 -> Lift n k (Lambda t1) (Lambda t2)

	data Replace k t n r where
		ReplaceVarLess :: Less i k -> Replace k (Var i) n (Var i)
		ReplaceVarEq :: Equal i k -> Lift k Z n r -> Replace k (Var i) n r
		ReplaceVarMore :: Less k (S i) -> Replace k (Var (S i)) n (Var i)
		ReplaceApply :: Replace k t1 n r1 -> Replace k t2 n r2 -> Replace k (Apply t1 t2) n (Apply r1 r2)
		ReplaceLambda :: Replace (S k) t n r -> Replace k (Lambda t) n (Lambda r)

	data ReduceEventually t1 t2 where
		ReduceZero :: ReduceEventually t1 t1
		ReduceSucc :: Reduce t1 t2 -> ReduceEventually t2 t3 -> ReduceEventually t1 t3

	data Normal t where
		NormalVar :: Normal (Var n)
		NormalLambda :: Normal t -> Normal (Lambda t)
		NormalApplyVar :: Normal t -> Normal (Apply (Var i) t)
		NormalApplyApply :: Normal (Apply t1 t2) -> Normal t3 -> Normal (Apply (Apply t1 t2) t3)

	data Terminating where
		Terminating :: Term t -> ReduceEventually t t' -> Normal t' -> Terminating

	data Reducible t1 where
		Reducible :: Reduce t1 t2 -> Reducible t1

	type Infinite t1 = forall t2 . ReduceEventually t1 t2 -> Reducible t2

	data NonTerminating where
		NonTerminating :: Term t -> Infinite t -> NonTerminating

	test1 :: Terminating
	test1 = Terminating (Var Zero) ReduceZero NormalVar

	test2 :: Terminating
	test2 = Terminating (Apply (Lambda (Var Zero)) (Var Zero))














