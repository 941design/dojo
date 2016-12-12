module Populations where


-- | http://rosalind.info/problems/fib/
fib :: Int -> Int -> Int
fib 0 _ = 0
fib 1 _ = 1
fib n k = fib (n - 1) k + k * fib (n - 2) k


-- | http://rosalind.info/problems/fibd/
fibd :: Int -> Int -> Integer
fibd n m = sum $ progression!!(n - 1)
  where
    initial = 1:replicate (m - 1) 0
    progression = iterate f initial
    f :: [Integer] -> [Integer]
    f (x:xs) = take m $ sum xs:x:xs
