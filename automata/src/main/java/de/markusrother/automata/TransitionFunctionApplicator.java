package de.markusrother.automata;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import de.markusrother.automata.exceptions.InvalidOriginException;
import de.markusrother.automata.exceptions.NotInAlphabetException;

/**
 * Stateful object for executing a {@link de.markusrother.automata.TransitionFunction} for a given input.
 *
 * @param <T> - the generic token/alphabet type.
 *
 * @see de.markusrother.automata.FiniteAutomaton#accepts(Iterable)
 */
class TransitionFunctionApplicator<T> implements Iterator<Collection<AutomatonState>> {

	private final TransitionFunction<T> transitionFunction;
	private final Iterator<T> tokens;

	private Collection<AutomatonState> currentStates;

	TransitionFunctionApplicator(TransitionFunction<T> transitionFunction, Collection<AutomatonState> startStates,
			Iterable<T> tokens) {
		this.transitionFunction = transitionFunction;
		this.currentStates = startStates;
		this.tokens = tokens.iterator();
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

	public Collection<AutomatonState> getCurrentStates() {
		return currentStates;
	}

	private Collection<AutomatonState> collectSuccessors(Collection<AutomatonState> predecessors, T token)
			throws NotInAlphabetException, InvalidOriginException {
		return predecessors.stream()
					.flatMap(predecessor -> transitionFunction.getSuccessors(predecessor, token)
																		.stream())
					.collect(Collectors.toList());
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
