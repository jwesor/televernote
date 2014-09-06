package com.televernote.morse;

import java.util.ArrayList;
import java.util.List;

/**
 * Records taps and converts them to an array of Tap objects.
 * @author jwesor
 *
 */
public class Transcriber {
	private List<Tap> taps;
	private List<Long> timestamps;
	private boolean transcribing;

	public Transcriber() {
		taps = new ArrayList<Tap>();
		timestamps = new ArrayList<Long>();
		transcribing = false;
	}

	public void begin() {
		if (transcribing)
			return;
		transcribing = true;
		taps.clear();
		timestamps.clear();
	}

	public void tap(long startTime, long endTime) {
		if (!transcribing)
			return;
		timestamps.add(startTime);
		timestamps.add(endTime);
	}

	public void end() {
		if (!transcribing)
			return;
		transcribing = false;
		boolean tap = true;
		for (int i = 0; i < timestamps.size() - 1; i ++) {
			taps.add(new Tap(timestamps.get(i), timestamps.get(i + 1), tap));
			tap = !tap;
		}
	}

	public List<Tap> getTaps() {
		return taps;
	}
}
