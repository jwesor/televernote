package com.televernote.telegraph;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.televernote.R;
import com.televernote.morse.Decoder;
import com.televernote.morse.Transcriber;

public abstract class TelegraphActivity extends ActionBarActivity  {

	protected TelegraphBeeper beeper;
	private long downTime;
	protected Transcriber transcriber;
	protected Decoder decoder;
	protected TelegraphView telegraphView;
	protected TextView text;
	protected Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telegraph);

		beeper = new TelegraphBeeper(this);

		transcriber = new Transcriber();
		decoder = new Decoder();
		transcriber.setDecoder(decoder);

		telegraphView = (TelegraphView) findViewById(R.id.telegraphView);
		text = (TextView) findViewById(R.id.morseTicker);
		button = (Button) findViewById(R.id.telegraphButton);
		setTelegraphTouchListener();
	}

	protected abstract void setTelegraphTouchListener();

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

	protected abstract void updateText();
	public abstract void buttonPressed(View view);
}
