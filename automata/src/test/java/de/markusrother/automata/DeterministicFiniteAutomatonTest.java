package de.markusrother.automata;

import static de.markusrother.automata.AutomatonStateMatcher.isState;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.markusrother.automata.exceptions.DuplicateStateException;
import de.markusrother.automata.exceptions.DuplicateTransitionException;
import de.markusrother.automata.exceptions.NoStartStateException;
import de.markusrother.automata.exceptions.NoSuchStateException;

public class DeterministicFiniteAutomatonTest {

	private static final String S1 = "s1";
	private static final String S2 = "s2";
	private static final Object TOKEN = new Object();

	private DeterministicFiniteAutomaton<Object> dfa;

	@Before
	public void before() {
		dfa = new DeterministicFiniteAutomaton<Object>();
	}

	@Test
	public void testNewAutomatonHasNoStates() throws Exception {
		assertAmountOfStates(0);
	}

	@Test(expected = NoStartStateException.class)
	public void testNewAutomatonHasNoStartState() throws Exception {
		dfa.accepts(new ArrayList<Object>());
	}

	@Test
	public void testNewAutomatonHasNoAcceptingStates() throws Exception {
		assertAmountOfAcceptingStates(0);
	}

	@Test
	public void testNewAutomatonHasNoTransitions() throws Exception {
		assertAmountOfTransitions(0);
	}

	@Test
	public void testAutomatonStartsInAcceptingState() throws Exception {
		dfa.createStates(S1)
			.setStartState(S1)
			.addAcceptingStates(S1);
		assertAccepts();
	}

	@Test
	public void testCreateState() throws Exception {
		dfa.createStates(S1);
		assertAmountOfStates(1);
	}

	@Test(expected = DuplicateStateException.class)
	public void testCreateDuplicateState() throws Exception {
		dfa.createStates(S1)
			.createStates(S1);
	}

	@Test
	public void testCreateStartState() throws Exception {
		dfa.createStates(S1)
			.setStartState(S1);
		Assert.assertThat(dfa.getStartState(), isState(S1));
	}

	@Test(expected = NoSuchStateException.class)
	public void testCreateInexistentStartState() throws Exception {
		dfa.setStartState(S1);
	}

	@Test
	public void testCreateAcceptingState() throws Exception {
		dfa.createStates(S1)
			.addAcceptingStates(S1);
		assertAmountOfAcceptingStates(1);
		Assert.assertThat(dfa.getAcceptingStates(), contains(isState(S1)));
	}

	@Test(expected = NoSuchStateException.class)
	public void testCreateInexistentAcceptingState() throws Exception {
		dfa.addAcceptingStates(S1);
	}

	@Test
	public void testCreateTransition() throws Exception {
		dfa.createStates(S1, S2)
			.createTransition(S1, S2, TOKEN);
		assertAmountOfTransitions(1);
	}

	@Test(expected = DuplicateTransitionException.class)
	public void testCreateDuplicateTransition() throws Exception {
		final String token = "token";
		dfa.createStates(S1, S2)
			.createTransition(S1, S2, token)
			.createTransition(S1, S2, token);
	}

	@Test(expected = NoSuchStateException.class)
	public void testCreateTransitionWithInexistentStates() throws Exception {
		dfa.createTransition(S1, S2, TOKEN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateTransitionWithNullToken() throws Exception {
		dfa.createStates(S1, S2)
			.createTransition(S1, S2, null);
	}

	@Test
	public void testAutomatonRejects() throws Exception {
		dfa.createStates(S1)
			.setStartState(S1);
		final Object token = new Object();
		assertRejects();
		assertRejects(token);
		assertRejects(token, token);
	}

	@Test
	public void testAutomatonAcceptSingleObject() throws Exception {
		final Object obj = new Object();
		dfa.createStates(S1, S2)
			.setStartState(S1)
			.addAcceptingStates(S2)
			.createTransition(S1, S2, obj);
		assertRejects();
		assertAccepts(obj);
	}

	@Test
	public void testAutomatonLeavesAcceptingState() throws Exception {
		final Object token = new Object();
		dfa.createStates(S1, S2)
			.setStartState(S1)
			.addAcceptingStates(S2)
			.createTransition(S1, S2, token)
			.createTransition(S2, S1, token);
		assertRejects();
		assertAccepts(token);
		assertRejects(token, token);
		assertAccepts(token, token, token);
		assertRejects(token, token, token, token);
	}

	@Test
	public void testAutomatonLeavesAcceptingStateIntoNullState() throws Exception {
		final Object token = new Object();
		dfa.createStates(S1, S2)
			.setStartState(S1)
			.addAcceptingStates(S2)
			.createTransition(S1, S2, token);
		assertRejects();
		assertAccepts(token);
		assertRejects(token, token);
		assertRejects(token, token, token);
		assertRejects(token, token, token, token);
	}

	@Test
	public void testAutomatonAcceptsInMultipleStates() throws Exception {
		final Object obj = new Object();
		dfa.createStates(S1, S2)
			.setStartState(S1)
			.addAcceptingStates(S1, S2)
			.createTransition(S1, S2, obj)
			.createTransition(S2, S1, obj);
		assertAccepts();
		assertAccepts(obj);
		assertAccepts(obj, obj);
	}

	@Test
	public void testAutomatonAcceptEvenZeros() throws Exception {
		dfa.createStates(S1, S2)
			.setStartState(S1)
			.addAcceptingStates(S1)
			.createTransition(S1, S1, 1)
			.createTransition(S1, S2, 0)
			.createTransition(S2, S2, 1)
			.createTransition(S2, S1, 0);
		assertAccepts();
		assertAccepts(1);
		assertRejects(0);
		assertAccepts(0, 0);
		assertAccepts(0, 1, 1, 0, 1);
		assertRejects(0, 1, 0, 1, 0);
	}

	private void assertAccepts(Object... tokens) throws NoStartStateException {
		Assert.assertTrue(dfa.accepts(Arrays.asList(tokens)));
	}

	private void assertRejects(Object... tokens) throws NoStartStateException {
		Assert.assertFalse(dfa.accepts(Arrays.asList(tokens)));
	}

	private void assertAmountOfStates(int amountOfStates) {
		Assert.assertEquals(amountOfStates, dfa.getStates().size());
	}

	private void assertAmountOfAcceptingStates(int amountOfAcceptingStates) {
		Assert.assertEquals(amountOfAcceptingStates, dfa.getAcceptingStates()
														.size());
	}

	private void assertAmountOfTransitions(int amountOfTransitions) {
		Assert.assertEquals(amountOfTransitions, dfa.getTransitions().size());
	}

}
