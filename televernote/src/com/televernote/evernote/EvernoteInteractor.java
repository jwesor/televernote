package com.televernote.evernote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.evernote.client.android.AsyncNoteStoreClient;
import com.evernote.client.android.AsyncUserStoreClient;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.transport.TTransportException;

public class EvernoteInteractor {
	private static final String LOGTAG = "EvernoteInteractor";
	
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
	//called every time app is started; checks that necessary files are in place
	//if not, makes such necessary files
	public static void initializeIfNeeded(Context session) {
		final EvernoteSession mEvernoteSession = getSession(session);
		try {
			AsyncNoteStoreClient store = mEvernoteSession.getClientFactory().createNoteStoreClient();
			store.listNotebooks(new OnClientCallback<List<Notebook>>() {
				@Override
				public void onSuccess(final List<Notebook> notebooks) {
					List<String> namesList = new ArrayList<String>(notebooks.size());
					boolean hasInitial = false;
			        for (Notebook notebook : notebooks) {
			        	namesList.add(notebook.getName());
			        	//lol@hardcoded in strings
			        	if (notebook.getName().equals("Televernote Info")) {
			        		hasInitial = true;
			        	}
			        }
			        if (!hasInitial) {
			        	//make new notes
			        	try {
							createNote(mEvernoteSession, "Televernote Info", "Televernote!");
						} catch (TTransportException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
				}
				@Override
				public void onException(Exception e) {
					Log.e(LOGTAG, "Error retrieving notebooks", e);
				}
			});
			
			//AsyncUserStoreClient store1= getSession(session).getClientFactory().createUserStoreClient();
			
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void createNotebook(Context context) throws TTransportException {
		EvernoteSession mEvernoteSession = getSession(context);
		
		Notebook notebook = new Notebook();
		notebook.setGuid(notebook.getGuid());
		
		mEvernoteSession.getClientFactory().createNoteStoreClient().createNotebook(notebook,  new OnClientCallback<Notebook>() {
			@Override
			public void onSuccess(Notebook data) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void onException(Exception exception) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	public static void createNote(Context context, String title, String content) throws TTransportException {
		EvernoteSession mEvernoteSession = getSession(context);
		createNote(mEvernoteSession, title, content);
	}
	public static void createNote(EvernoteSession mEvernoteSession, String title, String content) throws TTransportException {
		if (mEvernoteSession.isLoggedIn()) {
			Note note = new Note();
			note.setTitle(title);
			note.setContent(EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX);
			mEvernoteSession.getClientFactory().createNoteStoreClient().createNote(note, new OnClientCallback<Note>() {
				@Override
				public void onSuccess(final Note data) {
					//Toast.makeText(getApplicationContext(), data.getTitle() + " has been created", Toast.LENGTH_LONG).show();
				}	
				@Override
				public void onException(Exception exception) {
					Log.e(LOGTAG, "Error creating note", exception);
				}
			});
		}
	}
}
