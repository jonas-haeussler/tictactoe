package com.game.tictactoe;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication implements Runnable {
	private TicTacToeGame game;
	private BlueToothConnection blueToothConnection;
	private final int REQUEST_ENABLE_BT = 0;
	Object threadCommunicator1, threadCommunicator2;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		threadCommunicator1 = new Object();
		threadCommunicator2 = new Object();
		game = new TicTacToeGame(threadCommunicator1, threadCommunicator2);
		initialize(game, config);
		blueToothConnection = new BlueToothConnection(game,this, REQUEST_ENABLE_BT, threadCommunicator1, threadCommunicator2);
		new Thread(this).start();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == REQUEST_ENABLE_BT) {
			if(resultCode == RESULT_OK){
				synchronized (game) {
					game.btOn = true;
					synchronized (game){
						game.notifyAll();
					}
				}
			}
			else if(resultCode == RESULT_CANCELED){
				synchronized (game){
					game.btOn = false;
				}
			}
		}
	}
	public void setBT(boolean btMode){
		game.btOn = btMode;
	}

	@Override
	public void run() {
		while(true) {
			try {
				synchronized (game) {
					game.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			blueToothConnection.onBluetoothMode();
			game.setBTDevices(blueToothConnection.getDevices());
			blueToothConnection.cancel();
		}
	}
}
