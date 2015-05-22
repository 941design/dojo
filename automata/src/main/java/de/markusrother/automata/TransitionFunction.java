package de.markusrother.automata;

import java.util.Collection;

import de.markusrother.automata.exceptions.InvalidOriginException;
import de.markusrother.automata.exceptions.NotInAlphabetException;

/**
 * @param <T> - the generic token/alphabet type.
 */
public interface TransitionFunction<T> {

	/**
	 * @param predecessor - The origin {@link de.markusrother.automata.AutomatonState}.
	 * @param token - The object for which a transition may exist.
	 * @return The {@link de.markusrother.automata.AutomatonState}s reached by following a transition from the given
	 *         state for the given token.
	 *
	 * @throws NotInAlphabetException if given token does not belong to alphabet.
	 * @throws InvalidOriginException if given state does not belong to automaton.
	 *
	 * @see {@link de.markusrother.automata.NullState}
	 * @see {@link de.markusrother.automata.AutomatonTransition}
	 */
	Collection<AutomatonState> getSuccessors(AutomatonState predecessor, T token) throws NotInAlphabetException,
			InvalidOriginException;

}
