#!/usr/bin/env python3

import sys
from functools import reduce
from itertools import repeat

SEP = ";"
NL = "\n"


class AbstractToken(object):
    """Abstract superclass for all tokens generated when tokenizing a .csv file.""" 
    pass


class Cell(AbstractToken):
    """Token for .csv cells."""

    def __init__(self, content):
        self.content = content

    def __str__(self):
        return "T[" + self.content + "]"


class Separator(AbstractToken):
    """Token for .csv cell delimiters."""

    def __str__(self):
        return "SEP"


class Newline(AbstractToken):
    """Token for .csv row delimiters."""

    def __str__(self):
        return "NL"


def tokenize(csv):
    """Generator, tokenizing a .csv stream into subclass implementations of AbstractToken."""
    buffer = ""
    for c in csv.read():
        if c == SEP:
            yield Cell(buffer)
            yield Separator()
            buffer = ""
        elif c == NL:
            yield Cell(buffer)
            yield Newline()
            buffer = ""
        else:
            buffer += c
    if buffer:
        # Final token in case final newline is missing
        yield Cell(buffer)


def parse(csv):
    """Parses .csv stream to a nested list.  NOT VALIDATING ROW WIDTHS."""
    rows = []
    row = []
    for token in tokenize(csv):
        if type(token) is Cell:
            row.append(token.content)
        elif type(token) is Newline:
            rows.append(row)
            row = []
        else:
            # Ignoring separators
            pass
    return rows


def printTable(rows):
    row_widths = rowWidths(rows)
    row_format = "|" + "|".join(" {:" +str(i) + "} " for i in row_widths) + "|"
    sep_format = "+-" + "-+-".join("-" * i for i in row_widths) + "-+"
    printHeader(rows[0], row_format, sep_format)
    printRows(rows[1:], row_format)


def rowWidths(rows):
    increment_max = lambda maxs, xs: [max(x, len(y)) for x,y in zip(maxs, xs)]
    max_widths = reduce(increment_max, rows, repeat(0))
    return max_widths


def printHeader(header, row_format, sep_format):
    print(row_format.format(*header))
    print(sep_format)


def printRows(rows, row_format):
    for row in rows:
        print(row_format.format(*row))


if __name__ == "__main__":
    
    if len(sys.argv) != 2:
        print("provide .csv file argument")
        sys.exit(1)
        
    csv = open(sys.argv[1])
    rows = parse(csv)
    # validateTable(rows)
    printTable(rows)
