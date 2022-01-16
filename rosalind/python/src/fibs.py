from itertools import islice


def take(n, g):
    '''
    >>> take(5, range(0, 100, 2))
    [0, 2, 4, 6, 8]
    '''
    return list(islice(g, n))


def drop(n, g):
    take(n, g)
    return g


def fibs_iterative():
    ''' generates fibonacci sequence
    
    >>> g = fibs_iterative()
    >>> [next(g) for _ in range(10)]
    [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
    '''
    n_0 = 0
    yield n_0
    n_1 = 1
    while True:
        yield n_1
        n_0, n_1 = n_1, n_0 + n_1


def fibs_recursive(n=0, k=1):
    ''' generates fibonacci sequence
    
    >>> g = fibs_recursive()
    >>> [next(g) for _ in range(10)]
    [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
    '''
    yield n
    yield from fibs_recursive(k, n+k)


def rabbits(n, k):
    '''
    https://rosalind.info/problems/fib/

    >>> rabbits(5, 3)
    19
    '''
    def g(init=0, succ=1):
        yield init
        yield from g(succ, k*init + succ)
    return next(drop(n, g()))  # first element is 0
