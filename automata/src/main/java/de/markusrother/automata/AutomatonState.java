package de.markusrother.automata;

interface AutomatonState {

	boolean isAccepting();

	void setAccepting(boolean b);

	String getLabel();

	boolean hasLabel(String label);

}
