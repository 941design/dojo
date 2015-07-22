package de.markusrother.acceptors;

import static de.markusrother.acceptors.IteratorMatcher.hasNoNext;
import static de.markusrother.acceptors.IteratorMatcher.iteratesOver;
import static de.markusrother.acceptors.PositiveClosure.close;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

public class PositiveClosureTest extends AbstractAcceptorTest {

    @Test
    public void testEmptyClosureConsumesNothing() {
        final Acceptor<Boolean> closure = close(trueP);
        final List<Boolean> items = Collections.emptyList();
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(closure.consume(iterator), is(empty()));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testClosureConsumesSingleItem() {
        final Acceptor<Boolean> closure = close(trueP);
        final List<Boolean> items = Arrays.asList(T);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(closure.consume(iterator), contains(T));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testClosureConsumesMultipleItems() {
        final Acceptor<Boolean> closure = close(trueP);
        final List<Boolean> items = Arrays.asList(T, T, T);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(closure.consume(iterator), contains(T, T, T));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testClosureConsumesHead() {
        final Acceptor<Boolean> closure = close(trueP);
        final List<Boolean> items = Arrays.asList(T, T, T, F, F);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(closure.consume(iterator), contains(T, T, T));
        assertThat(iterator, iteratesOver(F, F));
    }

}
