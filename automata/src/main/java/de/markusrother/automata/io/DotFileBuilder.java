package de.markusrother.automata.io;

import java.io.IOException;
import java.io.Writer;

import de.markusrother.automata.AutomatonState;
import de.markusrother.automata.AutomatonTransition;
import de.markusrother.automata.FiniteAutomaton;
import de.markusrother.automata.exceptions.NoAcceptingStatesException;
import de.markusrother.automata.exceptions.NoStartStateException;

public class DotFileBuilder<T> {

	private static final String NL = "\n";
	private static final String TAB = "\t";
	private static final String SPACE = " ";
	private static final String VIRTUAL_START_STATE_LABEL = "virtual_start_state";

	private final Writer out;
	private final FiniteAutomaton<T> automaton;

	public static <T> void write(FiniteAutomaton<T> dfa, Writer out) throws IOException, NoStartStateException,
			NoAcceptingStatesException {
		if (!dfa.hasStartState()) {
			throw new NoStartStateException();
		}
		if (!dfa.hasAcceptingStates()) {
			throw new NoAcceptingStatesException();
		}
		final DotFileBuilder<T> dotFileBuilder = new DotFileBuilder<T>(dfa, out);
		dotFileBuilder.writeAutomaton();
	}

	public DotFileBuilder(FiniteAutomaton<T> automaton, Writer out) {
		this.automaton = automaton;
		this.out = out;
	}

	private void writeAutomaton() throws IOException {
		this.writeHeader()
			.writeVirtualState()
			.writeAcceptingStates()
			.writeRemainingStates()
			.writeTransitions()
			.writeFooter();
		out.flush();
		out.close();
	}

	private DotFileBuilder<T> write(String format, Object... args) throws IOException {
		out.write(String.format(format, args));
		return this;
	}

	private DotFileBuilder<T> writeLine(String format, Object... args) throws IOException {
		return this.write(format + NL, args);
	}

	private DotFileBuilder<T> writeHeader() throws IOException {
		return this.writeLine("digraph DFA {")
					.writeLine(TAB + "rankdir=LR;")
					.writeLine(TAB + "size=\"%s\"", 8.5);
	}

	private DotFileBuilder<T> writeFooter() throws IOException {
		return this.writeLine("}");
	}

	private DotFileBuilder<T> writeVirtualState() throws IOException {
		return this.writeLine(TAB + "node [shape=point, style=invis]; %s;", VIRTUAL_START_STATE_LABEL);
	}

	private DotFileBuilder<T> writeAcceptingStates() throws IOException {
		// TODO - create Node class with attributes
		this.write(TAB + "node [shape=doublecircle, style=solid];");
		for (AutomatonState state : automaton.getAcceptingStates()) {
			this.write(SPACE + state.getLabel());
		}
		return this.writeLine(";");
	}

	private DotFileBuilder<T> writeRemainingStates() throws IOException {
		// TODO - create Node class with attributes
		this.write(TAB + "node [shape=circle, style=solid];");
		for (AutomatonState state : automaton.getStates()) {
			if (!state.isAccepting()) {
				this.write(SPACE + state.getLabel());
			}
		}
		return this.writeLine(";");
	}

	private DotFileBuilder<T> writeTransitions() throws IOException {
		// TODO - create Edge class with attributes
		this.writeLine(TAB + "%s -> %s;", VIRTUAL_START_STATE_LABEL, automaton.getStartState()
								.getLabel());
		for (AutomatonTransition<T> transition : automaton.getTransitions()) {
			this.writeLine(TAB + "%s -> %s [label=\"%s\"];", transition.getOriginLabel(),
					transition.getTargetLabel(), transition.getToken());
		}
		return this;
	}

}
