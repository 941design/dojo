#!/usr/bin/env python3

from romans import romans
from romparser import parse


if __name__ == '__main__':

    for n in range(1, 3999):
        r = romans(n)
        a = parse(r)
        assert(a == n)
