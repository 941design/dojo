package de.markusrother.automata;

public class DeterministicFiniteAutomaton<T> extends AbstractFiniteAutomaton<T> {

	@Override
	public FiniteAutomaton<T> copy() {
		return copyInto(new DeterministicFiniteAutomaton<T>());
	}

}
