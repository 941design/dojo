package de.markusrother.automata;

public interface AutomatonTransition<T> {

	AutomatonState getOrigin();

	AutomatonState getTarget();

	boolean hasOrigin(AutomatonState state);

	boolean hasTarget(AutomatonState state);

	T getToken();

	boolean hasToken(T token);

	boolean isEmpty();

	String getOriginLabel();

	String getTargetLabel();

	boolean hasOriginLabel(String originLabel);

	boolean hasTargetLabel(String targetLabel);

}
