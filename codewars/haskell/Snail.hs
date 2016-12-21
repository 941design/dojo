module Snail where

snail :: [[Int]] -> [Int]
snail [] = []
snail (xs:xss) = xs ++ (snail . rotateCCW $ xss)

rotateCCW = rotateCW . rotateCW . rotateCW

rotateCW :: [[a]] -> [[a]]
rotateCW ([]:xs) = []
rotateCW xss = [map head $ reverse xss] ++ (rotateCW $ map tail xss)
