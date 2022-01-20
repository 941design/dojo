MIN = 4
MAX = 12

DICT = dict(zip('ACGT','TGCA'))


def reversePalindromes(s):
    """
    >>> sorted(reversePalindromes('TCAATGCATGCGGGTCTATATGCAT'))
    [(3, 6), (4, 4), (5, 6), (6, 4), (16, 4), (17, 4), (19, 6), (20, 4)]
    >>> sorted(reversePalindromes('ATAT'))
    [(0, 4)]
    >>> sorted(reversePalindromes('ATATAT'))
    [(0, 4), (0, 6), (1, 4), (2, 4)]

    ::start:learnings::
    
    My old implementation was insufficient, but passed at that time.
    I assume the test data from [rosalind.info](https://rosalind.info/problems/revp/)
    was insufficient itself.

    Overlapping palindromes were not recognized. Neither were
    palindromes at the dna's very end (same fencepost mistake as in
    my recent implementation). Current test data probably covers both
    issues regularly.
    
    ::end:learnings::
    """
    #TODO - can be solved by recursion instead !!!
    # maximum recursion depth depends only on MAX
    current = set()
    for i in range(len(s)-1):
        next = set()
        for a, b in current:
            if DICT[s[a]] == s[b]: #predicate function on two indices
                if MIN <= b-a+1 <= MAX: # in-range-function from generator
                    yield(a, b-a+1)
                if a > 0: # b can never exceed stream, recursion predicate
                    next.add((a-1, b+1),) # recursion call
        next.add((i, i+1),)
        current = next


def revp(dna):
    for a, b in reversePalindromes(dna):
        print(a+1, b)
