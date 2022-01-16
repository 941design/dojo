def partition(predicate, coll):
    """ Takes function and collection. Evaluates each
    element of coll with a function. Elements that yield
    the same evaluation result are collected in sets.
    A dictionary mapping evaluation results to these sets
    is returned. The returned dictionary's values represent
    equivalence classes for the given predicate function.    
    """
    partitions = {}
    for e in coll:
        k = predicate(e)
        if partitions.has_key(k):
            partitions[k].add(e)
        else:
            partitions[k] = set([e])
    return partitions

