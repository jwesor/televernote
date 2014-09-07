package com.televernote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.evernote.client.android.EvernoteSession;
import com.televernote.morse.Decoder;
import com.televernote.telegraph.ReceiverActivity;
import com.televernote.tests.MorseTests;



public class MainActivity extends ActionBarActivity {

	private static final String CONSUMER_KEY = "eric5-5494";
	private static final String CONSUMER_SECRET = "2b514688c7e57a5d";


	private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

	private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;

	// Current evernote session
	private EvernoteSession mEvernoteSession;

	private boolean tryingToLog = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MorseTests.execute();
		//Set up the Evernote Singleton Session
		mEvernoteSession = EvernoteSession.getInstance(this, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_SERVICE, SUPPORT_APP_LINKED_NOTEBOOKS);
		if (!tryingToLog && !mEvernoteSession.isLoggedIn()) {
			tryingToLog = true;
			mEvernoteSession.authenticate(this);
		}
		else {
			tryingToLog = true;
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("Resuming");
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
	    	// Update UI when oauth activity returns result
	    	case EvernoteSession.REQUEST_CODE_OAUTH:
	    		if (resultCode == Activity.RESULT_OK) {
	    			System.out.println("Logged successfully");
	    			// Authentication was successful, do what you need to do in your app

	    			//check if this is the first time user is using this app; set up appropriate stacks
	    			EvernoteInteractor.initializeIfNeeded(this);

	    			// Goto screen showing my messages
	    			startActivity(new Intent(getApplicationContext(), ViewMessagesActivity.class));
	    		}
	    		else {
	    			System.out.println("??");
	    			// Goto screen prompting user to log in?
	    			//startActivity(new Intent(getApplicationContext(), LoginActivity.class));
	    		}
	    		break;
		}
	 */

	@Override
	public void onResume() {
		super.onResume();
		//Intent intent = new Intent(this, TelegraphActivity.class);
		Intent intent = new Intent(this, ReceiverActivity.class);
		long d = Decoder.DEFAULT_TIME_UNIT;
		long[] timestamps = new long[]{
				d*0, d*1,
				d*2, d*5,

				d*8, d*11,
				d*12, d*13,
				d*14, d*15,
				d*16, d*17,

				d*20, d*23,
				d*24, d*25,
				d*25, d*28,
				d*29, d*30
		};
		intent.putExtra(ReceiverActivity.TIMESTAMPS_KEY, timestamps);
		startActivity(intent);
	}
}
