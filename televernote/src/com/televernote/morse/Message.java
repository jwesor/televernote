package com.televernote.morse;

import java.util.ArrayList;
import java.util.List;

public class Message {
	private List<Tap> taps;
	private String morseDecoded;
	
	//get taps from transcriber and packages it into a message object
	public Message(Transcriber transcriber) {
		taps = transcriber.getTaps();
		//initially do not decode anything
		morseDecoded = "";
	}
	//gets some sort of data from the Evernote servers and packages it into a list of taps
	public Message(String fromServer) {
		taps = new ArrayList<Tap>(0);
		morseDecoded = "";
	}
	
	//returns title to show in list of messages
	public String getTitle() {
		return "Fill this in";
	}
}
