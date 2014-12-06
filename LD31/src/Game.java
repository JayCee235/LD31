import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JComponent;


public class Game extends JComponent implements Runnable{
	public static final int HEIGHT = Main.WIDTH;
	public static final int WIDTH = Main.HEIGHT;
	
	long startTime;
	
	public static HashMap<String, BufferedImage[]> sprites = new HashMap<String, BufferedImage[]>();
	
	
	Tile[][] board;
	int x, y;
	Color background;
	int tileX, tileY;
	int dcooldown;
	
	boolean paused;
	
	Player player;
	
	ArrayList<Entity> entities;
	ArrayList<Entity> toRemove;
	ArrayList<Entity> toAdd;
	
	boolean running;
	boolean blizzard;
	
	int enemyCooldown;
	int snowCooldown;
	
	public Game(int x, int y, Player player) {
		this.startTime = 0;
		
		//Sprite loading
		BufferedImage[] playerUp = new BufferedImage[4];
		BufferedImage[] playerDown = new BufferedImage[4];
		BufferedImage[] playerLeft = new BufferedImage[4];
		BufferedImage[] playerRight = new BufferedImage[4];
		BufferedImage[] enemyUp = new BufferedImage[4];
		BufferedImage[] enemyDown = new BufferedImage[4];
		BufferedImage[] enemyLeft = new BufferedImage[4];
		BufferedImage[] enemyRight = new BufferedImage[4];
		
		try {
			String path = "res/LD31/";
			String playerPath = "Player/Player_";
			String enemyPath = "Enemy/Enemy_";
			
			String w = "Up";
			String a = "Left";
			String s = "Down";
			String d = "Right";
			
			for(int i = 0; i < 4; i++) {
				int ind = 1;
				if(i == 1)
					ind++;
				if(i == 3)
					ind = i;
				String app = "" + ind + ".png";
				playerUp[i] = ImageIO.read(new File(path+playerPath+w+app));
				playerDown[i] = ImageIO.read(new File(path+playerPath+s+app));
				playerLeft[i] = ImageIO.read(new File(path+playerPath+a+app));
				playerRight[i] = ImageIO.read(new File(path+playerPath+d+app));
				
				enemyUp[i] = ImageIO.read(new File(path+enemyPath+w+app));
				enemyDown[i] = ImageIO.read(new File(path+enemyPath+s+app));
				enemyLeft[i] = ImageIO.read(new File(path+enemyPath+a+app));
				enemyRight[i] = ImageIO.read(new File(path+enemyPath+d+app));
				
			}
			sprites.put("playerUp", playerUp);
			sprites.put("playerDown", playerDown);
			sprites.put("playerLeft", playerLeft);
			sprites.put("playerRight", playerRight);
			
			sprites.put("enemyUp", enemyUp);
			sprites.put("enemyDown", enemyDown);
			sprites.put("enemyLeft", enemyLeft);
			sprites.put("enemyRight", enemyRight);
			
			
		} catch (IOException e) {
			System.out.println("Failed to load sprites!");
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
		Thread t = new Thread(this);
		this.startTime = System.currentTimeMillis();
		
		t.start();
	}
	
	@Override
	public void run() {
		while(this.running) {
			if (!paused) {
				this.tick();
			}
			this.repaint();
			
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				//TODO
				e.printStackTrace();
			}
		}
		//End code?
	}
	
	public void tick() {
		this.blizzard = blizzard?(Math.random()<0.999):(Math.random()<0.0002);	
		if(blizzard) {
			//TODO: Blizzard particles.
			for(int i = 0; i < board.length; i++) {
				for(int j = 0; j < board[i].length; j++) {
					board[i][j].fill(1);
				}
			}
			
		} else {
			
		}
		
		this.snowCooldown--;
		if(this.snowCooldown < 0 && this.blizzard) {
			entities.add(new Snowpile(Game.randInt(Main.WIDTH-16), Game.randInt(Main.HEIGHT-16), 
					16, 16, Color.white, this));
			this.snowCooldown = 400 + dcooldown++;
		}
		
		this.enemyCooldown--;
		if(this.enemyCooldown < 0 && !this.blizzard) {
			entities.add(new Enemy(Game.randInt(Main.WIDTH-16), Game.randInt(Main.HEIGHT-16), 
					16, 16, Color.red, this));
			
			this.enemyCooldown = 400 - dcooldown++;
		}
		
		if(dcooldown > 200) {
			dcooldown = 50;
		}
		
		
		for(Entity m : entities) {
			m.tick();
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
			m.draw(g);
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
	
	public void lose() {
		System.out.printf("You lost! You lasted %d seconds\n", (System.currentTimeMillis() - startTime) / 1000);
		System.exit(0);
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
		System.out.printf("You win! You won in %d seconds.\n", (System.currentTimeMillis() - startTime) / 1000);
		System.exit(0);
	}

}
