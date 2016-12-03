module Codewars.Kata.PiApprox where

import Data.List (find)
import Data.Maybe (fromJust)


trunc10Dble :: Double -> Double
trunc10Dble d = (fromInteger $ truncate $ d * (10^10)) / (10.0^^10)
    
iterPi :: Double -> (Integer, Double)
iterPi epsilon = (i, trunc10Dble d)
  where (i, d) = fromJust $ find (\(_, p) -> abs (pi - p) <= epsilon) $ zip [0,1..] [x * 4 | x <- leibniz]

leibniz :: [Double]
leibniz = scanl (+) 0 $ map (uncurry (/)) $ zip (cycle [1,-1]) [1,3..]
