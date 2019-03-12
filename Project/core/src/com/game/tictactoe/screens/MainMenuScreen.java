package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.tictactoe.TicTacToeGame;

public class MainMenuScreen implements Screen {
    private TicTacToeGame game;
    private OrthographicCamera camera;
    private Label headingLabel;
    private TextButton singlePlayerButton, twoPlayerButton, onlinePlayerButton, helpButton, exitButton;
    private Table table;
    private Stage stage;
    public MainMenuScreen(final TicTacToeGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();

        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = game.font2;
        headingLabel = new Label("[RED]T[BLUE]i[RED]c [BLUE]T[RED]a[BLUE]c [RED]T[BLUE]o[RED]e\n [BLUE]o[RED]f\n [BLUE]T[RED]i[BLUE]c [RED]T[BLUE]a[RED]c [BLUE]T[RED]o[BLUE]e[RED]´[BLUE]s"
                                    , labelStyle2);
        headingLabel.setWrap(true);
        headingLabel.setAlignment(Align.center);
        singlePlayerButton = new TextButton("Single Player Mode", game.comicSkin);
        singlePlayerButton.getLabel().setFontScale(1.5f);
        singlePlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                byte x = (byte) (Math.random() * 2 + 1);
                game.setScreen(new PlayableScreen(game, x, (byte) 0));
            }
        });
        twoPlayerButton = new TextButton("Two Player Mode", game.comicSkin);
        twoPlayerButton.getLabel().setFontScale(1.5f);
        twoPlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                game.setScreen(new PlayableScreen(game, (byte) 0, (byte) 0));
            }
        });
        onlinePlayerButton = new TextButton("Online Mode", game.comicSkin);
        onlinePlayerButton.getLabel().setFontScale(1.5f);
        helpButton = new TextButton("Help", game.comicSkin);
        helpButton.getLabel().setFontScale(1.5f);
        helpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                game.setScreen(new HelpScreen(game));
            }
        });
        exitButton = new TextButton("Exit", game.comicSkin);
        exitButton.getLabel().setFontScale(1.5f);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        table.add(singlePlayerButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10);
        table.row();
        table.add(twoPlayerButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50);
        table.row();
        table.add(onlinePlayerButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50);
        table.row();
        table.add(helpButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50);
        table.row();
        table.add(exitButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50);




        stage.addActor(headingLabel);
        stage.addActor(table);
        headingLabel.setPosition(Gdx.graphics.getWidth() / 2 - headingLabel.getWidth() / 2, Gdx.graphics.getHeight() - headingLabel.getPrefHeight());
        table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2.5f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
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

        stage.draw();    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
