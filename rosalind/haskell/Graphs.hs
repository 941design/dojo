module Graphs where

import qualified Data.Set as Set
import Data.Set (Set, empty, fromList, singleton, member, partition, union, insert)


-- | http://rosalind.info/problems/tree/
tree :: Integer -> [(Integer, Integer)] -> Int
tree n xs = length (connectedComponents n xs) - 1


-- | http://rosalind.info/problems/cc/
cc :: Integer -> [(Integer, Integer)] -> Int
cc n xs = length $ connectedComponents n xs


-- | Returns set of connected components
connectedComponents :: Integer -> [(Integer, Integer)] -> Set (Set Integer)
connectedComponents n xs = foldl f initial xs
  where
    -- | Starting with unconnected nodes, as given adjacency list only
    -- contains connected nodes:
    initial = fromList $ map singleton [1..n]
    -- | Refining connected components for each adjacency:
    f :: Set(Set Integer) -> (Integer, Integer) -> Set(Set Integer)
    f sets (a, b) = insert (Set.fold union (fromList [a, b]) pos) neg
      where
        (pos, neg) = partition (\s -> member a s || member b s) sets
