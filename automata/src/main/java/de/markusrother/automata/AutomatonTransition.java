package de.markusrother.automata;

/**
 * @param <T> - the generic token/alphabet type.
 */
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

	boolean hasOriginLabel(String stateLabel);

	boolean hasTargetLabel(String stateLabel);

}
