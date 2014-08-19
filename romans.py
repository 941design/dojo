#!/usr/bin/env python3

import sys

I = 1
V = 5
X = 10
L = 50
C = 100
D = 500
M = 1000

MAX_ROMAN = M+M+M-C+M-X+C-I+X


def romans(i):
    if i > MAX_ROMAN:
        raise Exception("number too large")
    elif i // M > 0:
        return 'M' + romans(i-M) # 1000-3999
    elif (i+C) // D > 1:
        return 'C' + romans(i+C) # 900-999
    elif i // D > 0:
        return 'D' + romans(i-D) # 500-899
    elif i // C > 3:
        return 'C' + romans(i+C) # 400-499
    elif i // C > 0:
        return 'C' + romans(i-C) # 100-399
    elif (i+X) // L > 1:
        return 'X' + romans(i+X) # 90-99
    elif i // L > 0:
        return 'L' + romans(i-L) # 50-89
    elif i // X > 3:
        return 'X' + romans(i+X) # 40-49
    elif i // X > 0:
        return 'X' + romans(i-X) # 10-39
    elif (i+I) // V > 1:
        return 'I' + romans(i+I) # 9
    elif i // V > 0:
        return 'V' + romans(i-V) # 5-8
    elif i // I > 3:
        return 'I' + romans(i+I) # 4
    elif i > 0: 
        return 'I' + romans(i-I) # 1-3
    else:
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
    
