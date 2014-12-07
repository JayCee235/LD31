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

import javax.imageio.ImageIO;
import javax.swing.JComponent;


public class Game extends JComponent implements Runnable{
	public static final int HEIGHT = Main.WIDTH;
	public static final int WIDTH = Main.HEIGHT;
	
	long startTime;
	long endTime;
	
	public static HashMap<String, BufferedImage[]> sprites = new HashMap<String, BufferedImage[]>();
	public static boolean imgLoaded = false;
	
	private static BufferedImage pauseOverlay, winOverlay, loseOverlay;
	
	private static BufferedImage numbers; 
	
	boolean endGame;
	
	Tile[][] board;
	int x, y;
	Color background;
	int tileX, tileY;
	int dcooldown;
	
	int blizzardTimer;
	
	boolean paused;
	double blizIndex;
	
	Player player;
	
	ArrayList<Entity> entities;
	ArrayList<Entity> toRemove;
	ArrayList<Entity> toAdd;
	
	boolean running;
	boolean blizzard;
	
	int enemyCooldown;
	int snowCooldown;
	private boolean win;
	
	public Game(int x, int y, Player player) {
		this.win = false;
		this.endGame = false;
		this.blizIndex = 0;
		
		this.blizzardTimer = 1000;
		
		this.startTime = 0;
		this.endTime = 0;
		
		if (!Game.imgLoaded) {
			//Sprite loading
			BufferedImage[] playerUp = new BufferedImage[4];
			BufferedImage[] playerDown = new BufferedImage[4];
			BufferedImage[] playerLeft = new BufferedImage[4];
			BufferedImage[] playerRight = new BufferedImage[4];
			BufferedImage[] enemyUp = new BufferedImage[4];
			BufferedImage[] enemyDown = new BufferedImage[4];
			BufferedImage[] enemyLeft = new BufferedImage[4];
			BufferedImage[] enemyRight = new BufferedImage[4];
			BufferedImage[] snowImg = new BufferedImage[3];
			BufferedImage[] wellImg = new BufferedImage[3];
			BufferedImage[] wallImg = new BufferedImage[1];
			BufferedImage[] turretImg = new BufferedImage[1];
			BufferedImage[] snowball = new BufferedImage[1];
			BufferedImage[] tileImg = new BufferedImage[2];
			BufferedImage[] snowfall = new BufferedImage[4];
			BufferedImage[] bars = new BufferedImage[4];
			try {
				String path = "res/LD31/";
				String playerPath = "Player/Player_";
				String enemyPath = "Enemy/Enemy_";

				String w = "Up";
				String a = "Left";
				String s = "Down";
				String d = "Right";

				for (int i = 0; i < 4; i++) {
					int ind = 1;
					if (i == 1)
						ind++;
					if (i == 3)
						ind = i;
					String app = "" + ind + ".png";
					
					playerUp[i] = ImageIO.read(this.getClass().getResource(path + playerPath + w
							+ app));
					playerDown[i] = ImageIO.read(this.getClass().getResource(path + playerPath + s
							+ app));
					playerLeft[i] = ImageIO.read(this.getClass().getResource(path + playerPath + a
							+ app));
					playerRight[i] = ImageIO.read(this.getClass().getResource(path + playerPath
							+ d + app));

					enemyUp[i] = ImageIO.read(this.getClass().getResource(path + enemyPath + w
							+ app));
					enemyDown[i] = ImageIO.read(this.getClass().getResource(path + enemyPath + s
							+ app));
					enemyLeft[i] = ImageIO.read(this.getClass().getResource(path + enemyPath + a
							+ app));
					enemyRight[i] = ImageIO.read(this.getClass().getResource(path + enemyPath + d
							+ app));

				}
				String snowPath = "Entity/SnowPile";
				String wallPath = "Entity/Wall";
				String wellPath = "Entity/Well";
				String turretPath = "Entity/Turret";

				String app = ".png";

				for (int i = 0; i < 3; i++) {
					snowImg[i] = ImageIO.read(this.getClass().getResource(path + snowPath
							+ (i + 1) + app));
				}
				for (int i = 0; i < 3; i++) {
					wellImg[i] = ImageIO.read(this.getClass().getResource(path + wellPath
							+ (i + 1) + app));
				}
				for (int i = 0; i < 1; i++) {
					wallImg[i] = ImageIO.read(this.getClass().getResource(path + wallPath + app));
				}
				for (int i = 0; i < 1; i++) {
					turretImg[i] = ImageIO.read(this.getClass().getResource(path + turretPath
							+ app));
				}

				for (int i = 0; i < 4; i++) {
					snowfall[i] = ImageIO.read(this.getClass().getResource(path + "Tile/Snow"
							+ (i + 1) + ".png"));
				}

				tileImg[0] = ImageIO.read(this.getClass().getResource(path + "Tile/Snow.png"));
				tileImg[1] = ImageIO.read(this.getClass().getResource(path + "Tile/Burn.png"));

				snowball[0] = ImageIO.read(this.getClass().getResource(path
						+ "Entity/Snowball.png"));
				
				bars[0] = ImageIO.read(this.getClass().getResource(path + "SnowBar.png"));
				bars[1] = ImageIO.read(this.getClass().getResource(path + "XPBar.png"));
				bars[2] = ImageIO.read(this.getClass().getResource(path + "HPBar.png"));
				bars[3] = ImageIO.read(this.getClass().getResource(path + "HPBarBig.png"));

				sprites.put("playerUp", playerUp);
				sprites.put("playerDown", playerDown);
				sprites.put("playerLeft", playerLeft);
				sprites.put("playerRight", playerRight);

				sprites.put("enemyUp", enemyUp);
				sprites.put("enemyDown", enemyDown);
				sprites.put("enemyLeft", enemyLeft);
				sprites.put("enemyRight", enemyRight);

				sprites.put("snowPile", snowImg);
				sprites.put("wall", wallImg);
				sprites.put("well", wellImg);
				sprites.put("turret", turretImg);
				sprites.put("snowball", snowball);

				sprites.put("tile", tileImg);

				sprites.put("blizzard", snowfall);
				sprites.put("bars", bars);

				Game.pauseOverlay = ImageIO.read(this.getClass().getResource(path
						+ "PauseScreen.png"));
				Game.winOverlay = ImageIO
						.read(this.getClass().getResource(path + "WinScreen.png"));
				Game.loseOverlay = ImageIO.read(this.getClass().getResource(path
						+ "LoseScreen.png"));

				Game.numbers = ImageIO.read(this.getClass().getResource(path + "Numbers.png"));

				Game.imgLoaded = true;
			} catch (IOException e) {
				System.out.println("Failed to load sprites!");
			}
		}
		//Sound loading
		if(!SoundUtil.loaded) {
			SoundUtil.setup();
		}
		
		this.board = new Tile[x][y];
		this.x = x;
		this.y = y;
		this.tileX = Main.WIDTH / x;
		this.tileY = Main.HEIGHT / y;
		
		this.paused = false;
		
		this.background = Color.green.darker();
		
		this.toRemove = new ArrayList<Entity>();
		this.toAdd = new ArrayList<Entity>();
		this.entities = new ArrayList<Entity>();
		if (player!=null) {
			entities.add(player);
		}
		this.player = player;
		
		this.enemyCooldown = 0;
		this.snowCooldown = 0;
		this.dcooldown = 0;
		
		for(int i = 0; i < 10; i++) {
			int xr = (int) ((Main.WIDTH-16) * Math.random());
			int yr = (int) ((Main.HEIGHT-16) * Math.random());
			
			Snowpile s = new Snowpile(xr, yr, 16, 16, Color.white, this);
			
			entities.add(s);
		}
		
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				board[i][j] = new Tile(i, j, Color.white.darker());
			}
		}
		
		
	}
	
	public void start() {
		this.running = true;
		this.blizzard = true;
		this.paused = true;
		Thread t = new Thread(this);
		
		this.startTime = System.currentTimeMillis();
		
		t.start();
		
	}
	
	@Override
	public void run() {
		SoundUtil.playMusic("LD31_Sno-Man.wav");
		while(this.running) {
			if (!paused && !endGame) {
				this.tick();
			}
			this.repaint();
			
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				
			}
		}
		return;
	}
	
	public void tick() {
		if(blizzard) {
			this.blizzardTimer--;
			if(this.blizzardTimer <= 0)
				this.blizzard = false;
			//DONE: Blizzard particles.
			for(int i = 0; i < board.length; i++) {
				for(int j = 0; j < board[i].length; j++) {
					board[i][j].fill(1);
				}
			}		
		} else {
			double rand = Math.random();
			
			if(rand < 1.0/3200.0) {
				this.blizzard = true;
				this.blizzardTimer = (int) (600 * 3200 * rand);
			}
		}
		
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
		
		if(dcooldown > 400) {
			dcooldown = 0;
			this.blizzard = true;
		}
		
		for(Entity m : entities) {
			m.tick();
		}
		
		while(toAdd.size()>0) {
			Entity m = toAdd.get(0);
			entities.add(m);
			toAdd.remove(m);
		}
		
		while(toRemove.size()>0) {
			Entity m = toRemove.get(0);
			entities.remove(m);
			toRemove.remove(m);
		}
	}
	
	public void remove(Entity e) {
		toRemove.add(e);
	}
	
	public void add(Entity e) {
		toAdd.add(e);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(this.background);
		((Graphics2D) g).fillRect(0, 0, Main.WIDTH * Main.SCALE, Main.HEIGHT * Main.SCALE);
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				if (board[i][j]!=null) {
					board[i][j].draw(g, i * tileX * Main.SCALE, j * tileY * Main.SCALE, 
							tileX * Main.SCALE, tileY * Main.SCALE);
				}
			}
		}
		for(Entity m : entities) {
			if (m!=this.player) {
				m.draw(g);
			}
		}
		this.player.draw(g);
		
		//Moved to tick()
		
//		while(toAdd.size()>0) {
//			Entity m = toAdd.get(0);
//			entities.add(m);
//			toAdd.remove(m);
//		}
//		
//		while(toRemove.size()>0) {
//			Entity m = toRemove.get(0);
//			entities.remove(m);
//			toRemove.remove(m);
//		}
		
		if(blizzard) {
			int sc = Main.SCALE;
			for(int i = 0; i < Main.WIDTH / 8 + 1; i++) {
				for(int j = 0; j < Main.HEIGHT / 8 + 1; j++) {
					g.drawImage(sprites.get("blizzard")[(int) blizIndex], 8*i*sc, 8*j*sc, 8*sc, 8*sc, null);
				}
			}
			blizIndex += 0.1;
			if(blizIndex >= 4) {
				blizIndex = 0;
			}
		}
		
		//DONE: Pause menu overlay. 
		if(paused && !endGame) {
			int sc = Main.SCALE;
			g.drawImage(pauseOverlay, 0, 0, Main.WIDTH * sc, Main.HEIGHT * sc, 
					0, 0, pauseOverlay.getWidth(), pauseOverlay.getHeight(), null);
		}
		
		if(endGame) {
			int sc = Main.SCALE;
			g.drawImage(win?winOverlay:loseOverlay, 0, 0, Main.WIDTH * sc, Main.HEIGHT * sc, 
					0, 0, pauseOverlay.getWidth(), pauseOverlay.getHeight(), null);
			//TODO;
			
			long time = (endTime - startTime) / 1000;
			int t = (int) time;
			
			String draw = Integer.toString(t);
			
			for (int index = 0; index < draw.length(); index++) {
				int x = 0;
				int y = 0;
				int d = draw.charAt(index) - 48;
				
				if(d==0) {
					x = 4 * 8;
					y = 2 * 8;
				} else {
					x = 8*((d-1) % 5);
					y = 8*((d-1) / 5);
				}
				
				int startx = sc * (1 + 9*index);
				int starty = sc * (1 + 9 * 3);
				
				int endx = startx + sc*8;
				int endy = starty + sc*8;
				
				g.drawImage(numbers, startx, starty, endx, endy, x, y, x + 8, y + 8, null);
			}
			
			
		}
	}
	
	public void lose() {
		this.paused = true;
		this.endTime = System.currentTimeMillis();
		this.win = false;
		this.endGame = true;
	}
	
	public static int randInt(int max) {
		return (int) (Math.random() * max);
	}
	
	public void pause() {
		this.paused = !paused;
	}
	
	public static Tile getAt(Tile[][] board, int x, int y, int tileSizeX, int tileSizeY) {
		int ix = x / tileSizeX;
		int iy = y / tileSizeY;
		
		if(ix >= 0 && ix < board.length && iy >= 0 && iy < board[ix].length) {
			return board[ix][iy];
		} else {
			return null;
		}
	}

	public void win() {
		this.paused = true;
		this.endTime = System.currentTimeMillis();
		this.win = true;
		this.endGame = true;
	}

	public void restart() {
		this.stop();
		Main.restart();
	}
	
	public void stop() {
		this.running = false;
	}

}
