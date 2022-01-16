from collections import Counter

from parsers import FastaParser


def gc_content(dna):
    '''
    >>> dna = 'CCACCCTCGTGGTATGGCTAGGCATTCAGGAACCGGAGAACGCTTCAGACCAGCCCGGACTGGGAACCTGCGGGCAGTAGGTGGAAT'
    >>> gc_content(dna)
    60.91954
    '''
    c = Counter(dna)
    return round(100 * (c['C'] + c['G']) / c.total(), 6)


def max_gc_content(file):
    '''
    https://rosalind.info/problems/gc/
    '''
    xs = [(k, v, gc_content(v)) for k,v in FastaParser(file).parseTuples()]
    xs.sort(key=lambda kvc: kvc[2], reverse=True)
    k, v, gc = xs[0]
    print(f'{k}\n{gc}')
