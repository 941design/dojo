from collections import Counter


def iev(dd, dh, dr, hh, hr, rr):
    '''
    https://rosalind.info/problems/iev/
    
    >>> iev(1, 0, 0, 1, 0, 1)
    3.5
    '''
    genotypes = population(dd, dh, dr, hh, hr, rr, children=2)
    print(genotypes['D'] + genotypes['H'])


def population(dd, dh, dr, hh, hr, rr, children=2):
    '''
    >>> population(1, 0, 0, 0, 0, 0, children=4) == Counter({'D': 4})
    True
    >>> population(0, 1, 0, 0, 0, 0, children=4) == Counter({'D': 2, 'H': 2})
    True
    >>> population(0, 0, 1, 0, 0, 0, children=4) == Counter({'H': 4})
    True
    >>> population(0, 0, 0, 1, 0, 0, children=4) == Counter({'D': 1, 'H': 2, 'R': 1})
    True
    >>> population(0, 0, 0, 0, 1, 0, children=4) == Counter({'H': 2, 'R': 2})
    True
    >>> population(0, 0, 0, 0, 0, 1, children=4) == Counter({'R': 4})
    True
    >>> dd, dh, dr, hh, hr, rr = (23, 51, 42, 100, 1, 55)
    >>> c = population(dd, dh, dr, hh, hr, rr, children=1)
    >>> sum(c.values()) == sum((dd, dh, dr, hh, hr, rr))
    True
    '''
    return (
        Counter({ 'D': children * dd }) +
        Counter({ 'D': dh * children/2, 'H': dh * children/2 }) +
        Counter({                       'H': dr * children/1 }) +
        Counter({ 'D': hh * children/4, 'H': hh * children/2, 'R': hh * children/4 }) +
        Counter({                       'H': hr * children/2, 'R': hr * children/2 }) +
        Counter({                                             'R': rr * children/1 })
    )
