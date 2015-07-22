package de.markusrother.acceptors;

import static de.markusrother.acceptors.IteratorMatcher.hasNoNext;
import static de.markusrother.acceptors.IteratorMatcher.iteratesOver;
import static de.markusrother.acceptors.Union.union;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;


public class UnionTest extends AbstractAcceptorTest {

    @Test
    public void testEmptyUnionConsumesNothing() {
        final Acceptor<Boolean> union = union();
        final List<Boolean> items = Collections.emptyList();
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(union.consume(iterator), is(empty()));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testEmptyUnionRejectsItems() {
        final Acceptor<Boolean> union = union();
        final List<Boolean> items = Arrays.asList(T, F);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(union.consume(iterator), is(empty()));
        assertThat(iterator, iteratesOver(T, F));
    }

    @Test
    public void testSingleUnionConsumesNothing() {
        final Acceptor<Boolean> union = union(trueP);
        final List<Boolean> items = Collections.emptyList();
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(union.consume(iterator), is(empty()));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testSingleUnionRejectsItems() {
        final Acceptor<Boolean> union = union(trueP);
        final List<Boolean> items = Arrays.asList(T, F);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(union.consume(iterator), contains(T));
        assertThat(iterator, iteratesOver(F));
    }

    @Test
    public void testConsumeFirst() {
        final Acceptor<Boolean> union = union(trueP, falseP);
        final List<Boolean> items = Arrays.asList(T);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(union.consume(iterator), contains(T));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testConsumeSecond() {
        final Acceptor<Boolean> union = union(trueP, falseP);
        final List<Boolean> items = Arrays.asList(F);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(union.consume(iterator), contains(F));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

}
