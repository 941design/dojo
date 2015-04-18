package de.markusrother.automata.exceptions;

public class NoSuchStateException extends Exception {

	private final String label;

	public NoSuchStateException(String label) {
		this.label = label;
	}

}
