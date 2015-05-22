package de.markusrother.automata.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

public class GraphvizBuilder {

	public enum Shape {
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

	public enum Style {
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

	public static class NodeStyle {

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

	public static class Edge {

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

	private final Writer out;

	public GraphvizBuilder(Writer out) {
		this.out = out;
	}

	public GraphvizBuilder write(String format, Object... args) throws IOException {
		out.write(String.format(format, args));
		return this;
	}

	public GraphvizBuilder writeLine(String format, Object... args) throws IOException {
		return this.write(format + NL, args);
	}

	public GraphvizBuilder writeHeader() throws IOException {
		return this.writeLine("digraph FiniteAutomaton {")
					.writeLine(TAB + "rankdir=LR;")
					.writeLine(TAB + "size=\"%s\"", 8.5);
	}

	public GraphvizBuilder writeFooter() throws IOException {
		return this.writeLine("}");
	}

	public GraphvizBuilder writeNodes(NodeStyle nodeStyle, Collection<String> labels) throws IOException {
		this.write(TAB + "node ["//
				+ "shape=" + nodeStyle.getShapeName() + ", "//
				+ "style=" + nodeStyle.getStyleName() + "];");
		for (String label : labels) {
			this.write(SPACE + label);
		}
		return this.writeLine(";");
	}

	public GraphvizBuilder writeEdge(Edge edge) throws IOException {
		this.write(TAB + "%s -> %s", edge.origin, edge.target);
		if (edge.hasLabel()) {
			return this.writeLine(" [label=\"%s\"];", edge.label);
		}
		else {
			return this.writeLine(";");
		}

	}

	public GraphvizBuilder flush() throws IOException {
		out.flush();
		return this;
	}

	public GraphvizBuilder close() throws IOException {
		out.close();
		return this;
	}

}
