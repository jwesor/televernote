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
	private Decoder decoder;

	public Transcriber() {
		taps = new ArrayList<Tap>();
		timestamps = new ArrayList<Long>();
	}

	public void clear() {
		taps.clear();
		timestamps.clear();

		if (decoder != null)
			decoder.clear();
	}

	public void tap(long startTime, long endTime) {
		boolean skipGap = timestamps.isEmpty();

		if (!skipGap) {
			long lastEnd = timestamps.get(timestamps.size() - 1);
			Tap gapTap = new Tap(lastEnd, startTime, false);
			taps.add(gapTap);
			if (decoder != null)
				decoder.addToDecodeBuffer(gapTap);
		}

		Tap tap = new Tap(startTime, endTime, true);
		taps.add(tap);
		if (decoder != null)
			decoder.addToDecodeBuffer(tap);

		timestamps.add(startTime);
		timestamps.add(endTime);

	}

	public void setDecoder(Decoder decoder) {
		this.decoder = decoder;
	}
	public List<Tap> getTaps() {
		return taps;
	}
	public String getTitle() {
		return "Fill this in";
	}
}
