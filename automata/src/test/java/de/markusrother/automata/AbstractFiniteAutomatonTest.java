package de.markusrother.automata;

import static de.markusrother.automata.AutomatonStateMatcher.isState;
import static de.markusrother.automata.AutomatonTransitionMatcher.isTransition;
import static de.markusrother.automata.EitherOrAccepting.ACCEPTING;
import static de.markusrother.automata.EitherOrAccepting.NOT_ACCEPTING;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import de.markusrother.automata.exceptions.DuplicateStateException;
import de.markusrother.automata.exceptions.DuplicateTransitionException;
import de.markusrother.automata.exceptions.InvalidOriginException;
import de.markusrother.automata.exceptions.NoAcceptingStatesException;
import de.markusrother.automata.exceptions.NoStartStateException;
import de.markusrother.automata.exceptions.NoSuchStateException;
import de.markusrother.automata.exceptions.NotInAlphabetException;

/**
 * TODO - What about invalid (disconnected states) automata? Should they fail upon calling accept?
 *
 * TODO - What about unreachable states?
 */
public abstract class AbstractFiniteAutomatonTest {

	protected static final String S1 = "S1";
	protected static final String S2 = "S2";
	protected static final String S3 = "S3";
	protected static final String S4 = "S4";
	protected static final String T1 = "T1";
	protected static final String T2 = "T2";
	protected static final Object TOKEN = new Object();
	protected static final AutomatonState NO_STATE = null;
	protected static final Object NO_TOKEN = null;

	protected AbstractFiniteAutomaton<Object> automaton;

	abstract <T> MutableFiniteAutomaton<T> createAutomaton();

	@Test
	public void testNewAutomatonHasNoStates() throws Exception {
		Assert.assertThat(automaton.getStates(), empty());
	}

	@Test(expected = NoStartStateException.class)
	public void testAcceptanceRequiresStartState() throws Exception {
		automaton.accepts(new ArrayList<Object>());
	}

	@Test(expected = NoAcceptingStatesException.class)
	public void testAcceptanceRequiresAcceptingStates() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.setStartState(S1);
		automaton.accepts(new ArrayList<Object>());
	}

	@Test
	public void testNewAutomatonHasNoAcceptingStates() throws Exception {
		Assert.assertThat(automaton.getAcceptingStates(), empty());
	}

	@Test
	public void testNewAutomatonHasNoTransitions() throws Exception {
		Assert.assertThat(automaton.getTransitions(), empty());
	}

	@Test
	public void testNewAutomatonHasNoAlphabet() throws Exception {
		Assert.assertThat(automaton.getAlphabet(), empty());
	}

	@Test
	public void testAutomatonStartsInAcceptingState() throws Exception {
		automaton.createState(S1, ACCEPTING)
					.setStartState(S1);
		assertAccepts();
	}

	@Test
	public void testCreateState() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING);
		Assert.assertThat(automaton.getStates(), contains(isState(S1)));
	}

	@Test(expected = DuplicateStateException.class)
	public void testCreateDuplicateState() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S1, NOT_ACCEPTING);
	}

	@Test
	public void testCreateStartState() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.setStartState(S1);
		Assert.assertThat(automaton.getStartState(), isState(S1));
	}

	@Test(expected = NoSuchStateException.class)
	public void testCreateInexistentStartState() throws Exception {
		automaton.setStartState(S1);
	}

	@Test
	public void testCreateAcceptingState() throws Exception {
		automaton.createState(S1, ACCEPTING);
		Assert.assertThat(automaton.getAcceptingStates(), contains(isState(S1)));
	}

	@Test
	public void testCreateTransition() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.createTransition(S1, S2, TOKEN);
		Assert.assertThat(automaton.getTransitions(), contains(isTransition(S1, S2, TOKEN)));
	}

	@Test
	public void testCreateTransitionExtendsAlphabet() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.createTransition(S1, S2, TOKEN);
		Assert.assertThat(automaton.getAlphabet(), contains(is(TOKEN)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetTransitionThrowsExceptionIfTransitionIsNull() {
		automaton.getTransition(NO_STATE, Mockito.mock(Object.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetTransitionThrowsExceptionIfTokenIsNull() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.getTransition(automaton.getState(S1), NO_TOKEN);
	}

	@Test(expected = NotInAlphabetException.class)
	public void testGetTransitionThrowsExceptionIfTokenDoesNotBelongToAlphabet() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.getTransition(automaton.getState(S1), new Object());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetSuccessorsThrowsExceptionIfStateIsNull() {
		automaton.getSuccessors(NO_STATE, Mockito.mock(Object.class));
	}

	@Test(expected = InvalidOriginException.class)
	public void testGetSuccessorsThrowsExceptionIfStateIsInvalid() {
		automaton.getSuccessors(Mockito.mock(AutomatonState.class), NO_TOKEN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetSuccessorsThrowsExceptionIfTokenIsNull() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.getSuccessors(automaton.getState(S1), NO_TOKEN);
	}

	@Test(expected = NotInAlphabetException.class)
	public void testGetSuccessorsThrowsExceptionIfTokenDoesNotBelongToAlphabet() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.getSuccessors(automaton.getState(S1), new Object());
	}

	@Test
	public void testAlphabetIsSet() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S2, TOKEN);
		Assert.assertThat(automaton.getAlphabet(), contains(is(TOKEN)));
	}

	@Test(expected = DuplicateTransitionException.class)
	public void testCreateDuplicateTransition() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S1, S2, TOKEN);
	}

	@Test(expected = NoSuchStateException.class)
	public void testCreateTransitionWithInexistentStates() throws Exception {
		automaton.createTransition(S1, S2, TOKEN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateTransitionWithNullToken() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.createTransition(S1, S2, null);
	}

	@Test
	public void testAutomatonRejects() throws Exception {
		// TODO - All states should be connected!
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1);
		assertRejects();
	}

	@Test(expected = NotInAlphabetException.class)
	public void testAutomatonThrowsExceptionForInvalidToken() throws Exception {
		// TODO - All states should be connected!
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1)
					.accepts(Arrays.asList(TOKEN));
	}

	@Test
	public void testAutomatonAcceptSingleObject() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN);
		assertRejects();
		assertAccepts(TOKEN);
	}

	@Test
	public void testAutomatonLeavesAcceptingState() throws Exception {
		final Object token = new Object();
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1)
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
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1)
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
		automaton.createState(S1, ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S2, obj)
					.createTransition(S2, S1, obj);
		assertAccepts();
		assertAccepts(obj);
		assertAccepts(obj, obj);
	}

	@Test
	public void testAutomatonAcceptEvenZeros() throws Exception {
		automaton.createState(S1, ACCEPTING)
					.createState(S2, NOT_ACCEPTING)
					.setStartState(S1)
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
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING);
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertThat(copy.getStates(), contains(isState(S1), isState(S2)));
	}

	@Test
	public void testCopyStartState() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.setStartState(S1);
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertThat(copy.getStartState(), isState(S1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCopyAcceptingStates() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.createState(S3, ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S3, TOKEN);
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertThat(copy.getAcceptingStates(), contains(isState(S2), isState(S3)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCopyTransitions() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S2, TOKEN);
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertThat(copy.getTransitions(), contains(isTransition(S1, S2, TOKEN), isTransition(S2, S2, TOKEN)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCopyAlphabet() throws Exception {
		final MutableFiniteAutomaton<Integer> automaton = createAutomaton();
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S2, 0)
					.createTransition(S2, S2, 1);
		final FiniteAutomaton<Integer> copy = automaton.copy();
		Assert.assertThat(copy.getAlphabet(), contains(is(0), is(1)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCopyCircularAutomaton() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S1, TOKEN);
		final FiniteAutomaton<Object> copy = automaton.copy();
		Assert.assertThat(copy.getTransitions(), contains(isTransition(S1, S2, TOKEN), isTransition(S2, S1, TOKEN)));
	}

	@Test
	public void testCompareCircularAutomata() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S1, TOKEN);
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(T1, NOT_ACCEPTING)
				.createState(T2, ACCEPTING)
				.setStartState(T1)
				.createTransition(T1, T2, TOKEN)
				.createTransition(T2, T1, TOKEN);
		Assert.assertEquals(automaton, other);
	}

	@Test
	public void testAllAutomataWithoutStartStatesAreEqual() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createTransition(S1, S1, "foobar");
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(T1, NOT_ACCEPTING)
				.createTransition(T1, T1, 23);
		Assert.assertEquals(automaton, other);
	}

	@Test
	public void testCompareToAutomatonWithoutStartState() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S1, TOKEN);
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(S1, NOT_ACCEPTING)
				.createTransition(S1, S1, TOKEN);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testCompareAutomatonWithoutStartState() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createTransition(S1, S1, TOKEN);
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(S1, NOT_ACCEPTING)
				.setStartState(S1)
				.createTransition(S1, S1, TOKEN);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testAllButAcceptingStatesEqual() throws Exception {
		automaton.createState(S1, ACCEPTING)
					.createState(S2, NOT_ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN);
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(T1, NOT_ACCEPTING)
				.createState(T2, ACCEPTING)
				.setStartState(T1)
				.createTransition(T1, T2, TOKEN);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testStructuralInequalTokenTypes() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S1, "foobar");
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(T1, NOT_ACCEPTING)
				.setStartState(T1)
				.createTransition(T1, T1, 23);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testCompareWithAutomatonWithMissingTransitions() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S2, TOKEN)
					.createTransition(S2, S2, TOKEN);
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(S1, NOT_ACCEPTING)
				.createState(S2, ACCEPTING)
				.setStartState(S1)
				.createTransition(S1, S2, TOKEN);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testStructuralInequality() throws Exception {
		automaton.createState(S1, ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S1, TOKEN);
		// This automaton is logically(!) equivalent:
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(T1, ACCEPTING)
				.createState(T2, ACCEPTING)
				.setStartState(T1)
				.createTransition(T1, T2, TOKEN)
				.createTransition(T2, T2, TOKEN);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testStructuralEquality() throws Exception {
		automaton.createState(S1, ACCEPTING)
					.createState(S2, NOT_ACCEPTING)
					.setStartState(S1)
					.createTransition(S1, S1, 1)
					.createTransition(S1, S2, 0)
					.createTransition(S2, S2, 1)
					.createTransition(S2, S1, 0);
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(T1, ACCEPTING)
				.createState(T2, NOT_ACCEPTING)
				.setStartState(T1)
				.createTransition(T1, T1, 1)
				.createTransition(T1, T2, 0)
				.createTransition(T2, T2, 1)
				.createTransition(T2, T1, 0);
		Assert.assertEquals(automaton, other);
	}

	@Ignore("Structural equality comparison cannot be exhaustive, because we cannot compare unreachable/disconnected components.")
	@Test
	public
			void testStructuralEqualityWithUnreachableStates() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, NOT_ACCEPTING);
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(T1, NOT_ACCEPTING)
				.createState(
						T2, NOT_ACCEPTING);
		Assert.assertEquals(automaton, other);
	}

	@Ignore("Structural equality comparison cannot be exhaustive, because we cannot compare unreachable/disconnected components.")
	@Test
	public
			void testStructuralInequalityWithUnreachableStates() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING);
		final MutableFiniteAutomaton<Object> other = createAutomaton();
		other.createState(T1, NOT_ACCEPTING)
				.createState(T2, NOT_ACCEPTING);
		Assert.assertNotEquals(automaton, other);
	}

	@Test
	public void testTokensNotCopied() throws Exception {
		automaton.createState(S1, NOT_ACCEPTING)
					.createState(S2, ACCEPTING)
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
