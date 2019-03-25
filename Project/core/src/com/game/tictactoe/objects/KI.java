package com.game.tictactoe.objects;

import com.badlogic.gdx.math.Vector2;
import com.game.tictactoe.TicTacToeGame;
import com.game.tictactoe.screens.GameScreen;

public class KI implements Runnable {
    private MapGrid bigMapGrid;
    private MapGrid[][] smallMapGrids;
    private byte kiPlayer;
    private byte kiLevel;
    private GameScreen gameScreen;
    private TicTacToeGame game;
    private MapGrid activeMapGrid;
    public KI(MapGrid bigMapGrid, MapGrid[][] smallMapGrids, byte kiPlayer, byte kiLevel, GameScreen gameScreen, TicTacToeGame game){
        this.bigMapGrid = bigMapGrid;
        this.smallMapGrids = smallMapGrids;
        this.kiPlayer = kiPlayer;
        this.kiLevel = kiLevel;
        this.gameScreen = gameScreen;
        this.game = game;
    }
    public void setGridSet(MapGrid bigMapGrid, MapGrid[][] smallMapGrids){
        this.bigMapGrid = bigMapGrid;
        this.smallMapGrids = smallMapGrids;
    }

    @Override
    public void run() {
        while(gameScreen.isActive()){
            if(gameScreen.getPlayer() && kiPlayer == 2 || !gameScreen.getPlayer() && kiPlayer == 1){
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                activeMapGrid = null;
                for (int i = 0; i < smallMapGrids.length; i++) {
                    for (int j = 0; j < smallMapGrids.length; j++) {
                        if(smallMapGrids[i][j].isActive() && !smallMapGrids[i][j].hasWinner()){
                            activeMapGrid = smallMapGrids[i][j];
                        }
                    }
                }
                if(kiLevel == 0){
                    int i = 0;
                    while(i < 100) {
                        int x = (int) (Math.random() * 3);
                        int y = (int) (Math.random() * 3);
                        if(activeMapGrid != null && activeMapGrid.getArray()[x][y] == 0){
                            if(kiPlayer == 1){
                                activeMapGrid.addPlayer1Move(new Vector2(activeMapGrid.getButtons()[x][y].getX(), activeMapGrid.getButtons()[x][y].getY()));
                            }
                            else if(kiPlayer == 2){
                                activeMapGrid.addPlayer2Move(new Vector2(activeMapGrid.getButtons()[x][y].getX(), activeMapGrid.getButtons()[x][y].getY()));

                            }
                            break;
                        }
                        i++;
                    }
                    if(i >= 100 && activeMapGrid != null){
                        activeMapGrid.setActive(false);
                    }
                }
                else if(kiLevel == 1){
                    //hard ki
                    int i = 0;
                        if(activeMapGrid != null){
                            byte[][] mapGridArray = activeMapGrid.getArray();
                            byte[] b;
                            if((b = canWinField(mapGridArray, kiPlayer)) == null){
                                if((b = takeBestAvailableField(mapGridArray)) == null){
                                    if((b = preventEnemyWinning(mapGridArray)) == null){
//                                        System.out.println("Taking random field");
                                        for (int j = 0; j < mapGridArray.length; j++) {
                                            for (int k = 0; k < mapGridArray.length; k++) {
                                                if(activeMapGrid != null && activeMapGrid.getArray()[j][k] == 0){
                                                    b = new byte[]{(byte) j, (byte) k};
                                                }
                                            }
                                        }
                                    }

                                }
                            }

                            if(kiPlayer == 1){
                                activeMapGrid.addPlayer1Move(new Vector2(activeMapGrid.getButtons()[b[0]][b[1]].getX(), activeMapGrid.getButtons()[b[0]][b[1]].getY()));
                            }
                            else if(kiPlayer == 2){
                                activeMapGrid.addPlayer2Move(new Vector2(activeMapGrid.getButtons()[b[0]][b[1]].getX(), activeMapGrid.getButtons()[b[0]][b[1]].getY()));
                            }
                        }
                    if(i >= 100 && activeMapGrid != null){
                        activeMapGrid.setActive(false);
                    }
                }
            }
            else {
                try {
                    synchronized (gameScreen) {
                        gameScreen.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private byte[] canWinField(byte[][] mapGrid, int player){
        for (int j = 0; j < mapGrid.length; j++) {
            int x = 0, y = 0, z = 0, q = 0;
            for (int k = 0; k < mapGrid.length; k++) {
                if(mapGrid[j][k] == player){
                    x += 1;
                }
                if(mapGrid[k][j] == player){
                    y += 1;
                }
                if(mapGrid[k][k] == player){
                    z += 1;
                }
                if(mapGrid[k][mapGrid.length - 1 - k] == player){
                    q += 1;
                }
            }
            if(x >= 2){
                for (int i = 0; i < mapGrid.length; i++) {
                    if(mapGrid[j][i] == 0){
                        return new byte[]{(byte) j, (byte) i};
                    }
                }
            }
            else if(y >= 2){
                for (int i = 0; i < mapGrid.length; i++) {
                    if(mapGrid[i][j] == 0){
                        return new byte[]{(byte) i, (byte) j};
                    }
                }
            }
            else if(z >= 2){
                for (int i = 0; i < mapGrid.length; i++) {
                    if(mapGrid[i][i] == 0){
                        return new byte[]{(byte) i, (byte) i};
                    }
                }
            }
            else if(q >= 2){
                for (int i = 0; i < mapGrid.length; i++) {
                    if(mapGrid[i][mapGrid.length - 1 - i] == 0){
                        return new byte[]{(byte) i, (byte) (mapGrid.length - 1 - i)};
                    }
                }
            }
        }
        return null;
    }
    private byte[] takeBestAvailableField(byte[][] mapGrid){


        if(mapGrid[0][mapGrid.length - 2] == 0){
            if(canWinField(gameScreen.getMapGrids()[0][mapGrid.length - 2].getArray(), (kiPlayer == 1) ? 2 : 1) == null
                    && !gameScreen.getMapGrids()[0][mapGrid.length - 2].hasWinner()){
                return new byte[]{(byte) (0), (byte) (mapGrid.length - 2)};
            }
        }
        if(mapGrid[mapGrid.length - 1][mapGrid.length - 2] == 0){
            if(canWinField(gameScreen.getMapGrids()[mapGrid.length - 1][mapGrid.length - 2].getArray(), (kiPlayer == 1) ? 2 : 1) == null
                    && !gameScreen.getMapGrids()[mapGrid.length - 1][mapGrid.length - 2].hasWinner()){
                return new byte[]{(byte) (mapGrid.length - 1), (byte) (mapGrid.length - 2)};
            }
        }
        if(mapGrid[mapGrid.length - 2][mapGrid.length - 1] == 0){
            if(canWinField(gameScreen.getMapGrids()[mapGrid.length - 2][mapGrid.length - 1].getArray(), (kiPlayer == 1) ? 2 : 1) == null
                    && !gameScreen.getMapGrids()[mapGrid.length -2][mapGrid.length - 1].hasWinner()){
                return new byte[]{(byte) (mapGrid.length - 2), (byte) (mapGrid.length - 1)};
            }
        }
        if(mapGrid[mapGrid.length - 2][0] == 0){
            if(canWinField(gameScreen.getMapGrids()[mapGrid.length - 2][0].getArray(), (kiPlayer == 1) ? 2 : 1) == null
                    && !gameScreen.getMapGrids()[mapGrid.length - 2][0].hasWinner()){
                return new byte[]{(byte) (mapGrid.length - 2), (byte) (0)};
            }
        }
        if(mapGrid[0][0] == 0){
            if(canWinField(gameScreen.getMapGrids()[0][0].getArray(), (kiPlayer == 1) ? 2 : 1) == null
                    && !gameScreen.getMapGrids()[0][0].hasWinner()){
                return new byte[]{(byte) (0), (byte) (0)};
            }
        }
        if(mapGrid[0][mapGrid.length - 1] == 0){
            if(canWinField(gameScreen.getMapGrids()[0][mapGrid.length - 1].getArray(), (kiPlayer == 1) ? 2 : 1) == null
                    && !gameScreen.getMapGrids()[0][mapGrid.length - 1].hasWinner()){
                return new byte[]{(byte) (0), (byte) (mapGrid.length - 1)};
            }
        }
        if(mapGrid[mapGrid.length - 1][0] == 0){
            if(canWinField(gameScreen.getMapGrids()[mapGrid.length - 1][0].getArray(), (kiPlayer == 1) ? 2 : 1) == null
                    && !gameScreen.getMapGrids()[mapGrid.length - 1][0].hasWinner()){
                return new byte[]{(byte) (mapGrid.length - 1), (byte) 0};
            }
        }
        if(mapGrid[mapGrid.length - 1][mapGrid.length - 1] == 0){
            if(canWinField(gameScreen.getMapGrids()[mapGrid.length - 1][mapGrid.length - 1].getArray(), (kiPlayer == 1) ? 2 : 1) == null
                    && !gameScreen.getMapGrids()[mapGrid.length - 1][mapGrid.length - 1].hasWinner()){
                return new byte[]{(byte) (mapGrid.length - 1), (byte) (mapGrid.length - 1)};
            }
        }
        if(mapGrid[mapGrid.length / 2][mapGrid.length / 2] == 0){
            if(canWinField(gameScreen.getMapGrids()[mapGrid.length / 2][mapGrid.length / 2].getArray(), (kiPlayer == 1) ? 2 : 1) == null
                    && !gameScreen.getMapGrids()[mapGrid.length / 2][mapGrid.length / 2].hasWinner()){
                return new byte[]{(byte) (mapGrid.length / 2), (byte) (mapGrid.length / 2)};
            }
        }
        return null;
    }

    private byte[] preventEnemyWinning(byte[][] mapGrid){
        for (int j = 0; j < mapGrid.length; j++) {
            for (int k = 0; k < mapGrid.length; k++) {
                if(mapGrid[j][k] == 0){
//                    System.out.println(leadsToVictory(j, k, (kiPlayer == 1) ? 2 : 1));
                    if(!leadsToVictory(j, k, (kiPlayer == 1) ? 2 : 1) && !gameScreen.getMapGrids()[j][k].hasWinner()){
                        return new byte[]{(byte) j, (byte) k};
                    }
                }
            }
        }
        return null;
    }

    private boolean leadsToVictory(int j, int k, int enemy){
        int x = 1, y = 1, z = 0, q = 0;
        byte[][] mapGrid = gameScreen.getMapGrid().getArray();
        if(j == 0 && k == 0 || j == mapGrid.length / 2 && k == mapGrid.length / 2 || j == mapGrid.length - 1 && k == mapGrid.length - 1){
            z = 1;
        }
        if(j == mapGrid.length - 1 && k == 0 || j == mapGrid.length / 2 && k == mapGrid.length / 2 || j == 0 && k == mapGrid.length - 1){
            q = 1;
        }

        for (int i = 0; i < mapGrid.length; i++) {
            if(mapGrid[i][k] == enemy){
                y++;
            }
            if(mapGrid[j][i] == enemy){
                x++;
            }
            if(mapGrid[i][i] == enemy){
                z++;
            }
            if(mapGrid[i][mapGrid.length - 1 - i] == enemy){
                q++;
            }
        }
        if(y >= 3 || x >= 3 || z >= 3 || q >= 3){
            return true;
        }
        return false;
    }

}
