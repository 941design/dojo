# https://www.codewars.com/kata/tiny-three-pass-compiler/

# TODO  - write  ast calculator  in order  to determine  whether error
# occurs in pass1, or pass2, or  in pass3!  create ast from tuples for
# readability!

# TODO - migrate doctests (and simulator) to module!

import re


def simulate(asm, argv):
    r0, r1 = None, None
    stack = []
    for ins in asm:
        if ins[:2] == 'IM' or ins[:2] == 'AR':
            ins, n = ins[:2], int(ins[2:])
        if ins == 'IM':   r0 = n
        elif ins == 'AR': r0 = argv[n]
        elif ins == 'SW': r0, r1 = r1, r0
        elif ins == 'PU': stack.append(r0)
        elif ins == 'PO': r0 = stack.pop()
        elif ins == 'AD': r0 += r1
        elif ins == 'SU': r0 -= r1
        elif ins == 'MU': r0 *= r1
        elif ins == 'DI': r0 /= r1
    return r0


class Compiler(object):
    """    
    function   ::= '[' arg-list ']' expression

    arg-list   ::= /* nothing */
                 | variable arg-list

    expression ::= term
                 | expression '+' term
                 | expression '-' term

    term       ::= factor
                 | term '*' factor
                 | term '/' factor

    factor     ::= number
                 | variable
                 | '(' expression ')'

    """
    
    def compile(self, program):
        """
        >>> prog = Compiler().compile('[ ] 24 / 8')
        >>> simulate(prog, [])
        3
        >>> prog = Compiler().compile('[ x y z ] ( 2*3*x + 5*y - 3*z ) / (1 + 3 + 2*2)')
        >>> simulate(prog, [0, 0, 0])
        0.0
        >>> simulate(prog, [2, 0, 0])
        1.5
        >>> simulate(prog, [0, 2, 0])
        1.25
        >>> simulate(prog, [-1, 0, 2])
        -1.5

        """
        return self.pass3(self.pass2(self.pass1(program)))
        
    def tokenize(self, program):
        """Turn a program string into an array of tokens.  

        Each token is either '[', ']', '(', ')', '+', '-', '*', '/', a
        variable name or a number (as a string)

        """
        token_iter = (m.group(0) for m in re.finditer(r'[-+*/()[\]]|[A-Za-z]+|\d+', program))
        return [int(tok) if tok.isdigit() else tok for tok in token_iter]

    
    def pass1(self, program):
        """Returns an un-optimized AST.

        { 'op': '+', 'a': a, 'b': b }    // add subtree a to subtree b
        { 'op': '-', 'a': a, 'b': b }    // subtract subtree b from subtree a
        { 'op': '*', 'a': a, 'b': b }    // multiply subtree a by subtree b
        { 'op': '/', 'a': a, 'b': b }    // divide subtree a from subtree b
        { 'op': 'arg', 'n': n }          // reference to n-th argument, n integer
        { 'op': 'imm', 'n': n }          // immediate value n, n integer

        >>> r = Compiler().pass1('[ xx yy ] ( xx + yy ) / 2')
        >>> r == {
        ... 'op': '/',
        ... 'a' : {
        ...        'op' : '+', 
        ...        'a'  : {'op': 'arg', 'n': 0}, 
        ...        'b'  : {'op': 'arg', 'n': 1}
        ...       },
        ... 'b' : {'op': 'imm', 'n': 2}}
        True
        """
        def parse_function(tokens):
            # apart from args, program is parsed right to left
            assert(tokens[0] == '[')
            tokens.remove('[')
            args = parse_args(tokens)
            assert(tokens[0] == ']')
            tokens.remove(']')
            return parse_expression(args, tokens)

        def parse_args(tokens):
            args = []
            while tokens[0] != ']':
                args.append(tokens[0])
                tokens.remove(tokens[0])
            return args
        
        def parse_expression(args, tokens):
            term = parse_term(args, tokens)
            if not tokens or tokens[-1] == '(':
                return term
            else:
                op = tokens.pop()
                expr = parse_expression(args, tokens)
                assert(op in '+-')
                return { 'op': op,
                         'a': expr,
                         'b': term }

        def parse_term(args, tokens):
            factor = parse_factor(args, tokens)
            if not tokens or tokens[-1] not in '*/':
                return factor
            else:
                op = tokens.pop()
                assert(op in '*/')
                return { 'op': op,
                         'a': parse_term(args, tokens),
                         'b': factor }
            
        def parse_factor(args, tokens):
            last = tokens.pop()
            if last == ')':
                expr = parse_expression(args, tokens)
                assert(tokens.pop() == '(')
                return expr
            elif type(last) is int:
                return {'op': 'imm', 'n': int(last)}
            else:
                assert(last in args)
                return { 'op': 'arg', 'n': args.index(last) }
                
        tokens = self.tokenize(program)        
        return parse_function(tokens)
    
        
    def pass2(self, ast):
        """Returns an AST with constant expressions reduced.

        """
        OPERATIONS = {'+': lambda x, y: x + y,
                      '-': lambda x, y: x - y,
                      '*': lambda x, y: x * y,
                      '/': lambda x, y: x / y}

        if ast['op'] in ('imm', 'arg'):
            return ast
        
        ast = {'op': ast['op'],
               'a': self.pass2(ast['a']),
               'b': self.pass2(ast['b'])}
        
        if ast['a']['op'] == ast['b']['op'] == 'imm':
            fn = OPERATIONS[ast['op']]
            operands = ast['a']['n'], ast['b']['n']
            return {'op': 'imm',
                    'n': fn(*operands)}
        
        return ast

    
    def pass3(self, ast):
        """Returns assembly instructions.

        >>> simulate([ "IM 10", "SW", "AR 0", "AD" ], [23])
        33
        >>> ast = { 'op': '+', 'a': { 'op': 'arg', 'n': 0 }, 'b': { 'op': 'imm', 'n': 10 } }
        >>> asm = Compiler().pass3(ast)
        >>> simulate(asm, [23])
        33
        >>> ast = { 'op': '+', 'a': { 'op': 'imm', 'n': 1 }, 'b': { 'op': 'imm', 'n': 2 } }
        >>> asm = Compiler().pass3(ast)
        >>> simulate(asm, [])
        3
        >>> ast = {'op': 'imm', 'n': 23}
        >>> Compiler().pass3(ast)
        ['IM 23']
        >>> ast = {'op': '+', 'a': {'op': 'imm', 'n': 1}, 'b': {'op': 'imm', 'n': 2}}
        >>> Compiler().pass3(ast)
        ['IM 1', 'IM 2', 'AD']

        """
        OP_CODES = {'+': 'AD',
                    '-': 'SU',
                    '*': 'MU',
                    '/': 'DI'}
        
        def compile_ast(ast, prog=[]):
            
            if ast['op'] == 'imm':
                assert(int(ast['n']) == ast['n'])  # floats not allowed!
                prog.append('IM ' + str(int(ast['n'])))
                
            elif ast['op'] == 'arg':
                prog.append('AR ' + str(ast['n']))
                
            else:
                # TODO - save registers only if necessary!
                compile_ast(ast['a'], prog)
                prog.append('PU')                
                compile_ast(ast['b'], prog)
                prog.append('SW')
                prog.append('PO')
                prog.append(OP_CODES[ast['op']])
            return prog
        
        return compile_ast(ast)
