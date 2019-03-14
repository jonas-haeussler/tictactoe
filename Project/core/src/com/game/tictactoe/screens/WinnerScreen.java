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
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                game.setScreen(new PlayableScreen(game, gameScreen.getKiPlayer1(), gameScreen.getKiLevel()));
            }
        });
        menuButton = new TextButton("Back to Menu", game.comicSkin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });


    }
    public void setWinner(byte winner){
        winnerLabel.setText("Player " + winner + " wins!");
    }
    public void setDraw(){
        winnerLabel.setText("Draw!");
    }
    public void winnerAnimation(float delta){
        if(animationTime < 2) {
            clear();
            animationTime += delta;
            add(cupImage).width(animationTime * Gdx.graphics.getWidth() / 4).height(animationTime * Gdx.graphics.getHeight() / 4);
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
    public void drawAnimation(float delta){
        if(animationTime < 2) {
            clear();
            animationTime += delta;
            add(swordsImage).width(animationTime * Gdx.graphics.getWidth() / 4).height(animationTime * Gdx.graphics.getHeight() / 4);
            row();
            winnerLabel.setFontScale(animationTime / 2);
            add(winnerLabel);
            setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }
    }
}
