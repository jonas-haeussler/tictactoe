package com.game.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.tictactoe.screens.MainMenuScreen;
import com.game.tictactoe.utils.AssetLoader;

public class TicTacToeGame extends Game {
	public SpriteBatch batch;
	public Texture background, cup, swords;
	public Skin comicSkin;
	private AssetLoader loader;
	public FreeTypeFontGenerator generator1;
	public FreeTypeFontGenerator generator2;
	public FreeTypeFontGenerator generator3;
	public BitmapFont font1;
	public BitmapFont font2;
	public BitmapFont font3;
	public Sound buttonSound, circleSound, crossSound, winnerSound, fieldSound;
	public Music gameMusic;

	public BitmapFont createFont(FreeTypeFontGenerator ftfg, int size) {
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = size;
		return ftfg.generateFont(parameter);
	}
	@Override
	public void create () {
		batch = new SpriteBatch();
		loader = new AssetLoader(this);
		loader.loadAll();
		font1 = createFont(generator1, (int) (Gdx.graphics.getWidth() / 10f));
		font2 = createFont(generator2, (int) (Gdx.graphics.getWidth() / 8f));
		font3 = createFont(generator3, (int) (Gdx.graphics.getWidth() / 8f));
		font2.getData().markupEnabled = true;

		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		background.dispose();
		cup.dispose();
		swords.dispose();
		comicSkin.dispose();
		generator1.dispose();
		generator2.dispose();
		generator3.dispose();
		font1.dispose();
		font2.dispose();
		font3.dispose();
		buttonSound.dispose();
		winnerSound.dispose();
		fieldSound.dispose();
		crossSound.dispose();
		circleSound.dispose();
		gameMusic.dispose();
		batch.dispose();

	}
}
