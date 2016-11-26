# https://www.codewars.com/kata/memoized-fibonacci

class memoize():
    
    def __init__(self, fn):
        self.fn = fn
        self.mem = {}
        
    def __call__(self, arg):
        return self.mem[arg] if arg in self.mem \
            else self.mem.setdefault(arg, self.fn(arg))

@memoize
def fibonacci(n):
    if n in [0, 1]:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)
