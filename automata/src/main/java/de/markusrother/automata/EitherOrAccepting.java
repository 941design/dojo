package de.markusrother.automata;

public enum EitherOrAccepting {
	ACCEPTING,
	NOT_ACCEPTING;

	public boolean isAccepting() {
		return this == ACCEPTING;
	}

}
