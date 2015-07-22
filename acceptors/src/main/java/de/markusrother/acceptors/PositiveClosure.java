package de.markusrother.acceptors;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PositiveClosure<T> implements Acceptor<T> {

    public static <T> PositiveClosure<T> close(Acceptor<T> acceptor) {
        return new PositiveClosure<T>(acceptor);
    }

    private final Acceptor<T> acceptor;

    public PositiveClosure(Acceptor<T> acceptor) {
        this.acceptor = acceptor;
    }

    public List<T> consume(ListIterator<T> items) {
        final List<T> collector = new LinkedList<T>();
        while (true) {
            final List<T> consumed = acceptor.consume(items);
            if (consumed.isEmpty()) {
                return collector;
            }
            collector.addAll(consumed);
        }
    }

}
