package com.televernote;

import com.evernote.client.android.EvernoteSession;
import com.televernote.activities.ViewMessagesActivity;
import com.televernote.tests.MorseTests;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
	
	private static final String CONSUMER_KEY = "eric5";
	private static final String CONSUMER_SECRET = "c286186b14af5124";
	
	private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

	private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;

	// Current evernote session
	private EvernoteSession mEvernoteSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MorseTests.execute();
		//Set up the Evernote Singleton Session
		mEvernoteSession = EvernoteSession.getInstance(this, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_SERVICE, SUPPORT_APP_LINKED_NOTEBOOKS);
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
	@Override
	protected void onResume() {
		super.onResume();
		if (!mEvernoteSession.isLoggedIn()) {
	    	mEvernoteSession.authenticate(this);
	    }
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
	    	// Update UI when oauth activity returns result
	    	case EvernoteSession.REQUEST_CODE_OAUTH:
	    		if (resultCode == Activity.RESULT_OK) {
	    			// Authentication was successful, do what you need to do in your app
	    			
	    			//check if this is the first time user is using this app; set up appropriate stacks
	    				
	    			// Goto screen showing my messages
	    			startActivity(new Intent(getApplicationContext(), ViewMessagesActivity.class));
	    		}
	    		else {
	    			// Goto screen prompting user to log in?
	    			//startActivity(new Intent(getApplicationContext(), LoginActivity.class));
	    		}
	    		break;
		}
	}
}
