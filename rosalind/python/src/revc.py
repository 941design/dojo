TRANSLATION = str.maketrans({'A': 'T', 'C': 'G', 'G': 'C', 'T': 'A'})


def reverseComplement(dna):
    '''
    https://rosalind.info/problems/revc/
    
    >>> reverseComplement('AAAACCCGGT')
    'ACCGGGTTTT'
    '''
    return dna[::-1].translate(TRANSLATION)
