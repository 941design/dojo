def isSolved(board, unoccupied=0, player1=1, player2=2):
    '''
    https://www.codewars.com/kata/525caa5c1bf619d28c000335/python

    ::start:learnings::
    This is my solution of around 2015.

    In hindsight I don't like the many functions, nor their names.
    `invert` is a transposition, and `won` and `full` are too general,
    and should possibly be defined within `isSolved`.

    There are some clever aspects to this solution which I still like,
    such as `[player] * 3 in board`. Also the `isSolved` function is
    very readable.
    ::end:learnings::    
    '''
    return 1 if won(board, player1) \
        else 2 if won(board, player2) \
        else -1 if not full(board, unoccupied) \
        else 0

def won(board, player):
    return [player] * 3 in board \
        or [player] * 3 in invert(board) \
        or board[0][0] == board[1][1] == board[2][2] == player \
        or board[0][2] == board[1][1] == board[2][0] == player

def full(board, unoccupied):
    return not any([True for row in board for cell in row if cell == unoccupied])

def invert(board):
    """
    >>> invert([[1,2,3], [4,5,6], [7,8,9]])
    [[1, 4, 7], [2, 5, 8], [3, 6, 9]]
    """
    return [[row[n] for row in board] for n in range(3)]
