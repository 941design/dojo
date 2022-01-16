#! /usr/env python

IN = 'rosalind_revp.txt' #'revp_in.txt'
OUT = 'revp_out.txt'

MIN = 4
MAX = 12

# TODO - import ALPHABET and complements from some general lib!
DICT = dict(zip('ACGT','TGCA'))


def reversePalindromes(s): # fn-out, fn-out-p, fn-rec
    """
    """
    #TODO - can be solved by recursion instead !!!
    # maximum recursion depth depends only on MAX
    current = set()
    for i in range(len(s)-1):
        next = set()
        for a, b in current:
            if DICT[s[a]] == s[b]: #predicate function on two indices
                if MIN <= b-a+1 <= MAX: # in-range-function from generator
                    print a+1, b-a+1 # output function
                if a > 0: # b can never exceed stream, recursion predicate
                    next.add((a-1, b+1),) # recursion call
        next.add((i, i+1),)
        current = next

if __name__ == """__main__""":
    """
    approach:
    do i need random access ??
    how many comparisons do i need ?
    """

    #TODO - call with Arguments MIN, MAX, IN, OUT

    #TODO - redirect stdout

    #TODO - create modules and a map of input files to modules
    # either include a main method in each module or name the
    # main method according to the file/task.
    # then call the file and produce output
    # add mapping (input file names) to module itself !!!
    # somewhere into header!

    f = open(IN)
    reversePalindromes(f.read())
    f.close()
