package com.game.tictactoeoftictactoes.utils;

public interface ConnectionHandler {
    public void startServer();
    public void startClient();
    public boolean enableConnection();
    public void cancel();
}
