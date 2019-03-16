package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.game.tictactoe.TicTacToeGame;

public class BTPlayableScreen extends PlayableScreen {
    public BTPlayableScreen(TicTacToeGame game, byte kiPlayer, byte kiLevel) {
        super(game, kiPlayer, kiLevel);
    }
    @Override
    public void render(float delta){
        super.render(delta);
        if(game.btOn && !game.btConnected && ki1 != null){
            dimRenderer.setProjectionMatrix(camera.combined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            dimRenderer.begin(ShapeRenderer.ShapeType.Filled);
            dimRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            dimRenderer.end();
            overLayStage1.draw();
            if(animation < 1) {
                animation += delta;
            }
            else{
                animation = 0;
            }
            if(Gdx.input.getInputProcessor().equals(stage)){
                Gdx.input.setInputProcessor(overLayStage1);
            }
        }
    }
}
