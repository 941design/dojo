from parsers import FastaParser

from revc import reverseComplement


def revp(f, out=None):
    '''
    https://rosalind.info/problems/revp/
    '''
    [(_, dna)] = FastaParser(f).parseTuples()
    # rosalind expects 1-based indices
    s = '\n'.join(f'{i+1} {l}'
                  for i, l in reverse_palindromes(dna, min=4, max=12))
    if out:
        with open(out, 'w') as f: f.write(s)
    else:
        print(s)


def reverse_palindromes(dna, min, max):
    '''
    https://rosalind.info/problems/revp/
    
    >>> sorted(reverse_palindromes('TCAATGCATGCGGGTCTATATGCAT', 4, 12))
    [(3, 6), (4, 4), (5, 6), (6, 4), (16, 4), (17, 4), (19, 6), (20, 4)]
    >>> sorted(reverse_palindromes('ATAT', 4, 12))
    [(0, 4)]
    >>> sorted(reverse_palindromes('ATATAT', 4, 12))
    [(0, 4), (0, 6), (1, 4), (2, 4)]

    ::start:learnings::
    
    My while-condition had a nice off by one error:
    `while i < len(dna) - min // 2:`
    It should read:
    `while i < len(dna) + 1 - min // 2:`
    For readability I chose:
    `while i < len(dna):`
    Finally, I decided to drop the while-loop entirely and go with `range`

    Also for readability I am using the more explicit `len(left+right)`

    READABILITY IS KING!

    ::end:learnings::
    '''
    for i in range(len(dna)):
        # extending palindrome from its center (i)
        # starting at min length
        l = min // 2
        left = dna[i-l:i]
        right = reverseComplement(dna[i:i+l])
        while left == right and len(left+right) <= max:
            yield i-l, len(left+right)
            l+=1
            left = dna[i-l:i]
            right = reverseComplement(dna[i:i+l])
