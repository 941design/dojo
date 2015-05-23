package de.markusrother.automata;

public interface AutomatonState {

	boolean isAccepting();

	String getLabel();

	boolean hasLabel(String label);

	AutomatonState copy();

}
