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
