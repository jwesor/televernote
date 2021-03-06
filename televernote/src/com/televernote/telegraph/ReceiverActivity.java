package com.televernote.telegraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.televernote.R;
import com.televernote.morse.Transcriber;

/**
 * Activity for displaying received messages.
 * @author jwesor
 */
public class ReceiverActivity extends TelegraphActivity {

	public static final String TIMESTAMPS_KEY = "timestamps";
	private static final int INITIAL_DELAY = 1000;
	public static String SENDER_KEY = "sender";

	private List<Long> times;
	private int index;
	private String sender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		String data = bundle.getString("timestamps");
		times = new ArrayList<Long>();
		String[] timestamps = data.split(Transcriber.DELIM);
		for (String ts: timestamps) {
			if (ts.length() > 0) {
				try {
					times.add(Long.parseLong(ts));
				} catch (NumberFormatException e) {
					System.out.println("Improperly formatted timestamp " + ts);
				}
			}
		}

		sender = bundle.getString(SENDER_KEY);
		updateText();
		index = 0;
		button.setText(R.string.replay_message);
		initialTap();
	}

	@Override
	protected void setTelegraphTouchListener() {
		telegraphView.setOnTouchListener(null);
	}

	@Override
	protected void updateText() {
		String header = getString(R.string.message_sender);
		header = String.format(header, sender.toUpperCase(Locale.US));
		text.setText(header + "\n" + decoder.getTranslatedMorseBuffer(index >= times.size() - 1));
	}

	private void initialTap() {
		button.setEnabled(false);
		CountDownTimer timer = new TapTimer(INITIAL_DELAY, true);
		timer.start();
	}

	private void timeNextTap() {
		index ++;
		if (index >= times.size()) {
			button.setEnabled(true);
		} else {
			CountDownTimer timer = new TapTimer(times.get(index) - times.get(index-1), index % 2 == 0);
			timer.start();
		}
	}

	private class TapTimer extends CountDownTimer {

		boolean down;

		public TapTimer(long time, boolean down) {
			super(time, time);
			this.down = down;
		}

		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			if (down)
				beeper.tapDown(telegraphView);
			else
				beeper.tapUp(telegraphView);
			timeNextTap();
		}
	}

	@Override
	public void buttonPressed(View view) {
		index = 0;
		transcriber.clear();
		decoder.clear();
		initialTap();
	}
}
