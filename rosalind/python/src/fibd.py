import numpy as np


def fibd(n, m):
    '''
    https://rosalind.info/problems/fibd/

    >>> fibd(6, 3)
    4
    '''
    if m < 1: raise ValueError
    pop = (m-1) * [0] + [1]
    for _ in range(n-1):
        pop = pop[1:] + [sum(pop[:-1])]
    return sum(pop)
