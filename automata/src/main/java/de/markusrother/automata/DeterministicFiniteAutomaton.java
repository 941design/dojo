package de.markusrother.automata;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import de.markusrother.automata.exceptions.InvalidOriginException;
import de.markusrother.automata.exceptions.NoAcceptingStatesException;
import de.markusrother.automata.exceptions.NoStartStateException;
import de.markusrother.automata.exceptions.NotInAlphabetException;

/**
 * @param <T> - the generic token/alphabet type.
 */
public class DeterministicFiniteAutomaton<T> extends AbstractFiniteAutomaton<T> {

	@Override
	public FiniteAutomaton<T> copy() {
		final DeterministicFiniteAutomaton<T> copy = new DeterministicFiniteAutomaton<T>();
		copy.copyFrom(this);
		return copy;
	}

	@SuppressWarnings("unused")
	@Override
	public MutableFiniteAutomaton<T> createEmptyTransition(String originLabel, String targetLabel) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws NotInAlphabetException if given token does not belong to alphabet.
	 * @throws InvalidOriginException if given state does not belong to automaton.
	 * @throws IllegalArgumentException if either parameter is null.
	 */
	@Override
	public Collection<AutomatonState> getSuccessors(AutomatonState predecessor, T token) throws NotInAlphabetException,
			InvalidOriginException {
		if (isNullState(predecessor)) {
			return Collections.emptyList();
		}
		final AutomatonTransition<T> transition = getTransition(predecessor, token);
		final AutomatonState successor = transition == null ? getNullState() : transition.getTarget();
		return Arrays.asList(successor);
	}

	@Override
	public boolean accepts(Iterable<T> tokens) throws NoStartStateException, NoAcceptingStatesException {
		if (!hasStartState()) {
			throw new NoStartStateException();
		}
		if (!hasAcceptingStates()) {
			throw new NoAcceptingStatesException();
		}
		final Collection<AutomatonState> startStates = Arrays.asList(getStartState());
		final TransitionFunctionApplicator<T> runner = new TransitionFunctionApplicator<T>(this, startStates, tokens);
		while (runner.hasNext()) {
			runner.next();
		}
		return containsAcceptingState(runner.getCurrentStates());
	}

}
