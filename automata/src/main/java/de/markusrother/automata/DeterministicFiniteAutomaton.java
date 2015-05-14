package de.markusrother.automata;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import de.markusrother.automata.exceptions.NoAcceptingStatesException;
import de.markusrother.automata.exceptions.NoStartStateException;

/**
 * @param <T> - the generic token/alphabet type.
 */
public class DeterministicFiniteAutomaton<T> extends AbstractFiniteAutomaton<T> {

	@Override
	public FiniteAutomaton<T> copy() {
		return copyInto(new DeterministicFiniteAutomaton<T>());
	}

	@SuppressWarnings("unused")
	@Override
	public FiniteAutomaton<T> createEmptyTransition(String originLabel, String targetLabel) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws IllegalArgumentException if either parameter is null.
	 */
	@Override
	public Collection<AutomatonState> getSuccessors(AutomatonState predecessor, T token) {
		// TODO - We should also assert that state and token belong to this automaton!
		final AutomatonTransition<T> transition = getTransition(predecessor, token);
		final AutomatonState successor = transition == null ? getNullState() : transition.getTarget();
		return Collections.unmodifiableCollection(Arrays.asList(successor));
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
		return runner.isAccepting();
	}

}
