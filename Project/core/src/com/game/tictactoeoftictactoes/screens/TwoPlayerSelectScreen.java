package com.game.tictactoeoftictactoes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.tictactoeoftictactoes.TicTacToeGame;

public class TwoPlayerSelectScreen implements Screen {
    private TicTacToeGame game;
    private Label headingLabel;
    private TextButton localButton, blueToothButton, onlineButton, menuButton;
    private Table table;
    private Stage stage;
    private OrthographicCamera camera;


    public TwoPlayerSelectScreen(final TicTacToeGame game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();


        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = game.font2;
        headingLabel = new Label("[RED]C[BLUE]h[RED]o[BLUE]o[RED]s[BLUE]e\n[RED]o[BLUE]n[RED]e\n[BLUE]O[RED]p" +
                                        "[BLUE]t[RED]i[BLUE]o[RED]n"
                , labelStyle2);
        headingLabel.setWrap(true);
        headingLabel.setAlignment(Align.center);

        Label.LabelStyle labelStyle1 = new Label.LabelStyle();
        labelStyle1.font = game.font1;
        labelStyle1.fontColor = Color.BLACK;

        localButton = new TextButton("Local", game.comicSkin);
        localButton.getLabel().setFontScale(1.5f);
        localButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                game.getScreen().dispose();
                game.setScreen(new LocalPlayableScreen(game));
            }
        });
        blueToothButton = new TextButton("Bluetooth", game.comicSkin);
        blueToothButton.getLabel().setFontScale(1.5f);
        blueToothButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                game.getScreen().dispose();
                if (game.conHandler != null) {
                    game.conHandler.cancel();
                    if (game.conHandler.enableConnection()) {
                        game.setScreen(new com.game.tictactoeoftictactoes.screens.BTDevicesScreen(game));
                    }
                }
            }
        });
        onlineButton = new TextButton("Online", game.comicSkin);
        onlineButton.getLabel().setFontScale(1.5f);
        onlineButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                if(game.googleConHandler.loggedIn()) {
                    game.getScreen().dispose();
                    game.setScreen(new OnlineSelectScreen(game));
                }
                else {
                    game.googleConHandler.showConnectFirstStatement();
                }
            }
        });
        menuButton = new TextButton("Back To Menu", game.comicSkin);
        menuButton.getLabel().setFontScale(1.5f);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                game.getScreen().dispose();
                game.setScreen(new com.game.tictactoeoftictactoes.screens.MainMenuScreen(game));
            }
        });


        table.add(localButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10);
        table.row();
        table.add(blueToothButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50);
        table.row();
        table.add(onlineButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50);
        table.row();
        table.add(menuButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50);

        stage.addActor(headingLabel);
        stage.addActor(table);
        headingLabel.setPosition(Gdx.graphics.getWidth() / 2 - headingLabel.getWidth() / 2, Gdx.graphics.getHeight() - headingLabel.getPrefHeight());
        table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(game.background, 0,0, camera.viewportWidth, camera.viewportHeight);
        game.batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        if(game.gameMusic != null && !game.gameMusic.isPlaying()){
            game.gameMusic.play();
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
