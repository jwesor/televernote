package com.televernote.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.televernote.morse.Decoder;
import com.televernote.morse.Transcriber;
import com.televernote.telegraph.ReceiverActivity;
import com.televernote.telegraph.TelegraphActivity;
import com.televernote.telegraph.TransmitterActivity;

public class ViewMessagesActivity extends Activity {
	
	private ListView messagesView;
	
	private Button newMessage;
	
	private List<String> messageDatum;
	private List<String> messageTitles;
	private ArrayAdapter<String> messageAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        messageTitles = new ArrayList<String>();
        messageDatum = new ArrayList<String>();
        
        setContentView(R.layout.view_messages);
        messagesView = (ListView) findViewById(R.id.message_list);
        messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView parent, View view, int position, long id) {
        		//read message at int position
        		if (position < messageDatum.size()) {
        			String data = messageDatum.get(position);
        			startViewData(data);
        		}
        		//open this message
        	}
		});
        messageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messageTitles);
        messagesView.setAdapter(messageAdapter);
        
        //start fetching messages
        EvernoteInteractor.getAllMessages(this, this);
        
        newMessage = (Button) findViewById(R.id.new_button);
        final Context me = this;
        newMessage.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
    			//startActivity(new Intent(getApplicationContext(), TelegraphActivity.class));
        		System.out.println("Test debug");
        		
        		//EvernoteInteractor.addUser(me, "jwesor@gmail.com");
        		/*try {
        			long d = 75;
        			Transcriber t = new Transcriber();
        			Decoder de = new Decoder();
        			t.setDecoder(de);
        			t.tap(d*0, d*1);
        					t.tap(	    d*2, d*5);

        							t.tap(	    d*8, d*11);
        									t.tap(	    d*12, d*13);
        											t.tap(	    d*14, d*15);
        													t.tap(	    d*16, d*17);
        			
        			EvernoteInteractor.createNote(me, t.getTitle(), de.getMorseBuffer(), t.getTimestampData(), "jwesor@gmail.com");
				} catch (TTransportException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
        		Intent intent = new Intent(me, TransmitterActivity.class);
        		intent.putExtra(TransmitterActivity.RECIPIENT_KEY, "jwesor@gmail.com");
        		startActivity(intent);
        	}
        });
        //logOff = (Button) findViewById(R.id.log_off);
    }
	public void receiveNotes(List<Note> notes) {
		int index = 0;
		for(Note n: notes) {
			String title = n.getTitle();
			messageTitles.add(title);
			messageAdapter.add(title);
			EvernoteInteractor.unpackageNodeData(this, n, messageDatum, index);
			index++;
		}
	}
	public void startViewData(String data) {
		Intent intent = new Intent(this, ReceiverActivity.class);
		intent.putExtra(ReceiverActivity.TIMESTAMPS_KEY, data);
		startActivity(intent);
	}
}
