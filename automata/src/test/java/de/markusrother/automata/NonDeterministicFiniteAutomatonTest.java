package de.markusrother.automata;

import static de.markusrother.automata.AutomatonStateMatcher.isState;
import static de.markusrother.automata.EitherOrAccepting.ACCEPTING;
import static de.markusrother.automata.EitherOrAccepting.NOT_ACCEPTING;
import static org.hamcrest.Matchers.contains;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NonDeterministicFiniteAutomatonTest extends AbstractFiniteAutomatonTest {

	@Before
	public void before() {
		automaton = new NonDeterministicFiniteAutomaton<Object>();
	}

	@Override
	<T> NonDeterministicFiniteAutomaton<T> createAutomaton() {
		return new NonDeterministicFiniteAutomaton<T>();
	}

	@Test
	public void testCreateEmptyTransition() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, NOT_ACCEPTING)
					.createEmptyTransition(S1, S2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSuccessorsOfEmptyTransition() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, NOT_ACCEPTING)
					.createState(S3, NOT_ACCEPTING)
					.createTransition(S1, S2, TOKEN)
					.createEmptyTransition(S2, S2)
					.createEmptyTransition(S2, S3);
		final AutomatonState s1 = automaton.getState(S1);
		Assert.assertThat(automaton.getSuccessors(s1, TOKEN), contains(isState(S2), isState(S3)));
	}

	@Test
	public void testSingleCircularEmptyTransition() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createTransition(S1, S1, TOKEN)
					.createEmptyTransition(S1, S1);
		final AutomatonState s1 = automaton.getState(S1);
		Assert.assertThat(automaton.getSuccessors(s1, TOKEN), contains(isState(S1)));
	}

	@Test
	public void testMultipleCircularEmptyTransitions() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, NOT_ACCEPTING)
					.createState(S3, NOT_ACCEPTING)
					.createState(S4, ACCEPTING)
					.setStartState(S1)
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
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, NOT_ACCEPTING)
					.createState(S3, ACCEPTING)
					.setStartState(S1)
					.createEmptyTransition(S1, S2)
					.createTransition(S2, S3, TOKEN);
		assertRejects();
		assertAccepts(TOKEN);
	}

	@Test
	public void testAcceptsEmptyWordIfStartStateHasEmptyTransitionToAcceptingState() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, NOT_ACCEPTING)
					.createState(S3, ACCEPTING)
					.setStartState(S1)
					.createEmptyTransition(S1, S2)
					.createEmptyTransition(S2, S3);
		assertAccepts();
	}

}
