package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.tictactoe.TicTacToeGame;

import java.util.HashSet;

public class BTDevicesScreen implements Screen, Runnable {
    private TicTacToeGame game;
    private Label headingLabel, overlayHeading;
    private TextButton createGameButton, joinGameButton, backButton, backOverlayButton, refreshDeviceButton;
    private HashSet<TextButton> devicesButtons;
    private Table table;
    private Stage stage;
    private OrthographicCamera camera;
    private Table overlayTable;
    private Stage overLayStage1, overLayStage2;
    private Table overLayRootTable1, overLayRootTable2;
    private Label pleaseWaitLabel;
    private TextButton backFromWaitButton;
    private ShapeRenderer waitAnimationRenderer;
    private Thread thread;
    private ShapeRenderer dimRenderer;
    private boolean overLaymode1;
    private boolean overLaymode2;
    private float animation;

    public BTDevicesScreen(final TicTacToeGame game){
        this.game = game;
        thread = new Thread(this);

        devicesButtons = new HashSet<TextButton>();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        overLayStage2 = new Stage();
        overLayStage1 = new Stage();
        overlayTable = new Table();
        overLayRootTable2 = new Table();
        overLayRootTable1 = new Table();

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
        overlayHeading = new Label("Select a Device"
                , labelStyle1);
        overlayHeading.setAlignment(Align.center);
        pleaseWaitLabel = new Label("Please Wait..."
                , labelStyle1);
        pleaseWaitLabel.setAlignment(Align.center);
        pleaseWaitLabel.getStyle().fontColor = Color.WHITE;

        createGameButton = new TextButton("Create Game", game.comicSkin);
        createGameButton.getLabel().setFontScale(1.5f);
        createGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                synchronized (game.threadCommunicator1) {
                    game.threadCommunicator1.notifyAll();
                }
                overLaymode1 = true;
                Gdx.input.setInputProcessor(overLayStage1);
            }
        });
        joinGameButton = new TextButton("Join Game", game.comicSkin);
        joinGameButton.getLabel().setFontScale(1.5f);
        joinGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                synchronized (thread) {
                    if (!thread.isAlive()) {
                        try {
                            thread.start();
                        } catch(IllegalThreadStateException e){

                        }
                    }
                }
                overLaymode2 = true;
                Gdx.input.setInputProcessor(overLayStage2);
            }
        });
        backButton = new TextButton("Back", game.comicSkin);
        backButton.getLabel().setFontScale(1.5f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                game.getScreen().dispose();
                game.setScreen(new TwoPlayerSelectScreen(game));
            }
        });
        backOverlayButton = new TextButton("Back", game.comicSkin);
        backOverlayButton.getLabel().setFontScale(1.5f);
        backOverlayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                overLaymode2 = false;
                Gdx.input.setInputProcessor(stage);
                synchronized (game){
                    game.notifyAll();
                }
            }
        });
        refreshDeviceButton = new TextButton("Refresh", game.comicSkin);
        refreshDeviceButton.getLabel().setFontScale(1.5f);
        refreshDeviceButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                synchronized (thread) {
                    if (!thread.isAlive()) {
                        try {
                            thread.start();
                        } catch(IllegalThreadStateException e){

                        }
                    }
                }
            }
        });
        backFromWaitButton = new TextButton("Back", game.comicSkin);
        backFromWaitButton.getLabel().setFontScale(1.5f);
        backFromWaitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                synchronized (game){
                    game.notifyAll();
                }
                game.buttonSound.play(0.8f);
                overLaymode1 = false;
                if(overLaymode2){
                    Gdx.input.setInputProcessor(overLayStage2);
                }
                else {
                    Gdx.input.setInputProcessor(stage);
                }
            }
        });


        table.add(createGameButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10);
        table.row();
        table.add(joinGameButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50);
        table.row();
        table.add(backButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50);

        dimRenderer = new ShapeRenderer();
        dimRenderer.setAutoShapeType(true);
        dimRenderer.setColor(new Color(0, 0, 0, 0.7f));
        waitAnimationRenderer = new ShapeRenderer();
        waitAnimationRenderer.setAutoShapeType(true);
        waitAnimationRenderer.setColor(Color.BLUE);

        ScrollPane scroller = new ScrollPane(overlayTable);
        scroller.setScrollingDisabled(true, false);
        overLayRootTable2.add(overlayHeading).width(Gdx.graphics.getWidth() / 1.5f);
        overLayRootTable2.row();
        overLayRootTable2.add(scroller).height(Gdx.graphics.getHeight() / 2).width(Gdx.graphics.getWidth() / 1.5f);
        overLayRootTable2.row();
        overLayRootTable2.add(refreshDeviceButton).width(Gdx.graphics.getWidth() / 1.2f).height(Gdx.graphics.getHeight() / 8).padTop(50);
        overLayRootTable2.row();
        overLayRootTable2.add(backOverlayButton).width(Gdx.graphics.getWidth() / 1.2f).height(Gdx.graphics.getHeight() / 8).padTop(50);
        overLayRootTable1.add(pleaseWaitLabel).width(Gdx.graphics.getWidth() / 1.5f);
        overLayRootTable1.row();
        overLayRootTable1.add(backFromWaitButton).width(Gdx.graphics.getWidth() / 1.2f).height(Gdx.graphics.getHeight() / 8).padTop(50);
        overLayStage2.addActor(overLayRootTable2);
        overLayStage1.addActor(overLayRootTable1);
        overLayRootTable2.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        overLayRootTable1.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

        stage.addActor(headingLabel);
        stage.addActor(table);
        headingLabel.setPosition(Gdx.graphics.getWidth() / 2 - headingLabel.getWidth() / 2, Gdx.graphics.getHeight() - headingLabel.getPrefHeight());
        table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2.5f);

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
        synchronized (game) {
            stage.draw();
        }
        if(overLaymode2) {
            dimRenderer.setProjectionMatrix(camera.combined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            dimRenderer.begin(ShapeRenderer.ShapeType.Filled);
            dimRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            dimRenderer.end();
            synchronized (game) {
                overLayStage2.draw();
            }
            overLayStage2.act();
        }
        if(overLaymode1){
            dimRenderer.setProjectionMatrix(camera.combined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            dimRenderer.begin(ShapeRenderer.ShapeType.Filled);
            dimRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            dimRenderer.end();
            overLayStage1.draw();
            waitAnimationRenderer.begin(ShapeRenderer.ShapeType.Filled);
            waitAnimationRenderer.arc(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 1.4f, Gdx.graphics.getWidth() / 8, 90, animation * 360);
            waitAnimationRenderer.end();
            if(animation < 1) {
                animation += delta;
            }
            else{
                animation = 0;
            }
        }
        synchronized (game) {
            if (game.btConnected && game.getScreen() instanceof BTDevicesScreen) {
                game.getScreen().dispose();
                PlayableScreen playableScreen = new PlayableScreen(game, game.player, (byte) 0);
                game.setScreen(playableScreen);
            }
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

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void run() {
            synchronized (game) {
                game.notifyAll();
            }
            if(!game.btOn) {
             synchronized (game){
                 try {
                     game.wait();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
            }
            if (game.btDevices.size() != devicesButtons.size()) {
                System.out.println("device update");
                devicesButtons.clear();
                synchronized (game) {
                    overlayTable.clear();
                }
                for (int i = 0; i < game.btDevices.size(); i++) {
                    synchronized (game) {
                        if (i < game.btDevices.size()) {
                            final TextButton button = new TextButton(game.btDevices.get(i), game.comicSkin);
                            button.getLabel().setFontScale(1.5f);
                            button.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    game.buttonSound.play(0.8f);
                                    overLaymode1 = true;
                                    Gdx.input.setInputProcessor(overLayStage1);
                                    game.setActiveBtDevice(String.valueOf(button.getLabel().getText()));
                                }
                            });
                            devicesButtons.add(button);
                            overlayTable.add(button).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10);
                            overlayTable.row();

                        }
                    }
                }

            }

    }
}
