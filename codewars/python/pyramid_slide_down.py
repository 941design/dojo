# https://www.codewars.com/kata/pyramid-slide-down/
# learnings:
# + interesting solutions out there
# + it was worth implementing the brute force solution first


def longest_slide_down(pyramid):
    '''Naive approach.

    >>> longest_slide_down([])
    0
    >>> longest_slide_down([[1]])
    1
    >>> longest_slide_down([[1], [1, 1]])
    2
    >>> longest_slide_down([[1], [1, 2]])
    3
    '''
    if not pyramid:
        return 0
    first, rest = pyramid[0][0], pyramid[1:]
    if not rest:
        return first
    left = [row[:-1] for row in rest]
    right = [row[1:] for row in rest]
    return first + max(longest_slide_down(left), longest_slide_down(right))


def longest_slide_down(pyramid):
    '''
    >>> longest_slide_down([[1]])
    1
    >>> longest_slide_down([[1], [1, 1]])
    2
    >>> longest_slide_down([[1], [1, 2]])
    3
    '''
    
    def lsd(pyramid, depth, sums):
        if depth == len(pyramid):
            return sums
        max_sums = map(max, zip(sums + [0], [0] + sums))
        current_row = pyramid[depth]
        return lsd(pyramid, depth + 1, [x + y  for x, y in zip(current_row, max_sums)])
    
    return max(lsd(pyramid, 1, pyramid[0]))
