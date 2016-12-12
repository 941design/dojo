module Green where

import Data.List (find, isSuffixOf)
import Data.Maybe


-- | https://www.codewars.com/kata/last-digits-of-n-2-equals-equals-n/
green :: Int -> Integer
green = (!!) $ 0:1:merge (iterate fn 5) (iterate fn 6)
  where
    fn x = fromJust $ find isGreen $ tail [x,y..]
      where y = read ("1" ++ show x)


isGreen :: Integer -> Bool
isGreen x = (show x) `isSuffixOf` (show (x*x))


merge :: Ord a => [a] -> [a] -> [a]
merge [] [] = []
merge xs [] = xs
merge [] ys = ys
merge (x:xs) (y:ys)
  | x <= y = x:merge xs (y:ys)
  | x > y  = y:merge ys (x:xs)
    
