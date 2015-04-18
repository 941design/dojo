package de.markusrother.automata;

public interface AutomatonTransition<T> {

	AutomatonState getOrigin();

	AutomatonState getTarget();

	T getToken();

	String getOriginLabel();

	String getTargetLabel();

}
