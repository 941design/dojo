module CalculatingWithFunctions (plus, minus, times, dividedBy, zero, one, two, three, four, five, six, seven, eight, nine) where

-- | https://www.codewars.com/kata/525f3eda17c7cd9f9e000b39/train/haskell

-- learnings ::
-- 1. `X :: (Int -> Int) -> Int` can be expressed as ($ x)
-- 2. there is a built-in identity function `id`
-- 3. lots of neat ways to express the operators
-- worth looking into another time

-- solution as submitted on 30.01.2022


identity :: a -> a
identity x = x


plus, minus, times, dividedBy :: ((Int -> Int) -> Int) -> (Int -> Int)
plus rhs = \lhs -> lhs + rhs identity
minus rhs = \lhs -> lhs - rhs identity
times rhs = \lhs -> lhs * rhs identity
dividedBy rhs = \lhs -> lhs `div` rhs identity


zero, one, two, three, four, five, six, seven, eight, nine :: (Int -> Int) -> Int
zero fn = fn 0
one fn = fn 1
two fn = fn 2
three fn = fn 3
four fn = fn 4
five fn = fn 5
six fn = fn 6
seven fn = fn 7
eight fn = fn 8
nine fn = fn 9
