package com.televernote.evernote;

import android.content.Context;

import com.evernote.client.android.EvernoteSession;

public class EvernoteInteractor {
	private static final String CONSUMER_KEY = "eric5";
	private static final String CONSUMER_SECRET = "c286186b14af5124";
	
	private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

	private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;

	// Current evernote session
	
	private static EvernoteSession getSession(Context session) {
		return EvernoteSession.getInstance(session, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_SERVICE, SUPPORT_APP_LINKED_NOTEBOOKS);
	}
	//methods to interact with evernote API
	public static boolean isLogged(Context session) {
		return getSession(session).isLoggedIn();
	}
	public static void authenticate() {
		
	}
}
