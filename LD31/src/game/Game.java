package game;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Handles most game logic, besides key listening implemented in Player.
 * @author JayCee235
 *
 */
public class Game extends JComponent implements Runnable{
	public static final int HEIGHT = Main.WIDTH;
	public static final int WIDTH = Main.HEIGHT;
	
	long timePlayed;
	long lastTick;
	
	public SpriteLibrary sprites = new SpriteLibrary();
	public SpriteLibrary font;
	
	/**
	 * True if images have already been loaded. If true, restart will pass the current Game's library 
	 * to the new game.
	 */
	public static boolean imgLoaded = false;
	
	private static BufferedImage pauseOverlay, winOverlay, loseOverlay;
	
	boolean endGame;
	boolean debugMode;
	
	Tile[][] board;
	int x, y;
	Color background;
	int tileX, tileY;
	int dcooldown;
	
	int blizzardTimer;
	
	boolean paused;
	double blizIndex;
	
	Player player;
	
	LinkedList<Entity> entities;
	LinkedList<Entity> toRemove;
	LinkedList<Entity> toAdd;
	
	boolean running;
	boolean blizzard;
	
	int enemyCooldown;
	int snowCooldown;
	private boolean win;
	
	public Game(int x, int y, Player player, SpriteLibrary library, SpriteLibrary font) {
		this.win = false;
		this.endGame = false;
		this.debugMode = false;
		this.blizIndex = 0;
		
		this.blizzardTimer = 1000;
		
		timePlayed = 0;
		lastTick = System.currentTimeMillis();
		
		if (library==null) {
			//Sprite loading
			try {
				String path = "res/LD31/";
				String playerPath = "Player/Player_";
				String enemyPath = "Enemy/Enemy_";

				String w = "Up";
				String a = "Left";
				String s = "Down";
				String d = "Right";
				
				String[] order = new String[]{"1","2","1","3"};
				
				sprites.addSprite("playerUp", path+playerPath+w, order, ".png");
				sprites.addSprite("playerDown", path+playerPath+s, order, ".png");
				sprites.addSprite("playerLeft", path+playerPath+a, order, ".png");
				sprites.addSprite("playerRight", path+playerPath+d, order, ".png");
				
				sprites.addSprite("enemyUp", path+enemyPath+w, order, ".png");
				sprites.addSprite("enemyDown", path+enemyPath+s, order, ".png");
				sprites.addSprite("enemyLeft", path+enemyPath+a, order, ".png");
				sprites.addSprite("enemyRight", path+enemyPath+d, order, ".png");
				
				String snowPath = "Entity/SnowPile";
				String wallPath = "Entity/Wall";
				String wellPath = "Entity/Well";
				String turretPath = "Entity/Turret";

				String app = ".png";
				
				sprites.addSprite("snowPile", path+snowPath, new String[]{"1","2","3"}, app);
				sprites.addImage("wall", path+wallPath + app);
				sprites.addSprite("well", path+wellPath, new String[]{"1","2","3"}, app);
				sprites.addImage("turret", path+turretPath+app);
				sprites.addSprite("blizzard", path+"Tile/Snow", new String[]{"1","2","3","4"}, app);
				
				sprites.addSprite("tile", path+"Tile/", new String[]{"Snow", "Burn"}, app);
				sprites.addImage("snowball", path + "Entity/Snowball.png");
				
				String[] bars = new String[]{"SnowBar", "XPBar", "HPBar", "HPBarBig"};
				sprites.addSprite("bars", path, bars, app);

				Game.pauseOverlay = ImageIO.read(this.getClass().getResource(path
						+ "PauseScreenBackground.png"));
				Game.winOverlay = ImageIO
						.read(this.getClass().getResource(path + "WinScreenBackground.png"));
				Game.loseOverlay = ImageIO.read(this.getClass().getResource(path
						+ "LoseScreenBackground.png"));

				String letter = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z . ! ? : 1 2 3 4 5 6 7 8 9 0";
				String[] letters = letter.split(" ");;
				this.font = new SpriteLibrary("", letters, path+"ScaledFont.png", 7, 7, 1, 1);

				Game.imgLoaded = true;
				
				
				
			} catch (IOException e) {
				String plus = e.getMessage();
				System.out.println("Failed to load sprites!\nDetals: " +
				plus==null?"Unknown exception":plus);
			}
		} else {
			this.sprites = library;
			this.font = font;
		}
		//Sound loading
		if(!SoundUtil.loaded) {
			SoundUtil.setup();
		}
		
		//Sets up a new board.
		this.board = new Tile[x][y];
		this.x = x;
		this.y = y;
		this.tileX = Main.WIDTH / x;
		this.tileY = Main.HEIGHT / y;
		
		this.paused = false;
		
		this.background = Color.green.darker();
		
		/* TODO: Possibly store entities in a way that specific kinds can be accessed quicker for looping.
		 * Possibly an array, where index 0 is the player, 1 is enemies, etc.
		 * This makes Building's loops looking for enemies or buildings faster.
		 */
		
		//Sets up new lists for entities.
		this.toRemove = new LinkedList<Entity>();
		this.toAdd = new LinkedList<Entity>();
		this.entities = new LinkedList<Entity>();
		if (player!=null) {
			entities.add(player);
		}
		this.player = player;
		
		this.enemyCooldown = 0;
		this.snowCooldown = 0;
		this.dcooldown = 0;
		
		//Spawns 10 random snow piles.
		for(int i = 0; i < 10; i++) {
			int xr = (int) ((Main.WIDTH-16) * Math.random());
			int yr = (int) ((Main.HEIGHT-16) * Math.random());
			
			Snowpile s = new Snowpile(xr, yr, 16, 16, Color.white, this);
			
			entities.add(s);
		}
		
		//Fills the board with new snow tiles.
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				board[i][j] = new Tile(i, j, Color.white.darker(), sprites);
			}
		}
		
		
	}
	
	/**
	 * Starts the game.
	 */
	public void start() {
		this.running = true;
		this.blizzard = true;
		this.paused = true;
		Thread t = new Thread(this);
		
		t.start();
		
	}
	
	@Override
	public void run() {
		SoundUtil.playMusic("LD31_Sno-Man.wav");
		while(this.running) {
			if (!paused && !endGame) {
				this.tick();
			}
			long newTime = System.currentTimeMillis();
			if(!this.paused) {
				long diff = newTime - this.lastTick;
				this.timePlayed += diff;
			}
			this.lastTick = newTime;
			
			this.repaint();
			
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				
			}
		}
		System.gc();
		return;
	}
	
	/**
	 * tick for the game. All logic is handled here.
	 */
	public void tick() {
		//Takes care of blizzards
		if(blizzard) {
			this.blizzardTimer--;
			if(this.blizzardTimer <= 0)
				this.blizzard = false;
			for(int i = 0; i < board.length; i++) {
				for(int j = 0; j < board[i].length; j++) {
					board[i][j].fill(1);
				}
			}		
		} else {
			double rand = Math.random();
			
			if(rand < 1.0/3200.0) {
				this.blizzard = true;
				//TODO: Look at this number. Tweak it a bit to get good random blizzards.
				this.blizzardTimer = (int) (600 * 3200 * rand);
			}
		}
		
		//Spawns snow on a cooldown.
		this.snowCooldown--;
		if(this.snowCooldown < 0 && this.blizzard) {
			entities.add(new Snowpile(Game.randInt(Main.WIDTH-16), Game.randInt(Main.HEIGHT-16), 
					16, 16, Color.white, this));
			if(dcooldown < 100) {
				entities.add(new Snowpile(Game.randInt(Main.WIDTH-16), Game.randInt(Main.HEIGHT-16), 
						16, 16, Color.white, this));
			}
			this.snowCooldown = 400 + dcooldown * 10 / 25;
			dcooldown += 25;
		}
		
		//Spawns enemies on a cooldown.
		this.enemyCooldown--;
		if(this.enemyCooldown < 0 && !this.blizzard) {
			entities.add(new Enemy(Game.randInt(Main.WIDTH-16), Game.randInt(Main.HEIGHT-16), 
					16, 16, Color.red, this));
			if(dcooldown > 200) {
				entities.add(new Enemy(Game.randInt(Main.WIDTH-16), Game.randInt(Main.HEIGHT-16), 
						16, 16, Color.red, this));
			}
			
			this.enemyCooldown = 400 - dcooldown * 10 / 25;
			dcooldown += 25;
		}
		
		//'Break' blizzard. 
		if(dcooldown > 400) {
			dcooldown = 0;
			blizzardTimer = 600;
			this.blizzard = true;
		}
		
		for(Entity m : entities) {
			m.tick();
		}
		
		//Adds all new entities to the game.
		while(toAdd.size()>0) {
			Entity m = toAdd.get(0);
			entities.add(m);
			toAdd.remove(m);
		}
		
		//Removes all removed entities from the game.
		while(toRemove.size()>0) {
			Entity m = toRemove.get(0);
			entities.remove(m);
			toRemove.remove(m);
		}
	}
	/**
	 * Adds the entity to a list to be removed from the game later.
	 * @param e
	 */
	public void remove(Entity e) {
		toRemove.add(e);
	}
	
	/**
	 * Adds the entity to a list to be added to a game later.
	 * @param e
	 */
	public void add(Entity e) {
		toAdd.add(e);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//Applets are weird.
		if(Main.appletMode) {
			super.paintComponent(g);
		}
		
		//First the background...
		g.setColor(this.background);
		((Graphics2D) g).fillRect(0, 0, Main.WIDTH * Main.SCALE, Main.HEIGHT * Main.SCALE);
		
		//...Now the ground...
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				if (board[i][j]!=null) {
					board[i][j].draw(g, i * tileX * Main.SCALE, j * tileY * Main.SCALE, 
							tileX * Main.SCALE, tileY * Main.SCALE);
				}
			}
		}
		
		//..And then everything on top.
		for(Entity m : entities) {
			//Player is drawn on the very top.
			if (m!=this.player) {
				m.draw(g);
			}
		}
		this.player.draw(g);
		
		//Draw blizzard particles.
		if(blizzard) {
			int sc = Main.SCALE;
			for(int i = 0; i < Main.WIDTH / 8 + 1; i++) {
				for(int j = 0; j < Main.HEIGHT / 8 + 1; j++) {
					g.drawImage(sprites.getSprite("blizzard")[(int) blizIndex], 8*i*sc, 8*j*sc, 8*sc, 8*sc, null);
				}
			}
			//Moves blizzard animation forward, at 0.1 frames/tick.
			blizIndex += 0.1;
			if(blizIndex >= 4) {
				blizIndex = 0;
			}
		}
		
		//Draws pause screen if paused. 
		//!endGame because endGame is also paused, and draws a different screen.
		if(paused && !endGame) {
			int sc = Main.SCALE;
			g.drawImage(pauseOverlay, 0, 0, Main.WIDTH * sc, Main.HEIGHT * sc, 
					0, 0, pauseOverlay.getWidth(), pauseOverlay.getHeight(), null);
			this.drawString(g, "PAUSED", 8, 8);
			
			this.drawString(g, "WASD to move", 8, 48);
			this.drawString(g, "C to build", 8, 56);
			this.drawString(g, "X to change buildings", 8, 64);
			this.drawString(g, "P to pause:unpause", 8, 80);
		}
		
		if(endGame) {
			int sc = Main.SCALE;
			long time = timePlayed / 1000;
			int t = (int) time;
			
			String draw = Integer.toString(t);
			if(win) {
				g.drawImage(winOverlay, 0, 0, Main.WIDTH*sc, Main.HEIGHT*sc, 0, 0, 
						winOverlay.getWidth(), winOverlay.getHeight(), null);
				this.drawString(g, "You win!", 8, 8);
				
			} else {
				g.drawImage(loseOverlay, 0, 0, Main.WIDTH*sc, Main.HEIGHT*sc, 0, 0, 
						loseOverlay.getWidth(), loseOverlay.getHeight(), null);
				this.drawString(g, "You lose!", 8, 8);
			}
			this.drawString(g, "Time: " + draw, 8, 16);
			
		}
		
		//Draws the debugMode screen on top of everything.
		if(debugMode) {
			int diff = (int) (timePlayed)/1000;
			this.drawString(g, "time: " + diff, 8, 200);
		}
	}
	
	/**
	 * Draws a string at a specified, un-scaled location.
	 * @param g
	 * Graphics to draw on.
	 * @param s
	 * String to draw
	 * @param startX
	 * x-position to draw string, before scaling.
	 * @param startY
	 * y-position to draw string, before scaling.
	 */
	public void drawString(Graphics g, String s, int startX, int startY) {
		char[] c = s.toCharArray();
		for(int i = 0; i < c.length; i++) { 
			char cUp = Character.toUpperCase(c[i]);
			String cc = ""+cUp;
			if(cc.equals(" ")) continue;
			g.drawImage(font.getSprite(cc)[0], 
					Main.SCALE*startX + 8*i*Main.SCALE, Main.SCALE*startY, 
					7*Main.SCALE, 7*Main.SCALE, null);
		}
	}
	
	/**
	 * Loses the game.
	 */
	public void lose() {
		this.paused = true;
		this.win = false;
		this.endGame = true;
	}
	
	/**
	 * Returns a random integer between 0 and max value, including both 0 and max.
	 * @param max
	 * upper bound
	 * @return
	 */
	public static int randInt(int max) {
		return (int) (Math.random() * (max+1));
	}
	
	/**
	 * Pauses or unpauses the game.
	 */
	public void pause() {
		this.paused = !paused;
	}
	
	/**
	 * Returns the tile at the specified position. Mainly for getting underfoot of entities.
	 * @param board
	 * array to pull tiles from
	 * @param x
	 * x-position of tile to get. (NOT position in array)
	 * @param y
	 * y-position of tile to get. (NOT position in array)
	 * @param tileSizeX
	 * Width of tiles.
	 * @param tileSizeY
	 * Height of tiles.
	 * @return
	 * The tile under the given position.
	 */
	public static Tile getAt(Tile[][] board, int x, int y, int tileSizeX, int tileSizeY) {
		int ix = x / tileSizeX;
		int iy = y / tileSizeY;
		
		if(ix >= 0 && ix < board.length && iy >= 0 && iy < board[ix].length) {
			return board[ix][iy];
		} else {
			return null;
		}
	}

	/**
	 * Wins the game.
	 */
	public void win() {
		this.paused = true;
		this.win = true;
		this.endGame = true;
	}

	/**
	 * Restarts the game.
	 */
	public void restart() {
		this.stop();
		Main.restart();
	}
	
	/**
	 * Ends the game.
	 */
	public void stop() {
		this.running = false;
	}
	
	/**
	 * Returns the Game's SpriteLibrary. Used to pass it to a new game during restart.
	 * @return
	 */
	public SpriteLibrary getLibrary() {
		return this.sprites;
	}

}
