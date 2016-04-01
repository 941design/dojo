import doctest


def iq_test(s):
    """
    >>> iq_test('1 1 2')
    3
    >>> iq_test('2 1 3 5')
    1
    >>> iq_test('1 2 3 5')
    2
    >>> iq_test('1 3 5 2')
    4
    >>> iq_test('2 2 1')
    3
    >>> iq_test('1 2 4 6')
    1
    >>> iq_test('2 1 4 6')
    2
    >>> iq_test('2 4 6 1')
    4
    """
    numbers = [ int(x) for x in s.split() ]
    fst, snd, trd = numbers[0], numbers[1], numbers[2]

    if odd(fst) == odd(snd):
        pred = even if odd(fst) else odd
    else:
        pred = even if odd(trd) else odd

    return index_where(pred, numbers) + 1


def even(n):
    return n % 2 == 0


def odd(n):
    return n % 2 != 0


def index_where(pred, coll):
    for x, i in zip(coll, range(len(coll))):
        if pred(x):
            return i
    return None
