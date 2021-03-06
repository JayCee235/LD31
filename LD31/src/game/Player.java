package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

/**
 * The player class. Self explanitory.
 * @author JayCee235
 *
 */
public class Player extends Mob implements KeyListener{
	boolean[] keysDown;
	int snowCount;
	int turretCooldown;
	
	int switchCooldown;
	int fadeTime;
	int stepTime;
	
	int goal;
	
	Game game;
	
	int buildType;
	long lastPause;

	public Player(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color);
		this.moveSpeed = 1.75;
		this.stepTime = 30;
		
		this.goal = 15000;
		
		this.keysDown = new boolean[65535];
		
		this.snowCount = 3000;
		
		this.turretCooldown = 0;
		this.buildType = 0;
		
		this.lastPause = System.currentTimeMillis();
		
		this.game = game;
	}
	
	/**
	 * Gets the proper sprites from game, and sets the player to facing Down.
	 */
	public void loadSprites() {
		this.up = game.sprites.getSprite("playerUp");
		this.down = game.sprites.getSprite("playerDown");
		this.left = game.sprites.getSprite("playerLeft");
		this.right = game.sprites.getSprite("playerRight");
		
		this.sprite = this.down;
	}

	@Override
	public void keyPressed(KeyEvent key) {
		//Sets the keyDown to true for that key.
		int code = key.getKeyCode();
		if(code>=0 && code<keysDown.length) {
			keysDown[code] = true;
		}
		//Things that should be taken care of as the key is pressed.
		if(code == KeyEvent.VK_EQUALS) game.debugMode = !game.debugMode;
		if(code == KeyEvent.VK_P) {
			game.pause();
		}
		if(code == KeyEvent.VK_X) {
			if (this.switchCooldown <= 0) {
				this.buildType = (this.buildType + 1) % 3;
				this.switchCooldown = 5;
				this.fadeTime = 25;
			}
			
		}
		if(code == KeyEvent.VK_R) {
			if(game.paused) {
				game.restart();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
		int code = key.getKeyCode();
		if(code>=0 && code<keysDown.length) {
			keysDown[code] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
	
	@Override
	public void tick() {
		//Win or lose here.
		if(this.snowCount <= 0) {
			game.lose();
		} else if(this.snowCount >= goal) {
			game.win();
		}
		
		int moveSpeedFix = game.blizzard?1:0;
		
		if(game.blizzard) {
			this.snowCount++;
		}
		
		//Stop moving.
		this.stop();
		
		//Move.
		if(keysDown[KeyEvent.VK_W]) {
			this.moving = true;
			this.dy = -moveSpeed-moveSpeedFix;
		}
		if(keysDown[KeyEvent.VK_S]) {
			this.moving = true;
			this.dy = moveSpeed+moveSpeedFix;
		}
		if(keysDown[KeyEvent.VK_A]) {
			this.moving = true;
			this.dx = -moveSpeed-moveSpeedFix;
		}
		if(keysDown[KeyEvent.VK_D]) {
			this.moving = true;
			this.dx = moveSpeed+moveSpeedFix;
		}
		
		//Try to build a building
		if(keysDown[KeyEvent.VK_C]) {
			if (this.turretCooldown <= 0 && this.snowCount > 100) {
				switch (buildType) {
				case 0:
					this.turretCooldown = 200;
					this.snowCount -= 100;
					Turret t = new Turret(this.x, this.y, 16, 16, Color.green,
							game);
					game.add(t);
					break;
				case 1:
					this.turretCooldown = 200;
					this.snowCount -= 100;
					SnowWell well = new SnowWell(this.x, this.y, 16, 16, Color.blue,
							game);
					game.add(well);
					break;
				case 2:
					this.turretCooldown = 200;
					this.snowCount -= 100;
					Wall wall = new Wall(this.x, this.y, 32, 32, Color.yellow,
							game);
					game.add(wall);
					break;
				default:
				}
				SoundUtil.playSound("buildingPlaced");
			} else {
				
			}
		}
		
		
		//Cooldowns and timers.
		if(this.turretCooldown > 0) {
			this.turretCooldown--;
		}
		
		if(this.switchCooldown > 0) {
			this.switchCooldown--;
		}
		
		if(this.fadeTime > 0) {
			this.fadeTime--;
		}
		
		//Fills the snow below your feet.
		for(Tile[] ta : this.getFoot(game.board, game.tileX, game.tileY)) {
			for(Tile t : ta) {
				if(t!=null && !t.snow) {
					t.fill(1);
					this.snowCount--;
				}
			}
		}
		
		//Plays the step sound.
		if(this.moving)
			this.stepTime--;
		if(this.stepTime <= 0) {
			this.stepTime = 30;
			SoundUtil.playSound("step");
		}
		
		
		super.tick();
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if(this.fadeTime > 0) {
			int sc = Main.SCALE;
			sc = 1;
			
			switch(this.buildType) {
			case 0:
				Turret draw = new Turret(false, this.x*sc, (this.y - 20) * sc, 16 * sc, 16 * sc, Color.green, game);
				draw.fade(g, fadeTime / 25.0f);
				break;
			case 1:
				SnowWell draww = new SnowWell(false, this.x*sc, (this.y - 20)*sc, 16*sc, 16*sc, Color.blue, game);
				draww.fade(g, fadeTime / 25.0f);
				break;
			case 2:
				Wall drawww = new Wall(false, (this.x - 8)*sc, (this.y - 36)*sc, 32*sc, 32*sc, Color.yellow, game);
				drawww.fade(g, fadeTime / 25.0f);
				break;
			default:
			}
		}
		
		this.displaySnow(g);
		this.displayCooldown(g);
		
		
		
	}
	
	/**
	 * Displays the amount of snow collected in a bar at the top of the screen.
	 * @param g
	 */
	public void displaySnow(Graphics g) {
		int sc = Main.SCALE;
		double frac = ((double) this.snowCount) / goal;
		int w = 250 * sc;
		int h = 16 * sc;
		int x = (Main.WIDTH / 2 - 125) * sc;
		int y = 10*sc;
		
		
		Rectangle2D.Double full = new Rectangle2D.Double(x, y, w, h);
		Rectangle2D.Double part = new Rectangle2D.Double(x, y, w * frac, h);
		
		Graphics2D g2 = (Graphics2D) g;
		
		float alpha = this.y < y/sc + h/sc + 20?0.3f: 1.0f;
		
		float[] scale = {alpha, alpha, alpha, alpha};
		float[] offs = new float[4];
		RescaleOp op = new RescaleOp(scale, offs, null);
		BufferedImage skin = game.sprites.getSprite("bars")[0];
		BufferedImage dr = op.filter(skin, null);
		g2.setColor(new Color(1.0f, 1.0f, 1.0f, alpha));
		g2.fill(part);
		g2.drawImage(dr, x-4*sc, y-4*sc, x + w+4*sc, y + h+4*sc, 
				0, 0, dr.getWidth(), dr.getHeight(), null);
		
	}
	
	/**
	 * Displays the building cooldown.
	 * @param g
	 */
	public void displayCooldown(Graphics g) {
		int sc = Main.SCALE;
		double frac = ((double) this.turretCooldown) / 200.0;
		int x = (Main.WIDTH / 2 - 100) * sc;
		int y = 37*sc;
		int w = 200*sc;
		int h = 4*sc;
		
		Rectangle2D.Double full = new Rectangle2D.Double(x, y, w, h);
		Rectangle2D.Double part = new Rectangle2D.Double(x, y, w * frac, h);
		
		Graphics2D g2 = (Graphics2D) g;
		
		float alpha = this.y < y/sc + h/sc + 20?0.3f: 1.0f;
		
		float[] scale = {alpha, alpha, alpha, alpha};
		float[] offs = new float[4];
		RescaleOp op = new RescaleOp(scale, offs, null);
		BufferedImage skin = game.sprites.getSprite("bars")[1];
		BufferedImage dr = op.filter(skin, null);
		g2.setColor(new Color(1.0f, 1.0f, 1.0f, alpha));
		g2.fill(part);
		g2.drawImage(dr, x-4*sc, y-4*sc, x + w+4*sc, y + h+4*sc, 
				0, 0, dr.getWidth(), dr.getHeight(), null);
	}
}
