package de.markusrother.automata;

public class EmptyAutomatonTransitionImpl<T> extends AbstractAutomatonTransitionImpl<T> {

	public EmptyAutomatonTransitionImpl(AutomatonState origin, AutomatonState target) {
		super(origin, target);
	}

	@Override
	public T getToken() {
		// NOT returning null in order to fail fast. Otherwise we would assign meaning to null.
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasToken(@SuppressWarnings("unused") T token) {
		// Returning false instead of throwing an Exception is expected behaviour. This transition has no token.
		return false;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public String toString() {
		return getOriginLabel() + "-()->" + getTargetLabel();
	}

}
