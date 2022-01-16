def cartesian(*colls):
    """ Takes zero or more collections as arguments.
    Returns their cartesian product as a set of tuples.
    """
    if len(colls) == 0:
        return ((),)
    s = set([])
    for e in colls[0]:
        for cp in cartesian(*colls[1:]):
            s.add((e,) + cp)
    return s

