package com.game.tictactoeoftictactoes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class WinnerScreen extends Table {
    private com.game.tictactoeoftictactoes.TicTacToeGame game;
    private Label winnerLabel;
    private TextButton newGameButton, menuButton;
    private float animationTime;
    private Image cupImage, swordsImage;
    private boolean playerWins;

    public WinnerScreen(final com.game.tictactoeoftictactoes.TicTacToeGame game, final GameScreen gameScreen){
        super();
        this.game = game;
        Label.LabelStyle labelStyle1 = new Label.LabelStyle();
        labelStyle1.font = game.font3;
        labelStyle1.fontColor = Color.GOLD;
        winnerLabel = new Label("", labelStyle1);
        cupImage = new Image(game.cup);
        swordsImage = new Image(game.swords);
        newGameButton = new TextButton("New Game", game.comicSkin);
        newGameButton.getLabel().setFontScale(1.3f);
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.getScreen().dispose();
                game.adShower.showInterstitialAd();
                if(game.getScreen() instanceof KIPlayableScreen) {
                    game.setScreen(new KIPlayableScreen(game, gameScreen.getKiPlayer1(), gameScreen.getKiLevel()));
                }
                else if(game.getScreen() instanceof LocalPlayableScreen){
                    game.googleConHandler.incrementAchievement(5, 0);
                    game.setScreen(new LocalPlayableScreen(game));
                }
                else if(game.getScreen() instanceof BTPlayableScreen){
                    game.btConnected = false;
                    game.player = 0;
                    game.setScreen(new BTDevicesScreen(game));
                }
                else if(game.getScreen() instanceof OnlinePlayableScreen){
                    game.googleConHandler.leaveRoom();
                    game.player = 0;
                    if(playerWins){
                        game.googleConHandler.addWinToScore();
                        game.googleConHandler.incrementAchievement(6, 7);
                        game.googleConHandler.showScoreIncreased();
                    }
                    game.setScreen(new OnlineSelectScreen(game));
                }
                if(!game.muteSound) {
                    game.gameMusic.play();
                }
            }
        });
        menuButton = new TextButton("Back to Menu", game.comicSkin);
        menuButton.getLabel().setFontScale(1.3f);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.getScreen().dispose();
                game.btConnected = false;
                game.player = 0;
                game.adShower.showInterstitialAd();
                game.googleConHandler.leaveRoom();
                if(game.getScreen() instanceof OnlinePlayableScreen){
                    if(playerWins){
                        game.googleConHandler.addWinToScore();
                        game.googleConHandler.incrementAchievement(6, 7);
                        game.googleConHandler.showScoreIncreased();

                    }
                }
                else if(game.getScreen() instanceof LocalPlayableScreen){
                    game.googleConHandler.incrementAchievement(5, 0);
                }
                game.setScreen(new MainMenuScreen(game));
                if(!game.muteSound) {
                    game.gameMusic.play();
                }
            }
        });


    }
    public void setWinner(byte winner, boolean playerWins){
        if(game.getScreen() instanceof LocalPlayableScreen) {
            winnerLabel.setText("Player " + winner + " wins!");
        }
        else {
            winnerLabel.setText(playerWins ? "You Win!" : "You Loose!");
        }
        this.playerWins = playerWins;
    }
    public void setDraw(){
        winnerLabel.setText("Draw!");
    }
    public void winnerAnimation(float delta){
        if(newGameButton.getStage() != null){
            Gdx.input.setInputProcessor(newGameButton.getStage());
        }
        if(playerWins) {
            if (animationTime < 2) {
                clear();
                if (animationTime == 0) {
                    game.circleSound.stop();
                    game.crossSound.stop();
                    game.fieldSound.stop();
                    game.gameMusic.stop();
                    if(!game.muteSound) {
                        game.winnerMusic.play();
                    }
                }
                animationTime += delta;
                add(cupImage).width(animationTime * Gdx.graphics.getWidth() / 4).height(animationTime * Gdx.graphics.getHeight() / 4);
                row();
                winnerLabel.setFontScale(animationTime / 2);
                add(winnerLabel);
                row();
                setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2);
            } else if (newGameButton.getStage() == null) {
                add(newGameButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 12);
                row();
            } else if (menuButton.getStage() == null) {
                add(menuButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 12).padTop(50);
            }
        }
        else {
//            winnerLabel.setText("You loose!");
            winnerLabel.getStyle().fontColor = Color.DARK_GRAY;
            drawAnimation(delta);
        }
    }
    public void drawAnimation(float delta){
        if(animationTime < 2) {
            clear();
            if(animationTime == 0){
                game.circleSound.stop();
                game.crossSound.stop();
                game.fieldSound.stop();
                game.gameMusic.stop();
                if(!game.muteSound) {
                    game.drawMusic.play();
                }
            }
            animationTime += delta;
            add(swordsImage).width(animationTime * Gdx.graphics.getWidth() / 4).height(animationTime * Gdx.graphics.getHeight() / 4);
            row();
            winnerLabel.setFontScale(animationTime / 2);
            add(winnerLabel);
            row();
            setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }
        else if(newGameButton.getStage() == null){
            add(newGameButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 12);
            row();
        }
        else if(menuButton.getStage() == null){
            add(menuButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 12).padTop(50);
        }
    }
}
