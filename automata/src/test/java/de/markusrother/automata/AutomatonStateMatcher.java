package de.markusrother.automata;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class AutomatonStateMatcher extends BaseMatcher<AutomatonState> {

	public static AutomatonStateMatcher isState(String label) {
		return new AutomatonStateMatcher(label);
	}

	private final String label;

	public AutomatonStateMatcher(String label) {
		this.label = label;
	}

	@Override
	public boolean matches(Object item) {
		final AutomatonState state = (AutomatonState) item;
		return label.equals(state.getLabel());
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(label);
	}

}
