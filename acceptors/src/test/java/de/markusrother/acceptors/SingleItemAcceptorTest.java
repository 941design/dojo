package de.markusrother.acceptors;

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

public class SingleItemAcceptorTest extends AbstractAcceptorTest {

    @Test
    public void testConsumeNothing() {
        final List<Boolean> items = Collections.emptyList();
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(trueP.consume(iterator), is(empty()));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testConsumeSingleItem() {
        final List<Boolean> items = Arrays.asList(true);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(trueP.consume(iterator), contains(true));
        assertThat(iterator, hasNoNext(Boolean.class));
    }

    @Test
    public void testConsumeFirstItemOnly() {
        final List<Boolean> items = Arrays.asList(true, true);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(trueP.consume(iterator), contains(true));
        assertThat(iterator, iteratesOver(true));
    }

    @Test
    public void testRejectSingleItem() {
        final List<Boolean> items = Arrays.asList(false);
        final ListIterator<Boolean> iterator = items.listIterator();
        //
        assertThat(trueP.consume(iterator), is(empty()));
        assertThat(iterator, iteratesOver(false));
    }

}
