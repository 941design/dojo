#!/usr/bin/env python3

import sys

I = 1
V = 5
X = 10
L = 50
C = 100
D = 500
M = 1000

MAX_ROMAN = M+M+M-C+M-X+C-I+X    # == 3999


def romans(n):
    """Returns all uppercase roman numeral representation of given
    integer."""

    # Recursively converting given int to roman numeral.  In each
    # iteration, we subtract the largest possible roman character from
    # the int. The special cases: IV, IX, etc. simply increment the
    # integer before recurring. Assume n = 4, then we return 'I' +
    # romans(5). Recursion ends when n equals 0.
    
    if n > MAX_ROMAN:
        raise Exception("number too large")
    elif n // M > 0:
        return 'M' + romans(n-M) # 1000-3999
    elif (n+C) // D > 1:
        return 'C' + romans(n+C) # 900-999
    elif n // D > 0:
        return 'D' + romans(n-D) # 500-899
    elif n // C > 3:
        return 'C' + romans(n+C) # 400-499
    elif n // C > 0:
        return 'C' + romans(n-C) # 100-399
    elif (n+X) // L > 1:
        return 'X' + romans(n+X) # 90-99
    elif n // L > 0:
        return 'L' + romans(n-L) # 50-89
    elif n // X > 3:
        return 'X' + romans(n+X) # 40-49
    elif n // X > 0:
        return 'X' + romans(n-X) # 10-39
    elif (n+I) // V > 1:
        return 'I' + romans(n+I) # 9
    elif n // V > 0:
        return 'V' + romans(n-V) # 5-8
    elif n // I > 3:
        return 'I' + romans(n+I) # 4
    elif n > 0: 
        return 'I' + romans(n-I) # 1-3
    else:
        assert(n == 0)
        return ''


if __name__ == "__main__":

    if len(sys.argv) != 2:
        print("provide number argument")
        sys.exit(1)
        
    try:
        i = int(sys.argv[1])
    except ValueError:
        print("provide number argument")
        sys.exit(1)

    if (i < 1) | (i > MAX_ROMAN):
        print("provide number between 1 and " + str(MAX_ROMAN))
        sys.exit(1)
        
    print(romans(i))
    
