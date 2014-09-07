package com.televernote.telegraph;

import android.view.View;

import com.evernote.thrift.transport.TTransportException;
import com.televernote.evernote.EvernoteInteractor;


public class TransmitterActivity extends TelegraphActivity {

	@Override
	protected void setTelegraphTouchListener() {
		telegraphView.setOnTouchListener(beeper);
	}

	@Override
	protected void updateText() {
		text.setText(decoder.getMorseBuffer());
	}

	@Override
	public void buttonPressed(View view) {
		try {
			EvernoteInteractor.createNote(this, transcriber.getTitle(), decoder.getMorseBuffer(), transcriber.getTimestampData(), "jwesor@gmail.com");
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}

}
