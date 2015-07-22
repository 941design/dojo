package de.markusrother.acceptors;

import java.util.List;
import java.util.ListIterator;

public interface Acceptor<T> {

    /**
     * @param items
     * @return The consumed items
     */
    public List<T> consume(ListIterator<T> items);

}
