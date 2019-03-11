package com.game.tictactoe.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.game.tictactoe.objects.MapGrid;
import com.game.tictactoe.screens.GameScreen;

public class FieldListener extends ChangeListener {
    private MapGrid mapGrid;
    private GameScreen gameScreen;
    public FieldListener(MapGrid mapGrid, GameScreen gameScreen){
        this.mapGrid = mapGrid;
        this.gameScreen = gameScreen;

    }
    @Override
    public void changed(ChangeEvent event, Actor actor) {
        if(mapGrid.isActive() && !mapGrid.hasWinner()) {
            Vector2 position = new Vector2(actor.getX(), actor.getY());
                if (gameScreen.getPlayer()) {
                    if(mapGrid.addPlayer2Move(position)){
                        gameScreen.setPlayer(!gameScreen.getPlayer());
                    }

                } else {
                    if(mapGrid.addPlayer1Move(position)){
                        gameScreen.setPlayer(!gameScreen.getPlayer());
                    }
                }
        }
    }
}
