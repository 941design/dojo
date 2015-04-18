package de.markusrother.automata;

class AutomatonTransitionImpl<T> implements AutomatonTransition<T> {

	private final AutomatonState origin;
	private final AutomatonState target;
	private final T token;

	AutomatonTransitionImpl(AutomatonState origin, AutomatonState target, T token) {
		this.origin = origin;
		this.target = target;
		this.token = token;
	}

	@Override
	public AutomatonState getOrigin() {
		return origin;
	}

	@Override
	public AutomatonState getTarget() {
		return target;
	}

	@Override
	public T getToken() {
		return token;
	}

	@Override
	public String getOriginLabel() {
		return origin.getLabel();
	}

	@Override
	public String getTargetLabel() {
		return target.getLabel();
	}

}
