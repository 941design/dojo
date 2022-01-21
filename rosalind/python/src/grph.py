from parsers import FastaParser


def grph(f, o=3):
    '''
    https://rosalind.info/problems/grph/

    >>> NL = chr(10)
    >>> import io
    >>> f = io.StringIO('>Rosalind_0498' + NL + 'AAATAAA' + NL \
    + '>Rosalind_2391' + NL + 'AAATTTT' + NL \
    + '>Rosalind_2323' + NL + 'TTTTCCC' + NL \
    + '>Rosalind_0442' + NL + 'AAATCCC' + NL \
    + '>Rosalind_5013' + NL + 'GGGTGGG' + NL)
    >>> grph(f)
    Rosalind_0498 Rosalind_2391
    Rosalind_0498 Rosalind_0442
    Rosalind_2391 Rosalind_2323

    ::start:learnings::

    A case can be made for long list comprehensions.

    ::end:learnings::
    '''
    d = FastaParser(f).parseDict()
    print('\n'.join(f'{key} {k}'
                    for key, val in d.items()
                    for k, v in d.items()
                    if not k is key and v[:o] == val[-o:]))
