package de.markusrother.automata;

import org.junit.Before;

public class DeterministicFiniteAutomatonTest extends AbstractFiniteAutomatonTest {

	@Before
	public void before() {
		automaton = new DeterministicFiniteAutomaton<Object>();
	}

	@Override
	<T> DeterministicFiniteAutomaton<T> createAutomaton() {
		return new DeterministicFiniteAutomaton<T>();
	}

}
