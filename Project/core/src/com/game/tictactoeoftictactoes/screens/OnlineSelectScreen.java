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

public class OnlineSelectScreen implements Screen {
    private TextButton quickGameButton, invitePlayersButton, invitationsButton, backButton;
    private Label headingLabel;
    private TextButton leaderBoardButton, achievementsButton;
    private TicTacToeGame game;
    private Table table;
    private Stage stage;
    private OrthographicCamera camera;

    private Table overLayRootTable;
    private Label pleaseWaitLabel;
    private Stage overLayStage;

    private ShapeRenderer dimRenderer;
    private ShapeRenderer waitAnimationRenderer;
    private boolean overLaymode;
    private float animation;
    private float overLayTimeOut;


    public OnlineSelectScreen(final TicTacToeGame game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        overLayRootTable = new Table();
        overLayStage = new Stage();


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
        pleaseWaitLabel = new Label("Please Wait..."
                , labelStyle1);
        pleaseWaitLabel.setAlignment(Align.center);
        pleaseWaitLabel.getStyle().fontColor = Color.WHITE;

        quickGameButton = new TextButton("Quick Game", game.comicSkin);
        quickGameButton.getLabel().setFontScale(1.5f);
        quickGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                if(game.googleConHandler.loggedIn()) {
                    if(game.getScreen() instanceof OnlineSelectScreen) {
                        ((OnlineSelectScreen) game.getScreen()).setOverLaymode(true);
                    }
                    game.googleConHandler.startQuickGame();
                }
                else {
                    game.googleConHandler.showConnectFirstStatement();
                }

            }
        });
        invitePlayersButton = new TextButton("Invite Players", game.comicSkin);
        invitePlayersButton.getLabel().setFontScale(1.5f);
        invitePlayersButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                if(game.googleConHandler.loggedIn()){
                    if(game.getScreen() instanceof OnlineSelectScreen){
                        ((OnlineSelectScreen)game.getScreen()).setOverLaymode(true);
                    }
                    game.googleConHandler.invitePlayers();
                }

                else {
                    game.googleConHandler.showConnectFirstStatement();
                }


            }
        });
        invitationsButton = new TextButton("Invitations", game.comicSkin);
        invitationsButton.getLabel().setFontScale(1.5f);
        invitationsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                if(game.googleConHandler.loggedIn()){
                    game.googleConHandler.showInvitationInbox();
                    if(game.getScreen() instanceof OnlineSelectScreen){
                        ((OnlineSelectScreen)game.getScreen()).setOverLaymode(true);
                    }
                }

                else {
                    game.googleConHandler.showConnectFirstStatement();
                }            }
        });
        backButton = new TextButton("Back", game.comicSkin);
        backButton.getLabel().setFontScale(1.5f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.buttonSound.play(0.8f);
                game.getScreen().dispose();
                game.setScreen(new com.game.tictactoeoftictactoes.screens.TwoPlayerSelectScreen(game));
            }
        });


        table = new Table();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        dimRenderer = new ShapeRenderer();
        dimRenderer.setAutoShapeType(true);
        dimRenderer.setColor(new Color(0, 0, 0, 0.7f));
        waitAnimationRenderer = new ShapeRenderer();
        waitAnimationRenderer.setAutoShapeType(true);
        waitAnimationRenderer.setColor(Color.BLUE);

        table.add(quickGameButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).colspan(2);
        table.row();
        table.add(invitePlayersButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50).colspan(2);
        table.row();
        table.add(invitationsButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50).colspan(2);
        table.row();
        table.add(backButton).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 10).padTop(50).colspan(2);
        table.row();
        table.add(leaderBoardButton).padTop(50).width(Gdx.graphics.getWidth() / 4).height(Gdx.graphics.getHeight() / 15);
        table.add(achievementsButton).padTop(50).width(Gdx.graphics.getWidth() / 4).height(Gdx.graphics.getHeight() / 15);


        stage.addActor(headingLabel);
        stage.addActor(table);
        headingLabel.setPosition(Gdx.graphics.getWidth() / 2 - headingLabel.getWidth() / 2, Gdx.graphics.getHeight() - headingLabel.getPrefHeight());
        table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2.2f);
        overLayRootTable.add(pleaseWaitLabel).width(Gdx.graphics.getWidth() / 1.5f);
        overLayStage.addActor(overLayRootTable);

        overLayRootTable.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

    }
    public void setOverLaymode(boolean overLaymode){
        this.overLaymode = overLaymode;
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

        if(overLaymode){
            overLayTimeOut += delta;
            if(overLayTimeOut >= 7){
                overLayTimeOut = 0;
                game.googleConHandler.leaveRoom();
                game.googleConHandler.showConnectionFailedStatement();
                overLaymode = false;
            }
            if(!Gdx.input.getInputProcessor().equals(overLayStage)){
                Gdx.input.setInputProcessor(overLayStage);
            }
            dimRenderer.setProjectionMatrix(camera.combined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            dimRenderer.begin(ShapeRenderer.ShapeType.Filled);
            dimRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            dimRenderer.end();
            overLayStage.draw();
            waitAnimationRenderer.begin(ShapeRenderer.ShapeType.Filled);
            waitAnimationRenderer.arc(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 1.4f, Gdx.graphics.getWidth() / 8, 90, animation * 360);
            waitAnimationRenderer.end();
            if(animation < 1) {
                animation += delta;
            }
            else{
                animation = 0;
            }
            if(game.player != 0){
                game.getScreen().dispose();
                game.setScreen(new OnlinePlayableScreen(game, game.player, (byte) 0));
            }
        }
        else if(Gdx.input.getInputProcessor().equals(overLayStage)){
            Gdx.input.setInputProcessor(stage);
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
