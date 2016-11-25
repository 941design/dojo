# https://www.codewars.com/kata/help-suzuki-pack-his-coal-basket/

# learnings:
# + different power-set implementations
# + itertools.combinations


import re

def pack_basket(basket, pile):
    """
    >>> pack_basket(50, 'dust83dust 45 25 22 46')
    'The basket weighs 47 kilograms'
    """
    pieces = [int(s) for s in re.findall('\d+', pile) if int(s) <= basket]
    max = 0
    for s in depth_first_power_set(pieces):
        if max < sum(s) <= basket:
            max = sum(s)
            if max == basket:
                break
    return 'The basket weighs {} kilograms'.format(max)

def depth_first_power_set(coll):
    """
    >>> sorted(depth_first_power_set([23]))
    [(), (23,)]
    >>> sorted(depth_first_power_set([1, 2]))
    [(), (1,), (1, 2), (2,)]
    >>> sorted([''.join(s) for s in depth_first_power_set('abc')])
    ['', 'a', 'ab', 'abc', 'ac', 'b', 'bc', 'c']
    """
    yield ()
    ps = [()]
    for item in coll:
        yield from [s + (item,) for s in ps]
        ps += [s + (item,) for s in ps]

def pack_basket_1(basket, pile):
    """
    >>> pack_basket_1(50, 'dust83dust 45 25 22 46')
    'The basket weighs 47 kilograms'
    """    
    pieces = [int(s) for s in re.findall('\d+', pile) if int(s) <= basket]
    weights = [sum(s) for s in power_set(pieces) if sum(s) <= basket]
    return 'The basket weighs {} kilograms'.format(sorted(weights)[-1])

def power_set(coll):
    """
    >>> sorted(power_set([23]))
    [(), (23,)]
    >>> sorted(power_set([1, 2]))
    [(), (1,), (1, 2), (2,)]
    >>> sorted([''.join(s) for s in power_set('abc')])
    ['', 'a', 'ab', 'abc', 'ac', 'b', 'bc', 'c']
    """
    if not coll:
        return [()]
    first, rest = coll[0], coll[1:]
    sets = power_set(rest)
    return [(first,) + s for s in sets] + sets

def pack_basket_2(basket, pile):
    """
    >>> pack_basket_2(50, 'dust83dust 45 25 22 46')
    'The basket weighs 47 kilograms'
    """
    pieces = [int(s) for s in re.findall('\d+', pile) if int(s) <= basket]
    weights = [sum(s) for s in conditional_power_set(pieces, lambda s: sum(s) <= basket)]
    return 'The basket weighs {} kilograms'.format(max(weights))

def conditional_power_set(coll, pred=lambda x: True):
    """
    >>> sorted(conditional_power_set([23]))
    [(), (23,)]
    >>> sorted(conditional_power_set([1, 2]))
    [(), (1,), (1, 2), (2,)]
    >>> sorted([''.join(s) for s in conditional_power_set('abc')])
    ['', 'a', 'ab', 'abc', 'ac', 'b', 'bc', 'c']
    """
    if not coll:
        return [()]
    first, rest = coll[0], coll[1:]
    sets = conditional_power_set(rest, pred)
    return [(first,) + s for s in sets if pred((first,) + s)] + sets

def pack_basket_3(basket, pile):
    """
    >>> pack_basket_3(50, 'dust83dust 45 25 22 46')
    'The basket weighs 47 kilograms'
    """    
    pieces = [int(s) for s in re.findall('\d+', pile) if int(s) <= basket]
    weights = [sum(s) for s in lazy_power_set(pieces) if sum(s) <= basket]
    return 'The basket weighs {} kilograms'.format(sorted(weights)[-1])

def lazy_power_set(coll):
    """
    >>> sorted(lazy_power_set([23]))
    [(), (23,)]
    >>> sorted(lazy_power_set([1, 2]))
    [(), (1,), (1, 2), (2,)]
    >>> sorted([''.join(s) for s in lazy_power_set('abc')])
    ['', 'a', 'ab', 'abc', 'ac', 'b', 'bc', 'c']
    """
    if not coll:
        yield ()
    else:
        first, rest = coll[0], coll[1:]
        yield from [(first,) + s for s in lazy_power_set(rest)]
        yield from lazy_power_set(rest)
