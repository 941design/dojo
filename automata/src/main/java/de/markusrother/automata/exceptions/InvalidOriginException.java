package de.markusrother.automata.exceptions;

import de.markusrother.automata.AutomatonState;

public class InvalidOriginException extends RuntimeException {

	private final AutomatonState origin;

	public InvalidOriginException(AutomatonState origin) {
		this.origin = origin;
	}

}
