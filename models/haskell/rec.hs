{-# LANGUAGE RankNTypes, GADTs, DataKinds, PolyKinds #-}

module Rec where
	data Nat :: * where
		Z :: Nat
		S :: Nat -> Nat

	data Ev :: Nat -> * where
		Ev_Z :: Ev Z
		Ev_SS :: forall n . Ev n -> Ev (S (S n))

	data Le :: Nat -> Nat -> * where
		Le_Z :: forall n . Le Z n
		Le_S :: forall n m . Le n m -> Le n ( S m )