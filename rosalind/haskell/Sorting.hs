module Sorting where

import Control.Monad.State


type Counter = State Int

inc :: Counter ()
inc = get >>= put . (+1)


-- | http://rosalind.info/problems/ins/
ins :: [Integer] -> Int
ins xs = swapCount
  where
    (sorted, swapCount) = runState (insertionSort xs) 0

insertionSort :: [Integer] -> Counter [Integer]
insertionSort xs = foldM f xs [1..length xs]
  where
    f :: [Integer] -> Int -> Counter [Integer]
    f xs n = do
      head <- sortHead (take n xs)
      return $ head ++ drop n xs

sortHead :: Ord a => [a] -> Counter [a]
sortHead [] = return []
sortHead [x] = return [x]
sortHead (x:y:xs) = do
  (z:zs) <- sortHead (y:xs)
  if x <= z
    then return (x:z:zs)
    else inc >> return (z:x:zs)
