module Haskell.Codewars.SlidingPuzzleWorker where


-- | https://www.codewars.com/kata/sliding-tile-puzzle-worker/
slideTiles :: [[Int]] -> [Int] -> [[Int]]
slideTiles puzzle = foldl (\m i -> swap (0, i) m) puzzle
  where
    swap :: (Int, Int) -> [[Int]] -> [[Int]]
    swap (x, y) m = map (map f) m
      where f z
              | z == x = y
              | z == y = x
              | otherwise = z
            
