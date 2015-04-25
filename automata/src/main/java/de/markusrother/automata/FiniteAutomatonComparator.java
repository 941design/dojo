package de.markusrother.automata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiniteAutomatonComparator<T> {

	private final FiniteAutomaton<T> that;
	private final FiniteAutomaton<T> other;
	private final List<T> alphabet;
	private final Map<AutomatonState, AutomatonState> stateMap;

	@SuppressWarnings("unchecked")
	public static <U> boolean areStructurallyEqual(FiniteAutomaton<U> that, FiniteAutomaton<?> other) {
		if (!that.hasStartState() && !other.hasStartState()) {
			// By definition all automata without start states are equal!
			return true;
		}
		final Collection<U> thatAlphabet = that.getAlphabet();
		final Collection<?> otherAlphabet = other.getAlphabet();
		if (!thatAlphabet.containsAll(otherAlphabet) || !otherAlphabet.containsAll(thatAlphabet)) {
			return false;
		}
		// We can now safely cast, because alphabets are equal and alphabets define an automaton's generic type.
		final FiniteAutomatonComparator<U> comparator =
				new FiniteAutomatonComparator<>(that, (FiniteAutomaton<U>) other);
		return comparator.areEqual();
	}

	private FiniteAutomatonComparator(FiniteAutomaton<T> that, FiniteAutomaton<T> other) {
		this.that = that;
		this.other = other;
		this.alphabet = new ArrayList<>(that.getAlphabet());
		this.stateMap = new HashMap<>();
	}

	private boolean areEqual() {
		// alphabet is already checked!
		final AutomatonState thatStartState = that.getStartState();
		final AutomatonState otherStartState = other.getStartState();
		stateMap.put(thatStartState, otherStartState);
		return areEqual(thatStartState, otherStartState);
	}

	private boolean areEqual(AutomatonState thatState, AutomatonState otherState) {
		if ((thatState == null) != (otherState == null)) {
			return false;
		}
		if (thatState.isAccepting() != otherState.isAccepting()) {
			return false;
		}
		for (T token : alphabet) {
			final AutomatonTransition<T> thatTransition = that.getTransition(thatState, token);
			final AutomatonTransition<T> otherTransition = other.getTransition(otherState, token);
			if (!areEqual(thatTransition, otherTransition)) {
				return false;
			}
		}
		return true;
	}

	private boolean areEqual(final AutomatonTransition<T> thatTransition, final AutomatonTransition<T> otherTransition) {
		final AutomatonState thatTarget = thatTransition.getTarget();
		final AutomatonState otherTarget = otherTransition.getTarget();
		if (stateMap.containsKey(thatTarget)) {
			final AutomatonState expectedTarget = stateMap.get(thatTarget);
			return expectedTarget == otherTarget;
		}
		else {
			stateMap.put(thatTarget, otherTarget);
			return areEqual(thatTarget, otherTarget);
		}
	}

}
