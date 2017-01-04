module Balanced.Parens where

-- | https://www.codewars.com/kata/all-balanced-parentheses
balancedParens :: Int -> [String]
balancedParens 0 = [""]
balancedParens n = combinations "(" (n - 1) n

combinations :: String -> Int -> Int -> [String]
combinations s 0 n = [s ++ replicate n ')']
combinations s k n
  | k == n = opening
  | otherwise = opening ++ closing
  where
    opening = combinations (s ++ "(") (k - 1) n
    closing = combinations (s ++ ")") k (n - 1)
