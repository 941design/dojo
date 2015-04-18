package de.markusrother.automata;

import java.util.Iterator;

import de.markusrother.automata.exceptions.NoStartStateException;

/**
 * Stateful object for executing a {@link de.markusrother.automata.TransitionFunction} for a given input.
 *
 * {@see de.markusrother.automata.DeterministicFiniteAutomaton#accepts(Iterable)}
 */
class TransitionFunctionApplicator<T> implements Iterator<AutomatonState> {

	static <U> TransitionFunctionApplicator<U> create(DeterministicFiniteAutomaton<U> automaton, Iterable<U> tokens)
			throws NoStartStateException {
		if (!automaton.hasStartState()) {
			throw new NoStartStateException();
		}
		return new TransitionFunctionApplicator<U>(automaton, automaton.getStartState(), tokens.iterator());
	}

	private final TransitionFunction<T> transitionFunction;
	private final Iterator<T> tokens;

	private AutomatonState currentState;

	private TransitionFunctionApplicator(TransitionFunction<T> transitionFunction, AutomatonState startState,
			Iterator<T> tokens) {
		this.transitionFunction = transitionFunction;
		this.currentState = startState;
		this.tokens = tokens;
	}

	@Override
	public boolean hasNext() {
		return tokens.hasNext();
	}

	@Override
	public AutomatonState next() {
		final T token = tokens.next();
		currentState = transitionFunction.getSuccessor(currentState, token);
		return currentState;
	}

	boolean isAccepting() {
		return currentState.isAccepting();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
