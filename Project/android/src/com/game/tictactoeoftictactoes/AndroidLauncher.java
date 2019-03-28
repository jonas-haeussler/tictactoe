package com.game.tictactoeoftictactoes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.game.tictactoe.R;
import com.game.tictactoeoftictactoes.screens.MainMenuScreen;
import com.game.tictactoeoftictactoes.screens.OnlineSelectScreen;
import com.google.android.gms.ads.*;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.games.*;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;


import java.util.ArrayList;

public class AndroidLauncher extends AndroidApplication implements com.game.tictactoeoftictactoes.utils.AdShower {
	private TicTacToeGame game;
	private BlueToothConnection blueToothConnection;
	private GoogleConnection googleConnection;
	private final int REQUEST_ENABLE_BT = 0;
	protected final int RC_SIGN_IN = 9001;
	protected static final int RC_LEADERBOARD_UI = 9004;
	protected static final int RC_ACHIEVEMENT_UI = 9003;
	protected static final int RC_SELECT_PLAYERS = 9006;
	protected static final int RC_INVITATION_INBOX = 9008;
	protected static final int RC_WAITING_ROOM = 9007;

	private boolean doubleBackToExitPressedOnce;
	private InterstitialAd mInterstitialAd;
	private AdView mAdView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		game = new TicTacToeGame();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		initialize(game, config);
		View gameView = initializeForView(game, config);
		MobileAds.initialize(this, "ca-app-pub-2027760715189652~4834217373");
		RelativeLayout layout = new RelativeLayout(this);

		mAdView = new AdView(this);
		mAdView.setAdSize(AdSize.SMART_BANNER);
		mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");


		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		layout.addView(gameView);

		RelativeLayout.LayoutParams adParams =
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		layout.addView(mAdView, adParams);
		setContentView(layout);

		mAdView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// Code to be executed when an ad finishes loading.
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				// Code to be executed when an ad request fails.
			}

			@Override
			public void onAdOpened() {
				// Code to be executed when an ad opens an overlay that
				// covers the screen.
			}

			@Override
			public void onAdLeftApplication() {
				// Code to be executed when the user has left the app.
			}

			@Override
			public void onAdClosed() {
				// Code to be executed when the user is about to return
				// to the app after tapping on an ad.
			}
		});


		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
		mInterstitialAd.loadAd(new AdRequest.Builder().build());
		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// Code to be executed when an ad finishes loading.
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				// Code to be executed when an ad request fails.
				mInterstitialAd.loadAd(new AdRequest.Builder().build());
			}

			@Override
			public void onAdOpened() {
				// Code to be executed when the ad is displayed.
			}

			@Override
			public void onAdLeftApplication() {
				// Code to be executed when the user has left the app.
			}

			@Override
			public void onAdClosed() {
				// Load the next interstitial.
				mInterstitialAd.loadAd(new AdRequest.Builder().build());
				if(!game.gameMusic.isPlaying()){
					if(!game.muteSound) {
						game.gameMusic.play();
					}
				}
			}
		});
		game.adShower = this;


		blueToothConnection = new BlueToothConnection(game,this, REQUEST_ENABLE_BT);
		googleConnection = new GoogleConnection(this, game);
		game.setConHandler(blueToothConnection);
		game.setGoogleConHandler(googleConnection);



	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == REQUEST_ENABLE_BT) {
			if(resultCode == RESULT_OK){
				synchronized (game) {
					game.btOn = true;
				}
			}
			else if(resultCode == RESULT_CANCELED){
				synchronized (game){
					game.btOn = false;
				}
			}
		}

		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				// The signed in account is stored in the result.
				GoogleSignInAccount signedInAccount = result.getSignInAccount();
				if(game.getScreen() instanceof MainMenuScreen){
					((MainMenuScreen) game.getScreen()).initStage();
				}
			} else {
				String message = result.getStatus().getStatusMessage();
				if (message == null || message.isEmpty()) {
					message = getString(R.string.con_failed_message);
				}
				new AlertDialog.Builder(this).setMessage(message)
						.setNeutralButton(android.R.string.ok, null).show();
			}
		}
		if(requestCode == RC_ACHIEVEMENT_UI || requestCode == RC_LEADERBOARD_UI){
			if(!googleConnection.loggedIn()){
				if(game.getScreen() instanceof MainMenuScreen){
					((MainMenuScreen) game.getScreen()).initStage();
				}
			}
		}
		if (requestCode == RC_SELECT_PLAYERS) {
			if (resultCode != Activity.RESULT_OK) {
				// Canceled or some other error.
				if(game.getScreen() instanceof OnlineSelectScreen){
					game.player = 0;
					((OnlineSelectScreen) game.getScreen()).setOverLaymode(false);
				}
				return;
			}

			// Get the invitee list.
			final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

			// Get Automatch criteria.
			int minAutoPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
			int maxAutoPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

			// Create the room configuration.
			RoomConfig.Builder roomBuilder = RoomConfig.builder(googleConnection.mRoomUpdateCallback)
					.setOnMessageReceivedListener(googleConnection.mMessageReceivedHandler)
					.setRoomStatusUpdateCallback(googleConnection.mRoomStatusCallbackHandler)
					.addPlayersToInvite(invitees);
			if (minAutoPlayers > 0) {
				roomBuilder.setAutoMatchCriteria(
						RoomConfig.createAutoMatchCriteria(minAutoPlayers, maxAutoPlayers, 0));
			}

			// Save the roomConfig so we can use it if we call leave().
			googleConnection.mJoinedRoomConfig = roomBuilder.build();
			Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
					.create(googleConnection.mJoinedRoomConfig);
		}

		if (requestCode == RC_INVITATION_INBOX) {

			if (resultCode != Activity.RESULT_OK) {
				if(game.getScreen() instanceof OnlineSelectScreen) {
					game.player = 0;
					((OnlineSelectScreen) game.getScreen()).setOverLaymode(false);
				}
				// Canceled or some error.
				return;
			}
			Invitation invitation = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);
			if (invitation != null) {
				RoomConfig.Builder builder = RoomConfig.builder(googleConnection.mRoomUpdateCallback)
						.setOnMessageReceivedListener(googleConnection.mMessageReceivedHandler)
						.setRoomStatusUpdateCallback(googleConnection.mRoomStatusCallbackHandler)
						.setInvitationIdToAccept(invitation.getInvitationId());
				googleConnection.mJoinedRoomConfig = builder.build();
				Games.getRealTimeMultiplayerClient(this,
						GoogleSignIn.getLastSignedInAccount(this))
						.join(googleConnection.mJoinedRoomConfig);
				// prevent screen from sleeping during handshake
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		}


		if (requestCode == RC_WAITING_ROOM) {

			// Look for finishing the waiting room from code, for example if a
			// "start game" message is received.  In this case, ignore the result.
			if (googleConnection.mWaitingRoomFinishedFromCode) {
				return;
			}

			if (resultCode == Activity.RESULT_OK) {
				Log.d("TAG", "Waiting Room returned OK");

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Waiting room was dismissed with the back button. The meaning of this
				// action is up to the game. You may choose to leave the room and cancel the
				// match, or do something else like minimize the waiting room and
				// continue to connect in the background.

				// in this example, we take the simple approach and just leave the room:
				if(game.getScreen() instanceof OnlineSelectScreen){
					game.player = 0;
					((OnlineSelectScreen) game.getScreen()).setOverLaymode(false);
				}
				Games.getRealTimeMultiplayerClient(this,
						GoogleSignIn.getLastSignedInAccount(this))
						.leave(googleConnection.mJoinedRoomConfig, googleConnection.mRoom.getRoomId());
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			} else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
				Log.d("TAG", "Waiting Room returned LEFT ROOM");
				// player wants to leave the room.
				Games.getRealTimeMultiplayerClient(this,
						GoogleSignIn.getLastSignedInAccount(this))
						.leave(googleConnection.mJoinedRoomConfig, googleConnection.mRoom.getRoomId());
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		}



		if(game.gameMusic != null && !game.gameMusic.isPlaying() && !game.muteSound){
			game.gameMusic.play();
		}

	}
	public void setBT(boolean btMode){
		game.btOn = btMode;
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce=false;
			}
		}, 2000);
	}

	@Override
	public void showInterstitialAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mInterstitialAd.isLoaded()) {
					mInterstitialAd.show();
				} else {
					Log.d("TAG", "The interstitial wasn't loaded yet.");
				}
			}
		});

	}
	@Override
	protected void onResume() {
		super.onResume();
		if(googleConnection.loggedIn()) {
			googleConnection.logIn();
		}
	}


}
