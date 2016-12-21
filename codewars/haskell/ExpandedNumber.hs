module Kata where

import Data.List (intercalate)

expandedForm :: Int -> String
expandedForm n = intercalate " + " $ map show $ filter ((/=) 0) $ expand n 1

expand :: Int -> Int -> [Int]
expand 0 _ = []
expand n k = expand d (10 * k) ++ [m * k]
  where
    (d, m) = divMod n 10
