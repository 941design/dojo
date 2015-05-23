package de.markusrother.automata;

import java.util.Collection;

import de.markusrother.automata.exceptions.InvalidOriginException;
import de.markusrother.automata.exceptions.NoAcceptingStatesException;
import de.markusrother.automata.exceptions.NoStartStateException;
import de.markusrother.automata.exceptions.NotInAlphabetException;

/**
 * @param <T> - the generic token/alphabet type.
 */
public interface FiniteAutomaton<T> extends TransitionFunction<T> {

	/**
	 * @return Collection of all tokens for which {@link de.markusrother.automata.AutomatonTransition}s exist.
	 */
	Collection<T> getAlphabet();

	Collection<AutomatonState> getStates();

	AutomatonState getState(String label);

	AutomatonState getStartState();

	boolean hasStartState();

	Collection<AutomatonState> getAcceptingStates();

	boolean hasAcceptingStates();

	/**
	 * @return Collection of all {@link de.markusrother.automata.AutomatonTransition}s for given automaton.
	 */
	Collection<AutomatonTransition<T>> getTransitions();

	AutomatonTransition<T> getTransition(AutomatonState origin, T token) throws NotInAlphabetException,
			InvalidOriginException;

	/**
	 * @param tokens - The word for which to test acceptance.
	 *
	 * @return True if given automaton stops in an accepting {@link de.markusrother.automata.AutomatonState}.
	 *
	 * @throws NoStartStateException
	 * @throws NoAcceptingStatesException
	 * @throws NotInAlphabetException
	 */
	boolean accepts(Iterable<T> tokens) throws NoStartStateException, NoAcceptingStatesException,
			NotInAlphabetException;

	/**
	 * @return A full copy of the given automaton. Tokens are not copied!
	 */
	FiniteAutomaton<T> copy();

}
