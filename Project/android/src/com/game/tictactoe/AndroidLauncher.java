package com.game.tictactoe;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.game.tictactoe.utils.AdShower;
import com.google.android.gms.ads.*;

public class AndroidLauncher extends AndroidApplication implements AdShower {
	private TicTacToeGame game;
	private BlueToothConnection blueToothConnection;
	private final int REQUEST_ENABLE_BT = 0;
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
			}
		});
		game.adShower = this;


		blueToothConnection = new BlueToothConnection(game,this, REQUEST_ENABLE_BT);
		game.setConHandler(blueToothConnection);

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
	public void showBannerAd() {

	}
}
