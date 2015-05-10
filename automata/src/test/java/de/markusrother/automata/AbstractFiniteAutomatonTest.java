package de.markusrother.automata;

import static de.markusrother.automata.AutomatonStateMatcher.isState;
import static de.markusrother.automata.AutomatonTransitionMatcher.isTransition;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import de.markusrother.automata.exceptions.DuplicateStateException;
import de.markusrother.automata.exceptions.DuplicateTransitionException;
import de.markusrother.automata.exceptions.NoAcceptingStatesException;
import de.markusrother.automata.exceptions.NoStartStateException;
import de.markusrother.automata.exceptions.NoSuchStateException;

public abstract class AbstractFiniteAutomatonTest {

	protected static final String S1 = "S1";
	protected static final String S2 = "S2";
	protected static final String S3 = "S3";
	protected static final String S4 = "S4";
	protected static final String T1 = "T1";
	protected static final String T2 = "T2";
	protected static final Object TOKEN = new Object();

	protected FiniteAutomaton<Object> automaton;

	abstract <T> FiniteAutomaton<T> createAutomaton();

	@Test
	public void testNewAutomatonHasNoStates() throws Exception {
		Assert.assertThat(automaton.getStates(), emptyIterable());
	}

	@Test(expected = NoStartStateException.class)
	public void testAcceptanceRequiresStartState() throws Exception {
		automaton.accepts(new ArrayList<Object>());
	}

	@Test(expected = NoAcceptingStatesException.class)
	public void testAcceptanceRequiresAcceptingStates() throws Exception {
		automaton.createStates(S1)
					.setStartState(S1);
		automaton.accepts(new ArrayList<Object>());
	}

	@Test
	public void testNewAutomatonHasNoAcceptingStates() throws Exception {
		Assert.assertThat(automaton.getAcceptingStates(), emptyIterable());
	}

	@Test
	public void testNewAutomatonHasNoTransitions() throws Exception {
		Assert.assertThat(automaton.getTransitions(), emptyIterable());
	}

	@Test
	public void testNewAutomatonHasNoAlphabet() throws Exception {
		Assert.assertThat(automaton.getAlphabet(), emptyIterable());
	}

	@Test
	public void testAutomatonStartsInAcceptingState() throws Exception {
		automaton.createStates(S1)
					.setStartState(S1)
					.addAcceptingStates(S1);
		assertAccepts();
	}

	@Test
	public void testCreateState() throws Exception {
		automaton.createStates(S1);
		Assert.assertThat(automaton.getStates(), contains(isState(S1)));
	}

	@Test(expected = DuplicateStateException.class)
	public void testCreateDuplicateState() throws Exception {
		automaton.createStates(S1)
					.createStates(S1);
	}

	@Test
	public void testCreateStartState() throws Exception {
		automaton.createStates(S1)
					.setStartState(S1);
		Assert.assertThat(automaton.getStartState(), isState(S1));
	}

	@Test(expected = NoSuchStateException.class)
	public void testCreateInexistentStartState() throws Exception {
		automaton.setStartState(S1);
	}

	@Test
	public void testCreateAcceptingState() throws Exception {
		automaton.createStates(S1)
					.addAcceptingStates(S1);
		Assert.assertThat(automaton.getAcceptingStates(), contains(isState(S1)));
	}

	@Test(expected = NoSuchStateException.class)
	public void testCreateInexistentAcceptingState() throws Exception {
		automaton.addAcceptingStates(S1);
	}

	@Test
	public void testCreateTransition() throws Exception {
		automaton.createStates(S1, S2)
					.createTransition(S1, S2, TOKEN);
		Assert.assertThat(automaton.getTransitions(), contains(isTransition(S1, S2, TOKEN)));
	}

	@Test
	public void testCreateTransitionExtendsAlphabet() throws Exception {
		automaton.createStates(S1, S2)
					.createTransition(S1, S2, TOKEN);
		Assert.assertThat(automaton.getAlphabet(), contains(is(TOKEN)));
	}

	@Test
	public void testAlphabetIsSet() throws Exception {
		automaton.createStates(S1, S2)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S2, TOKEN);
		Assert.assertThat(automaton.getAlphabet(), contains(is(TOKEN)));
	}

	@Test(expected = DuplicateTransitionException.class)
	public void testCreateDuplicateTransition() throws Exception {
		final String token = "token";
		automaton.createStates(S1, S2)
					.createTransition(S1, S2, token)
					.createTransition(S1, S2, token);
	}

	@Test(expected = NoSuchStateException.class)
	public void testCreateTransitionWithInexistentStates() throws Exception {
		automaton.createTransition(S1, S2, TOKEN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateTransitionWithNullToken() throws Exception {
		automaton.createStates(S1, S2)
					.createTransition(S1, S2, null);
	}

	@Test
	public void testAutomatonRejects() throws Exception {
		// TODO - All states should be connected!
		automaton.createStates(S1, S2)
					.setStartState(S1)
					.addAcceptingStates(S2);
		final Object token = new Object();
		assertRejects();
		assertRejects(token);
		assertRejects(token, token);
	}

	@Test
	public void testAutomatonAcceptSingleObject() throws Exception {
		final Object obj = new Object();
		automaton.createStates(S1, S2)
					.setStartState(S1)
					.addAcceptingStates(S2)
					.createTransition(S1, S2, obj);
		assertRejects();
		assertAccepts(obj);
	}

	@Test
	public void testAutomatonLeavesAcceptingState() throws Exception {
		final Object token = new Object();
		automaton.createStates(S1, S2)
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
		automaton.createStates(S1, S2)
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
		automaton.createStates(S1, S2)
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
		automaton.createStates(S1, S2)
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

	@Test
	public void testCopyNotIdentical() throws Exception {
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertTrue(copy != automaton);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCopyStates() throws Exception {
		automaton.createStates(S1, S2);
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertThat(copy.getStates(), contains(isState(S1), isState(S2)));
	}

	@Test
	public void testCopyStartState() throws Exception {
		automaton.createStates(S1)
					.setStartState(S1);
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertThat(copy.getStartState(), isState(S1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCopyAcceptingStates() throws Exception {
		automaton.createStates(S1, S2, S3)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S3, TOKEN)
					.addAcceptingStates(S2, S3);
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertThat(copy.getAcceptingStates(), contains(isState(S2), isState(S3)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCopyTransitions() throws Exception {
		automaton.createStates(S1, S2)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S2, TOKEN);
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertThat(copy.getTransitions(), contains(isTransition(S1, S2, TOKEN), isTransition(S2, S2, TOKEN)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCopyAlphabet() throws Exception {
		final FiniteAutomaton<Integer> automaton = createAutomaton();
		automaton.createStates(S1, S2)
					.setStartState(S1)
					.createTransition(S1, S2, 0)
					.createTransition(S2, S2, 1);
		final FiniteAutomaton<Integer> copy = automaton.copy();
		Assert.assertThat(copy.getAlphabet(), contains(is(0), is(1)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCopyCircularAutomaton() throws Exception {
		automaton.createStates(S1, S2)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S1, TOKEN);
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertThat(copy.getTransitions(), contains(isTransition(S1, S2, TOKEN), isTransition(S2, S1, TOKEN)));
	}

	@Test
	public void testCompareCircularAutomata() throws Exception {
		automaton.createStates(S1, S2)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S1, TOKEN);
		final FiniteAutomaton<Object> other = createAutomaton();
		other.createStates(T1, T2)
				.setStartState(T1)
				.createTransition(T1, T2, TOKEN)
				.createTransition(T2, T1, TOKEN);
		Assert.assertEquals(automaton, other);
	}

	@Test
	public void testAllAutomataWithoutStartStatesAreEqual() throws Exception {
		automaton.createStates(S1)
					.createTransition(S1, S1, "foobar");
		final FiniteAutomaton<Object> other = createAutomaton();
		other.createStates(T1)
				.createTransition(T1, T1, 23);
		Assert.assertEquals(automaton, other);
	}

	@Test
	public void testCompareToAutomatonWithoutStartState() throws Exception {
		automaton.createStates(S1)
					.setStartState(S1)
					.createTransition(S1, S1, TOKEN);
		final FiniteAutomaton<Object> other = createAutomaton();
		other.createStates(S1)
				.createTransition(S1, S1, TOKEN);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testCompareAutomatonWithoutStartState() throws Exception {
		automaton.createStates(S1)
					.createTransition(S1, S1, TOKEN);
		final FiniteAutomaton<Object> other = createAutomaton();
		other.createStates(S1)
				.setStartState(S1)
				.createTransition(S1, S1, TOKEN);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testAllButAcceptingStatesEqual() throws Exception {
		automaton.createStates(S1, S2)
					.setStartState(S1)
					.addAcceptingStates(S1)
					.createTransition(S1, S2, TOKEN);
		final FiniteAutomaton<Object> other = createAutomaton();
		other.createStates(T1, T2)
				.setStartState(T1)
				.addAcceptingStates(T2)
				.createTransition(T1, T2, TOKEN);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testStructuralInequalTokenTypes() throws Exception {
		automaton.createStates(S1)
					.setStartState(S1)
					.createTransition(S1, S1, "foobar");
		final FiniteAutomaton<Object> other = createAutomaton();
		other.createStates(T1)
				.setStartState(T1)
				.createTransition(T1, T1, 23);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testCompareWithAutomatonWithMissingTransitions() throws Exception {
		automaton.createStates(S1, S2)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S2, TOKEN);
		final FiniteAutomaton<Object> other = createAutomaton();
		other.createStates(S1, S2)
				.setStartState(S1)
				.createTransition(S1, S2, TOKEN);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testStructuralInequality() throws Exception {
		automaton.createStates(S1)
					.setStartState(S1)
					.addAcceptingStates(S1)
					.createTransition(S1, S1, TOKEN);
		// This automaton is logically(!) equivalent:
		final FiniteAutomaton<Object> other = createAutomaton();
		other.createStates(T1, T2)
				.setStartState(T1)
				.addAcceptingStates(T1, T2)
				.createTransition(T1, T2, TOKEN)
				.createTransition(T2, T2, TOKEN);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testStructuralEquality() throws Exception {
		automaton.createStates(S1, S2)
					.setStartState(S1)
					.addAcceptingStates(S1)
					.createTransition(S1, S1, 1)
					.createTransition(S1, S2, 0)
					.createTransition(S2, S2, 1)
					.createTransition(S2, S1, 0);
		final FiniteAutomaton<Object> other = createAutomaton();
		other.createStates(T1, T2)
				.setStartState(T1)
				.addAcceptingStates(T1)
				.createTransition(T1, T1, 1)
				.createTransition(T1, T2, 0)
				.createTransition(T2, T2, 1)
				.createTransition(T2, T1, 0);
		Assert.assertEquals(automaton, other);
	}

	@Test
	public void testTokensNotCopied() throws Exception {
		automaton.createStates(S1, S2)
					.createTransition(S1, S2, TOKEN);
		final FiniteAutomaton<Object> copy = automaton.copy();
		final Collection<Object> alphabet = copy.getAlphabet();
		Assert.assertThat(alphabet, contains(TOKEN));
		Assert.assertTrue(alphabet.iterator()
									.next() == TOKEN);
	}

	protected void assertAccepts(Object... tokens) throws NoStartStateException, NoAcceptingStatesException {
		Assert.assertTrue(automaton.accepts(Arrays.asList(tokens)));
	}

	protected void assertRejects(Object... tokens) throws NoStartStateException, NoAcceptingStatesException {
		Assert.assertFalse(automaton.accepts(Arrays.asList(tokens)));
	}

}
