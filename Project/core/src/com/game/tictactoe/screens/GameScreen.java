package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.tictactoe.TicTacToeGame;
import com.game.tictactoe.objects.KI;
import com.game.tictactoe.objects.MapGrid;


public abstract class GameScreen implements Screen {
    protected TicTacToeGame game;
    protected OrthographicCamera camera;

    protected ShapeRenderer renderer;
    protected ShapeRenderer circleRenderer;
    protected ShapeRenderer crossRenderer;
    protected ShapeRenderer ellipseRenderer;
    protected ShapeRenderer transparentRenderer;
    protected ShapeRenderer dimRenderer;
    private MapGrid mapGrid;
    private MapGrid[][] mapGrids;
    private int fieldSize;
    private Vector2[] winnerFields;


    private float[][][][] animation;
    private float[][] bigAnimation;

    private boolean player; // player1 = false, player2 = true

    protected Stage stage;
    private float[][] winnerTime;
    private KI ki1, ki2;
    private byte kiPlayer1, kiPlayer2;
    private byte kiLevel;
    protected boolean active;



    public GameScreen(final TicTacToeGame game, final byte kiPlayer1, final byte kiPlayer2, final byte kiLevel){
        this.game = game;
        this.camera = new OrthographicCamera();
        this.fieldSize = 3;
        this.kiPlayer1 = kiPlayer1;
        this.kiLevel = kiLevel;
        this.kiPlayer2 = kiPlayer2;
        this.active = true;
        animation = new float[fieldSize][fieldSize][fieldSize][fieldSize];
        bigAnimation = new float[fieldSize][fieldSize];
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.mapGrid = new MapGrid(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), fieldSize, game, this);
        mapGrids = new MapGrid[fieldSize][fieldSize];
        winnerFields = new Vector2[fieldSize];
        winnerTime = new float[fieldSize][fieldSize];


        stage = new Stage(new ScreenViewport());

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



        setAllActive(true);

        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        renderer.setColor(Color.BLACK);

        circleRenderer = new ShapeRenderer();
        circleRenderer.setAutoShapeType(true);
        circleRenderer.setColor(Color.BLUE);
        crossRenderer = new ShapeRenderer();
        crossRenderer.setAutoShapeType(true);
        crossRenderer.setColor(Color.RED);
        Gdx.gl.glLineWidth(40);
        ellipseRenderer = new ShapeRenderer();
        ellipseRenderer.setAutoShapeType(true);
        ellipseRenderer.setColor(Color.GREEN);
        transparentRenderer = new ShapeRenderer();
        transparentRenderer.setAutoShapeType(true);
        dimRenderer = new ShapeRenderer();
        dimRenderer.setAutoShapeType(true);

        game.batch.setProjectionMatrix(camera.combined);

        Gdx.input.setInputProcessor(stage);
        if(kiPlayer1 > 0) {
            this.ki1 = new KI(mapGrid, mapGrids, kiPlayer1, kiLevel, this, game);
            new Thread(ki1).start();
        }
        if(kiPlayer2 > 0){
            this.ki2 = new KI(mapGrid, mapGrids, kiPlayer2, kiLevel, this, game);
            new Thread(ki2).start();
        }
    }
    public boolean getPlayer(){
        return player;
    }
    public void setPlayer(boolean player){
        this.player = player;
        synchronized (game) {
            game.notifyAll();
        }
    }
    public void setActive(int i, int j){
        mapGrids[i][j].setActive(true);
    }
    public void setAllActive(boolean active){
        for (int i = 0; i < mapGrids.length; i++) {
            for (int j = 0; j < mapGrids.length; j++) {
                if(!mapGrids[i][j].hasWinner())
                   mapGrids[i][j].setActive(active);
            }
        }
    }
    public boolean hasWinner(int i, int j) {
        return mapGrids[i][j].hasWinner();
    }
    public byte getKiPlayer1(){
        return kiPlayer1;
    }
    public byte getKiPlayer2(){
        return kiPlayer2;
    }
    public byte getKiLevel(){
        return kiLevel;
    }
    public MapGrid getMapGrid(){
        return mapGrid;
    }
    public MapGrid[][] getMapGrids(){
        return mapGrids;
    }
    public boolean isActive(){
        return active;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(game.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
        transparentRenderer.setProjectionMatrix(camera.combined);
        transparentRenderer.begin(ShapeRenderer.ShapeType.Filled);
        transparentRenderer.rect(0, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getWidth() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 1.76f);
        transparentRenderer.end();

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        transparentRenderer.begin(ShapeRenderer.ShapeType.Filled);
        crossRenderer.setProjectionMatrix(camera.combined);
        crossRenderer.begin(ShapeRenderer.ShapeType.Filled);
        circleRenderer.setProjectionMatrix(camera.combined);
        circleRenderer.begin(ShapeRenderer.ShapeType.Filled);
        dimRenderer.setProjectionMatrix(camera.combined);
        dimRenderer.begin(ShapeRenderer.ShapeType.Filled);
        dimRenderer.setColor(new Color(0, 0, 0, 0.5f));
        mapGrid.drawMap(renderer, 15);
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if(!mapGrids[i][j].hasWinner() || winnerTime[i][j] < 1.75f){
                    mapGrids[i][j].drawMap(renderer, 7);
                    mapGrids[i][j].drawCircles(circleRenderer, transparentRenderer, animation[i][j], delta);
                    mapGrids[i][j].drawCrosses(crossRenderer, transparentRenderer, animation[i][j], delta);
                    if(!mapGrids[i][j].isActive() && !mapGrids[i][j].hasWinner()){
                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        mapGrids[i][j].dimGrid(dimRenderer);
                    }
                    if(mapGrids[i][j].hasWinner()) {
                        winnerTime[i][j] += delta;
                    }

                }
                if(((winnerTime[i][j] <= 0.5f && winnerTime[i][j] >= 0.25f)
                        || (winnerTime[i][j] <= 1f && winnerTime[i][j] >= 0.75f)
                        || (winnerTime[i][j] <= 1.5f && winnerTime[i][j] >= 1.25f))
                        && (mapGrids[i][j].hasWinner())) {
                    ellipseRenderer.setProjectionMatrix(camera.combined);
                    ellipseRenderer.begin(ShapeRenderer.ShapeType.Line);
                    winnerFields = mapGrids[i][j].getWinnerFields();
                    if(winnerFields[0].x == winnerFields[1].x){
                        ellipseRenderer.ellipse(mapGrids[i][j].getX0() + winnerFields[0].x - mapGrids[i][j].getCELL_SIZE() * 1.4f
                                            , mapGrids[i][j].getY0() + winnerFields[0].y - mapGrids[i][j].getCELL_SIZE() * 1.2f
                                            , mapGrid.getCELL_SIZE()
                                            , mapGrids[i][j].getCELL_SIZE(), 90f);
                    }
                    else if(winnerFields[0].y == winnerFields[1].y){
                        ellipseRenderer.ellipse(mapGrids[i][j].getX0() + winnerFields[0].x - mapGrids[i][j].getCELL_SIZE() / 2
                                        , mapGrids[i][j].getY0() + winnerFields[0].y - mapGrids[i][j].getCELL_SIZE() / 7
                                        , mapGrid.getCELL_SIZE()
                                        , mapGrids[i][j].getCELL_SIZE());
                    }
                    else if(winnerFields[0].x > winnerFields[1].x) {
                        ellipseRenderer.ellipse(mapGrids[i][j].getX0() + winnerFields[0].x - mapGrid.getCELL_SIZE() * 0.87f
                                , mapGrids[i][j].getY0() + winnerFields[0].y - mapGrids[i][j].getCELL_SIZE() * 1.3f
                                , mapGrid.getCELL_SIZE() * 1.3f
                                , mapGrids[i][j].getCELL_SIZE() * 1.1f, 45f);
                    }
                    else if(winnerFields[0].x < winnerFields[1].x){
                        ellipseRenderer.ellipse(mapGrids[i][j].getX0() + winnerFields[0].x - mapGrids[i][j].getCELL_SIZE() * 0.8f
                                , mapGrids[i][j].getY0() + winnerFields[0].y - mapGrids[i][j].getCELL_SIZE() * 1.3f
                                , mapGrid.getCELL_SIZE() * 1.3f
                                , mapGrids[i][j].getCELL_SIZE() * 1.1f, 135f);
                    }
                    ellipseRenderer.end();
                }
                else if(winnerTime[i][j] >= 1.75f) {
                    if(mapGrids[i][j].getWinner() == 2 && bigAnimation[i][j] <= 2) {
                        mapGrids[i][j].drawBigCircle(circleRenderer, transparentRenderer, bigAnimation[i][j]);
                        bigAnimation[i][j] += 2 * delta;
                    }
                    else if(mapGrids[i][j].getWinner() == 1 && bigAnimation[i][j] <= 2){
                        mapGrids[i][j].drawBigCross(crossRenderer, transparentRenderer, bigAnimation[i][j]);
                        bigAnimation[i][j] += 4 * delta;
                    }
                    else if(mapGrids[i][j].getWinner() == 2){
                        mapGrid.getArray()[i][j] = 2;
                        if(mapGrid.player2Win()){
                            // player 2 win animation
                        }
                        mapGrids[i][j].drawBigCircle(circleRenderer, transparentRenderer, 2);
                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        mapGrids[i][j].dimGrid(dimRenderer);


                    }
                    else if(mapGrids[i][j].getWinner() == 1){
                        mapGrid.getArray()[i][j] = 1;
                        if(mapGrid.player1Win()){
                            // player 1 win animation
                        }
                        mapGrids[i][j].drawBigCross(crossRenderer, transparentRenderer, 2);
                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        mapGrids[i][j].dimGrid(dimRenderer);

                    }
                }
            }
        }
        circleRenderer.end();
        crossRenderer.end();
        transparentRenderer.end();
        dimRenderer.end();

        renderer.end();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        camera.update();
        stage.getViewport().update(width, height, true);


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
        renderer.dispose();
        crossRenderer.dispose();
        ellipseRenderer.dispose();
        transparentRenderer.dispose();
        circleRenderer.dispose();
        dimRenderer.dispose();
    }
}
