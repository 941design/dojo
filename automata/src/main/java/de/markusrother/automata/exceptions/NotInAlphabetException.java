package de.markusrother.automata.exceptions;

import java.util.Collection;

public class NotInAlphabetException extends RuntimeException {

	private final Collection<?> alphabet;
	private final Object token;

	public NotInAlphabetException(Collection<?> alphabet, Object token) {
		this.alphabet = alphabet;
		this.token = token;

	}

}
