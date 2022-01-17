def hamm(s, t):
    '''
    https://rosalind.info/problems/hamm/
    
    >>> s = 'GAGCCTACTAACGGGAT'
    >>> t = 'CATCGTAATGACGGCCT'
    >>> hamm(s, t)
    7
    '''
    if len(s) != len(t): raise ValueError()
    return sum(1 for x,y in zip(s, t) if x != y)
