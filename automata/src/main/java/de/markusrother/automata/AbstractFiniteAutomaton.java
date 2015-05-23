package de.markusrother.automata;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;

import de.markusrother.automata.exceptions.DuplicateStateException;
import de.markusrother.automata.exceptions.DuplicateTransitionException;
import de.markusrother.automata.exceptions.InvalidOriginException;
import de.markusrother.automata.exceptions.NoSuchStateException;
import de.markusrother.automata.exceptions.NotInAlphabetException;

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
		addState(state);
		return this;
	}

	protected void addState(AutomatonState state) {
		this.states.add(state);
	}

	protected void addStates(Collection<AutomatonState> states) {
		this.states.addAll(states);
	}

	protected boolean hasState(String label) {
		return states.stream()
						.anyMatch(s -> s.hasLabel(label));
			}

	@Override
	public AutomatonState getState(String label) {
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

	protected boolean isNullState(AutomatonState state) {
		return state == getNullState();
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
		addToken(token);
		return this;
	}

	protected void addToken(T token) {
		alphabet.add(token);
	}

	protected void addTokens(Collection<T> tokens) {
		alphabet.addAll(tokens);
	}

	protected void addTransition(final AutomatonTransition<T> transition) {
		transitions.add(transition);
	}

	protected void addTransitions(Collection<AutomatonTransition<T>> transitions) {
		this.transitions.addAll(transitions);
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
	public AutomatonTransition<T> getTransition(AutomatonState origin, T token) throws NotInAlphabetException,
			InvalidOriginException {
		if (origin == null || isNullState(origin)) {
			throw new IllegalArgumentException();
		}
		if (!states.contains(origin)) {
			throw new InvalidOriginException(origin);
		}
		if (token == null) {
			throw new IllegalArgumentException();
		}
		if (!alphabet.contains(token)) {
			throw new NotInAlphabetException(alphabet, token);
		}
		return transitions.stream()
							.filter(t -> t.hasOrigin(origin))
							.filter(t -> t.hasToken(token))
							.findFirst()
							.orElse(getNullTransition());
	}

	protected void copyFrom(FiniteAutomaton<T> other) {
		try {
			copyStatesFrom(other);
			copyStartStateFrom(other);
			copyAcceptingStatesFrom(other);
			copyTransitionsFrom(other);
		}
		catch (NoSuchStateException | DuplicateStateException | DuplicateTransitionException e) {
			throw new IllegalStateException(e);
		}
	}

	protected void copyStatesFrom(final FiniteAutomaton<T> other) throws DuplicateStateException {
		for (AutomatonState state : other.getStates()) {
			final String stateLabel = state.getLabel();
			createState(stateLabel);
		}
	}

	protected void copyStartStateFrom(FiniteAutomaton<T> other) throws NoSuchStateException {
		if (other.hasStartState()) {
			final String startStateLabel = other.getStartState()
												.getLabel();
			setStartState(startStateLabel);
		}
	}

	protected void copyAcceptingStatesFrom(FiniteAutomaton<T> other) throws NoSuchStateException {
		for (AutomatonState state : other.getAcceptingStates()) {
			final String stateLabel = state.getLabel();
			addAcceptingState(stateLabel);
		}
	}

	protected void copyTransitionsFrom(FiniteAutomaton<T> other) throws NoSuchStateException,
			DuplicateTransitionException {
		for (AutomatonTransition<T> transition : other.getTransitions()) {
			final String originLabel = transition.getOriginLabel();
			final String targetLabel = transition.getTargetLabel();
			final T token = transition.getToken();
			createTransition(originLabel, targetLabel, token);
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
