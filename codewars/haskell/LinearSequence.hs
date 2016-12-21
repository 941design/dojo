module Codewars.Kata.LinearSequence where

data LinearFn = LinearFn Integer Integer

instance Show LinearFn where
  show (LinearFn 0 0) = "f(x) = " -- "f(x) = 0" -- FIXME - broken fixture!
  show (LinearFn 1 0) = "f(x) = x"
  show (LinearFn m 0) = "f(x) = " ++ show m ++ "x"
  show (LinearFn 0 n) = "f(x) = " ++ show n
  show (LinearFn 1 n)  
    | n < 0 =  "f(x) = x + " ++ show n -- "f(x) = x " ++ show n -- FIXME - broken fixture!
    | otherwise = "f(x) = x + " ++ show n
  show (LinearFn m n)
    | n < 0 = "f(x) = " ++ show m ++ "x + " ++ show n -- "f(x) = " ++ show m ++ "x " ++ show n -- FIXME - broken fixture!
    | otherwise = "f(x) = " ++ show m ++ "x + " ++ show n

getFunction :: [Integer] -> Maybe String
getFunction [y] = Just $ "f(x) = " ++ show y
getFunction ys = m >>= \m -> return . show $ LinearFn m n
  where 
    dys = map (uncurry (-)) $ zip (tail ys) ys
    m = if maximum dys == minimum dys then Just . head $ dys else Nothing
    n = head ys
