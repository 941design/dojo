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

class AbstractTablePrinter(object):

    def __init__(self, rows):
        self.header = rows[0]
        self.rows = rows[1:]
        row_widths = self._rowWidths(rows)
        self.row_format = "|" + "|".join(" {:" +str(i) + "} " for i in row_widths) + "|"
        self.sep_format = "+-" + "-+-".join("-" * i for i in row_widths) + "-+"

    def _rowWidths(self, rows):
        increment_max = lambda maxs, xs: [max(x, len(y)) for x,y in zip(maxs, xs)]
        max_widths = reduce(increment_max, rows, repeat(0))
        return max_widths

    def printHeader(self):
        self.printRows(self.header)
        print(self.sep_format)

    def printRows(self, *rows):
        for row in rows:
            print(self.row_format.format(*row))


class DefaultTablePrinter(AbstractTablePrinter):
    """Prints entire table in one batch."""

    def print(self):
        self.printHeader()
        self.printRows(*self.rows)


class IncrementalTablePrinter(AbstractTablePrinter):
    """Prints table incrementally."""

    NEXT_PAGE = "n"
    PREV_PAGE = "p"
    FIRST_PAGE = "f"
    LAST_PAGE = "l"
    JUMP_TO_PAGE = "j"
    EXIT = "x"

    def __init__(self, pageSize, rows):
        super().__init__(rows)
        self.page_size = pageSize
        self.row_index = 0

    def size(self):
        return len(self.rows)

    def print(self, userAction=NEXT_PAGE):
        while userAction != self.EXIT:
            if userAction == self.NEXT_PAGE:
                if self.row_index == self.size():
                    self._printLastPage()
                else:
                    self._printNextPage()
            elif userAction == self.PREV_PAGE:
                self._printPreviousPage()
            elif userAction == self.FIRST_PAGE:
                self._printFirstPage()
            elif userAction == self.LAST_PAGE:
                self._printLastPage()
            elif userAction == self.JUMP_TO_PAGE:
                pageIndex = self._getPageNumber()
                self._jumpToPage(pageIndex)
            userAction = self._getUserAction()

    def _printFirstPage(self):
        start = 0
        end = min(self.size(), self.page_size)
        self._printPage(start, end) 

    def _printLastPage(self):
        start = max(0, self.size() - self.page_size)
        end = self.size()
        self._printPage(start, end)

    def _printNextPage(self):
        start = self.row_index
        end = min(self.size(), self.row_index + self.page_size)
        self._printPage(start, end)

    def _printPreviousPage(self):
        # Must rewind two pages, in order to really skip the currently showing page
        start = max(0, self.row_index - 2*self.page_size)
        end = max(min(self.page_size, self.size()), self.row_index - self.page_size)
        self._printPage(start, end)

    def _jumpToPage(self, pageIndex):
        # Starting at 1
        start = min(self.size(), self.page_size*(pageIndex-1))
        end = min(self.size(), start + self.page_size)
        self._printPage(start, end)

    def _printPage(self, start, end):
        self.printHeader()
        self.printRows(*self.rows[start:end])
        self.row_index = end
        currentPage = (start + self.page_size) // self.page_size
        amountOfPages = self.size() // self.page_size
        print("Page " + str(currentPage) + " of " + str(amountOfPages))

    def _getUserAction(self):
        userAction = input("\nN(ext page, P(revious page, F(irst page, L(ast page, J(ump to page, eX(it\n").lower()
        return userAction

    def _getPageNumber(self):
        pageNumber = input("page no.: ")
        try:
            return int(pageNumber)
        except ValueError:
            return self._getPageNumber()


if __name__ == "__main__":
    
    if len(sys.argv) != 2:
        print("provide .csv file argument")
        sys.exit(1)
        
    PAGE_SIZE = 4
    csv = open(sys.argv[1])
    rows = parse(csv)
    # validateTable(rows)
    printer = IncrementalTablePrinter(PAGE_SIZE, rows)
    printer.print()
