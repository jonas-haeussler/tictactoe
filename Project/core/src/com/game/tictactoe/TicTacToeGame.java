package com.game.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.tictactoe.objects.MapGrid;
import com.game.tictactoe.screens.MainMenuScreen;
import com.game.tictactoe.screens.PlayableScreen;
import com.game.tictactoe.utils.AdShower;
import com.game.tictactoe.utils.AssetLoader;
import com.game.tictactoe.utils.ConnectionHandler;

import java.util.ArrayList;

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
	public Sound buttonSound, circleSound, crossSound, fieldSound;
	public Music gameMusic, drawMusic, winnerMusic;
	public long crossSoundId, circleSoundId, fieldSoundId;
	public boolean btConnected, btOn, isServer;
	public ArrayList<String> btDevices;
	public ArrayList<String> btDeviceAdresses;
	public String activeBTName;
	public byte player;
	private MapGrid[][] mapGrids;
	public ConnectionHandler conHandler;
	public AdShower adShower;


	public BitmapFont createFont(FreeTypeFontGenerator ftfg, int size) {
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = size;
		return ftfg.generateFont(parameter);
	}
	public void setBTDevices(ArrayList<String>[] devices){
		btDevices = devices[0];
		btDeviceAdresses = devices[1];
	}
	public void setConHandler(ConnectionHandler conHandler){
		this.conHandler = conHandler;
	}
	public void setActiveBtDevice(String name){
		activeBTName = name;
		conHandler.startClient();
	}

	public boolean getTurn(){
		if(screen instanceof PlayableScreen){
			return ((PlayableScreen)screen).getPlayer();
		}
		else {
			return false;
		}
	}
	public int getFieldSize(){
		if(screen instanceof PlayableScreen){
			return ((PlayableScreen)screen).getFieldSize();
		}
		return 3;
	}
	public MapGrid[][] getMapGrids(){
		if(screen instanceof PlayableScreen){
			return ((PlayableScreen) screen).getMapGrids();
		}
		return null;
	}
	public void addPlayerMove(int i, int j, int k, int l){
		if(screen instanceof PlayableScreen){
			if(player == 1) {
				((PlayableScreen) screen).getMapGrids()[i][j].addPlayer1Move(new Vector2(((PlayableScreen) screen).getMapGrids()[i][j].getButtons()[k][l].getX()
						, ((PlayableScreen) screen).getMapGrids()[i][j].getButtons()[k][l].getY()));
			}
			else if(player == 2){
				((PlayableScreen) screen).getMapGrids()[i][j].addPlayer2Move(new Vector2(((PlayableScreen) screen).getMapGrids()[i][j].getButtons()[k][l].getX()
						, ((PlayableScreen) screen).getMapGrids()[i][j].getButtons()[k][l].getY()));
			}
		}
//		System.out.println("Player move made");
	}


	@Override
	public void create () {
		batch = new SpriteBatch();
//		Gdx.input.setCatchBackKey(true);
		loader = new AssetLoader(this);
		loader.loadAll();
		font1 = createFont(generator1, (int) (Gdx.graphics.getWidth() / 12f));
		font2 = createFont(generator2, (int) (Gdx.graphics.getWidth() / 10f));
		font3 = createFont(generator3, (int) (Gdx.graphics.getWidth() / 8f));
		font2.getData().markupEnabled = true;

		// add music
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.3f);
		gameMusic.play();


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
		winnerMusic.dispose();
		fieldSound.dispose();
		crossSound.dispose();
		circleSound.dispose();
		drawMusic.dispose();
		gameMusic.dispose();
		batch.dispose();

	}
}
