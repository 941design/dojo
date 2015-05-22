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
 * TODO - This could become (or inherit from) a generic builder for graphs. .addNodes(Collection<Node> nodes, NodeType
 * nodeType); .addGraph(Graph<AutomatonState> g); interface Graph<T> { Collection<Transition<T>> getTransitions(T node);
 * }
 *
 * @param <T> - the generic token/alphabet type.
 */
public class DotFileBuilder<T> {

	protected enum Shape {
		CIRCLE("circle"),
		DOUBLECIRCLE("doublecircle"),
		POINT("point");

		private final String name;

		private Shape(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	protected enum Style {
		SOLID("solid"),
		INVISIBLE("invis");

		private final String name;

		private Style(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	protected static class NodeStyle {

		private final Shape shape;
		private final Style style;

		protected NodeStyle(Shape shape, Style style) {
			this.shape = shape;
			this.style = style;
		}

		public String getShapeName() {
			return shape.getName();
		}

		public String getStyleName() {
			return style.getName();
		}

	}

	protected static class Edge {

		protected final String origin;
		protected final String target;
		protected final String label;

		public Edge(String origin, String target, String label) {
			this.origin = origin;
			this.target = target;
			this.label = label;
		}

		public boolean hasLabel() {
			return this.label != null;
		}

	}

	private static final String NL = "\n";
	private static final String TAB = "\t";
	private static final String SPACE = " ";
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

	private final FiniteAutomaton<T> automaton;
	private final Writer out;

	public static <T> void write(FiniteAutomaton<T> automaton, Writer out) throws IOException, NoStartStateException,
			NoAcceptingStatesException {
		if (!automaton.hasStartState()) {
			throw new NoStartStateException();
		}
		if (!automaton.hasAcceptingStates()) {
			throw new NoAcceptingStatesException();
		}
		final DotFileBuilder<T> dotFileBuilder = new DotFileBuilder<T>(automaton, out);
		dotFileBuilder.writeAutomaton();
	}

	public DotFileBuilder(FiniteAutomaton<T> automaton, Writer out) {
		this.automaton = automaton;
		this.out = out;
	}

	private void writeAutomaton() throws IOException {
		this.writeHeader()
			.writeVirtualStartState()
			.writeAcceptingStates()
			.writeNonAcceptingStates()
			.writeEdges()
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
		return this.writeLine("digraph FiniteAutomaton {")
					.writeLine(TAB + "rankdir=LR;")
					.writeLine(TAB + "size=\"%s\"", 8.5);
	}

	private DotFileBuilder<T> writeFooter() throws IOException {
		return this.writeLine("}");
	}

	private DotFileBuilder<T> writeVirtualStartState() throws IOException {
		return this.writeNodes(virtualStartStateStyle, Arrays.asList(VIRTUAL_START_STATE_LABEL));
	}

	private DotFileBuilder<T> writeAcceptingStates() throws IOException {
		return this.writeNodes(acceptingStateStyle, automaton.getAcceptingStates()
																.stream()
																.map(s -> s.getLabel())
																.collect(Collectors.toList()));
	}

	private DotFileBuilder<T> writeNonAcceptingStates() throws IOException {
		return this.writeNodes(nonAcceptingStateStyle, automaton.getStates()
																.stream()
																.filter(s -> !s.isAccepting())
																.map(s -> s.getLabel())
																.collect(Collectors.toList()));
	}

	private DotFileBuilder<T> writeNodes(NodeStyle nodeStyle, Collection<String> labels) throws IOException {
		this.write(TAB + "node ["//
				+ "shape=" + nodeStyle.getShapeName() + ", "//
				+ "style=" + nodeStyle.getStyleName() + "];");
		for (String label : labels) {
			this.write(SPACE + label);
		}
		return this.writeLine(";");
	}

	private DotFileBuilder<T> writeEdges() throws IOException {
		this.writeEdge(new Edge(VIRTUAL_START_STATE_LABEL, automaton.getStartState()
																	.getLabel(), NO_LABEL));
		final Collection<Edge> edges = automaton.getTransitions()
												.stream()
												.map(edgeMapper)
												.collect(Collectors.toList());
		for (Edge edge : edges) {
			this.writeEdge(edge);
		}
		return this;
	}

	private DotFileBuilder<T> writeEdge(Edge edge) throws IOException {
		this.write(TAB + "%s -> %s", edge.origin, edge.target);
		if (edge.hasLabel()) {
			return this.writeLine(" [label=\"%s\"];", edge.label);
		}
		else {
			return this.writeLine(";");
		}

	}

}
