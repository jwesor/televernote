package com.televernote.morse;

/**
 * Basic morse code chars.
 * @author jwesor
 */
enum Morse {
	DOT, DASH, GAP, CHAR, SPACE;

	@Override
	public String toString() {
		switch (this) {
		case DOT:
			return ".";
		case DASH:
			return "-";
		case CHAR:
			return " ";
		case SPACE:
			return "  ";
		default:
			return "";
		}
	}
}
