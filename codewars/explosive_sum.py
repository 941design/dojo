def exp_sum(n):
    """
    >>> [ exp_sum(x) for x in range(1,11) ]
    [1, 2, 3, 5, 7, 11, 15, 22, 30, 42]
    """
    if n < 0:
        return 0
    if n == 0:
        return 1
    sums = []
    for i in range(n):
        s = set([(i,)])
        for j in range( (i+1) // 2 ):
            for k in sums[j]:
                for l in sums[i-j-1]:
                    s.add(tuple(sorted(k + l)))
        sums.append(s)
    return len(sums[-1])
