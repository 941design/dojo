package de.markusrother.acceptors;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractAcceptorCompound<T> implements Acceptor<T> {

    private final List<Acceptor<T>> acceptors;

    public AbstractAcceptorCompound(List<Acceptor<T>> acceptors) {
        this.acceptors = acceptors;
    }

    protected Iterator<Acceptor<T>> getAcceptorIterator() {
        return acceptors.iterator();
    }

}
