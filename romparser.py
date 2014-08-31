#!/usr/bin/env python3

import sys

# lot of structure, little code!

def parse(roman):
    """Parses roman numeral to arabic number."""
    # Creating finite automata to parse slices of the roman numeral.
    # All created automata have the same structure.  Each returns an
    # integer of what has successfully been parsed, and the remaining
    # slice.
    max, remainder = MMM().parse(roman)
    hi, remainder  = CDM().parse(remainder)
    mid, remainder = XLC().parse(remainder)
    low, remainder = IVX().parse(remainder)
    assert(remainder == "") # TODO - raise parse error!
    return max+hi+mid+low
        

class AbstractRomParser(object):

    def __init__(self, i, v, x, mul):
        """Where i is one of 'I', 'X', 'C', v is one of 'V', 'L', or
        'D', and x may be 'X', 'C', or 'M'."""
        I = mul
        V = 5 * mul
        X = 10 * mul
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
        state = 0
        result = 0
        index = 0
        for c in roman:
            if not c in self.adjacencies[state]:
                break
            next_state, k = self.adjacencies[state][c]
            state = next_state
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
