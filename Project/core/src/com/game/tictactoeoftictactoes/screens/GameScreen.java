package com.game.tictactoeoftictactoes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.tictactoeoftictactoes.TicTacToeGame;
import com.game.tictactoeoftictactoes.objects.KI;
import com.game.tictactoeoftictactoes.objects.MapGrid;


public abstract class GameScreen implements Screen {
    protected TicTacToeGame game;
    protected OrthographicCamera camera;

    protected ShapeRenderer renderer;
    protected ShapeRenderer circleRenderer;
    protected ShapeRenderer crossRenderer;
    protected ShapeRenderer ellipseRenderer;
    protected ShapeRenderer transparentRenderer;
    protected ShapeRenderer dimRenderer;
    protected MapGrid mapGrid;
    protected MapGrid[][] mapGrids;
    protected int fieldSize;
    protected Vector2[] winnerFields;


    protected float[][][][] animation;
    protected float[][] bigAnimation;

    private boolean player; // player1 = false, player2 = true

    protected Stage stage;
    protected float[][] winnerTime;
    private float bigWinnerTime;
    protected KI ki1;
    protected KI ki2;
    private byte kiPlayer1, kiPlayer2;
    protected byte kiLevel;
    protected boolean active;
    protected boolean[][] templateTime;


    public GameScreen(final TicTacToeGame game, final byte kiPlayer1, final byte kiPlayer2, final byte kiLevel){
        this.game = game;
        this.camera = new OrthographicCamera();
        this.fieldSize = 3;
        this.kiPlayer1 = kiPlayer1;
        this.kiLevel = kiLevel;
        this.kiPlayer2 = kiPlayer2;
        this.active = true;
        templateTime = new boolean[fieldSize][fieldSize];
        animation = new float[fieldSize][fieldSize][fieldSize][fieldSize];
        bigAnimation = new float[fieldSize][fieldSize];
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.mapGrid = new MapGrid(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), fieldSize, game, this);
        mapGrids = new MapGrid[fieldSize][fieldSize];
        winnerFields = new Vector2[fieldSize];
        winnerTime = new float[fieldSize][fieldSize];


        stage = new Stage(new ScreenViewport(camera));

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
        Gdx.gl.glLineWidth(50);
        ellipseRenderer = new ShapeRenderer();
        ellipseRenderer.setAutoShapeType(true);
        ellipseRenderer.setColor(Color.GREEN);
        transparentRenderer = new ShapeRenderer();
        transparentRenderer.setAutoShapeType(true);
        dimRenderer = new ShapeRenderer();
        dimRenderer.setAutoShapeType(true);

        game.batch.setProjectionMatrix(camera.combined);

        Gdx.input.setInputProcessor(stage);
        if(kiPlayer1 > 0 && !game.btConnected && !(this instanceof OnlinePlayableScreen)) {
            this.ki1 = new KI(mapGrid, mapGrids, kiPlayer1, kiLevel, this, game);
            new Thread(ki1).start();
        }
        if(kiPlayer2 > 0){
            this.ki2 = new KI(mapGrid, mapGrids, kiPlayer2, kiLevel, this, game);
            new Thread(ki2).start();
        }

        // debugging


    }
    private void drawSmallEllipse(int i, int j) {
        ellipseRenderer.begin(ShapeRenderer.ShapeType.Line);
        if (winnerFields[0].x == winnerFields[1].x) {
            ellipseRenderer.ellipse(mapGrids[i][j].getX0() + winnerFields[0].x - mapGrids[i][j].getCELL_SIZE() * 1.4f
                    , mapGrids[i][j].getY0() + winnerFields[0].y - mapGrids[i][j].getCELL_SIZE() * 1.2f
                    , mapGrid.getCELL_SIZE()
                    , mapGrids[i][j].getCELL_SIZE(), 90f);
        } else if (winnerFields[0].y == winnerFields[1].y) {
            ellipseRenderer.ellipse(mapGrids[i][j].getX0() + winnerFields[0].x - mapGrids[i][j].getCELL_SIZE() / 2f
                    , mapGrids[i][j].getY0() + winnerFields[0].y - mapGrids[i][j].getCELL_SIZE() / 7
                    , mapGrid.getCELL_SIZE()
                    , mapGrids[i][j].getCELL_SIZE());
        } else if (winnerFields[0].x > winnerFields[1].x) {
            ellipseRenderer.ellipse(mapGrids[i][j].getX0() + winnerFields[0].x - mapGrid.getCELL_SIZE() * 0.87f
                    , mapGrids[i][j].getY0() + winnerFields[0].y - mapGrids[i][j].getCELL_SIZE() * 1.3f
                    , mapGrid.getCELL_SIZE() * 1.3f
                    , mapGrids[i][j].getCELL_SIZE() * 1.1f, 45f);
        } else if (winnerFields[0].x < winnerFields[1].x) {
            ellipseRenderer.ellipse(mapGrids[i][j].getX0() + winnerFields[0].x - mapGrids[i][j].getCELL_SIZE() * 0.8f
                    , mapGrids[i][j].getY0() + winnerFields[0].y - mapGrids[i][j].getCELL_SIZE() * 1.3f
                    , mapGrid.getCELL_SIZE() * 1.3f
                    , mapGrids[i][j].getCELL_SIZE() * 1.1f, 135f);
        }
        ellipseRenderer.end();
    }
    private void drawBigEllipse(){
        ellipseRenderer.begin(ShapeRenderer.ShapeType.Line);
        if (winnerFields[0].x == winnerFields[1].x) {
            ellipseRenderer.ellipse(mapGrid.getX0() + winnerFields[0].x - mapGrid.getCELL_SIZE()
                    , mapGrid.getY0() + mapGrid.getCELL_SIZE()
                    , mapGrid.getWidth()
                    , mapGrid.getCELL_SIZE(), 90f);
        } else if (winnerFields[0].y == winnerFields[1].y) {
            ellipseRenderer.ellipse(mapGrid.getX0() + winnerFields[0].x
                    , mapGrid.getY0() + winnerFields[0].y - mapGrid.getCELL_SIZE() * 1.2f
                    , mapGrid.getWidth()
                    , mapGrid.getCELL_SIZE());
        } else if (winnerFields[0].x > winnerFields[1].x) {
            ellipseRenderer.ellipse(mapGrid.getX0() + winnerFields[0].x - mapGrid.getWidth() * 0.85f
                    , mapGrid.getY0() + winnerFields[0].y - mapGrid.getWidth() * 0.65f
                    , mapGrid.getWidth() * 1.3f
                    , mapGrid.getCELL_SIZE() * 1.1f, 45f);
        } else if (winnerFields[0].x < winnerFields[1].x) {
            ellipseRenderer.ellipse(mapGrid.getX0() + winnerFields[0].x - mapGrid.getCELL_SIZE() * 0.5f
                    , mapGrid.getY0() + winnerFields[0].y - mapGrid.getWidth() * 0.65f
                    , mapGrid.getWidth() * 1.3f
                    , mapGrid.getCELL_SIZE() * 1.1f, 135f);
        }
        ellipseRenderer.end();
    }
    public boolean getPlayer(){
        return player;
    }
    public void setPlayer(boolean player){
        this.player = player;
        synchronized (this) {
            this.notifyAll();
        }
    }
    public void setActive(int i, int j){
        mapGrids[i][j].setActive(true);
    }
    public void setAllActive(boolean active){
        for (int i = 0; i < mapGrids.length; i++) {
            for (int j = 0; j < mapGrids.length; j++) {
                if(!mapGrids[i][j].hasWinner() && !isFull(i, j))
                   mapGrids[i][j].setActive(active);
            }
        }
    }
    public boolean hasWinner(int i, int j) {
        return mapGrids[i][j].hasWinner();
    }
    public boolean isFull(int i, int j){
        boolean temp;
        if(temp = mapGrids[i][j].getTemplateCounter() >= (fieldSize * fieldSize)){
            if(!templateTime[i][j]) {
                templateTime[i][j] = true;
                mapGrid.incrementTemplateCounter();
            }
        }
        return temp;
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
    public boolean hasTotalWinner(){
        return mapGrid.hasWinner();
    }
    public int getFieldSize(){
        return fieldSize;
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
        transparentRenderer.rect(mapGrid.getX0(), mapGrid.getY0(), mapGrid.getCELL_SIZE() * 3, mapGrid.getCELL_SIZE() * 3);
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
        dimRenderer.setColor(new Color(0, 0, 0, 0.3f));
        mapGrid.drawMap(renderer, 15);
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                // draws map, circles and crosses with animation
                if(!mapGrids[i][j].hasWinner() || winnerTime[i][j] < 1.75f){
                    mapGrids[i][j].drawMap(renderer, 7);
                    mapGrids[i][j].drawCircles(circleRenderer, transparentRenderer, animation[i][j], delta);
                    mapGrids[i][j].drawCrosses(crossRenderer, transparentRenderer, animation[i][j], delta);
                    // dims the non active fields
                    if(!mapGrids[i][j].isActive() || isFull(i, j)){
                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        mapGrids[i][j].dimGrid(dimRenderer);
                    }
                    if(mapGrids[i][j].hasWinner()) {
                        winnerTime[i][j] += delta;
                    }

                }
                // draws the ellipses when a field has a winner
                if(((winnerTime[i][j] <= 0.5f && winnerTime[i][j] >= 0.25f)
                        || (winnerTime[i][j] <= 1f && winnerTime[i][j] >= 0.75f)
                        || (winnerTime[i][j] <= 1.5f && winnerTime[i][j] >= 1.25f))
                        && (mapGrids[i][j].hasWinner())) {
                    ellipseRenderer.setProjectionMatrix(camera.combined);
                    winnerFields = mapGrids[i][j].getWinnerFields();
                    if (winnerFields[0] != null) {
                        drawSmallEllipse(i, j);
                        if(winnerTime[i][j] <= 0.5f && winnerTime[i][j] >= 0.25f && this instanceof com.game.tictactoeoftictactoes.screens.PlayableScreen){
                            game.fieldSound.stop(game.fieldSoundId);
                            game.fieldSoundId = game.fieldSound.play(0.2f);
                        }
                    }
                }

                // draws a big cross or circle for a won field
                else if(winnerTime[i][j] >= 1.75f) {
                    if(mapGrids[i][j].getWinner() == 2 && bigAnimation[i][j] <= 2) {
                        if(bigAnimation[i][j] == 0 && this instanceof com.game.tictactoeoftictactoes.screens.PlayableScreen){
                            game.circleSound.stop(game.circleSoundId);
                            game.crossSound.stop(game.crossSoundId);
                            game.circleSoundId = game.circleSound.play(0.2f);
                        }
                        mapGrids[i][j].drawBigCircle(circleRenderer, transparentRenderer, bigAnimation[i][j]);
                        bigAnimation[i][j] += 2 * delta;
                    }
                    else if(mapGrids[i][j].getWinner() == 1 && bigAnimation[i][j] <= 2){
                        if(bigAnimation[i][j] == 0 && this instanceof com.game.tictactoeoftictactoes.screens.PlayableScreen){
                            game.crossSound.stop(game.crossSoundId);
                            game.circleSound.stop(game.circleSoundId);
                            game.crossSoundId = game.crossSound.play(0.3f);
                        }
                        mapGrids[i][j].drawBigCross(crossRenderer, transparentRenderer, bigAnimation[i][j]);
                        bigAnimation[i][j] += 4 * delta;
                    }
                    // check for total winner and draw big ellipse
                    else if(mapGrids[i][j].getWinner() == 2){
                        mapGrid.getArray()[i][j] = 2;
                        if(!templateTime[i][j]) {
                            templateTime[i][j] = true;
                            mapGrid.incrementTemplateCounter();
                        }
                        if(mapGrid.player2Win()){
                            mapGrid.setHasWinner(true);
                            winnerFields = new Vector2[fieldSize];
                        }
                        mapGrids[i][j].drawBigCircle(circleRenderer, transparentRenderer, 2);
                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        mapGrids[i][j].dimGrid(dimRenderer);


                    }

                    // check for total winner and draw big ellipse
                    else if(mapGrids[i][j].getWinner() == 1){
                        mapGrid.getArray()[i][j] = 1;
                        if(!templateTime[i][j]) {
                            templateTime[i][j] = true;
                            mapGrid.incrementTemplateCounter();
                        }
                        if(mapGrid.player1Win()){
                            mapGrid.setHasWinner(true);
                            winnerFields = mapGrid.getWinnerFields();
                            }
                        mapGrids[i][j].drawBigCross(crossRenderer, transparentRenderer, 2);
                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        mapGrids[i][j].dimGrid(dimRenderer);

                    }
                }
            }
        }
        if(mapGrid.hasWinner()){
            bigWinnerTime += delta;
        }
        if(((bigWinnerTime <= 0.5f && bigWinnerTime >= 0.25f)
                || (bigWinnerTime <= 1f && bigWinnerTime >= 0.75f)
                || (bigWinnerTime <= 1.5f && bigWinnerTime >= 1.25f))
                && (mapGrid.hasWinner())) {
            ellipseRenderer.setProjectionMatrix(camera.combined);
            winnerFields = mapGrid.getWinnerFields();
            if (winnerFields[0] != null) {
//                drawBigEllipse();

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
        if(game.gameMusic != null && !game.gameMusic.isPlaying()){
            game.gameMusic.play();
        }
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
