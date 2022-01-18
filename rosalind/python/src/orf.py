from parsers import FastaParser
from util import chunks
from util import CODONS
from rna import toRNA  # TODO -- use python naming conventions
from revc import reverseComplement  # TODO -- use python naming conventions


def is_valid_codon(rna):  # TODO -- extract codon module
    return rna in CODONS


def is_start_codon(rna):  # TODO -- extract codon module
    return rna == 'AUG'


def is_stop_codon(rna):  # TODO -- extract codon module
    return rna in CODONS and CODONS[rna] is None


def codons(rna, stop='-'):
    # this should be modelled differently and concealed in a codon module
    return ''.join(CODONS[c] or '-' for c in chunks(3, rna, incomplete=False))


def orf(f):
    '''
    https://rosalind.info/problems/orf/

    free online converter: https://web.expasy.org/translate/
    '''
    for _, dna in FastaParser(f).parseTuples():
        print('\n'.join(set(open_reading_frames(dna))))


def open_reading_frames(dna):
    '''
    >>> dna = 'AGCCATGTAGCTAACTCAGGTTACATGGGGATGACCCCGCGACTTGGATTAGAGTCTCTTTTGGAATAAGCCTGAATGATCCGAGTAGCATCTCAG'
    >>> list(open_reading_frames(dna))
    ['MGMTPRLGLESLLE', 'MTPRLGLESLLE', 'M', 'M', 'MLLGSFRLIPKETLIQVAGSSPCNLS']
    '''

    def start_and_stop_indices(rna):
        ''' yields ([start_index], stop_index)
        where indices point into RNA and stop_index is inclusive
        '''
        starts = []
        for i, c in enumerate(chunks(3, rna, incomplete=False)):
            if not is_valid_codon(c):
                print(f'invalid codon {c}')
                starts = []
            elif is_start_codon(c): starts.append(i*3)
            elif is_stop_codon(c):
                yield starts, i*3
                starts = []
            else: continue

    def iter_orf(dna):
        ''' yields codons from dna, duplicates possible
        '''
        rna = toRNA(dna)
        yield from (codons(rna[start:stop+1])
                    for starts, stop in start_and_stop_indices(rna)
                    for start in starts)

    # TODO - consider returning info on frame as well...
    yield from iter_orf(dna)
    yield from iter_orf(dna[1:])
    yield from iter_orf(dna[2:])
    yield from iter_orf(reverseComplement(dna))
    yield from iter_orf(reverseComplement(dna)[1:])
    yield from iter_orf(reverseComplement(dna)[2:])
