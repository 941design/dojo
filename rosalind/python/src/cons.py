import pandas as pd

from parsers import FastaParser


def cons(f):
    '''
    https://rosalind.info/problems/cons/

    >>> import io
    >>> NL = chr(10)
    >>> s = '>Rosalind_1' + NL \
    + 'ATCCAGCT' + NL \
    + '>Rosalind_2' + NL \
    + 'GGGCAACT' + NL \
    + '>Rosalind_3' + NL \
    + 'ATGGATCT' + NL \
    + '>Rosalind_4' + NL \
    + 'AAGCAACC' + NL \
    + '>Rosalind_5' + NL \
    + 'TTGGAACT' + NL \
    + '>Rosalind_6' + NL \
    + 'ATGCCATT' + NL \
    + '>Rosalind_7' + NL \
    + 'ATGGCACT' + NL
    >>> f = io.StringIO(s)
    >>> cons(f)
    ATGCAACT
    A: 5 1 0 0 5 5 0 0
    C: 0 0 1 4 2 0 6 1
    G: 1 1 6 3 0 1 0 0
    T: 1 5 0 0 0 1 1 6
    '''
    p = FastaParser(f)
    df = p.parseDataFrame()
    df = df['dna'].str.split('', expand=True)
    df = df.loc[:, (df != '').any()]
    profile = pd.DataFrame(dict(A=df[df == 'A'].count(),
                            C=df[df == 'C'].count(),
                            G=df[df == 'G'].count(),
                            T=df[df == 'T'].count())).T
    consensus = profile.idxmax()
    print(''.join(consensus))
    for name, row in profile.iterrows():
        print(f'{name}: {" ".join(str(x) for x in row)}')
