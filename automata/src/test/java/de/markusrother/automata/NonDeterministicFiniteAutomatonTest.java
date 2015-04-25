package de.markusrother.automata;

import static de.markusrother.automata.AutomatonStateMatcher.isState;
import static org.hamcrest.Matchers.contains;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO - test circular empty transitions
 *
 * TODO - test has (not) empty transitions
 *
 * TODO - test getSuccessors()
 *
 */
public class NonDeterministicFiniteAutomatonTest extends AbstractFiniteAutomatonTest {

	@Before
	public void before() {
		automaton = new NonDeterministicFiniteAutomaton<Object>();
	}

	@Override
	<T> FiniteAutomaton<T> createAutomaton() {
		return new NonDeterministicFiniteAutomaton<T>();
	}

	@Test
	public void testCreateEmptyTransition() throws Exception {
		automaton.createStates(S1, S2)
					.createEmptyTransition(S1, S2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSuccessorsOfEmptyTransition() throws Exception {
		automaton.createStates(S1, S2, S3)
					.createTransition(S1, S2, TOKEN)
					.createEmptyTransition(S2, S2)
					.createEmptyTransition(S2, S3);
		final AutomatonState s1 = automaton.getState(S1);
		Assert.assertThat(automaton.getSuccessors(s1, TOKEN), contains(isState(S2), isState(S3)));
	}

	@Test
	public void testSingleCircularEmptyTransition() throws Exception {
		automaton.createStates(S1)
					.createTransition(S1, S1, TOKEN)
					.createEmptyTransition(S1, S1);
		final AutomatonState s1 = automaton.getState(S1);
		Assert.assertThat(automaton.getSuccessors(s1, TOKEN), contains(isState(S1)));
	}

	@Test
	public void testMultipleCircularEmptyTransitions() throws Exception {
		automaton.createStates(S1, S2, S3, S4)
					.setStartState(S1)
					.addAcceptingStates(S4)
					.createTransition(S1, S2, 1)
					.createEmptyTransition(S2, S3)
					.createEmptyTransition(S3, S2)
					.createTransition(S3, S2, 0)
					.createTransition(S3, S4, 1);
		assertRejects();
		assertRejects(1);
		assertAccepts(1, 1);
		assertAccepts(1, 0, 1);
		assertAccepts(1, 0, 0, 1);
		assertRejects(1, 1, 1);
	}

	@Test
	public void testExpandStartState() throws Exception {
		automaton.createStates(S1, S2, S3)
					.setStartState(S1)
					.addAcceptingStates(S3)
					.createEmptyTransition(S1, S2)
					.createTransition(S2, S3, TOKEN);
		assertAccepts(TOKEN);
	}

}
