package de.markusrother.automata;

import java.util.Collection;

import de.markusrother.automata.exceptions.DuplicateStateException;
import de.markusrother.automata.exceptions.DuplicateTransitionException;
import de.markusrother.automata.exceptions.NoStartStateException;
import de.markusrother.automata.exceptions.NoSuchStateException;

public interface FiniteAutomaton<T> extends TransitionFunction<T> {

	/**
	 * @return Collection of all tokens for which {@link de.markusrother.automata.AutomatonTransition}s exist.
	 */
	Collection<T> getAlphabet();

	FiniteAutomaton<T> createStates(String... stateLabels) throws DuplicateStateException;

	Collection<AutomatonState> getStates();

	AutomatonState getStartState();

	FiniteAutomaton<T> setStartState(String startStateLabel) throws NoSuchStateException;

	boolean hasStartState();

	FiniteAutomaton<T> addAcceptingStates(String... acceptingStateLabels) throws NoSuchStateException;

	Collection<AutomatonState> getAcceptingStates();

	FiniteAutomaton<T> createTransition(String originLabel, String targetLabel, T token) throws NoSuchStateException,
			DuplicateTransitionException;

	/**
	 * @return Collection of all {@link de.markusrother.automata.AutomatonTransition}s for given automaton.
	 */
	Collection<AutomatonTransition<T>> getTransitions();

	AutomatonTransition<T> getTransition(AutomatonState origin, T token);

	/**
	 * @param tokens - The word for which to test acceptance.
	 *
	 * @return True if given automaton stops in an accepting {@link de.markusrother.automata.AutomatonState}.
	 *
	 * @throws NoStartStateException
	 */
	boolean accepts(Iterable<T> tokens) throws NoStartStateException;

	/**
	 * @return A full copy of the given automaton. Tokens are not copied!
	 */
	FiniteAutomaton<T> copy();

}
