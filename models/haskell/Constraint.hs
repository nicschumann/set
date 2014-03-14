
{-# LANGUAGE GADTs #-}
{-# LANGUAGE RankNTypes #-}
{-# LANGUAGE DataKinds, KindSignatures #-}
{-# LANGUAGE PolyKinds, StandaloneDeriving #-}
{-# LANGUAGE TypeOperators, TypeFamilies #-}


module Constraint where
	data Nat = Zero | Succ Nat deriving (Eq,Ord,Show)
	type One = Succ Zero

	data RedBlack = Black  deriving (Eq,Ord,Show)

	data RedBlackTree a = forall n. T ( Node Black n a )
	deriving instance (Show a) => Show (RedBlackTree a)

	data Node :: RedBlack -> Nat -> * -> * where
		Leaf :: forall (a :: *) . Node Black One a
		B :: forall ( a :: * ) .
			 forall (c1 :: RedBlack) .
			 forall (c2 :: RedBlack) .
			 forall ( n :: Nat ) . Node c1 n a -> a -> Node c2 n a -> Node Black (Succ n) a
		R :: forall ( a :: * ) .
			 forall ( n :: Nat ) . Node Black n a -> a -> Node Black n a -> Node Red n a

	data Context :: Nat -> RedBlack -> Nat -> * -> * where
		Root :: Context n Black n a 
		BC :: Bool -> a -> Node Black n a -> Context


	