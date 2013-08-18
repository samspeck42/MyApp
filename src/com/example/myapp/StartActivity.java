package com.example.myapp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class StartActivity extends Activity {
	private final int PORT = 7777;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	public void connectToServer() {
		new ConnectToServer().execute("");
	}
	
	private void startChat(Socket socket) {
		// TODO Auto-generated method stub
		
	}
	
	private class ConnectToServer extends AsyncTask<String, Void, Socket> {

		@Override
		protected Socket doInBackground(String... params) {
			Socket socket = null;
			try {
				socket = new Socket(params[0], PORT);
			} catch (UnknownHostException e) {
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Unknown host", Toast.LENGTH_SHORT);
				toast.show();
			} catch (IOException e) {
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Unexpected exception", Toast.LENGTH_SHORT);
				toast.show();
			}
			return socket;
		}
		
		@Override
		protected void onPostExecute(Socket result) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Connected to server", Toast.LENGTH_SHORT);
			toast.show();
			startChat(result);
		}
		
	}
}
