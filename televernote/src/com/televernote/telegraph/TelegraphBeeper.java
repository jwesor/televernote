package com.televernote.telegraph;

import java.io.IOException;

import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.televernote.R;

public class TelegraphBeeper implements OnTouchListener {

	private MediaPlayer media;
	private TelegraphActivity activity;

	public TelegraphBeeper(TelegraphActivity context) {
		activity = context;
		media = MediaPlayer.create(context, R.raw.beep);
		try {
			media.prepare();
			media.setLooping(true);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getPointerCount() > 1)
			return false;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			media.start();
			activity.tapDown();
			v.performClick();
			break;
		case MotionEvent.ACTION_UP:
			media.pause();
			activity.tapUp();
			v.performClick();
			break;
		}
		return true;
	}

	public void dispose() {
		media.stop();
	}

}
