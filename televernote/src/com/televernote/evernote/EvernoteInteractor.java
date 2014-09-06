package com.televernote.evernote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.evernote.client.android.AsyncNoteStoreClient;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.SharedNotebook;
import com.evernote.edam.type.SharedNotebookPrivilegeLevel;
import com.evernote.thrift.transport.TTransportException;

public class EvernoteInteractor {
	private static final String LOGTAG = "EvernoteInteractor";
	
	private static final String CONSUMER_KEY = "eric5-5494";
	private static final String CONSUMER_SECRET = "2b514688c7e57a5d";

	private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;

	private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;

	private static List<Notebook> currentNotebooks;
	
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
					currentNotebooks = notebooks.subList(0, notebooks.size());
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
							//createNote(mEvernoteSession, "Televernote Info", "Televernote!");
			        		createNotebook(mEvernoteSession, "Televernote Info");
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
	//where user is the email of the user you wish to add
	public static void addUser(Context context, String user) {
		final String f_user = user;
		final EvernoteSession mEvernoteSession = getSession(context);
		for (Notebook notebook : currentNotebooks) {
        	if (notebook.getName().equals("user")) {
        		//already have this user added
        		return;
        	}
        }
		try {
			
			Notebook notebook = new Notebook();
			notebook.setGuid(notebook.getGuid());
			notebook.setName(user);
			notebook.setStack("Televernote");
			
			
			mEvernoteSession.getClientFactory().createNoteStoreClient().createNotebook(notebook,  new OnClientCallback<Notebook>() {
				@Override
				public void onSuccess(Notebook data) {
					// TODO Auto-generated method stub
					currentNotebooks.add(data);
					SharedNotebook shared = new SharedNotebook();
					shared.setNotebookGuid(data.getGuid());
					shared.setAllowPreview(true);
					shared.setPrivilege(SharedNotebookPrivilegeLevel.MODIFY_NOTEBOOK_PLUS_ACTIVITY);
					shared.setEmail(f_user);
					try {
						mEvernoteSession.getClientFactory().createNoteStoreClient().createSharedNotebook(shared,  new OnClientCallback<SharedNotebook>() {
							@Override
							public void onSuccess(SharedNotebook data) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onException(Exception exception) {
								// TODO Auto-generated method stub
								
							}
							
						
						});
					} catch (TTransportException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				@Override
				public void onException(Exception exception) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void createNotebook(Context context, String name) throws TTransportException {
		EvernoteSession mEvernoteSession = getSession(context);
		createNotebook(mEvernoteSession, name);
	}
	public static void createNotebook(EvernoteSession mEvernoteSession, String name) throws TTransportException {
		Notebook notebook = new Notebook();
		notebook.setGuid(notebook.getGuid());
		notebook.setName(name);
		notebook.setStack("Televernote");
		
		mEvernoteSession.getClientFactory().createNoteStoreClient().createNotebook(notebook,  new OnClientCallback<Notebook>() {
			@Override
			public void onSuccess(Notebook data) {
				// TODO Auto-generated method stub
				currentNotebooks.add(data);
			}
			@Override
			public void onException(Exception exception) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	public static void createNote(Context context, String title, String content, String notebookGuid) throws TTransportException {
		EvernoteSession mEvernoteSession = getSession(context);
		createNote(mEvernoteSession, title, content, notebookGuid);
	}
	public static void createNote(EvernoteSession mEvernoteSession, String title, String content, String notebookGuid) throws TTransportException {
		if (mEvernoteSession.isLoggedIn()) {
			Note note = new Note();
			note.setTitle(title);
			note.setNotebookGuid(notebookGuid);
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
