package com.game.tictactoe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.tictactoe.screens.GameScreen;
import com.game.tictactoe.screens.MainMenuScreen;
import com.game.tictactoe.utils.AssetLoader;

public class TicTacToeGame extends Game {
	public SpriteBatch batch;
	public Texture background;
	public Texture menuIcon, replayIcon;
	public Skin comicSkin;
	private AssetLoader loader;
	public FreeTypeFontGenerator generator1;
	public FreeTypeFontGenerator generator2;
	public BitmapFont font1;
	public BitmapFont font2;

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
		font2.getData().markupEnabled = true;

		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
