package de.markusrother.automata;

class NullTransition<T> implements AutomatonTransition<T> {

	private static final NullTransition<?> INSTANCE = new NullTransition<>();

	@SuppressWarnings("unchecked")
	public static <T> AutomatonTransition<T> getInstance() {
		return (AutomatonTransition<T>) INSTANCE;
	}

	@Override
	public AutomatonState getOrigin() {
		// If necessary, we could instantiate null transitions once needed for each origin.
		throw new UnsupportedOperationException();
	}

	@Override
	public AutomatonState getTarget() {
		return NullState.getInstance();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean hasOrigin(AutomatonState state) {
		// If necessary, we could instantiate null transitions once needed for each origin.
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean hasTarget(AutomatonState state) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getOriginLabel() {
		// If necessary, we could instantiate null transitions once needed for each origin.
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTargetLabel() {
		return getTarget().getLabel();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean hasOriginLabel(String stateLabel) {
		// If necessary, we could instantiate null transitions once needed for each origin.
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean hasTargetLabel(String stateLabel) {
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
		// This is neither an empty, nor a non-empty transition!
		throw new UnsupportedOperationException();
	}

}
