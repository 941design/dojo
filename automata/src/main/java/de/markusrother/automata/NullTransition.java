package de.markusrother.automata;

class NullTransition<T> implements AutomatonTransition<T> {

	private static final NullTransition<?> INSTANCE = new NullTransition<>();

	@SuppressWarnings("unchecked")
	public static <T> AutomatonTransition<T> getInstance() {
		return (AutomatonTransition<T>) INSTANCE;
	}

	@Override
	public AutomatonState getOrigin() {
		throw new UnsupportedOperationException();
	}

	@Override
	public AutomatonState getTarget() {
		return NullState.getInstance();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean hasOrigin(AutomatonState state) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean hasTarget(AutomatonState state) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getOriginLabel() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTargetLabel() {
		return getTarget().getLabel();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean hasOriginLabel(String originLabel) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean hasTargetLabel(String targetLabel) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T getToken() {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean hasToken(T token) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}
