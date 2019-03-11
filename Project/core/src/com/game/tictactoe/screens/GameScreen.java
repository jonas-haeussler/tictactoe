package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.tictactoe.TicTacToeGame;
import com.game.tictactoe.objects.MapGrid;


public class GameScreen implements Screen {
    private TicTacToeGame game;
    private OrthographicCamera camera;
    private ShapeRenderer renderer;
    private ShapeRenderer circleRenderer;
    private ShapeRenderer crossRenderer;
    private ShapeRenderer transparentRenderer;
    private ShapeRenderer dimRenderer;
    private MapGrid mapGrid;
    private MapGrid[][] mapGrids;
    private int fieldSize;


    private float[][][][] animation;
    private float[][] bigAnimation;

    private boolean player; // player1 = false, player2 = true

    private Stage stage;



    public GameScreen(TicTacToeGame game){
        this.game = game;
        this.camera = new OrthographicCamera();
        this.fieldSize = 3;
        animation = new float[fieldSize][fieldSize][fieldSize][fieldSize];
        bigAnimation = new float[fieldSize][fieldSize];
        camera.setToOrtho(false, 1080, 1920);
        this.mapGrid = new MapGrid((int)camera.viewportWidth, (int)camera.viewportHeight, fieldSize, game, this);
        mapGrids = new MapGrid[fieldSize][fieldSize];

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
        transparentRenderer = new ShapeRenderer();
        transparentRenderer.setAutoShapeType(true);
        dimRenderer = new ShapeRenderer();
        dimRenderer.setAutoShapeType(true);


        Gdx.input.setInputProcessor(stage);
    }
    public boolean getPlayer(){
        return player;
    }
    public void setPlayer(boolean player){
        this.player = player;
    }
    public void setActive(int i, int j){
        mapGrids[i][j].setActive(true);
    }
    public void setAllActive(boolean active){
        for (int i = 0; i < mapGrids.length; i++) {
            for (int j = 0; j < mapGrids.length; j++) {
                mapGrids[i][j].setActive(active);
            }
        }
    }
    public boolean hasWinner(int i, int j){
        return mapGrids[i][j].hasWinner();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(game.background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.batch.end();
        transparentRenderer.begin(ShapeRenderer.ShapeType.Filled);
        transparentRenderer.rect(0, camera.viewportHeight / 2 - camera.viewportWidth / 2, camera.viewportWidth, camera.viewportHeight / 1.76f);
        transparentRenderer.end();

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        transparentRenderer.setProjectionMatrix(camera.combined);
        transparentRenderer.begin(ShapeRenderer.ShapeType.Filled);
        crossRenderer.setProjectionMatrix(camera.combined);
        crossRenderer.begin(ShapeRenderer.ShapeType.Filled);
        circleRenderer.setProjectionMatrix(camera.combined);
        circleRenderer.begin(ShapeRenderer.ShapeType.Filled);

        dimRenderer.begin(ShapeRenderer.ShapeType.Filled);
        dimRenderer.setColor(new Color(0, 0, 0, 0.5f));
        mapGrid.drawMap(renderer, 15);
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if(!mapGrids[i][j].hasWinner()){
                    mapGrids[i][j].drawMap(renderer, 7);
                    mapGrids[i][j].drawCircles(circleRenderer, transparentRenderer, animation[i][j], delta);
                    mapGrids[i][j].drawCrosses(crossRenderer, transparentRenderer, animation[i][j], delta);
                    if(!mapGrids[i][j].isActive()){
                        Gdx.gl.glEnable(GL20.GL_BLEND);
                        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        mapGrids[i][j].dimGrid(dimRenderer);
                    }

                }
                else {
                    if(mapGrids[i][j].getWinner() == 2) {
                        mapGrids[i][j].drawBigCircle(circleRenderer, transparentRenderer, bigAnimation[i][j]);
                        bigAnimation[i][j] += 2 * delta;
                    }
                    else if(mapGrids[i][j].getWinner() == 1){
                        mapGrids[i][j].drawBigCross(crossRenderer, transparentRenderer, bigAnimation[i][j]);
                        bigAnimation[i][j] += 4 * delta;
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
