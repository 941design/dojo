module RPN where

import Data.Char ( isDigit )
import Data.Maybe ( fromMaybe, listToMaybe )


-- | https://www.codewars.com/kata/52f78966747862fc9a0009ae/haskell
-- reverse polish calculator
--
-- learnings::
-- Can be achieved with a fold, although error handling is more cumbersome.
-- Very nice solutions out there.
calc :: String -> Double
calc = value . fromMaybe (Val 0.0) . listToMaybe . parse . tokenize
  where
    parse :: [Token] -> [Token]
    parse xs@([])                           = xs
    parse xs@(Val _ : [])                   = xs
    parse xs@(Val a : Val b  : Op _ f : ys) = parse $ Val (f a b) : ys
    parse xs@(Val _ : Val _  : Val _  :  _) = parse $ head xs : parse (tail xs)
    parse xs@(Val _ : Op _ _ : _)           = xs
    parse xs = error $ show xs

data Token = Op String (Double -> Double -> Double)
           | Val Double

instance Show Token where
  show (Op s _) = "Op(" ++ s ++ ")"
  show (Val d) = "Val(" ++ show d ++ ")"

value :: Token -> Double
value (Val d) = d
value (Op _ _) = undefined

tokenize :: String -> [Token]
tokenize [] = []
tokenize (' ':xs) = tokenize xs
tokenize ('+':xs) = Op "+" (+) : tokenize xs
tokenize ('-':xs) = Op "-" (-) : tokenize xs
tokenize ('*':xs) = Op "*" (*) : tokenize xs
tokenize ('/':xs) = Op "/" (/) : tokenize xs
tokenize (x:xs)
  | isDigit x = Val (read n) : tokenize ys
  | otherwise = error $ "no parse: " ++ (x:xs)
    where (n, ys) = span (isDigit ||| (==) '.') (x:xs)
          (|||) f g x = f x || g x
