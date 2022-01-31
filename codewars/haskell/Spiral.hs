module Spiral where


-- | https://www.codewars.com/kata/534e01fbbb17187c7e0000c6
-- create a spiral path in a square
--
-- learnings::
-- Found a very concise solution after completion, which required matrix rotation.
-- I had thought about it, but it didn't occur to me that reversal and transposition is sufficient.
spiralize :: Int -> [[Int]]
spiralize n = [[ if elem (x,y) path then 1 else 0 | y <- [0..n-1]] | x <- [0..n-1]]
  where
    -- vectors ::= [right, down, left, up, ...]
    vecs = cycle [(0,1), (1,0), (0,-1), (-1,0)]
    -- path lengths from outside to the inside, starting with one full line,
    -- then repeating two lines of the same length, until reaching the spiral's center.
    -- finally we need to distinguish squares of even width from uneven for the last line of length 1 if any.
    reps = [n-1] ++ concatMap (replicate 2) [n-1, n-3..2] ++ if even n then [1] else []
    -- creating path by repeatedly applying vectors to coordinates starting at (0,0) i.e. top left
    path = scanl add (0,0) $ concatMap (uncurry replicate) $ zip reps vecs
      where add (x,y) (v,w) = (x+v, y+w)


-- | inside out
-- this yields a different result and does not meet the requirement above that we always start with one full line.
spiral :: Int -> [String]
spiral 1 = ["0"]
spiral n = extend . rotate $ spiral (n-1)
  where rotate :: [[a]] -> [[a]]
        rotate m = [[m !! i !! j | i <- reverse [0..length m - 1]] | j <- [0..length m - 1]]
        extend :: [String] -> [String]
        extend m = replicate (length m + 1) '0' : map ((:) '.') m

