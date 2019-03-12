package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.game.tictactoe.TicTacToeGame;

public class HelpScreen extends GameScreen {
    public HelpScreen(TicTacToeGame game) {
        super(game, (byte) 1, (byte) 2, (byte) 0);
        camera.setToOrtho(false, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight(), 0);
    }
    @Override
    public void render(float delta){
        super.render(delta);
    }
}
