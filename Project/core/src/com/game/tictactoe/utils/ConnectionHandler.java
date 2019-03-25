package com.game.tictactoe.utils;

public interface ConnectionHandler {
    public void startServer();
    public void startClient();
    public boolean enableConnection();
    public void cancel();
}
