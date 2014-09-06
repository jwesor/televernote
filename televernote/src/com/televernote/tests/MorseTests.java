package com.televernote.tests;

import com.televernote.morse.Decoder;
import com.televernote.morse.Transcriber;

public class MorseTests {
	public static void execute() {
		System.out.println("Morse test...");
		Transcriber t = new Transcriber();
		int d = 125;
		t.begin();
		//a
		t.tap(0, d);
		t.tap(d, d*4);
		//e
		t.tap(d*7, d*8);
		//i
		t.tap(d*11, d*12);
		t.tap(d*13, d*14);
		//o
		t.tap(d*17, d*20);
		t.tap(d*21, d*24);
		t.tap(d*25, d*30);
		//u
		t.tap(d*33, d*34);
		t.tap(d*35, d*36);
		t.tap(d*37, d*40);

		t.end();

		Decoder de = new Decoder();
		String morse = de.decode(t.getTaps());
		System.out.println(morse);
		System.out.println(de.translate(morse));

		//We expect AEIOU
	}

}
