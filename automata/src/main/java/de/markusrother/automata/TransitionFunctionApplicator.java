package de.markusrother.automata;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import de.markusrother.automata.exceptions.NoStartStateException;

/**
 * Stateful object for executing a {@link de.markusrother.automata.TransitionFunction} for a given input.
 *
 * {@see de.markusrother.automata.FiniteAutomaton#accepts(Iterable)}
 */
class TransitionFunctionApplicator<T> implements Iterator<Collection<AutomatonState>> {

	static <U> TransitionFunctionApplicator<U> create(FiniteAutomaton<U> automaton, Iterable<U> tokens)
			throws NoStartStateException {
		if (!automaton.hasStartState()) {
			throw new NoStartStateException();
		}
		return new TransitionFunctionApplicator<U>(automaton, automaton.getStartState(), tokens.iterator());
	}

	private final TransitionFunction<T> transitionFunction;
	private final Iterator<T> tokens;

	private Collection<AutomatonState> currentStates;

	private TransitionFunctionApplicator(TransitionFunction<T> transitionFunction, AutomatonState startState,
			Iterator<T> tokens) {
		this.transitionFunction = transitionFunction;
		this.currentStates = Arrays.asList(startState);
		this.tokens = tokens;
	}

	@Override
	public boolean hasNext() {
		return tokens.hasNext();
	}

	@Override
	public Collection<AutomatonState> next() {
		final T token = tokens.next();
		currentStates = collectSuccessors(currentStates, token);
		return currentStates;
	}

	private Collection<AutomatonState> collectSuccessors(Collection<AutomatonState> predecessors, T token) {
		final Collection<AutomatonState> successors = new LinkedList<>();
		for (AutomatonState predecessor : predecessors) {
			successors.addAll(transitionFunction.getSuccessors(predecessor, token));
		}
		return successors;
	}

	boolean isAccepting() {
		return containsAcceptingState(currentStates);
	}

	private static boolean containsAcceptingState(Collection<AutomatonState> states) {
		for (AutomatonState state : states) {
			if (state.isAccepting()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
