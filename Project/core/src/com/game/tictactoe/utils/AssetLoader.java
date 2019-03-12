package com.game.tictactoe.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.tictactoe.TicTacToeGame;

public class AssetLoader {
    private TicTacToeGame game;

    public AssetLoader(TicTacToeGame game){
        this.game = game;
    }

    public void loadAll(){
        this.game = game;
        game.background = new Texture("PNG/squaredPaper.png");
        game.menuIcon = new Texture("PNG/menuIcon.png");
        game.replayIcon = new Texture("PNG/replayIcon.png");
        game.comicSkin = new Skin(Gdx.files.internal("skin/gdx-skins-master/comic/skin/comic-ui.json"));
        game.generator1 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SF_Arch_Rival.ttf"));
        game.generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/newcomictitle3d.ttf"));
    }

    public void dispose(){
        game.background.dispose();
    }
}
