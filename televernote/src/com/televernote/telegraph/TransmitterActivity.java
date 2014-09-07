package com.televernote.telegraph;

import java.util.Locale;

import android.os.Bundle;
import android.view.View;

import com.evernote.thrift.transport.TTransportException;
import com.televernote.R;
import com.televernote.evernote.EvernoteInteractor;

/**
 * Activity for sending messages.
 * @author jwesor
 */
public class TransmitterActivity extends TelegraphActivity {

	public static String RECIPIENT_KEY = "recipient";

	private String recipient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		recipient = bundle.getString(RECIPIENT_KEY);
	}
	@Override
	protected void setTelegraphTouchListener() {
		telegraphView.setOnTouchListener(beeper);
	}

	@Override
	protected void updateText() {
		String header = getString(R.string.message_recipient);
		header = String.format(header, recipient.toUpperCase(Locale.US));
		text.setText(header + "\n" + decoder.getMorseBuffer());
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
