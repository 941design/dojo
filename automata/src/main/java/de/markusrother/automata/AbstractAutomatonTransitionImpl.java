package de.markusrother.automata;

public abstract class AbstractAutomatonTransitionImpl<T> implements AutomatonTransition<T> {

	protected final AutomatonState origin;
	protected final AutomatonState target;

	protected AbstractAutomatonTransitionImpl(AutomatonState origin, AutomatonState target) {
		this.origin = origin;
		this.target = target;
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
	public String getOriginLabel() {
		return origin.getLabel();
	}

	@Override
	public String getTargetLabel() {
		return target.getLabel();
	}

	@Override
	public boolean hasOrigin(AutomatonState state) {
		return origin == state;
	}

	@Override
	public boolean hasTarget(AutomatonState state) {
		return target == state;
	}

	@Override
	public boolean hasOriginLabel(String originLabel) {
		if (originLabel == null) {
			throw new IllegalArgumentException();
		}
		return originLabel.equals(getOriginLabel());
	}

	@Override
	public boolean hasTargetLabel(String targetLabel) {
		if (targetLabel == null) {
			throw new IllegalArgumentException();
		}
		return targetLabel.equals(getTargetLabel());
	}

}
