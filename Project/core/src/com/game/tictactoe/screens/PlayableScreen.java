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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.tictactoe.TicTacToeGame;

public class PlayableScreen extends GameScreen {
    private Table table, overLayRootTable1;
    private TextButton menuButton, replayButton, backFromWaitButton;
    private Label headingLabel;
    private Label playerMovementLabel, connectionFailedLabel;
    private WinnerScreen winnerScreen;
    private ShapeRenderer dimRenderer;
    private Stage winnerStage, overLayStage1;
    private float animation;
    public PlayableScreen(final TicTacToeGame game, final byte kiPlayer, final byte kiLevel) {
        super(game, kiPlayer, (byte) 0,  kiLevel);
        table = new Table();
        winnerStage = new Stage(new ScreenViewport());
        overLayRootTable1 = new Table();
        overLayStage1 = new Stage();
        dimRenderer = new ShapeRenderer();
        dimRenderer.setAutoShapeType(true);
        dimRenderer.setColor(new Color(0, 0, 0, 0.5f));
        winnerScreen = new WinnerScreen(game, this);
        menuButton = new TextButton("Back to Menu", game.comicSkin);
        menuButton.getLabel().setFontScale(1.5f);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                game.getScreen().dispose();
                active = false;
                game.btConnected = false;
                game.player = 0;
                synchronized (game){
                    game.notifyAll();
                }
                game.setScreen(new MainMenuScreen(game));
            }
        });
        replayButton = new TextButton("Start New Game", game.comicSkin);
        replayButton.getLabel().setFontScale(1.5f);
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                game.getScreen().dispose();
                active = false;
                if(kiPlayer > 0 && !game.btConnected){
                    byte x = (byte) (Math.random() * 2 + 1);
                    game.setScreen(new PlayableScreen(game, x, kiLevel));
                }
                else if(game.btConnected){
                    game.btConnected = false;
                    game.player = 0;
                    synchronized (game){
                        game.notifyAll();
                    }
                    game.setScreen(new BTDevicesScreen(game));
                }
                else {
                    game.setScreen(new PlayableScreen(game, (byte) 0, kiLevel));
                }
            }
        });
        backFromWaitButton = new TextButton("Back", game.comicSkin);
        backFromWaitButton.getLabel().setFontScale(1.5f);
        backFromWaitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                game.btConnected = false;
                game.player = 0;
                synchronized (game){
                    game.notifyAll();
                }
                game.getScreen().dispose();
                game.setScreen(new BTDevicesScreen(game));
            }
        });
        Label.LabelStyle labelStyle1 = new Label.LabelStyle();
        labelStyle1.font = game.font1;
        labelStyle1.fontColor = Color.BLACK;
        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = game.font2;
        Label.LabelStyle labelStyle3 = new Label.LabelStyle();
        labelStyle3.font = game.font1;
        labelStyle3.fontColor = Color.WHITE;
        connectionFailedLabel = new Label("Connection failed..."
                , labelStyle3);
        connectionFailedLabel.setAlignment(Align.center);
        playerMovementLabel = new Label("Player 1", labelStyle1);
        headingLabel = new Label("[RED]L[BLUE]e[RED]t[BLUE]´[RED]s [BLUE]T[RED]i[BLUE]c [RED]T[BLUE]a[RED]c [BLUE]T[RED]o[BLUE]e", labelStyle2);
        table.add(replayButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 12);
        table.row().height(Gdx.graphics.getHeight() / 20);
        table.add(menuButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 12);

        overLayRootTable1.add(connectionFailedLabel).width(Gdx.graphics.getWidth() / 1.5f);
        overLayRootTable1.row();
        overLayRootTable1.add(backFromWaitButton).width(Gdx.graphics.getWidth() / 1.2f).height(Gdx.graphics.getHeight() / 8).padTop(50);
        stage.addActor(headingLabel);
        stage.addActor(playerMovementLabel);
        stage.addActor(table);
        overLayStage1.addActor(overLayRootTable1);
        overLayRootTable1.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        playerMovementLabel.setPosition(Gdx.graphics.getWidth() / 2 - playerMovementLabel.getWidth() / 2, Gdx.graphics.getHeight());
        headingLabel.setPosition(Gdx.graphics.getWidth() / 2 - headingLabel.getPrefWidth() / 2, Gdx.graphics.getHeight() - headingLabel.getPrefHeight());


        table.setPosition(Gdx.graphics.getWidth() / 2, 0);
    }

    public void setPlayer(boolean player){
        super.setPlayer(player);
        if(player){
            playerMovementLabel.setText("Player 2");
        }
        else {
            playerMovementLabel.setText("Player 1");
        }
    }

    @Override
    public void render(float delta){
        super.render(delta);
        if(getMapGrid().hasWinner()){
            if(winnerScreen.getStage() == null) {
                winnerScreen.setWinner(getMapGrid().getWinner());
                winnerStage.addActor(winnerScreen);
                Gdx.input.setInputProcessor(winnerStage);
                winnerScreen.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            }
            else{
                dimRenderer.setProjectionMatrix(camera.combined);
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                dimRenderer.begin(ShapeRenderer.ShapeType.Filled);
                dimRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                dimRenderer.end();
                winnerScreen.winnerAnimation(delta);
                winnerStage.draw();
            }
        }
        else if(getMapGrid().getTemplateCounter() >= fieldSize * fieldSize){
            if(winnerScreen.getStage() == null) {
                winnerScreen.setDraw();
                winnerStage.addActor(winnerScreen);
                winnerScreen.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            }
            else{
                dimRenderer.setProjectionMatrix(camera.combined);
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                dimRenderer.begin(ShapeRenderer.ShapeType.Filled);
                dimRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                dimRenderer.end();
                winnerScreen.drawAnimation(delta);
                winnerStage.draw();
            }
        }
        if(game.btOn && !game.btConnected){
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
    @Override
    public void resize(int width, int height){
        headingLabel.setPosition(width / 2 - headingLabel.getPrefWidth() / 2, height - headingLabel.getPrefHeight());
        playerMovementLabel.setPosition(width / 2 - playerMovementLabel.getPrefWidth() / 2, height / 1.18f - playerMovementLabel.getPrefHeight());
        table.setPosition(width / 2, height / 8);
    }
}
