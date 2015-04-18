package de.markusrother.automata.exceptions;

public class DuplicateStateException extends Exception {

	private final String label;

	public DuplicateStateException(String label) {
		this.label = label;
	}

}
