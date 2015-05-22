package de.markusrother.automata;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

import de.markusrother.automata.exceptions.DuplicateEmptyTransitionException;
import de.markusrother.automata.exceptions.DuplicateTransitionException;
import de.markusrother.automata.exceptions.NoAcceptingStatesException;
import de.markusrother.automata.exceptions.NoStartStateException;
import de.markusrother.automata.exceptions.NoSuchStateException;

/**
 * This automaton accepts the empty word if its start state has any empty transitions to an accepting state.
 *
 * TODO - implement a list based set
 *
 * @param <T> - the generic token/alphabet type.
 */
public class NonDeterministicFiniteAutomaton<T> extends AbstractFiniteAutomaton<T> {

	@Override
	public FiniteAutomaton<T> createEmptyTransition(String originLabel, String targetLabel)
			throws NoSuchStateException, DuplicateTransitionException {
		final AutomatonState origin = getExistingState(originLabel);
		final AutomatonState target = getExistingState(targetLabel);
		if (hasTransition(origin, target)) {
			throw new DuplicateEmptyTransitionException(originLabel, targetLabel);
		}
		final AutomatonTransition<T> transition = new EmptyAutomatonTransitionImpl<T>(origin, target);
		addTransition(transition);
		return this;
	}

	private boolean hasTransition(AutomatonState origin, AutomatonState target) {
		return getTransition(origin, target) != null;
	}

	private AutomatonTransition<T> getTransition(AutomatonState origin, AutomatonState target) {
		return getTransitions().stream()
								.filter(t -> t.hasOrigin(origin))
								.filter(t -> t.hasTarget(target))
								.filter(t -> t.isEmpty())
								.findFirst()
								.orElse(null);
	}

	@Override
	public FiniteAutomaton<T> copy() {
		return copyInto(new NonDeterministicFiniteAutomaton<T>());
	}

	/**
	 * @throws IllegalArgumentException if either parameter is null.
	 */
	@Override
	public Collection<AutomatonState> getSuccessors(AutomatonState predecessor, T token) {
		// TODO - We should also assert that state and token belong to this automaton!
		final AutomatonTransition<T> transition = getTransition(predecessor, token);
		final AutomatonState successor = transition == null ? getNullState() : transition.getTarget();
		return expandState(successor);
	}

	/**
	 * @param state
	 * @return A collection of states including the given state, and states that the given states has empty transitions
	 *         to, recursively.
	 */
	private Collection<AutomatonState> expandState(AutomatonState state) {
		final Collection<AutomatonState> visited = new LinkedList<>();
		final Collection<AutomatonState> collected = new LinkedList<>();
		collected.add(state);
		expandState(state, visited, collected);
		return collected;
	}

	private void
			expandState(AutomatonState state, Collection<AutomatonState> visited, Collection<AutomatonState> collected) {
		visited.add(state);
		final Collection<AutomatonTransition<T>> emptyTransitions = getEmptyTransitions(state);
		for (AutomatonTransition<T> transition : emptyTransitions) {
			final AutomatonState target = transition.getTarget();
			if (!visited.contains(target)) {
				collected.add(target);
				expandState(target, visited, collected);
			}
		}
	}

	/**
	 * @param state
	 * @return A collection of states that the given states has empty transitions to, NON - recursively.
	 */
	private Collection<AutomatonTransition<T>> getEmptyTransitions(AutomatonState state) {
		return getTransitions().stream()
								.filter(t -> t.hasOrigin(state))
								.filter(t -> t.isEmpty())
								.collect(Collectors.toList());
	}

	/**
	 * This automaton accepts the empty word if its start state has any empty transitions to an accepting state.
	 */
	@Override
	public boolean accepts(Iterable<T> tokens) throws NoStartStateException, NoAcceptingStatesException {
		if (!hasStartState()) {
			throw new NoStartStateException();
		}
		if (!hasAcceptingStates()) {
			throw new NoAcceptingStatesException();
		}
		final Collection<AutomatonState> startStates = expandState(getStartState());
		final TransitionFunctionApplicator<T> runner = new TransitionFunctionApplicator<T>(this, startStates, tokens);
		while (runner.hasNext()) {
			runner.next();
		}
		return runner.isAccepting();
	}

}
