package com.televernote.telegraph;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.televernote.R;

public class TelegraphView extends ImageView {

	private boolean down;

	public TelegraphView(Context context) {
		super(context);
		init(context);
	}

	public TelegraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TelegraphView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		down = false;
	}

	@Override
	public boolean performClick() {
		super.performClick();
		down = !down;
		if (down) {
			setImageResource(R.drawable.telegraph_key_down);
		} else {
			setImageResource(R.drawable.telegraph_key);
		}
		return true;
	}
}
