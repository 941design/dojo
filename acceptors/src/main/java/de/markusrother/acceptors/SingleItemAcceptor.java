package de.markusrother.acceptors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class SingleItemAcceptor<T> implements Acceptor<T> {

    private final T item;

    public SingleItemAcceptor(T item) {
        this.item = item;
    }

    @SuppressWarnings("unchecked")
    public List<T> consume(ListIterator<T> iterator) {
        if (iterator.hasNext()) {
            final T current = iterator.next();
            if (item.equals(current)) {
                return Arrays.asList(current);
            }
            iterator.previous();
        }
        return Collections.emptyList();
    }

}
