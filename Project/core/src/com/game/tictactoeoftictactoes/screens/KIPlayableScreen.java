package com.game.tictactoeoftictactoes.screens;

import com.game.tictactoeoftictactoes.TicTacToeGame;

public class KIPlayableScreen extends PlayableScreen {

    public KIPlayableScreen(TicTacToeGame game, byte kiPlayer, byte kiLevel) {
        super(game, kiPlayer, kiLevel);

        setPlayer(false);

    }
    @Override
    public void setPlayer(boolean player){
        super.setPlayer(player);
        if(player && getKiPlayer1() == 2 || !player && getKiPlayer1() == 1){
            playerMovementLabel.setText("Enemy Turn");
        }
        else {
            playerMovementLabel.setText("Your Turn");
        }
    }
}
