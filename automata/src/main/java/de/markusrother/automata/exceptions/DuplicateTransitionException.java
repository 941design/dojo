package de.markusrother.automata.exceptions;

public class DuplicateTransitionException extends Exception {

	private final String originLabel;
	private final String targetLabel;
	private final Object token;

	public DuplicateTransitionException(String originLabel, String targetLabel, Object token) {
		this.originLabel = originLabel;
		this.targetLabel = targetLabel;
		this.token = token;
	}

}
