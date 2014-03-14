{-# LANGUAGE GADTs #-}
{-# LANGUAGE RankNTypes #-}

module BinarySearch where
	data Array a n where
		Array :: Nat n -> 
				 (forall i . Nat i -> Less i n -> a) ->
				 Array a n

	data Z
	data S a

	data Nat n where
		Zero :: Nat Z
		Succ :: Nat n -> Nat (S n)

	data Less a b where
		Basis :: Nat b -> Less Z (S b)
		Induc :: Less a b -> Less (S a) (S b)

	data Equal a b where
		Equal :: Equal a a

	type LTE a b = Either (Less a b) (Equal a b)

	data Middle a b c where
		Empty :: Nat a -> Middle a a a
		One :: Nat a -> Middle a a (S a)
		Divide :: Middle a b c -> Middle (S a) (S b) (S c)
		AddRight :: Middle Z b c -> Middle Z (S b) (S (S c))