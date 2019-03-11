package com.game.tictactoe.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.game.tictactoe.TicTacToeGame;
import com.game.tictactoe.screens.GameScreen;
import com.game.tictactoe.utils.FieldListener;


public class MapGrid extends Table {

    private TicTacToeGame game;
    private GameScreen gameScreen;
    private byte[][] mapGrid;

    private int mapWidthHeight;
    private float CELL_SIZE;
    private float width, height;
    private float x0, y0;
    private Button[][] buttons;

    private boolean active;
    private boolean hasWinner;
    private byte winner;

    public MapGrid(float width, float height, int mapWidthHeight, TicTacToeGame game, GameScreen gameScreen){
        // 0 for empty field, 1 for player1, 2 for player2
        super();
        this.game = game;
        this.gameScreen = gameScreen;

        this.mapWidthHeight = mapWidthHeight;
        this.width = width;
        this.height = height;
        mapGrid = new byte[mapWidthHeight][mapWidthHeight];
        CELL_SIZE = width / (mapWidthHeight);
        buttons = new Button[mapWidthHeight][mapWidthHeight];
        for (int i = 0; i < mapWidthHeight; i++) {
            for (int j = 0; j < mapWidthHeight; j++) {
                buttons[i][j] = new Button(game.comicSkin);

            }
        }
        x0 = 0;
        y0 = height / 2 - width / 2;

    }

    public MapGrid(float x0, float y0, float width, float height, int mapWidthHeight, TicTacToeGame game, GameScreen gameScreen){
        // 0 for empty field, 1 for player1, 2 for player2
        super();
        this.game = game;
        this.gameScreen = gameScreen;

        this.mapWidthHeight = mapWidthHeight;
        this.width = width;
        this.height = height;
        mapGrid = new byte[mapWidthHeight][mapWidthHeight];
        CELL_SIZE = width / (mapWidthHeight + 1);
        CELL_SIZE += CELL_SIZE / (2 * mapWidthHeight);
        buttons = new Button[mapWidthHeight][mapWidthHeight];
        FieldListener listener = new FieldListener(this, gameScreen);
        for (int i = 0; i < mapWidthHeight; i++) {
            for (int j = 0; j < mapWidthHeight; j++) {
                buttons[i][j] = new ImageButton(game.comicSkin);
                buttons[i][j].setColor(1, 1, 1, 0);
                buttons[i][j].addListener(listener);
            }
        }
        this.x0 = x0;
        this.y0 = y0;



        initTable(x0, y0, width, height);


    }

    public void initTable(float x0, float y0, float width, float height){
        this.clear();
        setBounds(x0 - width / 20, y0 - height / 20, width, height);
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                add(buttons[i][j]).width(CELL_SIZE).height(CELL_SIZE);
            }
            row();
        }
    }


    public void drawMap(ShapeRenderer renderer, int size){
        for (int i = 1; i < mapGrid.length; i++) {
            for (int j = 1; j < mapGrid.length; j++) {
                drawBigLine(i * CELL_SIZE + x0, y0, i * CELL_SIZE + x0,  mapWidthHeight * CELL_SIZE + y0, size, renderer);
                drawBigLine(0 + x0, j * CELL_SIZE + y0, mapWidthHeight * CELL_SIZE + x0, j * CELL_SIZE + y0, size, renderer);
            }

        }
    }
    public void drawCircles(ShapeRenderer renderer, ShapeRenderer transparentRenderer, float[][] animation, float delta){
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                if(mapGrid[i][j] == 2 && animation[i][j] >= 1){
                    renderer.circle(x0 + buttons[i][j].getX() + CELL_SIZE / 3, y0 + buttons[i][j].getY() + CELL_SIZE / 3, CELL_SIZE / 2.5f);
                    transparentRenderer.circle(x0 + buttons[i][j].getX() + CELL_SIZE / 3, y0 + buttons[i][j].getY() + CELL_SIZE / 3, CELL_SIZE / 3.5f);
                }
                else if(mapGrid[i][j] == 2) {
                    renderer.arc(x0 + buttons[i][j].getX() + CELL_SIZE / 3, y0 + buttons[i][j].getY() + CELL_SIZE / 3, CELL_SIZE / 2.5f, 90, 360 * animation[i][j]);
                    animation[i][j] += 2 * delta;
                    transparentRenderer.circle(x0 + buttons[i][j].getX() + CELL_SIZE / 3, y0 + buttons[i][j].getY() + CELL_SIZE / 3, CELL_SIZE / 3.5f);

                }
            }
        }
    }
    public void drawBigCircle(ShapeRenderer renderer, ShapeRenderer transparentRenderer, float animation){
        if(animation >= 1){
            renderer.circle(x0 + width / 2.3f, y0 + height / 2.3f, width / 2.5f);
            transparentRenderer.circle(x0 + width / 2.3f, y0 + height / 2.3f, width / 3.5f);
        }
        else {
            renderer.arc(x0 + width / 2.3f, y0 + height / 2.3f, width / 2.5f, 90, 360 * animation);
            transparentRenderer.circle(x0 + width / 2.3f, y0 + height / 2.3f, width / 3.5f);
        }
    }

    public void drawCrosses(ShapeRenderer renderer, ShapeRenderer transparentRenderer, float[][] animation, float delta){
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                if(mapGrid[i][j] == 1 && animation[i][j] >= 2){
                    renderer.rectLine(x0 + buttons[i][j].getX()
                                    , y0 + buttons[i][j].getY()
                                    , x0 + buttons[i][j].getX() + CELL_SIZE / 1.5f
                                    , y0 + buttons[i][j].getY() + CELL_SIZE / 1.5f
                                    , 9);
                    renderer.rectLine(x0 + buttons[i][j].getX()
                                    , y0 + buttons[i][j].getY() + CELL_SIZE / 1.5f
                                    , x0 + buttons[i][j].getX() + CELL_SIZE / 1.5f
                                    , y0 + buttons[i][j].getY()
                                    , 9);

                }
                else if(mapGrid[i][j] == 1 && animation[i][j] <= 1){
                    renderer.rectLine(x0 + buttons[i][j].getX()
                                    , y0 + buttons[i][j].getY()
                                    , x0 + buttons[i][j].getX() + CELL_SIZE / 1.5f
                                    , y0 + buttons[i][j].getY() + CELL_SIZE / 1.5f
                                    , 9);
                    transparentRenderer.rectLine(x0 + buttons[i][j].getX() + CELL_SIZE / 1.5f * animation[i][j]
                                                , y0 + buttons[i][j].getY() + CELL_SIZE / 1.5f * animation[i][j]
                                                , x0 + buttons[i][j].getX() + CELL_SIZE / 1.5f
                                                , y0 + buttons[i][j].getY() + CELL_SIZE / 1.5f
                                                , 9);
                    animation[i][j] += 4 * delta;
                }
                else if(mapGrid[i][j] == 1){
                    renderer.rectLine(x0 + buttons[i][j].getX()
                                    , y0 + buttons[i][j].getY() + CELL_SIZE / 1.5f
                                    , x0 + buttons[i][j].getX() + CELL_SIZE / 1.5f
                                    , y0 + buttons[i][j].getY()
                                    , 9);
                    transparentRenderer.rectLine(x0 + buttons[i][j].getX() +  CELL_SIZE / 1.5f * (animation[i][j] - 1)
                                                , y0 + buttons[i][j].getY() + CELL_SIZE / 1.5f - (CELL_SIZE / 1.5f) * (animation[i][j] - 1)
                                                , x0 + buttons[i][j].getX() + CELL_SIZE / 1.5f
                                                , y0 + buttons[i][j].getY()
                                                , 9);
                    renderer.rectLine(x0 + buttons[i][j].getX()
                                    , y0 + buttons[i][j].getY()
                                    , x0 + buttons[i][j].getX() + CELL_SIZE / 1.5f
                                    , y0 + buttons[i][j].getY() + CELL_SIZE / 1.5f
                                    , 9);
                    animation[i][j] += 4 * delta;
                }
            }
        }
    }
    public void drawBigCross(ShapeRenderer renderer, ShapeRenderer transparentRenderer, float animation){
        if(animation >= 2){
            renderer.rectLine(x0 + width / 10
                    , y0 + height / 10
                    , x0 + width / 1.3f
                    , y0 + height / 1.3f
                    , 15);
            renderer.rectLine(x0 + width / 10
                    , y0 + height / 1.3f
                    , x0 + width / 1.3f
                    , y0 + height / 10
                    , 15);
        }
        else if(animation <= 1){
            renderer.rectLine(x0 + width / 10
                    , y0 + height / 10
                    , x0 + width / 1.3f
                    , y0 + height / 1.3f
                    , 15);
            transparentRenderer.rectLine(x0 + width / 1.3f * animation
                    , y0 + height / 1.3f * animation
                    , x0 + width / 1.3f
                    , y0 + height / 1.3f
                    , 15);
        }
        else {
            renderer.rectLine(x0 + width / 10
                    , y0 + height / 1.3f
                    , x0 + width / 1.3f
                    , y0 + height / 10
                    , 15);
            transparentRenderer.rectLine(x0 +  width / 10 + width / 1.3f * (animation - 1)
                    , y0 + height / 1.3f - (height / 1.3f) * (animation - 1)
                    , x0 + width / 1.3f
                    , y0 + height / 10
                    , 15);
            renderer.rectLine(x0 + width / 10
                    , y0 + height / 10
                    , x0 + width / 1.3f
                    , y0 + height / 1.3f
                    , 15);
        }
    }

    public boolean addPlayer2Move(Vector2 position){
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                if(position.x == (buttons[i][j].getX()) && position.y == (buttons[i][j].getY())){
                    if(mapGrid[i][j] == 0) {
                        mapGrid[i][j] = 2;
                        if(player2Win()){
                            hasWinner = true;
                        }
                        if(!gameScreen.hasWinner(i, j)){
                            gameScreen.setAllActive(false);
                            gameScreen.setActive(i, j);
                        }
                        else {
                            gameScreen.setAllActive(true);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean addPlayer1Move(Vector2 position){
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                if(position.x == (buttons[i][j].getX()) && position.y == (buttons[i][j].getY())){
                    if(mapGrid[i][j] == 0) {
                        mapGrid[i][j] = 1;
                        if(player1Win()){
                            hasWinner = true;
                        }
                        if(!gameScreen.hasWinner(i, j)){
                            gameScreen.setAllActive(false);
                            gameScreen.setActive(i, j);
                        }
                        else {
                            gameScreen.setAllActive(true);
                        }

                        return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean player2Win(){
        int x, y, z, q;
        for (int i = 0; i < mapGrid.length; i++) {
            x = 0;
            y = 0;
            z = 0;
            q = 0;
            for (int j = 0; j < mapGrid.length; j++) {
                if(mapGrid[i][j] == 2){
                    x += 1;
                }
                if(mapGrid[j][i] == 2){
                    y += 1;
                }
                if(mapGrid[j][j] == 2){
                    z += 1;
                }
                if(mapGrid[j][mapGrid.length - 1 - j] == 2){
                    q += 1;
                }
                if(x >= 3 || y >= 3 || z >= 3 || q >= 3){
                    winner = 2;
                    return true;
                }
            }
        }
        return false;
    }
    private boolean player1Win(){
        int x, y, z, q;
        for (int i = 0; i < mapGrid.length; i++) {
            x = 0;
            y = 0;
            z = 0;
            q = 0;
            for (int j = 0; j < mapGrid.length; j++) {
                if(mapGrid[i][j] == 1){
                    x += 1;
                }
                if(mapGrid[j][i] == 1){
                    y += 1;
                }
                if(mapGrid[j][j] == 1){
                    z += 1;
                }
                if(mapGrid[j][mapGrid.length - 1 - j] == 1){
                    q += 1;
                }
                if(x >= 3 || y >= 3 || z >= 3 || q >= 3){
                    winner = 1;
                    return true;
                }
            }
        }
        return false;
    }
    private void drawBigLine(float x1, float y1, float x2, float y2, int size, ShapeRenderer renderer){
        float width = x2 - x1;
        float height = y2 - y1;
        if(width == 0){
            width = size;
        }
        else if(height == 0){
            height = size;
        }
        renderer.rect(x1, y1,  width, height);
    }

    public float getCELL_SIZE(){
        return CELL_SIZE;
    }
    public int getMapWidthHeight(){
        return mapWidthHeight;
    }
    public float getX0(){
        return x0;
    }
    public float getY0(){
        return y0;
    }
    public void setActive(boolean active){
        this.active = active;
    }
    public boolean isActive(){
        return active;
    }
    public boolean hasWinner(){
        return hasWinner;
    }
    public byte getWinner(){
        return winner;
    }

    public void dimGrid(ShapeRenderer shapeRenderer){

        shapeRenderer.rect(x0 - width / 11, y0 - height / 11, width, height);
    }

}
