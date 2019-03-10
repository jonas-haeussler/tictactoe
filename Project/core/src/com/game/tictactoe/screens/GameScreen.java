package com.game.tictactoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.game.tictactoe.TicTacToeGame;
import com.game.tictactoe.objects.MapGrid;


public class GameScreen implements Screen {
    private TicTacToeGame game;
    private OrthographicCamera camera;
    private ShapeRenderer renderer;
    private MapGrid mapGrid;
    private MapGrid[][] mapGrids;
    private int fieldSize;



    public GameScreen(TicTacToeGame game){
        this.game = game;
        this.camera = new OrthographicCamera();
        this.fieldSize = 3;
        camera.setToOrtho(false, 480, 800);
        this.mapGrid = new MapGrid(480, 800, fieldSize);
        mapGrids = new MapGrid[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                mapGrids[i][j] = new MapGrid(mapGrid.getX0() + i * mapGrid.getCELL_SIZE() + mapGrid.getCELL_SIZE() / 11,
                                            mapGrid.getY0() + j * mapGrid.getCELL_SIZE() + mapGrid.getCELL_SIZE() / 11,
                                                mapGrid.getCELL_SIZE(), mapGrid.getCELL_SIZE(), mapGrid.getMapWidthHeight());
            }

        }

        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        renderer.setColor(Color.BLACK);
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

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        mapGrid.drawMap(renderer, 8);
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                mapGrids[i][j].drawMap(renderer, 3);
            }
        }

        renderer.end();
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
}
