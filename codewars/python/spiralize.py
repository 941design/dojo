# https://www.codewars.com/kata/make-a-spiral

def spiralize(size):
    """
    >>> spiralize(1)
    [[1]]
    >>> spiralize(2)
    [[1, 1], [0, 1]]
    >>> spiralize(3)
    [[1, 1, 1], [0, 0, 1], [1, 1, 1]]
    >>> spiralize(4)
    [[1, 1, 1, 1], [0, 0, 0, 1], [1, 0, 0, 1], [1, 1, 1, 1]]
    >>> spiralize(5)
    [[1, 1, 1, 1, 1], [0, 0, 0, 0, 1], [1, 1, 1, 0, 1], [1, 0, 0, 0, 1], [1, 1, 1, 1, 1]]
    """
    # The idea is that the demanded spiral is a composition (XOR)
    # of two matrices: rings and a diagonal.  
    # Visually speaking, the rings form the spiral's outline, whereas 
    # the 'diagonal' swaps the necessary cells to transform the rings 
    # into a spiral.

    diagonal = lambda x, y, size: x == y - 1 and (x < size // 2 - 1 if size % 4 == 0 else x < size // 2)
    return [[int(ring(x, y, size) ^ diagonal(x, y, size)) for x in range(size)] for y in range(size)]

def ring(x, y, size):
    """
    >>> r = lambda size: [[int(ring(x, y, size)) for x in range(size)] for y in range(size)]
    >>> r(1)
    [[1]]
    >>> r(2)
    [[1, 1], [1, 1]]
    >>> r(3)
    [[1, 1, 1], [1, 0, 1], [1, 1, 1]]
    >>> r(4)
    [[1, 1, 1, 1], [1, 0, 0, 1], [1, 0, 0, 1], [1, 1, 1, 1]]
    >>> r(5)
    [[1, 1, 1, 1, 1], [1, 0, 0, 0, 1], [1, 0, 1, 0, 1], [1, 0, 0, 0, 1], [1, 1, 1, 1, 1]]
    >>> r(6)
    [[1, 1, 1, 1, 1, 1], [1, 0, 0, 0, 0, 1], [1, 0, 1, 1, 0, 1], [1, 0, 1, 1, 0, 1], [1, 0, 0, 0, 0, 1], [1, 1, 1, 1, 1, 1]]
    """
    return x == 0 \
        or x == size - 1 \
        or y == 0 \
        or y == size - 1 \
        or size > 0 and not ring(x-1, y-1, size-2)

