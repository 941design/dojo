#!/usr/bin/env python3

"""A primitive implementation of the Hilbert's Hotel problem.

This implementation operates with infinite generators."""

def naturals():
    """Returns generator of whole numbers starting at 1."""
    n = 1
    while True:
        yield n
        n += 1


def rooms():
    """Returns generator of tuples of room number (int) and guest
    (string)."""
    nats = naturals()
    while True:
        n = next(nats)    
        yield (n, "guest_" + str(n))        

    
def check_in(guest, rooms):
    """Returns generator of tuples of room number (int) and guest
    (string), after checking in the given guest into the first room."""
    pred = guest
    while True:
        n, succ = next(rooms)
        yield (n, pred)
        pred = succ


if __name__ == "__main__":

    # nats = naturals()
    # print(next(nats))
    # print(next(nats))

    # hotel = rooms()
    # print(next(hotel))
    # print(next(hotel))

    hilberts_hotel = check_in("hilbert", rooms())
    assert(next(hilberts_hotel) == (1, "hilbert"))
    assert(next(hilberts_hotel) == (2, "guest_1"))
    assert(next(hilberts_hotel) == (3, "guest_2"))
