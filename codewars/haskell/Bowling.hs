module Bowling where

bowlingScore :: [Int] -> Int
bowlingScore = sum . (frames 1)


-- must carry framecount, because [.. 10, 1, 0] is ambiguous.
frames :: Int -> [Int] -> [Int]
frames 10 (a:b:[])   = [a + b]
frames 10 (a:b:c:[]) = [a + b + c]
-- frames 9 (a:b:c:[])  = (a + b) : frames 10 (b:c:[])
frames n (a:b:c:xs)
  | strike a  = (a + b + c) : frames (n + 1) (b:c:xs)
  | spare a b = (a + b + c) : frames (n + 1) (c:xs)
  | otherwise = (a + b)     : frames (n + 1) (c:xs)
  where
    strike a = a == 10
    spare a b = a + b == 10
frames _ _ = undefined
