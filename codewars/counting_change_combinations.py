import doctest


def count_change(money, coins):
    return len(change(money, coins))


def change(money, coins):
    """
    >>> change(2, [])
    []
    >>> sorted(change(2, [1]))
    [(1, 1)]
    >>> sorted(change(4, [1, 2]))
    [(1, 1, 1, 1), (1, 1, 2), (2, 2)]
    """
    if money == 0 or not coins:
        return []
    options = set()
    for coin in coins:
        if money == coin:
            options.add((coin, ))
        elif money - coin > 0:
            for x in change(money-coin, coins):
                options.add(tuple(sorted((coin, ) + x)))
    return options
