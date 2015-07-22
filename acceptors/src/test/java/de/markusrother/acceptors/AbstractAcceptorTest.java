package de.markusrother.acceptors;


public class AbstractAcceptorTest {

    protected static final boolean T = true;

    protected static final boolean F = false;

    protected static final Acceptor<Boolean> trueP = new SingleItemAcceptor<Boolean>(true);

    protected static final Acceptor<Boolean> falseP = new SingleItemAcceptor<Boolean>(false);

}
