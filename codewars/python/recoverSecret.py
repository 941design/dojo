# https://www.codewars.com/kata/recover-a-secret-string-from-random-triplets

def recoverSecret(triplets):
    """
    >>> triplets = [
    ... ['t','u','p'],
    ... ['w','h','i'],
    ... ['t','s','u'],
    ... ['a','t','s'],
    ... ['h','a','p'],
    ... ['t','i','s'],
    ... ['w','h','s']
    ... ]
    >>> recoverSecret(triplets)
    'whatisup'

    """
    word = ''
    while triplets:
        for head in first_elements(triplets):
            if not has_predecessor(head, triplets):
                word += head
                triplets = filter_empty(filter_element(head, triplets))
                break
    return word

def first_elements(colls):
    return set([x[0] for x in colls])

def has_predecessor(head, colls):
    return any([head in x and x.index(head) > 0 for x in colls])

def filter_element(x, colls):
    return [[y for y in t if x != y] for t in colls]

def filter_empty(colls):
    return [x for x in colls if x]
