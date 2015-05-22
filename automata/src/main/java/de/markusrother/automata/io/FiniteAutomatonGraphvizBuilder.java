package de.markusrother.automata.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.markusrother.automata.AutomatonTransition;
import de.markusrother.automata.FiniteAutomaton;
import de.markusrother.automata.exceptions.NoAcceptingStatesException;
import de.markusrother.automata.exceptions.NoStartStateException;

/**
 * @param <T> - the generic token/alphabet type.
 */
public class FiniteAutomatonGraphvizBuilder<T> extends GraphvizBuilder {

	private static final String VIRTUAL_START_STATE_LABEL = "virtual_start_state";
	private static final String NO_LABEL = null;
	private static final String EMPTY_TRANSITION_SYMBOL = "€";
	private static final NodeStyle virtualStartStateStyle = new NodeStyle(Shape.POINT, Style.INVISIBLE);
	private static final NodeStyle acceptingStateStyle = new NodeStyle(Shape.DOUBLECIRCLE, Style.SOLID);
	private static final NodeStyle nonAcceptingStateStyle = new NodeStyle(Shape.CIRCLE, Style.SOLID);
	private static final Function<AutomatonTransition<?>, Edge> edgeMapper = transition -> {
		final String originLabel = transition.getOriginLabel();
		final String targetLabel = transition.getTargetLabel();
		final String tokenString = transition.isEmpty() ? EMPTY_TRANSITION_SYMBOL
				: String.valueOf(transition.getToken());
		return new Edge(originLabel, targetLabel, tokenString);
	};

	public static <T> void write(FiniteAutomaton<T> automaton, Writer out) throws IOException, NoStartStateException,
			NoAcceptingStatesException {
		if (!automaton.hasStartState()) {
			throw new NoStartStateException();
		}
		if (!automaton.hasAcceptingStates()) {
			throw new NoAcceptingStatesException();
		}
		final FiniteAutomatonGraphvizBuilder<T> builder = new FiniteAutomatonGraphvizBuilder<T>(automaton, out);
		builder.writeAutomaton();
	}

	private final FiniteAutomaton<T> automaton;

	public FiniteAutomatonGraphvizBuilder(FiniteAutomaton<T> automaton, Writer out) {
		super(out);
		this.automaton = automaton;
	}

	private void writeAutomaton() throws IOException {
		super.writeHeader();
		this.writeVirtualStartState()
			.writeAcceptingStates()
			.writeNonAcceptingStates()
			.writeEdges()
			.writeFooter()
			.flush()
			.close();
	}

	private FiniteAutomatonGraphvizBuilder<T> writeVirtualStartState() throws IOException {
		writeNodes(virtualStartStateStyle, Arrays.asList(VIRTUAL_START_STATE_LABEL));
		return this;
	}

	private FiniteAutomatonGraphvizBuilder<T> writeAcceptingStates() throws IOException {
		writeNodes(acceptingStateStyle, automaton.getAcceptingStates()
													.stream()
													.map(s -> s.getLabel())
													.collect(Collectors.toList()));
		return this;
	}

	private FiniteAutomatonGraphvizBuilder<T> writeNonAcceptingStates() throws IOException {
		writeNodes(nonAcceptingStateStyle, automaton.getStates()
													.stream()
													.filter(s -> !s.isAccepting())
													.map(s -> s.getLabel())
													.collect(Collectors.toList()));
		return this;
	}

	private FiniteAutomatonGraphvizBuilder<T> writeEdges() throws IOException {
		writeEdge(new Edge(VIRTUAL_START_STATE_LABEL, automaton.getStartState()
																.getLabel(), NO_LABEL));
		final Collection<Edge> edges = automaton.getTransitions()
												.stream()
												.map(edgeMapper)
												.collect(Collectors.toList());
		for (Edge edge : edges) {
			writeEdge(edge);
		}
		return this;
	}

}
