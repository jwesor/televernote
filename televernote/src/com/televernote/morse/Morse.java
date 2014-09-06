package com.televernote.morse;

public enum Morse {
	DOT, DASH, CHAR, SPACE;

	@Override
	public String toString() {
		switch (this) {
		case DOT:
			return ".";
		case DASH:
			return "-";
		case CHAR:
			return "'";
		case SPACE:
			return " ";
		default:
			return "";
		}
	}
}
