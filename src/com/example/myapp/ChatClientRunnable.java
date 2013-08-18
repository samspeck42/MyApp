package com.example.myapp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

public class ChatClientRunnable extends Thread {
	private Handler handler;
	private DataInputStream in;
	private boolean done = false;
	
	public ChatClientRunnable(Handler handler, Socket socket) {
		this.handler = handler;
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(!done) {
			String input = "";
			try {
				input = in.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message message = handler.obtainMessage(ChatActivity.MESSAGE_RECEIVED, input);
			message.sendToTarget();
		}
	}
	
	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopThread() {
		done = true;
	}

}
