package de.markusrother.automata.exceptions;

import java.util.function.Supplier;

public class NoSuchStateException extends Exception implements Supplier<NoSuchStateException> {

	private final String label;

	public NoSuchStateException(String label) {
		this.label = label;
	}

	@Override
	public NoSuchStateException get() {
		return this;
	}

}
