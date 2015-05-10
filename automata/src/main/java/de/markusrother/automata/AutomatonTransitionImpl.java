package de.markusrother.automata;

/**
 * @param <T> - the generic token/alphabet type.
 */
class AutomatonTransitionImpl<T> extends AbstractAutomatonTransitionImpl<T> {

	private final T token;

	AutomatonTransitionImpl(AutomatonState origin, AutomatonState target, T token) {
		super(origin, target);
		this.token = token;
	}

	@Override
	public T getToken() {
		return token;
	}

	@Override
	public boolean hasToken(T token) {
		return this.token.equals(token);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public String toString() {
		return getOriginLabel() + "-(" + token + ")->" + getTargetLabel();
	}

}
