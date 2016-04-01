import doctest

COMPLEMENTS = { '(': ')',
                '[': ']',
                '{': '}' }


def group_check(s):
    """
    >>> group_check('(')
    False
    >>> group_check('()')
    True
    >>> group_check('([])')
    True
    >>> group_check('([])([]{}())')
    True
    """
    closes = lambda c, o: COMPLEMENTS[o] == c
    opened = ''
    for c in s:
        if c in '([{':
            opened += c
        elif opened and closes(c, opened[-1]):
            opened = opened[:-1]
        else:
            return False
    return opened == ''
