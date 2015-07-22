package de.markusrother.acceptors;

import static de.markusrother.acceptors.Concatenation.concat;
import static de.markusrother.acceptors.IteratorMatcher.hasNoNext;
import static de.markusrother.acceptors.IteratorMatcher.iteratesOver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

public class ConcatenationTest extends AbstractAcceptorTest {

    @Test
    public void testConsumeNothing() {
        final Acceptor<Boolean> concatenation = concat(trueP, trueP);
        final List<Boolean> items = Collections.emptyList();
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(concatenation.consume(iterator), is(empty()));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testTrivialConcatenationConsumeNothing() {
        final Acceptor<Boolean> concatenation = concat(trueP);
        final List<Boolean> items = Collections.emptyList();
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(concatenation.consume(iterator), is(empty()));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testTrivialConcatenationConsumeSingleItem() {
        final Acceptor<Boolean> concatenation = concat(trueP);
        final List<Boolean> items = Arrays.asList(T);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(concatenation.consume(iterator), contains(T));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testTrivialConcatenationRejectSingleItem() {
        final Acceptor<Boolean> concatenation = concat(trueP);
        final List<Boolean> items = Arrays.asList(F);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(concatenation.consume(iterator), is(empty()));
        assertThat(iterator.hasNext(), is(T));
    }

    @Test
    public void testIncompleteConsumption() {
        final Acceptor<Boolean> concatenation = concat(trueP, trueP);
        final List<Boolean> items = Arrays.asList(T);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(concatenation.consume(iterator), is(empty()));
        assertThat(iterator, iteratesOver(T));
    }

    @Test
    public void testConsumeList() {
        final Acceptor<Boolean> concatenation = concat(trueP, trueP, trueP);
        final List<Boolean> items = Arrays.asList(T, T, T);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(concatenation.consume(iterator), contains(T, T, T));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testEmbeddedConcatenationConsumeAll() {
        final Acceptor<Boolean> concatenation = concat(concat(trueP, trueP));
        final List<Boolean> items = Arrays.asList(T, T);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(concatenation.consume(iterator), contains(T, T));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testEmbeddedConcatenationRejectAfterFirstItem() {
        final Acceptor<Boolean> concatenation = concat(concat(trueP, trueP));
        final List<Boolean> items = Arrays.asList(T, F);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(concatenation.consume(iterator), is(empty()));
        assertThat(iterator, iteratesOver(T, F));
    }

    @Test
    public void testNonTrivialConcatenationConsumeAll() {
        final Acceptor<Boolean> concatenation = concat(concat(trueP, trueP), trueP);
        final List<Boolean> items = Arrays.asList(T, T, T);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(concatenation.consume(iterator), contains(T, T, T));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    /**
     * Tests unreading more than a single item
     */
    @Test
    public void testNonTrivialConcatenationRejectBeforeLastItem() {
        final Acceptor<Boolean> concatenation = concat(concat(trueP, trueP), trueP);
        final List<Boolean> items = Arrays.asList(T, T, F);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(concatenation.consume(iterator), is(empty()));
        assertThat(iterator, iteratesOver(T, T, F));
    }

}
