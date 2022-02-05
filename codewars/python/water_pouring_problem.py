def _wpp(cap_a, cap_b, target):
    '''
    >>> _wpp(0, 1, 1)
    [(0, 1)]
    >>> _wpp(1, 2, 1)
    [(1, 0)]
    >>> _wpp(1, 3, 2)
    [(1, 0), (0, 1), (1, 1), (0, 2)]
    >>> _wpp(3, 5, 4)
    [(2, 0), (0, 5), (2, 5), (3, 4)]
    >>> _wpp(4, 5, 4)
    [(4, 0)]
    '''
    def step(ab):
        a, b = ab
        return set([
            (0, b),      # empty a
            (a, 0),      # empty b
            (cap_a, b),  # fill a
            (a, cap_b),  # fill b
            (min(a+b, cap_a), b-min(cap_a-a, b)),  # empty b into a
            (a-min(cap_b-b, a), min(a+b, cap_b))   # empty a into b
            ])
    visited = {(0, 0)}
    paths = [[(0, 0)]]
    while paths:
        # we want to find a shortest path
        # as we append extended paths, the first is shortest
        path = paths.pop()
        for ab in step(path[-1]).difference(visited):
            if target in ab:
                # somewhat whack, that paths do NOT start with (0, 0)
                return path[1:] + [ab]
            visited.add(ab)
            paths += [path + [ab]]
    return []


def wpp(cap_a, cap_b, target):
    def g():
        a, b = 0, 0
        # Traversing all `k = remainder + n*cap_a` where `k <= cap_b`.
        # `remainder_n0 = 0`, then `remainder_n1 = cap_a - remainder_n0 + n_max*cap_a`.
        for _ in range(cap_a):
            while cap_b - b > cap_a:       # while a can be emptied into b
                a, b = cap_a, b                # refill a
                yield a, b
                a, b = 0, a + b                # empty a into b
                yield a, b
            a, b = cap_a, b                # fill a one last time
            yield a, b
            a, b = a - (cap_b - b), cap_b  # partially empty a into b
            yield a, b
            a, b = a, 0                    # keep remainder in a, empty b
            yield a, b
            a, b = 0, a                    # empty remainder into b, and start over
            yield a, b
    path = []
    for ab in g():
        path.append(ab)
        if target in ab:
            return path
    return []
