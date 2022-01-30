module Dups where

import Data.Char ( toLower )
import qualified Data.Set as Set

-- | https://www.codewars.com/kata/54b42f9314d9229fd6000d9c/train/haskell
duplicateEncode :: String -> String
duplicateEncode s = [ if Set.member c twice then ')' else '(' | c <- lower ]
  where lower = map toLower s
        fn (seen, twice) c
          | Set.member c seen = (seen, Set.insert c twice)
          | otherwise = (Set.insert c seen, twice)
        (_, twice) = foldl fn (Set.empty, Set.empty) lower
