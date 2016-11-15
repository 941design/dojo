"""
https://www.codewars.com/kata/simplifying-multilinear-polynomials

"""

import re

def simplify(poly):
    """
    >>> simplify('23')
    '23'
    >>> simplify('1+1')
    '2'
    >>> simplify('a+b')
    'a+b'
    >>> simplify('a-b')
    'a-b'
    >>> simplify('a+b-b')
    'a'
    >>> simplify('dc+dcba')
    'cd+abcd'
    >>> simplify('a+ac-ab')
    'a-ab+ac'
    >>> simplify('a+1')
    '1+a'
    >>> simplify('a-1')
    '-1+a'
    """
    terms = merge_terms(poly)
    return ''.join(['' if n == 0 else \
                    '+' + xyz if xyz and n == 1 else \
                    '-' + xyz if xyz and n == -1 else \                    
                    '+' + str(n) + xyz if n > 1 else \
                    str(n) + xyz
                    for xyz, n in terms]).lstrip('+')

def merge_terms(poly):
    d = {}
    for xyz, n in parse(poly):
        d.setdefault(xyz, 0)
        d[xyz] += n
    return sorted(d.items(), key=lambda tup: str(len(tup[0])) + tup[0])
    
def parse(poly):
    """
    >>> parse('')
    []
    >>> parse('23')
    [('', 23)]
    >>> parse('+23')
    [('', 23)]
    >>> parse('x')
    [('x', 1)]
    >>> parse('+x')
    [('x', 1)]
    >>> parse('-x')
    [('x', -1)]
    >>> parse('-2x')
    [('x', -2)]
    >>> parse('-2xy')
    [('xy', -2)]
    """
    return [(''.join(sorted(xyz)), int(op + n) if n else int(op + '1')) \
            for op, n, xyz in re.findall('([\+-]?)(\d*)(\w*)', poly) if n or xyz]
