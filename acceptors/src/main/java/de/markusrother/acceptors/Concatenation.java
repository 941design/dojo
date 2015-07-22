package de.markusrother.acceptors;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Concatenation<T> extends AbstractAcceptorCompound<T> {

    public static <T> Acceptor<T> concat(Acceptor<T>... acceptors) {
        return new Concatenation<T>(Arrays.asList(acceptors));
    }

    public Concatenation(List<Acceptor<T>> acceptors) {
        super(acceptors);
    }

    public List<T> consume(ListIterator<T> items) {
        final LinkedList<T> collector = new LinkedList<T>();
        final boolean successful = consumeRecursively(items, getAcceptorIterator(), collector);
        if (successful) {
            return collector;
        }
        else {
            return Collections.emptyList();
        }
    }

    /**
     * TODO - upon initial commit rewrite this to an iterative implementation and compare!
     */
    private boolean consumeRecursively(ListIterator<T> items, Iterator<Acceptor<T>> acceptors, List<T> collector) {
        if (!acceptors.hasNext()) {
            // We are done and do not care whether there are more items provided.
            // The collector now contains everything, we have consumed.
            return true;
        }
        if (!items.hasNext()) {
            // There are acceptors left, which did not consume any items.
            // The item iterator will be rewound when leaving the recursion.
            return false;
        }
        // There are unprocessed acceptors as well as unprocessed items.
        // We proceed with the next available acceptor.
        final Acceptor<T> nextAcceptor = acceptors.next();
        final List<T> consumed = nextAcceptor.consume(items);
        if (consumed.isEmpty()) {
            // The next acceptor did not match any items, and ends recursion prematurely.
            // The item iterator will be rewound when leaving the recursion.
            // The acceptor is itself responsible for rewinding any consumed items.
            return false;
        }
        // Updating the collector for further recursions.
        collector.addAll(consumed);
        if (!consumeRecursively(items, acceptors, collector)) {
            // Recursion did not succeed.
            // The item iterator has to be rewound and the collector discarded.
            // However, the caller may be interested in what was consumed.
            for (int i = 0; i < consumed.size(); i++) {
                items.previous();
            }
            return false;
        }
        // Final end of recursion.
        // The collector now contains what this concatenation can possibly consume.
        return true;

    }
}
