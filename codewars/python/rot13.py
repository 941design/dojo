# https://www.codewars.com/kata/rot13-1/
# learnings :: str.maketrans

import string
from codecs import encode as _dont_use_this_

ROT = 13
A = ord('A')
a = ord('a')
Z = ord('Z')
z = ord('z')

def rot13(s):
    return ''.join([enc(c) if c.isalpha() else c for c in s])

def enc(c, rot=ROT):
    if c.isupper(): 
        return chr(( ord(c) - A + rot ) % ( Z - A + 1 ) + A)        
    elif c.islower():
        return chr(( ord(c) - a + rot ) % ( z - a + 1 ) + a)
    else:
        raise ValueError

