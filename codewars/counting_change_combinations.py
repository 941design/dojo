import doctest


def count_change(money, coins):
    """
    >>> count_change(1, [1])
    1
    >>> count_change(2, [1, 2])
    2
    >>> count_change(4, [1, 2])
    3
    """
    lookup = {}
    for m in range(1, money + 1):
        lookup[m] = change(m, coins, lookup)
        #print (lookup)
    return len(lookup[money])


def change(total, coins, lookup):
    """
    >>> change(1, [1], [])
    {(1,)}
    >>> change(3, [1], {1: [(1,)], 2: [(1, 1)]})
    {(1, 1, 1)}
    >>> change(3, [1], {2: [(2,)]})
    {(1, 2)}
    >>> sorted(change(3, [1], {2: [(1, 1), (2,)]}))
    [(1, 1, 1), (1, 2)]
    >>> change(3, [2], [])
    set()
    """
    coll = set()
    for coin in coins:
        rest = total - coin
        if rest == 0:
            coll.add((coin, ))
        elif rest in lookup:
            for x in lookup[rest]:
                coll.add(tuple(sorted(x + (coin, ))))
        else:
            pass
    return coll
