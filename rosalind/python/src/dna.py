from collections import Counter


def countACGT(s):
    '''
    https://rosalind.info/problems/dna/
    
    >>> countACGT('AGCTTTTCATTCTGACTGCAACGGGCAATATGTCTCTGTGTGGATTAAAAAAAGAGTGTCTGATAGCAGC')
    20 12 17 21
    '''
    c = Counter(s)
    print(' '.join(str(c[k]) for k in 'ACGT'))
