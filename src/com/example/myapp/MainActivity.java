package com.example.myapp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myapp.MESSAGE";
	public final static int MESSAGE_RECEIVED = 0;
	
	private Socket socket = null;
	private DataOutputStream out = null;
	private ChatClientRunnable client = null;
	private Thread clientThread = null;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message inputMessage) {
			switch(inputMessage.what) {
			case MESSAGE_RECEIVED:
				displayMessage((String) inputMessage.obj);
				break;
			default:
				super.handleMessage(inputMessage);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new ConnectToServer().execute("192.168.0.3");
	}
	
	private void displayMessage(String msg) {
		LinearLayout messageDisplay = (LinearLayout) findViewById(R.id.messageDisplay);
		
		TextView messageTextView = new TextView(this);
		messageTextView.setText(msg);
		
		LinearLayout.LayoutParams messageTextViewLayout = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		messageDisplay.addView(messageTextView, messageTextViewLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** Called when the user clicks the Send button 
	 * @throws IOException */
	public void sendMessage(View view) throws IOException {
	    // Do something in response to button
		String message = ((EditText) findViewById(R.id.editMessage)).getText().toString();
		if(out != null) {
			out.writeUTF(message);
			out.flush();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		// close socket and output stream
		try {
			if(socket != null) {
				socket.close();
			}
			if(out != null) {
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client.close();
		if(clientThread != null) {
			Thread dummy = clientThread;
			clientThread = null;
			dummy.interrupt();
		}
		getMainLooper().quit();
	}
	
	private class ConnectToServer extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			try {
				socket = new Socket(params[0], 7777);
				// set up socket and output stream
				out = new DataOutputStream(socket.getOutputStream());
				client = new ChatClientRunnable(handler, socket);
				clientThread = new Thread(client);
				clientThread.start();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			Toast toast = Toast.makeText(getApplicationContext(), 
//					"Connected to server", Toast.LENGTH_SHORT);
//			toast.show();
			
			return null;
		}
		
	}

}
