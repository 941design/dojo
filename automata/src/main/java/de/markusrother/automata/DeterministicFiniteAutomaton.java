package de.markusrother.automata;

import java.util.Collection;
import java.util.LinkedList;

import de.markusrother.automata.exceptions.DuplicateStateException;
import de.markusrother.automata.exceptions.DuplicateTransitionException;
import de.markusrother.automata.exceptions.NoStartStateException;
import de.markusrother.automata.exceptions.NoSuchStateException;

public class DeterministicFiniteAutomaton<T> implements TransitionFunction<T> {

	private final Collection<AutomatonState> states;

	private final Collection<AutomatonTransition<T>> transitions;

	private AutomatonState startState;

	public DeterministicFiniteAutomaton() {
		states = new LinkedList<>();
		transitions = new LinkedList<>();
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

	public Collection<AutomatonTransition<T>> getTransitions() {
		return transitions;
	}

	private AutomatonTransition<T> getTransition(AutomatonState origin, T token) {
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

}
