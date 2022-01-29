module Dojo where


-- http://aperiodic.net/phil/scala/s-99/


-- | P01 (*) Find the last element of a list.
last :: [a] -> a
last [] = error "empty list"
last [x] = x
last (x:xs) = Dojo.last xs


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
reverse (x:xs) = (Dojo.reverse xs) ++ [x]


-- | P06 (*) Find out whether a list is a palindrome.
palindrome :: Eq a => [a] -> Bool
palindrome [] = True
palindrome xs = xs == Dojo.reverse xs


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


-- | P11 (*) Modified run-length encoding.
-- Not applicable, due to type constraints (unless using Either)


-- | P12 (**) Decode a run-length encoded list.
unpackl :: [(Int, a)] -> [a]
unpackl [] = []
unpackl ((n, x):xs) = (take n $ repeat x) ++ unpackl xs


-- | P13 (**) Run-length encoding of a list (direct solution).
packl2 :: Eq a => [a] -> [(Int, a)]
packl2 [] = []
packl2 (x:xs) = acc ++ [(i, c)]
  where
    (i, c, acc) = reduce fn (1, x, []) xs
    -- TODO - consider a right fold
    reduce :: (acc -> a -> acc) -> acc -> [a] -> acc
    reduce fn acc [] = acc
    reduce fn acc (x:xs) = reduce fn (fn acc x) xs
    fn (i, c, acc) x
      | c == x = (i+1, c, acc)
      | otherwise = (1, x, acc ++ [(i, c)])


-- | P14 (*) Duplicate the elements of a list.
dupl :: [a] -> [a]
dupl [] = []
dupl (x:xs) = [x, x] ++ dupl xs


-- | P15 (**) Duplicate the elements of a list a given number of times.
dupln :: Int -> [a] -> [a]
dupln _ [] = []
dupln n (x:xs) = (take n $ repeat x) ++ dupln n xs


-- | P16 (**) Drop every Nth element from a list.
dropnth :: Int -> [a] -> [a]
dropnth _ [] = []
dropnth n xs = [x | (i, x) <- zip [1..] xs, i `mod` n /= 0]


-- | P17 (*) Split a list into two parts.
splitn :: Int -> [a] -> ([a], [a])
splitn n xs = (take n xs, drop n xs)


-- | P18 (**) Extract a slice from a list.
slice :: Int -> Int -> [a] -> [a]
slice n k xs = take (k-n) $ drop n $ xs


-- | P19 (**) Rotate a list N places to the left.
rotate :: Int -> [a] -> [a]
rotate 0 xs = xs
rotate n xs
  | n > 0 = drop n xs ++ take n xs
  | otherwise = rotate ((l+n) `mod` l) xs
  where l = len xs


-- | P20 (*) Remove the Kth element from a list.
remn :: Int -> [a] -> [a]
remn _ [] = []  -- no error
remn 0 (x:xs) = xs
remn n (x:xs) = x:remn (n-1) xs


-- | P21 (*) Insert an element at a given position into a list.
insn :: a -> Int -> [a] -> [a]
insn a 0 [] = [a]
insn _ _ [] = [] -- no error
insn a 0 xs = a:xs
insn a n (x:xs) = x:insn a (n-1) xs


-- | P22 (*) Create a list containing all integers within a given range.
irange :: Int -> Int -> [Int]
irange n k = slice n k [0..]


-- | P49 (**) Gray code.
gray :: Int -> [String]
gray n = [padded $ bits i | i <- [0..2^n-1]]
  where bits 0 = "0"
        bits 1 = "1"
        bits n = (bits $ n `div` 2) ++ (bits $ n `mod` 2)
        padded s = (take (n - length s) $ repeat '0') ++ s
