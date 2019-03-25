package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.game.tictactoe.TicTacToeGame;

public class BTPlayableScreen extends PlayableScreen {
    private TextButton backFromWaitButton;
    private Table overLayRootTable;
    private Stage overLayStage;
    private Label connectionFailedLabel;

    public BTPlayableScreen(final TicTacToeGame game, byte kiPlayer, byte kiLevel) {
        super(game, kiPlayer, kiLevel);
        overLayStage = new Stage();
        overLayRootTable = new Table();
        backFromWaitButton = new TextButton("Back", game.comicSkin);
        backFromWaitButton.getLabel().setFontScale(1.5f);
        backFromWaitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                game.player = 0;
                game.conHandler.cancel();
                game.conHandler.enableConnection();
                game.btConnected = false;
                game.getScreen().dispose();
                game.setScreen(new BTDevicesScreen(game));
            }
        });
        Label.LabelStyle labelStyle3 = new Label.LabelStyle();
        labelStyle3.font = game.font1;
        labelStyle3.fontColor = Color.WHITE;
        connectionFailedLabel = new Label("Connection failed..."
                , labelStyle3);
        connectionFailedLabel.setAlignment(Align.center);

        overLayRootTable.add(connectionFailedLabel).width(Gdx.graphics.getWidth() / 1.5f);
        overLayRootTable.row();
        overLayRootTable.add(backFromWaitButton).width(Gdx.graphics.getWidth() / 1.2f).height(Gdx.graphics.getHeight() / 8).padTop(50);

        overLayStage.addActor(overLayRootTable);
        overLayRootTable.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);


    }

    @Override
    public void render(float delta){
        super.render(delta);
        if(game.btOn && !game.btConnected && ki1 == null){
            game.conHandler.cancel();
            dimRenderer.setProjectionMatrix(camera.combined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            dimRenderer.begin(ShapeRenderer.ShapeType.Filled);
            dimRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            dimRenderer.end();
            overLayStage.draw();
            if(animation < 1) {
                animation += delta;
            }
            else{
                animation = 0;
            }
            if(Gdx.input.getInputProcessor().equals(stage)){
                Gdx.input.setInputProcessor(overLayStage);
            }
        }
        else if(Gdx.input.getInputProcessor().equals(overLayStage)){
            Gdx.input.setInputProcessor(stage);
        }
    }
}
