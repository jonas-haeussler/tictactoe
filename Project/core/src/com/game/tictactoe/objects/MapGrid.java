package com.game.tictactoe.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class MapGrid {
    private byte[][] mapGrid;

    private int mapWidthHeight;
    private int CELL_SIZE;
    private int width, height;
    private int x0, y0;

    public MapGrid(int width, int height, int mapWidthHeight){
        // 0 for empty field, 1 for player1, 2 for player2

        this.mapWidthHeight = mapWidthHeight;
        this.width = width;
        this.height = height;
        mapGrid = new byte[mapWidthHeight][mapWidthHeight];
        CELL_SIZE = width / (mapWidthHeight);

        x0 = 0;
        y0 = height / 2 - width / 2;


    }

    public MapGrid(int x0, int y0, int width, int height, int mapWidthHeight){
        // 0 for empty field, 1 for player1, 2 for player2

        this.mapWidthHeight = mapWidthHeight;
        this.width = width;
        this.height = height;
        mapGrid = new byte[mapWidthHeight][mapWidthHeight];
        CELL_SIZE = width / (mapWidthHeight + 1);
        CELL_SIZE += CELL_SIZE / (2 * mapWidthHeight);

        this.x0 = x0;
        this.y0 = y0;


    }


    public void drawMap(ShapeRenderer renderer, int size){
        for (int i = 1; i < mapGrid.length; i++) {
            for (int j = 1; j < mapGrid.length; j++) {
                drawBigLine(i * CELL_SIZE + x0, y0, i * CELL_SIZE + x0,  mapWidthHeight * CELL_SIZE + y0, size, renderer);
                drawBigLine(0 + x0, j * CELL_SIZE + y0, mapWidthHeight * CELL_SIZE + x0, j * CELL_SIZE + y0, size, renderer);
            }

        }
    }
    private void drawBigLine(int x1, int y1, int x2, int y2, int size, ShapeRenderer renderer){
        int width = x2 - x1;
        int height = y2 - y1;
        if(width == 0){
            width = size;
        }
        else if(height == 0){
            height = size;
        }
        renderer.rect(x1, y1,  width, height);
    }

    public int getCELL_SIZE(){
        return CELL_SIZE;
    }
    public int getMapWidthHeight(){
        return mapWidthHeight;
    }
    public int getX0(){
        return x0;
    }
    public int getY0(){
        return y0;
    }
}
