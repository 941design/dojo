package de.markusrother.automata;

import java.util.Collection;

/**
 * TODO - test getSuccessors (?)
 *
 * @param <T>
 */
public interface TransitionFunction<T> {

	/**
	 * @param predecessor - The origin {@link de.markusrother.automata.AutomatonState}.
	 * @param token - The object for which a transition may exist.
	 * @return The {@link de.markusrother.automata.AutomatonState}s reached by following a transition from the given
	 *         state for the given token.
	 *
	 * @see {@link de.markusrother.automata.NullState}
	 * @see {@link de.markusrother.automata.AutomatonTransition}
	 */
	Collection<AutomatonState> getSuccessors(AutomatonState predecessor, T token);

}
