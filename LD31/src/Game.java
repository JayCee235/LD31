import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JComponent;


public class Game extends JComponent implements Runnable{
	public static final int HEIGHT = Main.WIDTH;
	public static final int WIDTH = Main.HEIGHT;
	
	long startTime;
	
	
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
		entities.add(player);
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
			System.out.println("It's snowing very hard");
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
		((Graphics2D) g).fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				if (board[i][j]!=null) {
					board[i][j].draw(g, i * tileX, j * tileY, tileX, tileY);
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
