# https://www.codewars.com/kata/loose-change-part-2

def loose_change(coins, amount):
    """
    >>> loose_change([1, 5, 10, 25], 37)
    4
    >>> loose_change([1, 3, 4], 6)
    2
    >>> loose_change([1, 4, 5, 10], 8)
    2
    >>> loose_change([1,2,5,10,20,50,100,200], 93)
    5
    """
    totals = set(coins)
    depth = 1
    while not amount in totals:
        totals.update(set([c + t for c in coins for t in totals]))
        depth += 1
    return depth
