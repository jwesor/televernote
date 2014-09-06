package com.televernote.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.televernote.R;
import com.televernote.morse.Message;

public class ViewMessagesActivity extends Activity {
	
	private ListView messagesView;
	
	private Button newMessage;
	
	private List<Message> messages;
	private List<String> messageTitles;
	private ArrayAdapter<String> messageAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        messageTitles = new ArrayList<String>();
        messages = new ArrayList<Message>();
        
        setContentView(R.layout.view_messages);
        messagesView = (ListView) findViewById(R.id.message_list);
        messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView parent, View view, int position, long id) {
        		//read message at int position
        		Message msg = messages.get(position);
        		//open this message
        	}
		});
        messageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messageTitles);
        messagesView.setAdapter(messageAdapter);
        
        newMessage = (Button) findViewById(R.id.new_button);
        newMessage.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
    			//startActivity(new Intent(getApplicationContext(), TelegraphActivity.class));
        	}
        });
        //logOff = (Button) findViewById(R.id.log_off);
    }
	public void addMessageToListView(Message msg) {
		messages.add(msg);
		String title = msg.getTitle();
		messageTitles.add(title);
		messageAdapter.add(title);
	}
	public void fetchMessages() {
		
	}
}
