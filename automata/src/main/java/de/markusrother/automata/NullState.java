package de.markusrother.automata;

/**
 * Class representing an implicit {@link de.markusrother.automata.AutomatonState} which is never accepting and has no
 * {@link de.markusrother.automata.AutomatonTransition}s. This class follows the null-object pattern.
 */
class NullState implements AutomatonState {

	private static final NullState INSTANCE = new NullState();

	public static AutomatonState getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean isAccepting() {
		return false;
	}

	@SuppressWarnings("unused")
	@Override
	public void setAccepting(boolean b) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLabel() {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unused")
	@Override
	public boolean hasLabel(String label) {
		throw new UnsupportedOperationException();
	}

}
