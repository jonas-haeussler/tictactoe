package com.game.tictactoe.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.tictactoe.objects.MapGrid;
import com.game.tictactoe.screens.GameScreen;
import com.game.tictactoe.screens.PlayableScreen;

public class FieldListener extends ChangeListener {
    private MapGrid mapGrid;
    private PlayableScreen playableScreen;
    public FieldListener(MapGrid mapGrid, PlayableScreen playableScreen){
        this.mapGrid = mapGrid;
        this.playableScreen = playableScreen;

    }
    public FieldListener(){

    }
    @Override
    public void changed(ChangeEvent event, Actor actor) {
        if(mapGrid.isActive() && !mapGrid.hasWinner()) {
            Vector2 position = new Vector2(actor.getX(), actor.getY());
                if (playableScreen.getPlayer() && !(playableScreen.getKiPlayer1() == 2)) {
                    if(mapGrid.addPlayer2Move(position)){
                        playableScreen.setPlayer(!playableScreen.getPlayer());
                    }

                }
                else if(!playableScreen.getPlayer() && !(playableScreen.getKiPlayer1() == 1)){
                    if(mapGrid.addPlayer1Move(position)){
                        playableScreen.setPlayer(!playableScreen.getPlayer());
                    }
                }
        }
    }
}
