package de.markusrother.automata;

class AutomatonStateImpl implements AutomatonState {

	private final String label;
	private final EitherOrAccepting eitherOrAccepting;

	public AutomatonStateImpl(String label, EitherOrAccepting eitherOrAccepting) {
		this.label = label;
		this.eitherOrAccepting = eitherOrAccepting;
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
	public boolean isAccepting() {
		return eitherOrAccepting.isAccepting();
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public AutomatonState copy() {
		return new AutomatonStateImpl(label, eitherOrAccepting);
	}

}
