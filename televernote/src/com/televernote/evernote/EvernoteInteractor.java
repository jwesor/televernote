package com.televernote.evernote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.evernote.client.android.AsyncNoteStoreClient;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.InvalidAuthenticationException;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.SharedNotebook;
import com.evernote.edam.type.SharedNotebookPrivilegeLevel;
import com.evernote.thrift.TException;
import com.evernote.thrift.transport.TTransportException;
import com.televernote.activities.ViewMessagesActivity;

public class EvernoteInteractor {

	private static final String PREFIXER = "Televernote: ";

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
	public static void logOut(Context ctx) {
		try {
			getSession(ctx).logOut(ctx);
		} catch (InvalidAuthenticationException e) {
			e.printStackTrace();
		}
	}

	//called every time app is started; check {s that necessary files are in place
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
			if (notebook.getName().equals(PREFIXER+"user")) {
				//already have this user added
				return;
			}
		}
		try {

			Notebook notebook = new Notebook();
			notebook.setGuid(notebook.getGuid());
			notebook.setName(PREFIXER+user);
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
	public static void createNote(Context context, String title, String content, String data, String recipient) throws TTransportException {
		EvernoteSession mEvernoteSession = getSession(context);
		createNote(mEvernoteSession, title, content, data, recipient);
	}
	public static void createNote(final EvernoteSession mEvernoteSession, String title, String content, final String timeData, String recipient) throws TTransportException {
		if (mEvernoteSession.isLoggedIn()) {
			List<String> tags = new ArrayList<String>();
			tags.add("televernote");

			Note note = new Note();
			note.setTitle(title);
			/*if (content.length() >= 5) {
				note.setTitle(content.substring(0, 4));
			}
			else {
				note.setTitle(content);
			}*/
			String nbGuid = getNotebookGUIDForUser(recipient);
			note.setNotebookGuid(nbGuid);
			//note.getAttributes().setCreatorId(userId);
			//note.setTagGuids(tags);
			
			NoteAttributes attr;
			int userId = mEvernoteSession.getAuthenticationResult().getUserId();
			if (note.getAttributes() == null) {
				attr = new NoteAttributes();
			}
			else {
				attr = note.getAttributes();	
			}
			attr.setCreatorId(userId);
			note.setAttributes(attr);

			note.setTagNames(tags);
			note.setContent(EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX);
			mEvernoteSession.getClientFactory().createNoteStoreClient().createNote(note, new OnClientCallback<Note>() {
				@Override
				public void onSuccess(final Note data) {
					//Toast.makeText(getApplicationContext(), data.getTitle() + " has been created", Toast.LENGTH_LONG).show();
					//note.setCreated(userId);
					try {
						mEvernoteSession.getClientFactory().createNoteStoreClient().setNoteApplicationDataEntry(data.getGuid(), "timestamp", timeData, new OnClientCallback<Integer>() {
							@Override
							public void onSuccess(Integer data) {
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
					Log.e(LOGTAG, "Error creating note", exception);
				}
			});
		}
	}
	public static String getNotebookGUIDForUser(String user) {
		String name;
		for (Notebook notebook: currentNotebooks) {
			name = notebook.getName();
			if (name.startsWith(PREFIXER)) {
				if (name.equals(PREFIXER + user)) {
					//this is the notebook we want
					return notebook.getGuid();
				}
			}
		}
		return "";
	}
	public static boolean isNotebookLinked(String guid) {
		String name;
		for (Notebook notebook: currentNotebooks) {
			if (notebook.getGuid().equals(guid)) {
				return !notebook.isSetSharedNotebookIds();
			}
		}
		return true;
	}
	public static void getAllMessages(Context context, final ViewMessagesActivity sender) {
		List<String> tags = new ArrayList<String>();
		tags.add("televernote");

		final EvernoteSession mEvernoteSession = getSession(context);
		/*String name;
		for (Notebook notebook: currentNotebooks) {
			name = notebook.getName();
			if (name.startsWith(PREFIXER)) {

			}
		}*/

		try {
			
			AsyncNoteStoreClient store = mEvernoteSession.getClientFactory().createNoteStoreClient();
			store.listLinkedNotebooks(new OnClientCallback<List<LinkedNotebook>>() {
				@Override
				public void onSuccess(List<LinkedNotebook> data) {
					// TODO Auto-generated method stub
					final List<Note> retNotes = new ArrayList<Note>();
					int index = 0;
					for (LinkedNotebook linked: data) {
						index++;
						final boolean end = (index == data.size() - 1);
						
						try {
							mEvernoteSession.getClientFactory().createLinkedNoteStoreClient(linked);
						} catch (EDAMUserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (EDAMSystemException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (EDAMNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						NoteFilter filter = new NoteFilter();
						filter.setOrder(NoteSortOrder.UPDATED.getValue());
						//filter.setTagGuids(tags);
						filter.setWords("televernote");
						filter.setAscending(true);
						try {
							mEvernoteSession.getClientFactory().createNoteStoreClient().findNotes(filter, 0, 50, new OnClientCallback<NoteList> () {
								@Override
								public void onSuccess(NoteList data) {
									// TODO Auto-generated method stub
									List<Note> notes = data.getNotes();
									int userId = mEvernoteSession.getAuthenticationResult().getUserId();
									System.out.println("My User ID: "+userId);
									for (Note m: notes) {
										m.getAttributes().getSourceURL();
										System.out.println("Note ID: "+m.getAttributes().getCreatorId());
										//System.out.println("Note ID: "+m.getCreated());
										//if (m.getAttributes().getCreatorId() != userId) {
										//if (m.getCreated() != userId) {
										if (isNotebookLinked(m.getNotebookGuid())) {
											retNotes.add(m);
										}
									}
									if (end) {
										sender.receiveNotes(notes);
									}
									//sender.receiveNotes(notes);
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
					sender.receiveNotes(retNotes);
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
	public static void unpackageNodeData(Context context, Note note, final ViewMessagesActivity sender, final int index) {
		EvernoteSession mEvernoteSession = getSession(context);
		try {
			mEvernoteSession.getClientFactory().createNoteStoreClient().getNoteApplicationDataEntry(note.getGuid(),"timestamp", new OnClientCallback<String>() {
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					System.out.println("S");
					sender.receiveData(data, index);
				}

				@Override
				public void onException(Exception exception) {
					// TODO Auto-generated method stub
					System.out.println("Failed to get timestamp data");
				}

			});
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
