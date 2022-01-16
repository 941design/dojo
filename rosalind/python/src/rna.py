def toRNA(dna):
    '''
    https://rosalind.info/problems/rna/
    
    >>> toRNA('GATGGAACTTGACTACGTAAATT')
    'GAUGGAACUUGACUACGUAAAUU'
    '''
    return dna.replace('T', 'U')
