package de.markusrother.automata.io;

import static de.markusrother.automata.EitherOrAccepting.ACCEPTING;
import static de.markusrother.automata.EitherOrAccepting.NOT_ACCEPTING;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Mockito;

import de.markusrother.automata.DeterministicFiniteAutomaton;
import de.markusrother.automata.FiniteAutomaton;
import de.markusrother.automata.MutableFiniteAutomaton;
import de.markusrother.automata.NonDeterministicFiniteAutomaton;
import de.markusrother.automata.exceptions.NoAcceptingStatesException;
import de.markusrother.automata.exceptions.NoStartStateException;

public class FiniteAutomatonGraphvizBuilderTest {

	private static final String S1 = "S1";
	private static final String S2 = "S2";
	private static final String S3 = "S3";
	private static final String TOKEN = "foobar";

	private static Matcher<String> matchesPattern(final String regex, final int flags) {
		final Pattern pattern = Pattern.compile(regex, flags);
		return new BaseMatcher<String>() {

			@Override
			public boolean matches(Object item) {
				final String string = (String) item;
				return pattern.matcher(string)
								.matches();
			}

			@Override
			public void describeTo(Description description) {
				description.appendValue(regex);
			}
		};
	}

	@Test(expected = NoStartStateException.class)
	public void testAutomatonNeedsStartState() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		final Writer out = Mockito.mock(Writer.class);
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
	}

	@Test(expected = NoAcceptingStatesException.class)
	public void testAutomatonNeedsAcceptingStates() throws Exception {
		final MutableFiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createState(S1, NOT_ACCEPTING)
			.setStartState(S1);
		final Writer out = Mockito.mock(Writer.class);
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
	}

	@Test
	public void testWriteDotFlushesWriter() throws Exception {
		final MutableFiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createState(S1, ACCEPTING)
			.setStartState(S1);
		final Writer out = Mockito.mock(Writer.class);
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
		Mockito.verify(out, Mockito.times(1))
				.flush();
	}

	@Test
	public void testWriteDotClosesWriter() throws Exception {
		final MutableFiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createState(S1, ACCEPTING)
			.setStartState(S1);
		final Writer out = Mockito.mock(Writer.class);
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
		Mockito.verify(out, Mockito.times(1))
				.close();
	}

	@Test
	public void testWriteHeader() throws Exception {
		final MutableFiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createState(S1, ACCEPTING)
			.setStartState(S1);
		final Writer out = new StringWriter();
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern("^digraph \\w* \\{\\n\\trankdir=LR;\\n\\tsize=\"\\d+(\\.\\d+)?\".*", Pattern.DOTALL));
	}

	@Test
	public void testWriteStates() throws Exception {
		final MutableFiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createState(S1, NOT_ACCEPTING)
			.createState(S2, NOT_ACCEPTING)
			.createState(S3, ACCEPTING)
			.setStartState(S1);
		final Writer out = new StringWriter();
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern(".+node \\[shape=circle, style=solid\\]; " + S1 + " " + S2 + ";\\n.*", Pattern.DOTALL));
	}

	@Test
	public void testWriteVirtualStartState() throws Exception {
		final MutableFiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createState(S1, ACCEPTING)
			.setStartState(S1);
		final Writer out = new StringWriter();
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern(".+node \\[shape=point, style=invis\\]; virtual_start_state;\\n.*", Pattern.DOTALL));
		assertThat(out.toString(), matchesPattern(".+virtual_start_state -> " + S1 + ";\\n.*", Pattern.DOTALL));
	}

	@Test
	public void testWriteAcceptingStates() throws Exception {
		final MutableFiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createState(S1, ACCEPTING)
			.createState(S2, ACCEPTING)
			.setStartState(S1);
		final Writer out = new StringWriter();
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern(".+node \\[shape=doublecircle, style=solid\\]; " + S1 + " " + S2 + ";\\n.*",
						Pattern.DOTALL));
	}

	@Test
	public void testWriteTransitions() throws Exception {
		final MutableFiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createState(S1, NOT_ACCEPTING)
			.createState(S2, ACCEPTING)
			.setStartState(S1)
			.createTransition(S1, S2, TOKEN)
			.createTransition(S1, S1, TOKEN);
		final Writer out = new StringWriter();
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern(".*" + S1 + " -> " + S2 + " \\[label=\"" + TOKEN + "\"\\];.*", Pattern.DOTALL));
	}

	@Test
	public void testWriteEmptyTransitions() throws Exception {
		final NonDeterministicFiniteAutomaton<String> dfa = new NonDeterministicFiniteAutomaton<String>();
		dfa.createState(S1, NOT_ACCEPTING)
			.createState(S2, ACCEPTING)
			.setStartState(S1)
			.createEmptyTransition(S1, S2);
		final Writer out = new StringWriter();
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern(".*" + S1 + " -> " + S2 + " \\[label=\"€\"\\];.*", Pattern.DOTALL));
	}

	@Test
	public void testShow() throws Exception {
		final MutableFiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createState(S1, NOT_ACCEPTING)
			.createState(S2, ACCEPTING)
			.setStartState(S1)
			.createTransition(S1, S2, TOKEN)
			.createTransition(S1, S1, TOKEN);
		final Writer out = new StringWriter();
		FiniteAutomatonGraphvizBuilder.write(dfa, out);
		Runtime.getRuntime()
				.exec("dot -Tpng < \"" + out.toString() + "\" > /tmp/foobar.png");
		final Process process = Runtime.getRuntime()
				.exec("eog /tmp/foobar.png");
		process.destroy();
	}

}
