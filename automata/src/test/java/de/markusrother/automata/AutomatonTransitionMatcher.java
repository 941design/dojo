package de.markusrother.automata;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class AutomatonTransitionMatcher<T> extends BaseMatcher<AutomatonTransition<T>> {

	private final String originLabel;
	private final String targetLabel;
	private final T token;

	public static <T> AutomatonTransitionMatcher<T> isTransition(String originLabel, String targetLabel, T token) {
		return new AutomatonTransitionMatcher<>(originLabel, targetLabel, token);
	}

	private AutomatonTransitionMatcher(String originLabel, String targetLabel, T token) {
		this.originLabel = originLabel;
		this.targetLabel = targetLabel;
		this.token = token;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(Object item) {
		final AutomatonTransition<T> transition = (AutomatonTransition<T>) item;
		return transition.hasOriginLabel(originLabel) //
				&& transition.hasTargetLabel(targetLabel) //
				&& transition.hasToken(token);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(originLabel)
					.appendText("-[")
					.appendValue(token)
					.appendText("]->")
					.appendText(targetLabel);
	}

}
