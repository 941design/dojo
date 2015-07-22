package de.markusrother.acceptors;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Union<T> extends AbstractAcceptorCompound<T> {

    public static <T> Acceptor<T> union(Acceptor<T>... acceptors) {
        return new Union<T>(Arrays.asList(acceptors));
    }

    public Union(List<Acceptor<T>> acceptors) {
        super(acceptors);
    }

    public List<T> consume(ListIterator<T> items) {
        final Iterator<Acceptor<T>> acceptors = getAcceptorIterator();
        while (acceptors.hasNext()) {
            final Acceptor<T> acceptor = acceptors.next();
            final List<T> consumed = acceptor.consume(items);
            if (!consumed.isEmpty()) {
                return consumed;
            }
        }
        return Collections.emptyList();
    }

}
