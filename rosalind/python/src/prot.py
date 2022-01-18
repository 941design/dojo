from functools import reduce

from util import chunks
from util import CODONS


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
