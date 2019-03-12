package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.tictactoe.TicTacToeGame;

public class PlayableScreen extends GameScreen {
    private Table table;
    private TextButton menuButton, replayButton;
    private Label headingLabel;
    private Label playerMovementLabel;
    public PlayableScreen(final TicTacToeGame game, final byte kiPlayer, final byte kiLevel) {
        super(game, kiPlayer, (byte) 0,  kiLevel);
        table = new Table();
        menuButton = new TextButton("Back to Menu", game.comicSkin);
        menuButton.getLabel().setFontScale(1.5f);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                active = false;
                game.setScreen(new MainMenuScreen(game));
            }
        });
        replayButton = new TextButton("Start New Game", game.comicSkin);
        replayButton.getLabel().setFontScale(1.5f);
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                active = false;
                if(kiPlayer > 0){
                    byte x = (byte) (Math.random() * 2 + 1);
                    game.setScreen(new PlayableScreen(game, x, kiLevel));
                }
                else {
                    game.setScreen(new PlayableScreen(game, (byte) 0, kiLevel));
                }
            }
        });
        Label.LabelStyle labelStyle1 = new Label.LabelStyle();
        labelStyle1.font = game.font1;
        labelStyle1.fontColor = Color.BLACK;
        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = game.font2;

        playerMovementLabel = new Label("Player 1", labelStyle1);
        headingLabel = new Label("[RED]L[BLUE]e[RED]t[BLUE]´[RED]s [BLUE]T[RED]i[BLUE]c [RED]T[BLUE]a[RED]c [BLUE]T[RED]o[BLUE]e", labelStyle2);
        table.add(replayButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 12);
        table.row().height(Gdx.graphics.getHeight() / 20);
        table.add(menuButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 12);

        stage.addActor(headingLabel);
        stage.addActor(playerMovementLabel);
        stage.addActor(table);
        playerMovementLabel.setPosition(Gdx.graphics.getWidth() / 2 - playerMovementLabel.getPrefWidth() / 2, Gdx.graphics.getHeight() / 1.18f - playerMovementLabel.getPrefHeight());
        headingLabel.setPosition(Gdx.graphics.getWidth() / 2 - headingLabel.getPrefWidth() / 2, Gdx.graphics.getHeight() - headingLabel.getPrefHeight());

        headingLabel.setRotation(45);

        table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 8);
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
    public void resize(int width, int height){
        headingLabel.setPosition(width / 2 - headingLabel.getPrefWidth() / 2, height - headingLabel.getPrefHeight());
        playerMovementLabel.setPosition(width / 2 - playerMovementLabel.getPrefWidth() / 2, height / 1.18f - playerMovementLabel.getPrefHeight());
        table.setPosition(width / 2, height / 8);
    }
}
