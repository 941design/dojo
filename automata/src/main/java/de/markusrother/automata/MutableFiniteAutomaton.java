package de.markusrother.automata;

import de.markusrother.automata.exceptions.DuplicateStateException;
import de.markusrother.automata.exceptions.DuplicateTransitionException;
import de.markusrother.automata.exceptions.NoSuchStateException;

/**
 * @param <T> - the generic token/alphabet type.
 */
public interface MutableFiniteAutomaton<T> extends FiniteAutomaton<T> {

	MutableFiniteAutomaton<T> createStates(String... stateLabels) throws DuplicateStateException;

	MutableFiniteAutomaton<T> setStartState(String startStateLabel) throws NoSuchStateException;

	MutableFiniteAutomaton<T> addAcceptingStates(String... acceptingStateLabels) throws NoSuchStateException;

	MutableFiniteAutomaton<T> createTransition(String originLabel, String targetLabel, T token)
			throws NoSuchStateException,
			DuplicateTransitionException;

	MutableFiniteAutomaton<T> createEmptyTransition(String originLabel, String targetLabel)
			throws NoSuchStateException,
			DuplicateTransitionException;

}
