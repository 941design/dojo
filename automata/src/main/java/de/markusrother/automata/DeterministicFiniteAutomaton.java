package de.markusrother.automata;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import de.markusrother.automata.exceptions.DuplicateStateException;
import de.markusrother.automata.exceptions.DuplicateTransitionException;
import de.markusrother.automata.exceptions.NoStartStateException;
import de.markusrother.automata.exceptions.NoSuchStateException;

public class DeterministicFiniteAutomaton<T> implements TransitionFunction<T> {

	private final Collection<AutomatonState> states;

	private final Collection<AutomatonTransition<T>> transitions;

	private final Collection<T> alphabet;

	private AutomatonState startState;

	public DeterministicFiniteAutomaton() {
		states = new LinkedList<>();
		transitions = new LinkedList<>();
		alphabet = new HashSet<>();
	}

	public Collection<T> getAlphabet() {
		return alphabet;
	}

	public DeterministicFiniteAutomaton<T> createStates(String... labels) throws DuplicateStateException {
		for (String label : labels) {
			createState(label);
		}
		return this;
	}

	public Collection<AutomatonState> getStates() {
		return states;
	}

	private void createState(String label) throws DuplicateStateException {
		if (hasState(label)) {
			throw new DuplicateStateException(label);
		}
		final AutomatonState state = new AutomatonStateImpl(label);
		states.add(state);
	}

	public boolean hasState(String label) {
		return getState(label) != null;
	}

	/**
	 * It is more convenient to throw an Exception here. However, if this method is made public, it should return null
	 * instead!
	 */
	private AutomatonState getState(String label) {
		for (AutomatonState state : states) {
			if (state.hasLabel(label)) {
				return state;
			}
		}
		return null;
	}

	private AutomatonState getExistingState(String label) throws NoSuchStateException {
		final AutomatonState state = getState(label);
		if (state != null) {
			return state;
		}
		throw new NoSuchStateException(label);
	}

	/**
	 * @return A non-accepting {@link de.markusrother.automata.AutomatonState} without any outgoing transitions.
	 *
	 * @see {@link de.markusrother.automata.NullState}
	 */
	private AutomatonState getNullState() {
		return NullState.INSTANCE;
	}

	public boolean hasStartState() {
		return startState != null;
	}

	public AutomatonState getStartState() {
		return startState;
	}

	public DeterministicFiniteAutomaton<T> setStartState(String startStateLabel) throws NoSuchStateException {
		startState = getExistingState(startStateLabel);
		return this;
	}

	public Collection<AutomatonState> getAcceptingStates() {
		final Collection<AutomatonState> acceptingStates = new LinkedList<>();
		for (AutomatonState state : states) {
			if (state.isAccepting()) {
				acceptingStates.add(state);
			}
		}
		return acceptingStates;
	}

	public DeterministicFiniteAutomaton<T> addAcceptingStates(String... labels) throws NoSuchStateException {
		for (String label : labels) {
			addAcceptingState(label);
		}
		return this;
	}

	private DeterministicFiniteAutomaton<T> addAcceptingState(String label) throws NoSuchStateException {
		final AutomatonState state = getExistingState(label);
		state.setAccepting(true);
		return this;
	}

	@Override
	public AutomatonState getSuccessor(AutomatonState predecessor, T token) {
		final AutomatonTransition<T> transition = getTransition(predecessor, token);
		return transition == null ? getNullState() : transition.getTarget();
	}

	public DeterministicFiniteAutomaton<T> createTransition(String originLabel, String targetLabel, T token)
			throws NoSuchStateException, DuplicateTransitionException {
		if (token == null) {
			throw new IllegalArgumentException();
		}
		final AutomatonState origin = getExistingState(originLabel);
		final AutomatonState target = getExistingState(targetLabel);
		if (hasTransition(origin, target, token)) {
			throw new DuplicateTransitionException(originLabel, targetLabel, token);
		}
		final AutomatonTransition<T> transition = new AutomatonTransition<T>(origin, target, token);
		transitions.add(transition);
		alphabet.add(token);
		return this;
	}

	private boolean hasTransition(AutomatonState origin, AutomatonState target, T token) {
		return getTransition(origin, target, token) != null;
	}

	private AutomatonTransition<T> getTransition(AutomatonState origin, AutomatonState target, T token) {
		for (AutomatonTransition<T> transition : transitions) {
			final AutomatonState transitionOrigin = transition.getOrigin();
			final AutomatonState transitionTarget = transition.getTarget();
			final Object transitionToken = transition.getToken();
			if (transitionOrigin.equals(origin)
					&& transitionTarget.equals(target)
					&& transitionToken.equals(token)) {
				return transition;
			}
		}
		return null;
	}

	Collection<AutomatonTransition<T>> getTransitions() {
		return transitions;
	}

	AutomatonTransition<T> getTransition(AutomatonState origin, T token) {
		for (AutomatonTransition<T> transition : transitions) {
			if (transition.getOrigin().equals(origin) && transition.getToken().equals(token)) {
				return transition;
			}
		}
		return null;
	}

	public boolean accepts(Iterable<T> tokens) throws NoStartStateException {
		final TransitionFunctionApplicator<T> runner = TransitionFunctionApplicator.create(this, tokens);
		while (runner.hasNext()) {
			runner.next();
		}
		return runner.isAccepting();
	}

	public DeterministicFiniteAutomaton<T> copy() {
		try {
			final DeterministicFiniteAutomaton<T> copy = new DeterministicFiniteAutomaton<T>();
			copyStatesInto(copy);
			copyTransitionsInto(copy);
			return copy;
		}
		catch (NoSuchStateException | DuplicateStateException | DuplicateTransitionException e) {
			throw new IllegalStateException(e);
		}
	}

	private void copyStatesInto(final DeterministicFiniteAutomaton<T> copy) throws NoSuchStateException,
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
			String startStateLabel = startState.getLabel();
			copy.setStartState(startStateLabel);
		}
	}

	private void copyTransitionsInto(DeterministicFiniteAutomaton<T> copy) throws NoSuchStateException,
			DuplicateTransitionException {
		for (AutomatonTransition<T> transition :transitions) {
			final String originLabel = transition.getOriginLabel();
			final String targetLabel = transition.getTargetLabel();
			final T token = transition.getToken();
			copy.createTransition(originLabel, targetLabel, token);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final DeterministicFiniteAutomaton<?> other = (DeterministicFiniteAutomaton<?>) obj;
		return DeterministicAutomatonComparator.areEqual(this, other);
	}

}
