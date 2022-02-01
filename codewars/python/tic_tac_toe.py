def is_solved(board):
    '''
    https://www.codewars.com/kata/525caa5c1bf619d28c000335/python
    
    ::start:learnings::
    Flattening the board beforehand makes it easier to index the fields
    as well as to describe winning patterns.

    This can be generalized, such that simplification may be appropriate,
    even though it distorts the original data structure.
    ::end:learnings::
    '''
    coords = [[(0,0), (0,1), (0,2)],
              [(1,0), (1,1), (1,2)],
              [(2,0), (2,1), (2,2)],
              [(0,0), (1,0), (2,0)],
              [(0,1), (1,1), (2,1)],
              [(0,2), (1,2), (2,2)],
              [(0,0), (1,1), (2,2)]]
    def solved(n):
        return any(all(board[x][y] == n for x,y in line) for line in coords)
    def incomplete(board):
        return any(any(x == 0 for x in row) for row in board)
    if   solved(1) and not solved(2): return 1
    elif solved(2) and not solved(1): return 2
    elif solved(1) and solved(2) or not incomplete(board): return 0
    else: return -1
