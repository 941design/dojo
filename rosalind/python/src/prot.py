from functools import reduce


CODONS = {
    'UUU': 'F', 'CUU': 'L', 'AUU': 'I', 'GUU': 'V',
    'UUC': 'F', 'CUC': 'L', 'AUC': 'I', 'GUC': 'V',
    'UUA': 'L', 'CUA': 'L', 'AUA': 'I', 'GUA': 'V',
    'UUG': 'L', 'CUG': 'L', 'AUG': 'M', 'GUG': 'V',
    'UCU': 'S', 'CCU': 'P', 'ACU': 'T', 'GCU': 'A',
    'UCC': 'S', 'CCC': 'P', 'ACC': 'T', 'GCC': 'A',
    'UCA': 'S', 'CCA': 'P', 'ACA': 'T', 'GCA': 'A',
    'UCG': 'S', 'CCG': 'P', 'ACG': 'T', 'GCG': 'A',
    'UAU': 'Y', 'CAU': 'H', 'AAU': 'N', 'GAU': 'D',
    'UAC': 'Y', 'CAC': 'H', 'AAC': 'N', 'GAC': 'D',
    'UAA': None,'CAA': 'Q', 'AAA': 'K', 'GAA': 'E',
    'UAG': None,'CAG': 'Q', 'AAG': 'K', 'GAG': 'E',
    'UGU': 'C', 'CGU': 'R', 'AGU': 'S', 'GGU': 'G',
    'UGC': 'C', 'CGC': 'R', 'AGC': 'S', 'GGC': 'G',
    'UGA': None,'CGA': 'R', 'AGA': 'R', 'GGA': 'G',
    'UGG': 'W', 'CGG': 'R', 'AGG': 'R', 'GGG': 'G'
}


def chunks(n, xs):
    '''
    >>> list(chunks(2, []))
    []
    >>> list(chunks(2, list(range(10))))
    [[0, 1], [2, 3], [4, 5], [6, 7], [8, 9]]
    >>> list(chunks(3, 'abcdefg'))
    ['abc', 'def', 'g']
    '''
    return [xs[i:i+n] for i in range(0, len(xs), n)]


def prot(rna):
    '''
    https://rosalind.info/problems/prot/
    
    >>> prot('AUGGCCAUGGCGCCCAGAACUGAGAUCAAUAGUACCCGUAUUAACGGGUGA')
    MAMAPRTEINSTRING
    '''
    s = ''
    for c in chunks(3, rna):
        if CODONS[c] is None: break
        s += CODONS[c]
    print(s)
