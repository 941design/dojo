package de.markusrother.automata.io;

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
import de.markusrother.automata.exceptions.NoAcceptingStatesException;
import de.markusrother.automata.exceptions.NoStartStateException;

public class DotFileBuilderTest {

	private static final String S1 = "S1";
	private static final String S2 = "S2";
	private static final String S3 = "S3";
	private static final String TOKEN = "foobar";

	private static Matcher<String> matchesPattern(String regex, int flags) {
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
				// TODO Auto-generated method stub
				throw new RuntimeException("TODO");
			}
		};
	}

	@Test(expected = NoStartStateException.class)
	public void testAutomatonNeedsStartState() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		final Writer out = Mockito.mock(Writer.class);
		DotFileBuilder.write(dfa, out);
	}

	@Test(expected = NoAcceptingStatesException.class)
	public void testAutomatonNeedsAcceptingStates() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createStates(S1)
			.setStartState(S1);
		final Writer out = Mockito.mock(Writer.class);
		DotFileBuilder.write(dfa, out);
	}

	@Test
	public void testWriteDotFlushesWriter() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createStates(S1)
			.setStartState(S1)
			.addAcceptingStates(S1);
		final Writer out = Mockito.mock(Writer.class);
		DotFileBuilder.write(dfa, out);
		Mockito.verify(out, Mockito.times(1))
				.flush();
	}

	@Test
	public void testWriteDotClosesWriter() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createStates(S1)
			.setStartState(S1)
			.addAcceptingStates(S1);
		final Writer out = Mockito.mock(Writer.class);
		DotFileBuilder.write(dfa, out);
		Mockito.verify(out, Mockito.times(1))
				.close();
	}

	@Test
	public void testWriteHeader() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createStates(S1)
			.setStartState(S1)
			.addAcceptingStates(S1);
		final Writer out = new StringWriter();
		DotFileBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern("^digraph \\w* \\{\\n\\trankdir=LR;\\n\\tsize=\"\\d+(\\.\\d+)?\".*", Pattern.DOTALL));
	}

	@Test
	public void testWriteStates() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createStates(S1, S2, S3)
			.setStartState(S1)
			.addAcceptingStates(S3);
		final Writer out = new StringWriter();
		DotFileBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern(".+node \\[shape=circle, style=solid\\]; " + S1 + " " + S2 + ";\\n.*", Pattern.DOTALL));
	}

	@Test
	public void testWriteVirtualStartState() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createStates(S1)
			.setStartState(S1)
			.addAcceptingStates(S1);
		final Writer out = new StringWriter();
		DotFileBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern(".+node \\[shape=point, style=invis\\]; virtual_start_state;\\n.*", Pattern.DOTALL));
		assertThat(out.toString(), matchesPattern(".+virtual_start_state -> " + S1 + ";\\n.*", Pattern.DOTALL));
	}

	@Test
	public void testWriteAcceptingStates() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createStates(S1, S2)
			.setStartState(S1)
			.addAcceptingStates(S1, S2);
		final Writer out = new StringWriter();
		DotFileBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern(".+node \\[shape=doublecircle, style=solid\\]; " + S1 + " " + S2 + ";\\n.*",
						Pattern.DOTALL));
	}

	@Test
	public void testWriteTransitions() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createStates(S1, S2)
			.setStartState(S1)
			.addAcceptingStates(S2)
			.createTransition(S1, S2, TOKEN)
			.createTransition(S1, S1, TOKEN);
		final Writer out = new StringWriter();
		DotFileBuilder.write(dfa, out);
		assertThat(out.toString(),
				matchesPattern(".*" + S1 + " -> " + S2 + " \\[label=\"" + TOKEN + "\"\\];.*", Pattern.DOTALL));
	}

	@Test
	public void testShow() throws Exception {
		final FiniteAutomaton<String> dfa = new DeterministicFiniteAutomaton<String>();
		dfa.createStates(S1, S2)
			.setStartState(S1)
			.addAcceptingStates(S2)
			.createTransition(S1, S2, TOKEN)
			.createTransition(S1, S1, TOKEN);
		final Writer out = new StringWriter();
		DotFileBuilder.write(dfa, out);
		Runtime.getRuntime()
				.exec("dot -Tpng < \"" + out.toString() + "\" > /tmp/foobar.png");
		final Process process = Runtime.getRuntime()
				.exec("eog /tmp/foobar.png");
		process.destroy();
	}

}
