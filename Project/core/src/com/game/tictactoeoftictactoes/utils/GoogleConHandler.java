package com.game.tictactoeoftictactoes.utils;

import com.game.tictactoeoftictactoes.objects.MapGrid;

public interface GoogleConHandler {
    public void logIn();
    public void logOut();
    public void leaveRoom();
    public boolean loggedIn();
    public void showLeaderBoard();
    public void addWinToScore();
    public void addLoseToScore();
    public void showAchievements();
    public void incrementAchievement(int id1, int id2);
    public void startQuickGame();
    public void showInvitationInbox();
    public void showConnectFirstStatement();
    public void showConnectionFailedStatement();
    public void invitePlayers();
    public void sendTurn(MapGrid[][] mapGrids);
}
