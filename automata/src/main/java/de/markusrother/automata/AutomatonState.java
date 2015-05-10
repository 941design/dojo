package de.markusrother.automata;

public interface AutomatonState {

	boolean isAccepting();

	void setAccepting(boolean b);

	String getLabel();

	boolean hasLabel(String label);

}
