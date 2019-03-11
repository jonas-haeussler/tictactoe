package com.game.tictactoe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.tictactoe.screens.GameScreen;
import com.game.tictactoe.utils.AssetLoader;

public class TicTacToeGame extends Game {
	public SpriteBatch batch;
	public Texture background;
	public Skin comicSkin;
	private AssetLoader loader;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		loader = new AssetLoader(this);
		loader.loadAll();

		this.setScreen(new GameScreen(this));
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
