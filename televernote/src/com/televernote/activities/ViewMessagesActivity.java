package com.televernote.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.evernote.edam.type.Note;
import com.evernote.thrift.transport.TTransportException;
import com.televernote.R;
import com.televernote.evernote.EvernoteInteractor;
import com.televernote.morse.Transcriber;

public class ViewMessagesActivity extends Activity {
	
	private ListView messagesView;
	
	private Button newMessage;
	
	private List<Transcriber> messages;
	private List<String> messageTitles;
	private ArrayAdapter<String> messageAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        messageTitles = new ArrayList<String>();
        messages = new ArrayList<Transcriber>();
        
        setContentView(R.layout.view_messages);
        messagesView = (ListView) findViewById(R.id.message_list);
        messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView parent, View view, int position, long id) {
        		//read message at int position
        		Transcriber msg = messages.get(position);
        		//open this message
        	}
		});
        messageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messageTitles);
        messagesView.setAdapter(messageAdapter);
        
        newMessage = (Button) findViewById(R.id.new_button);
        final Context me = this;
        newMessage.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
    			//startActivity(new Intent(getApplicationContext(), TelegraphActivity.class));
        		System.out.println("Test debug");
        		EvernoteInteractor.addUser(me, "jwesor@gmail.com");
        		try {
					EvernoteInteractor.createNote(me, "03/09/1994", "LOL", EvernoteInteractor.getNotebookGUIDForUser("jwesor@gmail.com"));
					EvernoteInteractor.createNote(me, "03/31/1994", "LOL", EvernoteInteractor.getNotebookGUIDForUser("jwesor@gmail.com"));
					EvernoteInteractor.createNote(me, "05/09/1994", "LOL", EvernoteInteractor.getNotebookGUIDForUser("jwesor@gmail.com"));
				} catch (TTransportException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });
        //logOff = (Button) findViewById(R.id.log_off);
    }
	public void addMessageToListView(Transcriber msg) {
		messages.add(msg);
		String title = msg.getTitle();
		messageTitles.add(title);
		messageAdapter.add(title);
	}
	public void receiveNotes(List<Note> notes) {
		
	}
}
