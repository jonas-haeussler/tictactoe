package com.game.tictactoe.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.game.tictactoe.TicTacToeGame;

public class AssetLoader {
    private TicTacToeGame game;

    public AssetLoader(TicTacToeGame game){
        this.game = game;
    }

    public void loadAll(){
        this.game = game;
        game.background = new Texture("PNG/squaredPaper.png");

    }

    public void dispose(){
        game.background.dispose();
    }
}
