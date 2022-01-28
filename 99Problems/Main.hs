module Main where


-- http://aperiodic.net/phil/scala/s-99/


-- TODO - adapt accordingly as lib
main :: IO()
main = undefined


-- | P01 (*) Find the last element of a list.
last :: [a] -> a
last [] = error "empty list"
last [x] = x
last (x:xs) = Main.last xs


-- | P02 (*) Find the last but one element of a list.
penultimate :: [a] -> a
penultimate [] = error "list too short"
penultimate [_] = error "list too short"
penultimate (x:y:[]) = x
penultimate (x:xs) = penultimate xs


-- | P03 (*) Find the Kth element of a list.
nth :: Int -> [a] -> a
nth 0 (x:xs) = x
nth i (x:xs) = nth (i - 1) xs


-- | P04 (*) Find the number of elements of a list.
len :: [a] -> Int
len [] = 0
len (x:xs) = 1 + len xs


-- | P05 (*) Reverse a list.
reverse :: [a] -> [a]
reverse [] = []
reverse [x] = [x]
reverse (x:xs) = (Main.reverse xs) ++ [x]


-- | P06 (*) Find out whether a list is a palindrome.
palindrome :: Eq a => [a] -> Bool
palindrome [] = True
palindrome xs = xs == Main.reverse xs


-- | P07 (**) Flatten a nested list structure.
flatten :: [[a]] -> [a]
flatten [] = []
flatten (xs:xss) = xs ++ flatten xss


-- | P08 (**) Eliminate consecutive duplicates of list elements.
remdup :: Eq a => [a] -> [a]
remdup [] = []
remdup [x] = [x]
remdup (x:y:xs) = if x == y then remdup (y:xs) else x:(remdup (y:xs))


-- | P09 (**) Pack consecutive duplicates of list elements into sublists.
pack :: Eq a => [a] -> [[a]]
pack [] = []
pack xs = [first] ++ pack remainder
  where first = takewhile (== head xs) xs
        remainder = dropwhile (== head xs) xs
        takewhile _ [] = []
        takewhile p (x:xs)
          | p x = x:takewhile p xs
          | otherwise = []
        dropwhile _ [] = []
        dropwhile p xs@(y:ys)
          | p y = dropwhile p ys
          | otherwise = xs


-- | P10 (*) Run-length encoding of a list.
packl :: Eq a => [a] -> [(Int, a)]
packl xs = [(len xs, head xs) | xs <- pack xs]


-- | P12 (**) Decode a run-length encoded list.
unpackl :: [(Int, a)] -> [a]
unpackl [] = []
unpackl ((n, x):xs) = (take n $ repeat x) ++ unpackl xs


-- | P49 (**) Gray code.
gray :: Int -> [String]
gray n = [padded $ bits i | i <- [0..2^n-1]]
  where bits 0 = "0"
        bits 1 = "1"
        bits n = (bits $ n `div` 2) ++ (bits $ n `mod` 2)
        padded s = (take (n - length s) $ repeat '0') ++ s
