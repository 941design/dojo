from math import factorial as fac


def perm(n, write=lambda s: print(s, end='')):
    '''
    https://rosalind.info/problems/perm/

    ::start:learnings::
    Learned a lot about permutations along the way.
    Could not recall the adjacent swap algo I implemented >10 years ago as a student.
    Next time I will implement Heap's Algorithm or Johnson-Trotter.
    ::end:learnings::

    >>> perm(3)
    6
    1 2 3
    1 3 2
    2 1 3
    2 3 1
    3 1 2
    3 2 1
    '''
    k = fac(n)
    write(f'{k}\n')
    g = permutations_lexicographically(sorted(range(1, n+1), reverse=True))
    for _ in range(k):
        write(' '.join(str(x) for x in next(g)) + '\n')


def swap(xs, i, j):
    '''
    >>> swap('abc', 1, 2)
    'acb'
    >>> swap((1, 2, 3), 0, 2)
    (3, 2, 1)
    >>> swap([1, 2, 3], 0, 1)
    [2, 1, 3]
    '''
    return swap(xs, j, i) if i > j else xs[:i] + xs[j:j+1] + xs[i+1:j] + xs[i:i+1] + xs[j+1:]


def sort(xs):
    '''
    >>> sort('bac')
    'abc'
    >>> sort([2, 1, 0])
    [0, 1, 2]
    >>> sort((1, 2, 0))
    (0, 1, 2)
    '''
    if type(xs) is str: return ''.join(sorted(xs))
    elif type(xs) is tuple: return tuple(sorted(xs))
    else: return sorted(xs)


def permutations_lexicographically(xs):
    ''' generates permutations in lexicographic order (indefinitely)

    >>> perm = permutations_lexicographically
    >>> next(perm([1, 2, 4, 3]))
    [1, 3, 2, 4]
    >>> next(perm([1, 4, 2, 3]))
    [1, 4, 3, 2]
    >>> next(perm([4, 1, 2, 3]))
    [4, 1, 3, 2]

    >>> import itertools
    >>> xs = tuple(range(7))
    >>> g = perm(tuple(reversed(xs)))
    >>> ys = [next(g) for _ in range(fac(7))]
    >>> zs = list(itertools.permutations(xs))
    >>> ys == zs
    True
    '''
    while True:
        
        # find largest index i where xs[i-1] < xs[i]
        # i.e. first index from right to left which is
        # NOT in reverse lexical order
        i = len(xs)
        while True:
            i -= 1
            if i == 0: # xs is in reverse lexical order, start from beginning
                xs = sort(xs)
                i = len(xs)-1
                yield xs
                break
            if xs[i] > xs[i-1]:
                break
        
        # find smallest x[j], j >= i where x[i-1] < x[j]
        for x in sort(xs[i:]):
            if x > xs[i-1]: break
        j = xs.index(x)
        
        # xs[j] is to advance to xs[i-1]
        xs = swap(xs, i-1, j)

        # tail is sorted lexicographically:
        xs = xs[:i] + sort(xs[i:])
        
        yield xs
