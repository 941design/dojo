def subs(f):
    '''
    https://rosalind.info/problems/subs/
    
    >>> import io
    >>> f = io.StringIO('GATATATGCATATACTT' + chr(10) + 'ATAT' + chr(10))
    >>> subs(f)
    2 4 10
    '''
    s, t = [line.strip() for line in f.readlines()]
    xs = all_substring_indices(s, t)
    print(' '.join(str(x + 1) for x in xs))


def all_substring_indices(s, t):
    '''
    >>> s = 'GATATATGCATATACTT'
    >>> t = 'ATAT'
    >>> list(all_substring_indices(s, t))
    [1, 3, 9]
    '''
    i = 0
    while i < len(s) - len(t):
        if s[i:i+len(t)] == t: yield i
        i += 1
