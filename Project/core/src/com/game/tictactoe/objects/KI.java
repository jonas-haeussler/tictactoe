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
                MapGrid activeMapGrid = null;
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
                    // hard ki
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
}
