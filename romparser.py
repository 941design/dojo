#!/usr/bin/env python3

import sys

def parse(roman):
    """Parses roman numeral to arabic number."""
    
    # Creating finite automata to parse slices of the given roman
    # numeral. One automata for parsing numbers from 'I' to 'IX' (the
    # 1st position), one for the 2nd position, and so forth.  Each
    # automaton returns a tuple of the number that has successfully
    # been parsed, and the remaining, unparsed slice.
    
    max, remainder = MMM().parse(roman)
    hi, remainder  = CDM().parse(remainder)
    mid, remainder = XLC().parse(remainder)
    low, remainder = IVX().parse(remainder)
    
    assert(remainder == "") # TODO - raise parse error!
    
    return max+hi+mid+low
        

class AbstractRomParser(object):
    """Abstract finite automaton for parsing roman numerals."""

    def __init__(self, i, v, x, mul):
        """Where i is one of 'I', 'X', 'C', v is one of 'V', 'L', or
        'D', and x may be 'X', 'C', or 'M'."""

        # For simplicity, variables are called I,V,X, although X,L,C,
        # and C,D,M can be used equivalently.
        I = mul
        V = 5 * mul
        X = 10 * mul

        # Adjacency list, describing a finite automaton to parse roman
        # numerals.  Each list item represents a dfa-state with a
        # dictionary of transitions, where each key maps to a tuple of
        # a successor state and a summand.
        self.adjacencies = [
            # state 0            # ack 0
            { i : (1, +I),       # -> I, II, III, IV, IX
              v : (2, +V),       # -> V, VI, VII, VIII
              },
            # state 1            # ack I
            { x : (3, -I-I+X),   # -> IX
              i : (4, +I),       # -> II, III
              v : (5, -I-I+V)    # -> IV
              },
            # state 2            # ack V
            { i : (7, +I)        # -> VI, VII, VIII
              },
            # state 3            # ack IX
            { }, 
            # state 4            # ack II
            { i : (6, +I)        # -> III
              },
            # state 5            # ack IV
            { },                 
            # state 6            # ack III, VIII
            { },                 
            # state 7            # ack VII
            { i : (4, +I)        # -> VIII
              },
            ]

    def parse(self, roman):
        """Takes string representation of a roman numeral.  Returns
        tuple of the successfully parsed number (possibly 0), and the
        remaining, unparsed string."""

        state = 0    # start state
        result = 0   # sum
        index = 0    # start of unparsed slice

        # Following transitions for each character in roman numeral,
        # while applying the mapped increment to sum.
        for c in roman:
            if not c in self.adjacencies[state]:
                # There was not transition for the given character.
                break
            state, k = self.adjacencies[state][c]
            result += k
            index += 1
            
        return result, roman[index:]
    

class IVX(AbstractRomParser):
    """Parser for roman numerals from I to IX."""

    def __init__(self):
        AbstractRomParser.__init__(self, 'I', 'V', 'X', 1)


class XLC(AbstractRomParser):
    """Parser for roman numerals from X to CX."""
    
    def __init__(self):
        AbstractRomParser.__init__(self, 'X', 'L', 'C', 10)


class CDM(AbstractRomParser):
    """Parser for roman numerals from C to CM."""

    def __init__(self):
        AbstractRomParser.__init__(self, 'C', 'D', 'M', 100)


class MMM(AbstractRomParser):
    """Parser for roman numerals from M to MMM."""
    
    def __init__(self):
        AbstractRomParser.__init__(self, 'M', None, None, 1000)


if __name__ == "__main__":

    if len(sys.argv) != 2:
        print("provide roman numeral!")
        sys.exit(1)

    roman = sys.argv[1]

    print(parse(roman))
