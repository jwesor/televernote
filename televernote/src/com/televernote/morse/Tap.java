package com.televernote.morse;

/**
 * Represents the base unit of more: a dot, dash, end of character, or space.
 * @author jwesor
 */
class Tap {
	private long start, end;
	private boolean tap; //true if tap, false if pause

	public Tap(long timeStart, long timeEnd, boolean tap) {
		start = timeStart;
		end = timeEnd;
		this.tap = tap;
	}

	public Morse decode(long timeUnit) {
		int units = (int) ((end - start) / timeUnit);
		if (tap) {
			int dot = Math.abs(units - 1);
			int dash = Math.abs(units - 3);
			return (dot <= dash) ? Morse.DOT : Morse.DASH;
		} else {
			return (units <= 2) ? Morse.GAP : Morse.CHAR;
		}
	}
}
