package com.televernote.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.evernote.edam.type.Note;
import com.televernote.R;
import com.televernote.evernote.EvernoteInteractor;
import com.televernote.telegraph.ReceiverActivity;
import com.televernote.telegraph.TransmitterActivity;

public class ViewMessagesActivity extends Activity {

	private ListView messagesView;

	private Button newMessage;

	private List<String> messageDatum;
	private List<String> messageTitles;
	private ArrayAdapter<String> messageAdapter;

	public static final int VIEW_MESSAGES = 23;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void onResume() {
		super.onResume();

		messageTitles = new ArrayList<String>();
		messageDatum = new ArrayList<String>();

		setContentView(R.layout.view_messages);
		messagesView = (ListView) findViewById(R.id.message_list);
		messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				//read message at int position
				System.out.println("Hit");
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

		EditText edit = (EditText) findViewById(R.id.edit_recipient);
		edit.setText("eric5@berkeley.edu");
	}

	public void clickedNewMessage(View view) {
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
		EditText edit = (EditText) findViewById(R.id.edit_recipient);
		Intent intent = new Intent(this, TransmitterActivity.class);
		intent.putExtra(TransmitterActivity.RECIPIENT_KEY, edit.getText().toString());
		startActivity(intent);
	}

	public void clickedLogout(View view) {
		EvernoteInteractor.logOut(this);
		finish();
	}

	public void receiveNotes(List<Note> notes) {
		System.out.println("Received notes");
		int index = 0;
		for(Note n: notes) {
			String title = n.getTitle();
			messageTitles.add(title);
			//messageAdapter.add(title);
			messageAdapter.notifyDataSetChanged();
			EvernoteInteractor.unpackageNodeData(this, n, this, index);
			index++;
		}
	}

	public void startViewData(String data) {
		Intent intent = new Intent(this, ReceiverActivity.class);
		intent.putExtra(ReceiverActivity.TIMESTAMPS_KEY, data);
		intent.putExtra(ReceiverActivity.SENDER_KEY, "eric5@berkeley.edu");
		startActivity(intent);
	}

	public void receiveData(String data, int index) {
		if (index >= messageDatum.size()) {
			messageDatum.add(data);
		}
		else {
			messageDatum.set(index, data);
		}
		System.out.println("Message received " + index);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
