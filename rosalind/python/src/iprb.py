
def iprb(dd, dr, rr):
    '''
    https://rosalind.info/problems/iprb/

    >>> round(iprb(2, 2, 2), 5)
    0.78333
    >>> round(iprb(24, 21, 17), 5)
    0.80592
    '''
    pop1 = dd + dr + rr
    pop2 = pop1 - 1
    return (
        dd * 1
      + dr * ( dd + (dr-1)*3/4 + rr*1/2 ) / pop2
      + rr * ( dd + dr*1/2 ) / pop2
      ) / pop1
