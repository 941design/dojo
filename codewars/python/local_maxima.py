def pick_peaks(xs):
    '''
    https://www.codewars.com/kata/5279f6fe5ab7f447890006a7/python

    ::start:learnings::
    
    Although seemingly more concise than my 2015 solution below, this solution
    has a downside. There is a hidden complexity when comparing sublists,
    whereas the 2015 solution only needs a single pass.

    This is of practical relevance, for languages in which collection comparison
    is trivial to write.
    
    ::end:learnings::
    '''
    d = dict(pos=[], peaks=[])
    for i, xyz in enumerate(zip(xs, xs[1:], xs[2:])):
        x,y,z = xyz
        if x < y > z or x < y >= z and [z] * (len(xs)-i-2) > xs[i+2:]:
            d['pos'].append(i+1)
            d['peaks'].append(y)
    return d


def pick_peaks_2015(seq):
    '''
    https://www.codewars.com/kata/5279f6fe5ab7f447890006a7/python
    '''
    d = {'pos': [], 'peaks': []}
    for idx, val in peaks(seq):
        d['pos'].append(idx)
        d['peaks'].append(val)
    return d


def peaks(seq):
    '''
    >>> list(peaks([]))
    []
    >>> list(peaks([1]))
    []
    >>> list(peaks([1, 1]))
    []
    >>> list(peaks([1, 1, 1]))
    []
    >>> list(peaks([1, 2]))
    []
    >>> list(peaks([2, 1]))
    []
    >>> list(peaks([2, 1, 2]))
    []
    >>> list(peaks([1, 2, 2]))
    []
    >>> list(peaks([2, 2, 1]))
    []
    >>> list(peaks([1, 2, 1]))
    [(1, 2)]
    >>> list(peaks([1, 2, 2, 1]))
    [(1, 2)]
    >>> list(peaks([1, 2, 1, 2, 1]))
    [(1, 2), (3, 2)]
    >>> list(peaks([1, 2, 3, 1]))
    [(2, 3)]
    '''
    if len(seq) < 3:
        return
    prev = seq[0]
    max_val, max_idx = None, None
    for i, e in enumerate(seq):
        if not max_val is None and e < max_val:
            yield max_idx, max_val
            max_idx = None
            max_val = None
        if e > prev:
            max_idx = i
            max_val = e
        prev = e
