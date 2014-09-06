package com.televernote.morse;

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
			return "'";
		case SPACE:
			return " ";
		default:
			return "";
		}
	}
}
