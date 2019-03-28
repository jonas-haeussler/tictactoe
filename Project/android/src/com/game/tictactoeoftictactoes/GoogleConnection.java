package com.game.tictactoeoftictactoes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import com.game.tictactoe.R;
import com.game.tictactoeoftictactoes.objects.MapGrid;
import com.game.tictactoeoftictactoes.screens.OnlineSelectScreen;
import com.game.tictactoeoftictactoes.utils.GoogleConHandler;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.*;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationCallback;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashSet;
import java.util.List;

public class GoogleConnection implements GoogleConHandler {
    private AndroidLauncher launcher;
    protected RealTimeMultiplayerClient client;
    protected RoomUpdateCallback mRoomUpdateCallback;
    protected RoomStatusUpdateCallback mRoomStatusCallbackHandler;
    protected RoomConfig mJoinedRoomConfig;
    protected String mMyParticipantId;
    protected OnRealTimeMessageReceivedListener mMessageReceivedHandler;
    protected boolean mWaitingRoomFinishedFromCode = false;
    private RealTimeMultiplayerClient.ReliableMessageSentCallback handleMessageSentCallback;
    private HashSet<Integer> pendingMessageSet = new HashSet<>();

    protected byte random;
    private boolean connected;


    private TicTacToeGame game;

    protected Room mRoom;


    // are we already playing?
    boolean mPlaying = false;

    // at least 2 players required for our game
    final static int MIN_PLAYERS = 2;


    public GoogleConnection(final AndroidLauncher launcher, final TicTacToeGame game){
        this.launcher = launcher;
        this.game = game;
        if(GoogleSignIn.getLastSignedInAccount(launcher) != null) {
            client = Games.getRealTimeMultiplayerClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher));
        }
        mRoomUpdateCallback = new RoomUpdateCallback() {
            @Override
            public void onRoomCreated(int code, @Nullable Room room) {
                // Update UI and internal state based on room updates.
                if (code == GamesCallbackStatusCodes.OK && room != null) {
                    Log.d("TAG", "Room " + room.getRoomId() + " created.");
                    showWaitingRoom(room, 2);
                    mRoom = room;
                } else {
                    Log.w("TAG", "Error creating room: " + code);
                    // let screen go to sleep
                    launcher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                }
            }

            @Override
            public void onJoinedRoom(int code, @Nullable Room room) {
                // Update UI and internal state based on room updates.
                if (code == GamesCallbackStatusCodes.OK && room != null) {
                    showWaitingRoom(room, 2);
                    mRoom = room;
                    Log.d("TAG", "Room " + room.getRoomId() + " joined.");
                } else {
                    Log.w("TAG", "Error joining room: " + code);
                    // let screen go to sleep
                    launcher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                }
            }

            @Override
            public void onLeftRoom(int code, @NonNull String roomId) {
                Log.d("TAG", "Left room " + roomId);
            }

            @Override
            public void onRoomConnected(int code, @Nullable Room room) {
                if (code == GamesCallbackStatusCodes.OK && room != null) {
//                    showWaitingRoom(room, 2);
                    mRoom = room;
                    Log.d("TAG", "Room " + room.getRoomId() + " connected.");
                } else {
                    Log.w("TAG", "Error connecting to room: " + code);
                    // let screen go to sleep
                    launcher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                }
            }
        };

        final GoogleConnection googleConnection = this;

        mRoomStatusCallbackHandler = new RoomStatusUpdateCallback() {
            @Override
            public void onRoomConnecting(@Nullable Room room) {
                mRoom = room;
                Log.w("TAG", "Connecting to Room: " + room);
                // Update the UI status since we are in the process of connecting to a specific room.
            }

            @Override
            public void onRoomAutoMatching(@Nullable Room room) {
                mRoom = room;
                Log.w("TAG", "Automatching to Room: " + room);
                // Update the UI status since we are in the process of matching other players.
            }

            @Override
            public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
                mRoom = room;
                Log.w("TAG", "Invited Peers to Room: " + room);
                // Update the UI status since we are in the process of matching other players.
            }

            @Override
            public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
                // Peer declined invitation, see if game should be canceled
                Log.w("TAG", "Peer declined connecting to Room: " + room);
                mRoom = room;
                if (!mPlaying && shouldCancelGame(room)) {
                    Games.getRealTimeMultiplayerClient(launcher,
                            GoogleSignIn.getLastSignedInAccount(launcher))
                            .leave(mJoinedRoomConfig, room.getRoomId());
                    launcher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }

            @Override
            public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
                Log.w("TAG", "Peer joined to Room: " + room);
                mRoom = room;
                // Update UI status indicating new players have joined!
            }

            @Override
            public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
                Log.w("TAG", "Peer left the Room: " + room);
                mRoom = room;
                // Peer left, see if game should be canceled.
                if (!mPlaying && shouldCancelGame(room)) {
                    Games.getRealTimeMultiplayerClient(launcher,
                            GoogleSignIn.getLastSignedInAccount(launcher))
                            .leave(mJoinedRoomConfig, room.getRoomId());
                    launcher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }

            @Override
            public void onConnectedToRoom(@Nullable Room room) {
                // Connected to room, record the room Id.
                Log.w("TAG", "Connected to Room: " + room);
                mRoom = room;
                Games.getPlayersClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .getCurrentPlayerId().addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String playerId) {
                        mMyParticipantId = mRoom.getParticipantId(playerId);
                    }
                });
            }

            @Override
            public void onDisconnectedFromRoom(@Nullable Room room) {
                // This usually happens due to a network error, leave the game.
                Log.w("TAG", "Disconnected from Room: " + room);
                connected = false;

                Games.getRealTimeMultiplayerClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .leave(mJoinedRoomConfig, room.getRoomId());
                launcher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                // show error message and return to main screen
                mRoom = null;
                mJoinedRoomConfig = null;
            }

            @Override
            public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
                Log.w("TAG", "Peers are connected in Room: " + room);
                if (mPlaying) {
                    // add new player to an ongoing game
                } else if (shouldStartGame(room)) {
                    mRoom = room;
                    random = (byte) ((int)(Math.random() * 2) + 1);
                    sendToAllReliably(new byte[]{googleConnection.random});
                    connected = true;
                }
            }

            @Override
            public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
                Log.w("TAG", "Peers are disconnected in Room: " + room);
                if (mPlaying) {
                    // do game-specific handling of this -- remove player's avatar
                    // from the screen, etc. If not enough players are left for
                    // the game to go on, end the game and leave the room.
                } else if (shouldCancelGame(room)) {
                    // cancel the game
                    Games.getRealTimeMultiplayerClient(launcher,
                            GoogleSignIn.getLastSignedInAccount(launcher))
                            .leave(mJoinedRoomConfig, room.getRoomId());
                    launcher.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }

            @Override
            public void onP2PConnected(@NonNull String participantId) {

                // Update status due to new peer to peer connection.
            }

            @Override
            public void onP2PDisconnected(@NonNull String participantId) {
                // Update status due to  peer to peer connection being disconnected.
            }
        };
        mMessageReceivedHandler =
                new OnRealTimeMessageReceivedListener() {
                    @Override
                    public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
                        // Handle messages received here.
                        Log.d("TAG", "A Message has been received");
                        byte[] message = realTimeMessage.getMessageData();
                        if (game.player == 0) {
                            if (message[0] != random) {
                                game.player = message[0];
                            } else {
                                random = (byte) ((int) (Math.random() * 2) + 1);
                                googleConnection.sendToAllReliably(new byte[]{random});
                            }
                        }
                        else {
                            if (game.getMapGrids() != null) {
                                MapGrid[][] mapGrids = game.getMapGrids();
                                for (int i = 0; i < mapGrids.length; i++) {
                                    for (int j = 0; j < mapGrids.length; j++) {
                                        for (int k = 0; k < mapGrids.length; k++) {
                                            for (int l = 0; l < mapGrids.length; l++) {
                                                if (message[i * mapGrids.length * mapGrids.length * mapGrids.length + j * mapGrids.length * mapGrids.length + k * mapGrids.length + l]
                                                        != mapGrids[i][j].getArray()[k][l]) {
                                                    game.addPlayerMove(i, j, k, l);
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                };


        handleMessageSentCallback =
                new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
                    @Override
                    public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {
                        // handle the message being sent.
                        synchronized (this) {
                            Log.d("TAG", "Message sent successfully");
                            pendingMessageSet.remove(tokenId);
                        }
                    }
                };


    }
    private synchronized void recordMessageToken(int tokenId) {
        pendingMessageSet.add(tokenId);
    }


    // Returns whether the room is in a state where the game should be canceled.
    boolean shouldCancelGame(Room room) {
        // TODO: Your game-specific cancellation logic here. For example, you might decide to
        // cancel the game if enough people have declined the invitation or left the room.
        // You can check a participant's status with Participant.getStatus().
        // (Also, your UI should have a Cancel button that cancels the game too)
        return false;
    }

    // returns whether there are enough players to start the game
    boolean shouldStartGame(Room room) {
        int connectedPlayers = 0;
        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) {
                ++connectedPlayers;
            }
        }
        return connectedPlayers == MIN_PLAYERS;
    }

    @Override
    public void showInvitationInbox() {
        Games.getInvitationsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                .getInvitationInboxIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        launcher.startActivityForResult(intent, launcher.RC_INVITATION_INBOX);
                    }
                });
    }

    private void showWaitingRoom(Room room, int maxPlayersToStartGame) {
        Games.getRealTimeMultiplayerClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                .getWaitingRoomIntent(room, maxPlayersToStartGame)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        launcher.startActivityForResult(intent, launcher.RC_WAITING_ROOM);
                    }
                });
    }

    private void checkForInvitation() {
        if(GoogleSignIn.getLastSignedInAccount(launcher) != null) {
            Games.getGamesClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                    .getActivationHint()
                    .addOnSuccessListener(
                            new OnSuccessListener<Bundle>() {
                                @Override
                                public void onSuccess(Bundle bundle) {
                                    if (bundle != null) {
                                        Invitation invitation = bundle.getParcelable(Multiplayer.EXTRA_INVITATION);
                                        if (invitation != null) {
                                            RoomConfig.Builder builder = RoomConfig.builder(mRoomUpdateCallback)
                                                    .setInvitationIdToAccept(invitation.getInvitationId());
                                            mJoinedRoomConfig = builder.setOnMessageReceivedListener(mMessageReceivedHandler)
                                                    .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
                                                    .build();
                                            Games.getRealTimeMultiplayerClient(launcher,
                                                    GoogleSignIn.getLastSignedInAccount(launcher))
                                                    .join(mJoinedRoomConfig);
                                            game.invitation = true;
                                            // prevent screen from sleeping during handshake
                                            launcher.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                                        }
                                    }
                                }
                            }
                    );
        }

    }

    @Override
    public void logIn() {
        signInSilently();
        checkForInvitation();
    }

    @Override
    public void showScoreIncreased(){
        launcher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(launcher).setMessage("Congratulation! Your Score has increased by 20!")
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        });
    }


    @Override
    public void leaveRoom() {
        client = Games.getRealTimeMultiplayerClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher));
        if(mRoom != null && client != null && mJoinedRoomConfig != null){
            client.leave(mJoinedRoomConfig, mRoom.getRoomId());
        }
    }

    private void signInSilently() {
        GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(launcher);
        if (GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            GoogleSignInAccount signedInAccount = account;
        } else {
            // Haven't been signed-in before. Try the silent sign-in first.
            GoogleSignInClient signInClient = GoogleSignIn.getClient(launcher, signInOptions);
            signInClient
                    .silentSignIn()
                    .addOnCompleteListener(
                            launcher,
                            new OnCompleteListener<GoogleSignInAccount>() {
                                @Override
                                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                    if (task.isSuccessful()) {
                                        // The signed in account is stored in the task's result.
                                        GoogleSignInAccount signedInAccount = task.getResult();
                                        new AlertDialog.Builder(launcher).setMessage("Connection was successful")
                                                .setNeutralButton(android.R.string.ok, null).show();
                                    } else {
                                        startSignInIntent();
                                    }
                                }
                            });
        }
    }

    protected void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(launcher,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        launcher.startActivityForResult(intent, launcher.RC_SIGN_IN);
    }

    @Override
    public boolean loggedIn(){
        GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(launcher);
        return GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray());
    }



    @Override
    public void startQuickGame() {
        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);

        // build the room config:
        RoomConfig roomConfig =
                RoomConfig.builder(mRoomUpdateCallback)
                        .setOnMessageReceivedListener(mMessageReceivedHandler)
                        .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
                        .setAutoMatchCriteria(autoMatchCriteria)
                        .build();

        // prevent screen from sleeping during handshake
        launcher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                launcher.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });

        // Save the roomConfig so we can use it if we call leave().
        mJoinedRoomConfig = roomConfig;

        // create room:
        Games.getRealTimeMultiplayerClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher)).create(roomConfig);

    }
    @Override
    public void showConnectFirstStatement(){
        launcher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(launcher).setMessage("Please connect with Google first")
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        });
    }
    @Override
    public void showConnectionFailedStatement(){
        launcher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(launcher).setMessage("Connection failed! Make sure that your device has Internet access...")
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        });
    }

    @Override
    public void showLeaderBoard(){
        if(GoogleSignIn.getLastSignedInAccount(launcher) != null) {
            Games.getLeaderboardsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                    .getLeaderboardIntent(launcher.getString(R.string.leaderboard_id))
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            launcher.startActivityForResult(intent, launcher.RC_LEADERBOARD_UI);
                        }
                    });
        }
        else {
            launcher.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(launcher).setMessage("Please connect with Google first")
                            .setNeutralButton(android.R.string.ok, null).show();
                }
            });
        }

    }

    @Override
    public void addWinToScore(){
        if(GoogleSignIn.getLastSignedInAccount(launcher) != null) {

            final LeaderboardsClient mLeaderboardsClient = Games.getLeaderboardsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher));
            final String leaderboardId = launcher.getString(R.string.leaderboard_id);
            mLeaderboardsClient.loadCurrentPlayerLeaderboardScore(
                    leaderboardId,
                    LeaderboardVariant.TIME_SPAN_ALL_TIME,
                    LeaderboardVariant.COLLECTION_PUBLIC
            ).addOnSuccessListener(new OnSuccessListener<AnnotatedData<LeaderboardScore>>() {
                @Override
                public void onSuccess(AnnotatedData<LeaderboardScore> leaderboardScoreAnnotatedData) {
                    if (leaderboardScoreAnnotatedData.get() == null) {
                        Log.d("TAG", "Adding new Score");
                        mLeaderboardsClient.submitScore(leaderboardId, 1020);
                    }
                    else {
                        Log.d("TAG", "Increasing Score");
                        long currentscore = leaderboardScoreAnnotatedData.get().getRawScore();
                        mLeaderboardsClient.submitScore(leaderboardId, currentscore + 20);
                    }
                }
            });
        }
    }

//    @Override
//    public void addLoseToScore(){
//        if(GoogleSignIn.getLastSignedInAccount(launcher) != null) {
//            final LeaderboardsClient mLeaderboardsClient = Games.getLeaderboardsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher));
//            final String leaderboardId = launcher.getString(R.string.leaderboard_id);
//            mLeaderboardsClient.loadCurrentPlayerLeaderboardScore(
//                    leaderboardId,
//                    LeaderboardVariant.TIME_SPAN_ALL_TIME,
//                    LeaderboardVariant.COLLECTION_PUBLIC
//            ).addOnSuccessListener(new OnSuccessListener<AnnotatedData<LeaderboardScore>>() {
//                @Override
//                public void onSuccess(AnnotatedData<LeaderboardScore> leaderboardScoreAnnotatedData) {
//                    if (leaderboardScoreAnnotatedData.get() == null) {
//                        Log.d("TAG", "Adding new Score");
//                        mLeaderboardsClient.submitScore(leaderboardId, 980);
//                    }
//                    else {
//                        Log.d("TAG", "Decreasing Score");
//                        long currentscore = leaderboardScoreAnnotatedData.get().getRawScore();
//                        mLeaderboardsClient.submitScore(leaderboardId, currentscore - 20);
//                    }
//                }
//            });
//        }
//    }

    @Override
    public void showAchievements(){
        if(GoogleSignIn.getLastSignedInAccount(launcher) != null) {
            Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                    .getAchievementsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            launcher.startActivityForResult(intent, launcher.RC_ACHIEVEMENT_UI);
                        }
                    });
        }
        else {
            launcher.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(launcher).setMessage("Please connect with Google first")
                            .setNeutralButton(android.R.string.ok, null).show();
                }
            });
        }
    }

    @Override
    public void incrementAchievement(int id1, int id2){
        if(GoogleSignIn.getLastSignedInAccount(launcher) != null) {
            if (id1 == 1 || id2 == 1) {
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .increment(launcher.getString(R.string.achievement_1_id), 1);
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .unlock(launcher.getString(R.string.achievement_1_id));
            }
            if (id1 == 2 || id2 == 2) {
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .increment(launcher.getString(R.string.achievement_2_id), 1);
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .unlock(launcher.getString(R.string.achievement_2_id));
            }
            if (id1 == 3 || id2 == 3) {
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .increment(launcher.getString(R.string.achievement_3_id), 1);
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .unlock(launcher.getString(R.string.achievement_3_id));
            }
            if (id1 == 4 || id2 == 4) {
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .increment(launcher.getString(R.string.achievement_4_id), 1);
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .unlock(launcher.getString(R.string.achievement_4_id));
            }
            if (id1 == 5 || id2 == 5) {
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .increment(launcher.getString(R.string.achievement_5_id), 1);
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .unlock(launcher.getString(R.string.achievement_5_id));
            }
            if (id1 == 6 || id2 == 6) {
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .increment(launcher.getString(R.string.achievement_6_id), 1);
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .unlock(launcher.getString(R.string.achievement_6_id));
            }
            if (id1 == 7 || id2 == 7) {
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .increment(launcher.getString(R.string.achievement_7_id), 1);
                Games.getAchievementsClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .unlock(launcher.getString(R.string.achievement_7_id));
            }
        }
    }


    @Override
    public void invitePlayers() {
        // launch the player selection screen
        // minimum: 1 other player; maximum: 3 other players
        Games.getRealTimeMultiplayerClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                .getSelectOpponentsIntent(1, 1, true)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        launcher.startActivityForResult(intent, launcher.RC_SELECT_PLAYERS);
                    }
                });
    }

    @Override
    public void sendTurn(final MapGrid[][] mapGrids) {
        launcher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final byte[] message = new byte[mapGrids.length * mapGrids.length * mapGrids.length * mapGrids.length];
                for (int i = 0; i < mapGrids.length; i++) {
                 for (int j = 0; j < mapGrids.length; j++) {
                        for (int k = 0; k < mapGrids.length; k++) {
                            for (int l = 0; l < mapGrids.length; l++) {
                                message[i * mapGrids.length * mapGrids.length * mapGrids.length + j * mapGrids.length * mapGrids.length + k * mapGrids.length + l] = mapGrids[i][j].getArray()[k][l];
                                }
                            }
                        }
                    }

                sendToAllReliably(message);

            }
        });
    }

    void sendToAllReliably(byte[] message) {
        for (String participantId : mRoom.getParticipantIds()) {
            if (!participantId.equals(mMyParticipantId)) {
                Log.d("TAG", "A Message has been sent");
                Task<Integer> task = Games.
                        getRealTimeMultiplayerClient(launcher, GoogleSignIn.getLastSignedInAccount(launcher))
                        .sendReliableMessage(message, mRoom.getRoomId(), participantId,
                                handleMessageSentCallback).addOnCompleteListener(new OnCompleteListener<Integer>() {
                            @Override
                            public void onComplete(@NonNull Task<Integer> task) {
                                // Keep track of which messages are sent, if desired
                                recordMessageToken(task.getResult());
                            }
                        });
            }
        }
    }
    @Override
    public boolean isConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) launcher.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();    }
}
