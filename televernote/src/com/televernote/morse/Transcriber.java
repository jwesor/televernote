package com.televernote.morse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Records taps and converts them to an array of Tap objects.
 * A Decoder can be attached to a Transcriber so that the decoder will decode taps
 * as they come in.
 * @author jwesor
 */
public class Transcriber {
	private List<Tap> taps;
	private List<Long> timestamps;
	private Decoder decoder;

	public static String DELIM = ",";

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

	public String getTimestampData() {
		StringBuffer buffer = new StringBuffer();
		for (Long time: timestamps) {
			buffer.append(time.toString() + DELIM);
		}
		return buffer.toString();
	}

	@SuppressWarnings("deprecation")
	public String getTitle() {
		if (timestamps.isEmpty())
			return "?";
		Date date = new Date(timestamps.get(0));
		return date.toGMTString();
	}
}
