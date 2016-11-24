# https://www.codewars.com/kata/calculator/python

# learnings:
# + float precision
# + operator module
# + ast module

import re

class Calculator(object):
    """
    >>> Calculator().evaluate('2 + 3')
    5.0
    >>> Calculator().evaluate('2 + 3 * 4')
    14.0
    >>> Calculator().evaluate('1 * 2 + 3 * 4')
    14.0
    >>> Calculator().evaluate('2 * 2 + 3 + 8 / 4')
    9.0
    >>> Calculator().evaluate('3 * 4 / 3 - 6 / 3 * 3')
    -2.0
    >>> Calculator().evaluate('2 + 3 * 4 / 3 - 6 / 3 * 3 + 8')
    8.0
    >>> Calculator().evaluate('1.1 + 2.2')
    3.3
    """

    FACTOR = re.compile('(.*)\s*(\*|/)\s*(.*)')
    TERM = re.compile('(.*)\s*(\+|-)\s*(.*)')
    NUMBER = re.compile('(\d*\.?\d*)')

    PRECISION = 10
    
    OPERATORS = {'+': lambda a, b: round(a + b, Calculator.PRECISION),
                 '-': lambda a, b: round(a - b, Calculator.PRECISION),
                 '*': lambda a, b: round(a * b, Calculator.PRECISION),
                 '/': lambda a, b: round(a / b, Calculator.PRECISION)}

    
    def evaluate(self, s):
        
        if self.TERM.match(s):
            a, op, b = self.TERM.match(s).groups()
            lhs = self.evaluate(a)
            rhs = self.evaluate(b)
            return self.OPERATORS[op](lhs, rhs)
        
        elif self.FACTOR.match(s):
            a, op, b = self.FACTOR.match(s).groups()
            lhs = self.evaluate(a)
            rhs = self.evaluate(b)
            return self.OPERATORS[op](lhs, rhs)
        
        elif self.NUMBER.match(s):
            n = self.NUMBER.match(s).group(1)
            return float(n)
        
        else:
            raise ValueError
