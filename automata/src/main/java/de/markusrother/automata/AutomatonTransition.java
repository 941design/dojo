package de.markusrother.automata;

class AutomatonTransition<T> {

	private final AutomatonState origin;
	private final AutomatonState target;
	private final T token;

	public AutomatonTransition(AutomatonState origin, AutomatonState target, T token) {
		this.origin = origin;
		this.target = target;
		this.token = token;
	}

	public AutomatonState getOrigin() {
		return origin;
	}

	public AutomatonState getTarget() {
		return target;
	}

	public T getToken() {
		return token;
	}

	public String getOriginLabel() {
		return origin.getLabel();
	}

	public String getTargetLabel() {
		return target.getLabel();
	}

}
