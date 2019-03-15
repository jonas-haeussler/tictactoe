package com.game.tictactoe.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Sound;
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
        game.cup = new Texture("PNG/cup.png");
        game.swords = new Texture("PNG/swords.png");
        game.comicSkin = new Skin(Gdx.files.internal("skin/gdx-skins-master/comic/skin/comic-ui.json"));
        game.generator1 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SF_Arch_Rival.ttf"));
        game.generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/newcomictitle3d.ttf"));
        game.generator3 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/newcomictitlehalf.ttf"));

        game.buttonSound = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonSound.ogg"));
        game.circleSound = Gdx.audio.newSound(Gdx.files.internal("sounds/circleSound.ogg"));
        game.crossSound = Gdx.audio.newSound(Gdx.files.internal("sounds/crossSound.ogg"));
        game.fieldSound = Gdx.audio.newSound(Gdx.files.internal("sounds/fieldSound.ogg"));
        game.winnerMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/winnerSound.wav"));
        game.drawMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/drawSound.wav"));
        game.gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameMusic.mp3"));
    }

}
