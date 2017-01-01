module MoleculeToAtoms where

import Text.Parsec
import Text.Parsec.String (Parser)


parseMolecule :: String -> Either String [(String,Int)]
parseMolecule formula = case parse molecule "" formula of
  Left e -> Left (show e)
  Right x -> Right x

mergeCount :: Eq a => [(a, Int)] -> [(a, Int)] -> [(a, Int)]
mergeCount xs [] = xs
mergeCount xs (y:ys) = mergeCount (updateOrDefault (p y) (f y) y xs) ys
  where
    p x y = fst x == fst y
    f (a, n) (_, k) = (a, n + k)

updateOrDefault :: (a -> Bool) -> (a -> a) -> a -> [a] -> [a]
updateOrDefault p f x [] = [x]
updateOrDefault p f x (y:ys) = if p y then f y:ys else y:updateOrDefault p f x ys
  
molecule :: Parser [(String, Int)]
molecule = ( mergeCount <$> simpleExpr <*> option [] molecule )
       <|> ( mergeCount <$> parenExpr  <*> option [] molecule )

simpleExpr :: Parser [(String, Int)]
simpleExpr = (:[]) <$> atom 

parenExpr :: Parser [(String, Int)]
parenExpr = do
  m <- '(' `mol` ')' <|> '[' `mol` ']' <|> '{' `mol` '}'
  n <- amount
  return $ fmap (\(s, k) -> (s, k * n)) m
  where
    mol open close = between (char open) (char close) molecule

atom :: Parser (String, Int)
atom = do
  s <- upper
  t <- many lower
  n <- amount
  return (s:t, n)

amount :: Parser Int
amount = option 1 $ many1 digit >>= return . read
