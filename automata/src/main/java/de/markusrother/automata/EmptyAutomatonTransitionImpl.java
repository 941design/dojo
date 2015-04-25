package de.markusrother.automata;


public class EmptyAutomatonTransitionImpl<T> extends AbstractAutomatonTransitionImpl<T> {

	public EmptyAutomatonTransitionImpl(AutomatonState origin, AutomatonState target) {
		super(origin, target);
	}

	@Override
	public T getToken() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasToken(@SuppressWarnings("unused") T token) {
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
