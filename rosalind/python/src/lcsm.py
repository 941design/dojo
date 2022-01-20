from parsers import FastaParser


def lcsm(f):
    '''
    https://rosalind.info/problems/lcsm/
    
    >>> import io
    >>> NL = chr(10)
    >>> f = io.StringIO('>Rosalind_1' + NL \
    + 'GATTACA' + NL \
    + '>Rosalind_2' + NL \
    + 'TAGACCA' + NL \
    + '>Rosalind_3' + NL \
    + 'ATACA')
    >>> lcsm(f) in ['AC', 'TA', 'CA']
    True
    '''
    xs = [dna for _,dna in FastaParser(f).parseTuples()]
    return longest_common_substring(*xs)


def longest_common_substring(fst, *xs):
    '''
    >>> longest_commons_substring(['foobar', 'foo'])
    'foo'
    >>> longest_commons_substring(['foobar', 'foobaz'])
    'fooba'
    >>> longest_commons_substring(['xxxfoo', 'foobar', 'foobaz'])
    'foo'
    '''
    max = ''
    for i in range(len(fst)):
        l = 1
        while i+l <= len(fst):
            prefix = fst[i:i+l]
            if not all(prefix in x for x in xs): break
            max = prefix if len(prefix) > len(max) else max
            l += 1
    return max


def common_substrings(fst, *xs):
    '''
    >>> common_substrings('')
    >>> common_substrings('o', 'x')
    >>> common_substrings('o', 'ox')
    {'o'}
    >>> common_substrings('o', 'xo', 'xox')
    {'o'}
    '''
    subs = set()
    for i in range(len(fst)):
        l = 1
        while i+l <= len(fst):
            prefix = fst[i:i+l]
            if all(prefix in x for x in xs): subs.add(prefix)
            else: break
            l += 1
    return subs or None
