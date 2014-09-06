package com.televernote.morse;

import java.util.HashMap;
import java.util.List;

/**
 * Converts Taps to a String message, using either the standard Morse table or a custom table.
 * @author jwesor
 *
 */
public class Decoder {
	private static long DEFAULT_TIME_UNIT = 125;
	private static String NULL_CHAR = "?";
	private static HashMap<String, String> DEFAULT_TABLE = new HashMap<String, String>();
	static {
		DEFAULT_TABLE.put(".-", "A");
		DEFAULT_TABLE.put("-...", "B");
		DEFAULT_TABLE.put("-.-.", "C");
		DEFAULT_TABLE.put("-..", "D");
		DEFAULT_TABLE.put(".", "E");
		DEFAULT_TABLE.put("..-.", "F");
		DEFAULT_TABLE.put("--.", "G");
		DEFAULT_TABLE.put("....", "H");
		DEFAULT_TABLE.put("..", "I");
		DEFAULT_TABLE.put(".---", "J");
		DEFAULT_TABLE.put("-.-", "K");
		DEFAULT_TABLE.put(".-..", "L");
		DEFAULT_TABLE.put("--", "M");
		DEFAULT_TABLE.put("-.", "N");
		DEFAULT_TABLE.put("---", "O");
		DEFAULT_TABLE.put(".--.", "P");
		DEFAULT_TABLE.put("--.-", "Q");
		DEFAULT_TABLE.put(".-.", "R");
		DEFAULT_TABLE.put("...", "S");
		DEFAULT_TABLE.put("-", "T");
		DEFAULT_TABLE.put("..-", "U");
		DEFAULT_TABLE.put("...-", "V");
		DEFAULT_TABLE.put(".--", "W");
		DEFAULT_TABLE.put("-..-", "X");
		DEFAULT_TABLE.put("-.--", "Y");
		DEFAULT_TABLE.put("--..", "Z");
		DEFAULT_TABLE.put(".----", "1");
		DEFAULT_TABLE.put("..---", "2");
		DEFAULT_TABLE.put("...--", "3");
		DEFAULT_TABLE.put("....-", "4");
		DEFAULT_TABLE.put(".....", "5");
		DEFAULT_TABLE.put("-....", "6");
		DEFAULT_TABLE.put("--...", "7");
		DEFAULT_TABLE.put("---..", "8");
		DEFAULT_TABLE.put("----.", "9");
		DEFAULT_TABLE.put("-----", "9");
	}

	private HashMap<String, String> table;
	private StringBuffer buffer;
	private long timeUnit;

	public Decoder() {
		table = DEFAULT_TABLE;
		timeUnit = DEFAULT_TIME_UNIT;
		buffer = new StringBuffer();
	}

	public String decodeAndTranslate(List<Tap> taps) {
		return translate(decode(taps));
	}

	public String decode(List<Tap> taps) {
		StringBuffer morseBuffer = new StringBuffer();
		for (Tap tap: taps) {
			morseBuffer.append(tap.decode(timeUnit).toString());
		}
		String morse = morseBuffer.toString();
		return morse;
	}

	public String translate(String morse) {
		String[] morseChars = morse.split(Morse.CHAR.toString());
		StringBuffer message = new StringBuffer(morseChars.length);
		for (String morseChar: morseChars) {
			if (table.containsKey(morseChar)) {
				message.append(table.get(morseChar));
			} else {
				message.append(morseChar);
			}
		}
		return message.toString();
	}

	public void clear() {
		buffer = new StringBuffer();
	}

	public void addToDecodeBuffer(Tap tap) {
		buffer.append(tap.decode(timeUnit).toString());
	}

	public String getMorseBuffer() {
		return buffer.toString();
	}

	public String getTranslatedMorseBuffer() {
		return translate(buffer.toString());
	}
}
