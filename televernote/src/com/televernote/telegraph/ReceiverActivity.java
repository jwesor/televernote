package com.televernote.telegraph;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.televernote.R;

public class ReceiverActivity extends TelegraphActivity {

	public static final String TIMESTAMPS_KEY = "timestamps";
	private static final int INITIAL_DELAY = 1000;

	private long[] times;
	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		times = bundle.getLongArray("timestamps");
		index = 0;
		initialTap();
	}

	@Override
	protected void setTelegraphTouchListener() {
		telegraphView.setOnTouchListener(null);
	}

	@Override
	protected void updateText() {
		TextView text = (TextView)(findViewById(R.id.morseTicker));
		text.setText(decoder.getTranslatedMorseBuffer(index >= times.length - 1));
	}

	private void initialTap() {
		CountDownTimer timer = new TapTimer(INITIAL_DELAY, true);
		timer.start();
	}

	private void timeNextTap() {
		index ++;
		if (index >= times.length)
			return;
		CountDownTimer timer = new TapTimer(times[index] - times[index - 1], index % 2 == 0);
		timer.start();
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
}
