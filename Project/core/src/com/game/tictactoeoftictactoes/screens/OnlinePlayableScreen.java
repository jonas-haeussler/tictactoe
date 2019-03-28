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
import com.badlogic.gdx.utils.Align;
import com.game.tictactoeoftictactoes.TicTacToeGame;


public class OnlinePlayableScreen extends PlayableScreen {

    private float timeOut;
    private Label areYouSureLabel;
    private TextButton yesButton, noButton;
    private Table overlayTable;
    private Stage overlayStage;
    private boolean overLayMode;
    private boolean backToMenu;

    public OnlinePlayableScreen(final TicTacToeGame game, byte kiPlayer, byte kiLevel) {
        super(game, kiPlayer, kiLevel);

        replayButton = new TextButton("Start New Game", game.comicSkin);
        replayButton.getLabel().setFontScale(1.5f);
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                overLayMode = true;
            }
        });
        menuButton = new TextButton("Back to Menu", game.comicSkin);
        menuButton.getLabel().setFontScale(1.5f);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                overLayMode = true;
                backToMenu = true;
            }
        });
        initTable();
        Label.LabelStyle labelStyle1 = new Label.LabelStyle();
        labelStyle1.font = game.font1;
        labelStyle1.fontColor = Color.BLACK;
        areYouSureLabel = new Label("Are you sure to leave?\nYour Ranking Points will be lost!"
                , labelStyle1);
        areYouSureLabel.setAlignment(Align.center);
        areYouSureLabel.setWrap(true);
        areYouSureLabel.getStyle().fontColor = Color.WHITE;

        yesButton = new TextButton("Yes", game.comicSkin);
        yesButton.getLabel().setFontScale(1.5f);
        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.getScreen().dispose();
                game.player = 0;
                active = false;
                if(backToMenu){
                    game.setScreen(new MainMenuScreen(game));
                }
                else{
                    game.setScreen(new OnlineSelectScreen(game));
                }
            }
        });
        noButton = new TextButton("No", game.comicSkin);
        noButton.getLabel().setFontScale(1.5f);
        noButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                overLayMode = false;
            }
        });

        overlayTable = new Table();
        overlayStage = new Stage();
        overlayTable.add(areYouSureLabel).width(Gdx.graphics.getWidth() / 1.5f).colspan(2);
        overlayTable.row();
        overlayTable.add(yesButton).width(Gdx.graphics.getWidth() / 3f).height(Gdx.graphics.getHeight() / 8).padTop(50);
        overlayTable.add(noButton).width(Gdx.graphics.getWidth() / 3f).height(Gdx.graphics.getHeight() / 8).padTop(50);

        overlayStage.addActor(overlayTable);

        overlayTable.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);


        setPlayer(false);


    }

    @Override
    public void setPlayer(boolean player){
        super.setPlayer(player);
        timeOut = 0;
        if(player && getKiPlayer1() == 2 || !player && getKiPlayer1() == 1){
            playerMovementLabel.setText("Enemy Turn  " + ((int) (15 - timeOut)));
        }
        else {
            playerMovementLabel.setText("Your Turn  " + ((int) (15 - timeOut)));
        }
    }

    @Override
    public void render(float delta){
        super.render(delta);
        timeOut += delta;
        if(playerMovementLabel.getText().toString().startsWith("Enemy Turn")){
            playerMovementLabel.setText("Enemy Turn  " + ((int) (15 - timeOut)));
        }
        else {
            playerMovementLabel.setText("Your Turn  " + ((int) (15 - timeOut)));
        }

        if(timeOut >= 15){
            timeOut = 15;
            if(winnerScreen.getStage() == null) {
                if(game.googleConHandler.isConnected()) {
                    winnerScreen.setWinner(getPlayer() ? (byte) 1 : (byte) 2, playerMovementLabel.getText().toString().startsWith("Enemy Turn"));
                    winnerStage.addActor(winnerScreen);
                    Gdx.input.setInputProcessor(winnerStage);
                    winnerScreen.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
                }
                else {
                    game.googleConHandler.showConnectionFailedStatement();
                }
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
        if(overLayMode){
            if(!Gdx.input.getInputProcessor().equals(overlayStage)){
                Gdx.input.setInputProcessor(overlayStage);
            }
            dimRenderer.setProjectionMatrix(camera.combined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            dimRenderer.begin(ShapeRenderer.ShapeType.Filled);
            dimRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            dimRenderer.end();
            overlayStage.draw();
        }
        else {
            if(Gdx.input.getInputProcessor().equals(overlayStage)){
                Gdx.input.setInputProcessor(stage);
            }
        }
        if(!game.googleConHandler.isConnected()){
            game.googleConHandler.showConnectionFailedStatement();
            game.player = 0;
            game.getScreen().dispose();
            game.setScreen(new TwoPlayerSelectScreen(game));
        }
    }

    public void dispose(){
        overlayStage.dispose();
    }

}
