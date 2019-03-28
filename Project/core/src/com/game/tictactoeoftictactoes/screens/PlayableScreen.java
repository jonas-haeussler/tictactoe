package com.game.tictactoeoftictactoes.screens;

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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class PlayableScreen extends GameScreen {
    private Table table;
    protected TextButton menuButton, replayButton;
    private Label headingLabel;
    protected Label playerMovementLabel;
    protected WinnerScreen winnerScreen;
    protected ShapeRenderer dimRenderer;
    protected Stage winnerStage;
    protected float animation;
    private boolean achievementIncremented;
    protected byte kiPlayer;
    public PlayableScreen(final com.game.tictactoeoftictactoes.TicTacToeGame game, final byte kiPlayer, final byte kiLevel) {
        super(game, kiPlayer, (byte) 0,  kiLevel);
        this.kiPlayer = kiPlayer;
        this.kiLevel = kiLevel;
        table = new Table();
        winnerStage = new Stage(new ScreenViewport());
        dimRenderer = new ShapeRenderer();
        dimRenderer.setAutoShapeType(true);
        dimRenderer.setColor(new Color(0, 0, 0, 0.5f));
        winnerScreen = new WinnerScreen(game, this);

        initButtons();

        Label.LabelStyle labelStyle1 = new Label.LabelStyle();
        labelStyle1.font = game.font1;
        labelStyle1.fontColor = Color.BLACK;
        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = game.font2;
        playerMovementLabel = new Label("Player 1", labelStyle1);
        headingLabel = new Label("[RED]L[BLUE]e[RED]t[BLUE]´[RED]s [BLUE]T[RED]i[BLUE]c [RED]T[BLUE]a[RED]c [BLUE]T[RED]o[BLUE]e", labelStyle2);
        Table labelTable = new Table();
        labelTable.add(headingLabel);
        labelTable.row();
        labelTable.add(playerMovementLabel);
        initTable();

        stage.addActor(labelTable);
        stage.addActor(table);



        labelTable.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - labelTable.getPrefHeight() / 2);
        table.setPosition(Gdx.graphics.getWidth() / 2, 0);
    }
    protected void initTable(){
        table.clear();
        table.add(replayButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 12);
        table.row().height(Gdx.graphics.getHeight() / 20);
        table.add(menuButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 12).padBottom(200);
    }
    private void initButtons(){
        menuButton = new TextButton("Back to Menu", game.comicSkin);
        menuButton.getLabel().setFontScale(1.5f);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.getScreen().dispose();
                active = false;
                game.player = 0;
                game.btConnected = false;
                if(game.conHandler != null){
                    game.conHandler.cancel();
                }
                game.setScreen(new MainMenuScreen(game));
            }
        });
        replayButton = new TextButton("Start New Game", game.comicSkin);
        replayButton.getLabel().setFontScale(1.5f);
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.getScreen().dispose();
                active = false;
                    if(kiPlayer > 0) {
                        byte x = (byte) (Math.random() * 2 + 1);
                        game.setScreen(new KIPlayableScreen(game, x, kiLevel));
                    }
                    else {
                        game.setScreen(new LocalPlayableScreen(game));
                    }
            }
        });
    }

    @Override
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
                winnerScreen.setWinner(getMapGrid().getWinner(), (getKiPlayer1() == 0) || getKiPlayer1() != getMapGrid().getWinner());
                if(!achievementIncremented && (getKiPlayer1() == 0) || getKiPlayer1() != getMapGrid().getWinner()){
                    System.out.println(getKiPlayer1() + ", " + getKiLevel() + ", " + !game.btConnected);
                    if(getKiPlayer1() != 0 && getKiLevel() == 0 && !game.btConnected){
                        game.googleConHandler.incrementAchievement(1, 2);
                        achievementIncremented = true;
                    }
                    if(getKiPlayer1() != 0 && getKiLevel() == 1 && !game.btConnected){
                        game.googleConHandler.incrementAchievement(3, 4);
                        achievementIncremented = true;
                    }
                    if(getKiPlayer1() == 0){
                        game.googleConHandler.incrementAchievement(5, 0);
                        achievementIncremented = true;
                    }
                    if(game.onlineMode){
                        game.googleConHandler.incrementAchievement(6, 7);
                        achievementIncremented = true;
                    }
                }
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
                if(getKiPlayer1() == 0 && !achievementIncremented){
                    game.googleConHandler.incrementAchievement(5, 0);
                    achievementIncremented = true;
                }
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

    }
    @Override
    public void resize(int width, int height){
        headingLabel.setPosition(width / 2 - headingLabel.getPrefWidth() / 2, height - headingLabel.getPrefHeight());
        playerMovementLabel.setPosition(width / 2 - playerMovementLabel.getPrefWidth() / 2, height / 1.18f - playerMovementLabel.getPrefHeight());
        table.setPosition(width / 2, height / 8);
    }
}
