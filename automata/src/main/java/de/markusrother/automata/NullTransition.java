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

	@Override
	public T getToken() {
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

}
