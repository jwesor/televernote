package com.televernote.telegraph;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.televernote.R;
import com.televernote.morse.Decoder;
import com.televernote.morse.Transcriber;

public class TelegraphActivity extends ActionBarActivity  {

	protected TelegraphBeeper beeper;
	private long downTime;
	protected Transcriber transcriber;
	protected Decoder decoder;
	protected TelegraphView telegraphView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telegraph);

		beeper = new TelegraphBeeper(this);

		transcriber = new Transcriber();
		decoder = new Decoder();
		transcriber.setDecoder(decoder);

		telegraphView = (TelegraphView) findViewById(R.id.telegraphView);

		setTelegraphTouchListener();
	}

	protected void setTelegraphTouchListener() {
		findViewById(R.id.telegraphView).setOnTouchListener(beeper);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		telegraphView.setOnTouchListener(null);
		beeper.dispose();
	}

	void tapDown() {
		downTime = System.currentTimeMillis();
	}

	void tapUp() {
		long upTime = System.currentTimeMillis();
		transcriber.tap(downTime, upTime);
		updateText();
	}

	protected void updateText() {
		TextView text = (TextView)(findViewById(R.id.morseTicker));
		text.setText(decoder.getMorseBuffer());
	}
}
