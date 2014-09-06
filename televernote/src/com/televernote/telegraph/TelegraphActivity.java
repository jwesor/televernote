package com.televernote.telegraph;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.televernote.R;
import com.televernote.morse.Decoder;
import com.televernote.morse.Transcriber;

public class TelegraphActivity extends ActionBarActivity  {

	private TelegraphBeeper beeper;
	private long downTime;
	private Transcriber transcriber;
	private Decoder decoder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telegraph);

		beeper = new TelegraphBeeper(this);
		findViewById(R.id.telegraphView).setOnTouchListener(beeper);

		transcriber = new Transcriber();
		decoder = new Decoder();
		transcriber.setDecoder(decoder);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		findViewById(R.id.telegraphView).setOnTouchListener(null);
		beeper.dispose();
	}

	void tapDown() {
		downTime = System.currentTimeMillis();
	}

	void tapUp() {
		long upTime = System.currentTimeMillis();
		transcriber.tap(downTime, upTime);
		TextView text = (TextView)(findViewById(R.id.morseTicker));
		text.setText(decoder.getMorseBuffer());
	}
}
