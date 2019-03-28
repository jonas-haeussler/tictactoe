package com.game.tictactoeoftictactoes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.tictactoeoftictactoes.TicTacToeGame;

public class MainMenuScreen implements Screen {
    private TicTacToeGame game;
    private OrthographicCamera camera;
    private Label headingLabel;
    private TextButton easyModeButton, hardModeButton, onlinePlayerButton, helpButton, achievementsButton, leaderBoardButton, logInButton;
    private ImageButton muteSoundButton;
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
        easyModeButton = new TextButton("Easy Mode", game.comicSkin);
        easyModeButton.getLabel().setFontScale(1.5f);
        easyModeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.getScreen().dispose();
                byte x = (byte) (Math.random() * 2 + 1);
                game.setScreen(new KIPlayableScreen(game, x, (byte) 0));
            }
        });
        hardModeButton = new TextButton("Hard Mode", game.comicSkin);
        hardModeButton.getLabel().setFontScale(1.5f);
        hardModeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.getScreen().dispose();
                byte x = (byte) (Math.random() * 2 + 1);
                game.setScreen(new KIPlayableScreen(game, x, (byte) 1));
            }
        });
        onlinePlayerButton = new TextButton("Two Player Mode", game.comicSkin);
        onlinePlayerButton.getLabel().setFontScale(1.5f);
        onlinePlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.getScreen().dispose();
                game.setScreen(new TwoPlayerSelectScreen(game));
            }
        });
        helpButton = new TextButton("Help", game.comicSkin);
        helpButton.getLabel().setFontScale(1.5f);
        helpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.getScreen().dispose();
                game.setScreen(new com.game.tictactoeoftictactoes.screens.HelpScreen(game));
            }
        });
        logInButton = new TextButton("Connect with Google", game.comicSkin);
        logInButton.getLabel().setFontScale(1.5f);
        logInButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.googleConHandler.logIn();
            }
        });
        leaderBoardButton = new TextButton("Leaderboard", game.comicSkin);
        leaderBoardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.googleConHandler.showLeaderBoard();
            }
        });
        achievementsButton = new TextButton("Achievements", game.comicSkin);
        achievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.buttonSound.play(0.8f);
                }
                game.googleConHandler.showAchievements();
            }
        });

        if(!game.muteSound) {
            muteSoundButton = new ImageButton(new TextureRegionDrawable(game.muteSoundTexture));
        }
        else {
            muteSoundButton = new ImageButton(new TextureRegionDrawable(game.playSoundTexture));
        }
        muteSoundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!game.muteSound) {
                    game.muteSound = true;
                    game.gameMusic.stop();
                    muteSoundButton = new ImageButton(new TextureRegionDrawable(game.playSoundTexture));
                    muteSoundButton.addListener(this);
                    initStage();
                }
                else {
                    game.buttonSound.play(0.8f);
                    game.muteSound = false;
                    game.gameMusic.play();
                    muteSoundButton = new ImageButton(new TextureRegionDrawable(game.muteSoundTexture));
                    muteSoundButton.addListener(this);
                    initStage();
                }

            }
        });

        initStage();



        stage.addActor(headingLabel);
        stage.addActor(table);
        headingLabel.setPosition(Gdx.graphics.getWidth() / 2 - headingLabel.getWidth() / 2, Gdx.graphics.getHeight() - headingLabel.getPrefHeight());
        table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2.3f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
    }

    public void initStage(){
        table.clear();
        stage.clear();

        table.add(easyModeButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).colspan(2);
        table.row();
        table.add(hardModeButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50).colspan(2);
        table.row();
        table.add(onlinePlayerButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50).colspan(2);
        table.row();
        table.add(helpButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50).colspan(2);
        table.row();
        if(game.googleConHandler.loggedIn()) {
            table.add(leaderBoardButton).padTop(50).width(Gdx.graphics.getWidth() / 4).height(Gdx.graphics.getHeight() / 15);
            table.add(achievementsButton).padTop(50).width(Gdx.graphics.getWidth() / 4).height(Gdx.graphics.getHeight() / 15);
        }
        else {
            table.add(logInButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50).colspan(2);
        }
        table.row();
        table.add(muteSoundButton).padTop(50).width(Gdx.graphics.getWidth() / 8).height(Gdx.graphics.getHeight() / 25).colspan(2);

        stage.addActor(headingLabel);
        stage.addActor(table);
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


        if(game.invitation){
            dispose();
            game.setScreen(new OnlineSelectScreen(game));
            ((OnlineSelectScreen)game.getScreen()).setOverLaymode(true);
            game.invitation = false;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        if(game.gameMusic != null && !game.gameMusic.isPlaying() && !game.muteSound){
            game.gameMusic.play();
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
