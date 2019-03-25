package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.tictactoe.TicTacToeGame;

public class WinnerScreen extends Table {
    private TicTacToeGame game;
    private Label winnerLabel;
    private TextButton newGameButton, menuButton;
    private float animationTime;
    private Image cupImage, swordsImage;
    private boolean playerWins;

    public WinnerScreen(final TicTacToeGame game, final GameScreen gameScreen){
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
                game.buttonSound.play(0.8f);
                game.getScreen().dispose();
                game.adShower.showInterstitialAd();
                if(!game.btConnected) {
                    game.setScreen(new PlayableScreen(game, gameScreen.getKiPlayer1(), gameScreen.getKiLevel()));
                }
                else {
                    game.btConnected = false;
                    game.player = 0;
                    game.setScreen(new BTDevicesScreen(game));
                }
                game.gameMusic.play();
            }
        });
        menuButton = new TextButton("Back to Menu", game.comicSkin);
        menuButton.getLabel().setFontScale(1.3f);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                game.getScreen().dispose();
                game.btConnected = false;
                game.player = 0;
                game.adShower.showInterstitialAd();
                game.setScreen(new MainMenuScreen(game));
                game.gameMusic.play();
            }
        });


    }
    public void setWinner(byte winner, boolean playerWins){
        winnerLabel.setText("Player " + winner + " wins!");
        this.playerWins = playerWins;
    }
    public void setDraw(){
        winnerLabel.setText("Draw!");
    }
    public void winnerAnimation(float delta){
        if(playerWins) {
            if (animationTime < 2) {
                clear();
                if (animationTime == 0) {
                    game.circleSound.stop();
                    game.crossSound.stop();
                    game.fieldSound.stop();
                    game.gameMusic.stop();
                    game.winnerMusic.play();
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
            winnerLabel.setText("You loose!");
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
                game.drawMusic.play();
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
