package de.markusrother.acceptors;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class IteratorMatcher<T> extends BaseMatcher<Iterator<T>> {

    public static <T> IteratorMatcher<T> hasNoNext(Class<T> clazz) {
        final List<T> emptyList = Collections.emptyList();
        return new IteratorMatcher<T>(emptyList);
    }

    public static <T> IteratorMatcher<T> iteratesOver(T... items) {
        return new IteratorMatcher<T>(Arrays.asList(items));
    }

    private final List<T> list;

    public IteratorMatcher(List<T> list) {
        this.list = list;
    }

    public boolean matches(Object item) {
        final Iterator<T> actual = (Iterator<T>) item;
        for (T obj : list) {
            if (!actual.hasNext()) {
                return false;
            }
            if (!obj.equals(actual.next())) {
                return false;
            }
        }
        return !actual.hasNext();
    }

    public void describeTo(Description description) {
        description.appendValueList("[", ", ", "]", list);
    }

}
