package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
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
import com.game.tictactoe.objects.MapGrid;

public class HelpScreen extends GameScreen {
    private TextButton menuButton;
    private Label headingLabel;
    private Label textLabel;
    private Table root;
    private Stage helpStage;
    public HelpScreen(final TicTacToeGame game) {
        super(game, (byte) 1, (byte) 2, (byte) 0);
        camera.setToOrtho(false, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 1.1f, 0);
        stage.setViewport(new ScreenViewport());
        helpStage = new Stage(new ScreenViewport());
        root = new Table();

        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = game.font2;

        headingLabel = new Label("[RED]G[BLUE]a[RED]m[BLUE]e [RED]R[BLUE]u[RED]l[BLUE]e[RED]s", labelStyle2);
        helpStage.addActor(headingLabel);
        headingLabel.setPosition(Gdx.graphics.getWidth() / 2 - headingLabel.getPrefWidth() / 2, Gdx.graphics.getHeight() - headingLabel.getPrefHeight());

        Label.LabelStyle labelStyle1 = new Label.LabelStyle();
        labelStyle1.font = game.font1;
        labelStyle1.fontColor = Color.BLACK;
        textLabel = new Label("Play the legendary Tic Tac Toe Game in big!\n\n" +
                                    "Get three templates in a row, column or diagonal to win a field of the big Tic Tac Toe.\n\n" +
                                    "You may only play in the big field that your opponent has chosen for you. If the chosen field is already decided, you can choose a field freely\n\n" +
                                    "You can play in Single Player, Two Player or Online to improve your skills. \n\nHave fun and good Tic Tac Toeing!\n", labelStyle1);
        textLabel.setFontScale(0.7f);
        textLabel.setAlignment(Align.center);
        textLabel.setWrap(true);

        menuButton = new TextButton("Back to Menu", game.comicSkin);
        menuButton.getLabel().setFontScale(1.5f);
        menuButton.setWidth(Gdx.graphics.getWidth() / 1.5f);
        menuButton.setHeight(Gdx.graphics.getHeight() / 12);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getScreen().dispose();
                active = false;
                game.setScreen(new MainMenuScreen(game));
            }
        });
        refreshRootTable(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        helpStage.addActor(root);
        helpStage.addActor(menuButton);


        menuButton.setPosition(Gdx.graphics.getWidth() / 2 - menuButton.getWidth() / 2, Gdx.graphics.getHeight() / 20);
        Gdx.input.setInputProcessor(helpStage);

    }

    private void refreshRootTable(int width, int height){
        root.clear();
        final ScrollPane scroll = new ScrollPane(textLabel);
        scroll.setScrollingDisabled(true, false);
        root.add(scroll).colspan(2).height(height / 2.4f).width(width / 1.2f);
        root.row();
        root.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 1.45f);


    }
    private void restartAnimation(){
        animation = new float[fieldSize][fieldSize][fieldSize][fieldSize];
        bigAnimation = new float[fieldSize][fieldSize];

        this.mapGrid = new MapGrid(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), fieldSize, game, this);
        mapGrids = new MapGrid[fieldSize][fieldSize];
        winnerFields = new Vector2[fieldSize];
        winnerTime = new float[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                mapGrids[i][j] = new MapGrid(mapGrid.getX0() + j * mapGrid.getCELL_SIZE() + mapGrid.getCELL_SIZE() / 11
                        , mapGrid.getY0() + (fieldSize - i - 1) * mapGrid.getCELL_SIZE() + mapGrid.getCELL_SIZE() / 11
                        , mapGrid.getCELL_SIZE()
                        , mapGrid.getCELL_SIZE()
                        , mapGrid.getMapWidthHeight()
                        , game
                        , this);

                stage.addActor(mapGrids[i][j]);
            }

        }
        templateTime = new boolean[fieldSize][fieldSize];
        setAllActive(true);
        ki1.setGridSet(mapGrid, mapGrids);
        ki2.setGridSet(mapGrid, mapGrids);
    }
    @Override
    public void render(float delta){
        super.render(delta);
        helpStage.act();
        helpStage.draw();
        if(getMapGrid().hasWinner() || getMapGrid().getTemplateCounter() >= fieldSize * fieldSize){
            restartAnimation();
        }
    }
}
