package com.game.tictactoeoftictactoes.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.tictactoeoftictactoes.TicTacToeGame;
import com.game.tictactoeoftictactoes.objects.MapGrid;
import com.game.tictactoeoftictactoes.screens.OnlinePlayableScreen;
import com.game.tictactoeoftictactoes.screens.PlayableScreen;

public class FieldListener extends ChangeListener {
    private MapGrid mapGrid;
    private PlayableScreen playableScreen;
    private TicTacToeGame game;
    public FieldListener(MapGrid mapGrid, PlayableScreen playableScreen, TicTacToeGame game){
        this.mapGrid = mapGrid;
        this.playableScreen = playableScreen;
        this.game = game;

    }
    public FieldListener(){

    }
    @Override
    public void changed(ChangeEvent event, Actor actor) {
        if(mapGrid.isActive() && !mapGrid.hasWinner()) {
            Vector2 position = new Vector2(actor.getX(), actor.getY());
                if (playableScreen.getPlayer() && !(playableScreen.getKiPlayer1() == 2)) {
                    if(mapGrid.addPlayer2Move(position)) {
                        if(playableScreen instanceof OnlinePlayableScreen){
                            game.googleConHandler.sendTurn(game.getMapGrids());
                        }
                        synchronized (playableScreen){
                            playableScreen.notifyAll();
                           }
                    }

                }
                else if(!playableScreen.getPlayer() && !(playableScreen.getKiPlayer1() == 1)){
                    if(mapGrid.addPlayer1Move(position)){
                        if(playableScreen instanceof OnlinePlayableScreen){
                            game.googleConHandler.sendTurn(game.getMapGrids());                      }
                        synchronized (playableScreen){
                            playableScreen.notifyAll();
                        }
                    }
                }
        }
    }
}
