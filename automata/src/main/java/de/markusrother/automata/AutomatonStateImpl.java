package de.markusrother.automata;

class AutomatonStateImpl implements AutomatonState {

	private final String label;

	private boolean accepting;

	public AutomatonStateImpl(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public boolean hasLabel(String requestedLabel) {
		return this.label.equals(requestedLabel);
	}

	@Override
	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}

	@Override
	public boolean isAccepting() {
		return accepting;
	}

	@Override
	public String toString() {
		return label;
	}

}
