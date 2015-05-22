package de.markusrother.automata;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;

import de.markusrother.automata.exceptions.DuplicateStateException;
import de.markusrother.automata.exceptions.DuplicateTransitionException;
import de.markusrother.automata.exceptions.NoSuchStateException;

/**
 * @param <T> - the generic token/alphabet type.
 */
public abstract class AbstractFiniteAutomaton<T> implements FiniteAutomaton<T> {

	private final Collection<AutomatonState> states;
	private final Collection<AutomatonTransition<T>> transitions;
	private final Collection<T> alphabet;
	private AutomatonState startState;

	public AbstractFiniteAutomaton() {
		states = new LinkedList<>();
		transitions = new LinkedList<>();
		alphabet = new HashSet<>();
	}

	@Override
	public Collection<T> getAlphabet() {
		return alphabet;
	}

	@Override
	public FiniteAutomaton<T> createStates(String... labels) throws DuplicateStateException {
		for (String label : labels) {
			createState(label);
		}
		return this;
	}

	@Override
	public Collection<AutomatonState> getStates() {
		return states;
	}

	private FiniteAutomaton<T> createState(String label) throws DuplicateStateException {
		if (hasState(label)) {
			throw new DuplicateStateException(label);
		}
		final AutomatonState state = new AutomatonStateImpl(label);
		states.add(state);
		return this;
	}

	protected boolean hasState(String label) {
		return getState(label) != null;
	}

	protected AutomatonState getState(String label) {
		return states.stream()
						.filter(s -> s.hasLabel(label))
						.findFirst()
						.orElse(null);
	}

	protected AutomatonState getExistingState(String label) throws NoSuchStateException {
		return states.stream()
						.filter(s -> s.hasLabel(label))
						.findFirst()
						.orElseThrow(new NoSuchStateException(label));
	}

	/**
	 * @return A non-accepting {@link de.markusrother.automata.AutomatonState} without any outgoing transitions.
	 *
	 * @see {@link de.markusrother.automata.NullState}
	 * @see {@link de.markusrother.automata.NullTransition}
	 */
	protected AutomatonState getNullState() {
		return NullState.getInstance();
	}

	/**
	 * @return A {@link de.markusrother.automata.AutomatonTransition} leading to a
	 *         {@link de.markusrother.automata.NullState}.
	 *
	 * @see {@link de.markusrother.automata.NullState}
	 * @see {@link de.markusrother.automata.NullTransition}
	 */
	private AutomatonTransition<T> getNullTransition() {
		return NullTransition.getInstance();
	}

	@Override
	public boolean hasStartState() {
		return startState != null;
	}

	@Override
	public AutomatonState getStartState() {
		return startState;
	}

	@Override
	public FiniteAutomaton<T> setStartState(String startStateLabel) throws NoSuchStateException {
		startState = getExistingState(startStateLabel);
		return this;
	}

	@Override
	public boolean hasAcceptingStates() {
		return states.stream()
						.anyMatch(s -> s.isAccepting());
	}

	@Override
	public Collection<AutomatonState> getAcceptingStates() {
		return states.stream()
						.filter(s -> s.isAccepting())
						.collect(Collectors.toList());
	}

	@Override
	public FiniteAutomaton<T> addAcceptingStates(String... labels) throws NoSuchStateException {
		for (String label : labels) {
			addAcceptingState(label);
		}
		return this;
	}

	private FiniteAutomaton<T> addAcceptingState(String label) throws NoSuchStateException {
		// It would be nice if states were immutable. We would have to replace the old state from our collection of
		// states, and also we would need to update all transitions, referencing the old state.
		// We could base transitions on labels only, instead of state instances.
		// That would be a rather primitive solution, but might be an interesting refactoring, because it would really
		// decouple transitions from states.
		final AutomatonState state = getExistingState(label);
		state.setAccepting(true);
		return this;
	}

	@Override
	public FiniteAutomaton<T> createTransition(String originLabel, String targetLabel, T token)
			throws NoSuchStateException, DuplicateTransitionException {
		if (token == null) {
			throw new IllegalArgumentException();
		}
		final AutomatonState origin = getExistingState(originLabel);
		final AutomatonState target = getExistingState(targetLabel);
		if (hasTransition(origin, target, token)) {
			throw new DuplicateTransitionException(originLabel, targetLabel, token);
		}
		final AutomatonTransition<T> transition = new AutomatonTransitionImpl<T>(origin, target, token);
		addTransition(transition);
		alphabet.add(token);
		return this;
	}

	protected void addTransition(final AutomatonTransition<T> transition) {
		transitions.add(transition);
	}

	private boolean hasTransition(AutomatonState origin, AutomatonState target, T token) {
		return getTransition(origin, target, token) != null;
	}

	private AutomatonTransition<T> getTransition(AutomatonState origin, AutomatonState target, T token) {
		return transitions.stream()
							.filter(t -> t.hasOrigin(origin))
							.filter(t -> t.hasTarget(target))
							.filter(t -> t.hasToken(token))
							.findFirst()
							.orElse(null);
	}

	@Override
	public Collection<AutomatonTransition<T>> getTransitions() {
		return transitions;
	}

	@Override
	public AutomatonTransition<T> getTransition(AutomatonState origin, T token) {
		if (origin == null) {
			throw new IllegalArgumentException();
		}
		if (token == null) {
			throw new IllegalArgumentException();
		}
		return transitions.stream()
							.filter(t -> t.hasOrigin(origin))
							.filter(t -> t.hasToken(token))
							.findFirst()
							.orElse(getNullTransition());
	}

	protected FiniteAutomaton<T> copyInto(AbstractFiniteAutomaton<T> copy) {
		try {
			copyStatesInto(copy);
			copyTransitionsInto(copy);
			return copy;
		}
		catch (NoSuchStateException | DuplicateStateException | DuplicateTransitionException e) {
			throw new IllegalStateException(e);
		}
	}

	private void copyStatesInto(final AbstractFiniteAutomaton<T> copy) throws NoSuchStateException,
			DuplicateStateException {
		for (AutomatonState state : states) {
			final String stateLabel = state.getLabel();
			copy.createState(stateLabel);
			// Not based on getAcceptingStates() for slightly better performance:
			if (state.isAccepting()) {
				copy.addAcceptingState(stateLabel);
			}
		}
		if (hasStartState()) {
			final String startStateLabel = startState.getLabel();
			copy.setStartState(startStateLabel);
		}
	}

	private void copyTransitionsInto(AbstractFiniteAutomaton<T> copy) throws NoSuchStateException,
			DuplicateTransitionException {
		for (AutomatonTransition<T> transition : transitions) {
			final String originLabel = transition.getOriginLabel();
			final String targetLabel = transition.getTargetLabel();
			final T token = transition.getToken();
			copy.createTransition(originLabel, targetLabel, token);
		}
	}

	/**
	 * TODO - Currently only tests structural equality, NOT semantic equality!
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final FiniteAutomaton<?> other = (FiniteAutomaton<?>) obj;
		return FiniteAutomatonComparator.areStructurallyEqual(this, other);
	}

	@Override
	public String toString() {
		final String format = "alphabet:%s\nstates: %s\nstart state: %s\naccepting states:%s\ntransitions:%s";
		return String.format(format, getAlphabet(), getStates(), getStartState(), getAcceptingStates(),
				getTransitions());
	}

}
