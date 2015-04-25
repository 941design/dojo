package de.markusrother.automata.exceptions;

public class DuplicateEmptyTransitionException extends DuplicateTransitionException {

	public DuplicateEmptyTransitionException(String originLabel, String targetLabel) {
		super(originLabel, targetLabel, null);
	}

}
