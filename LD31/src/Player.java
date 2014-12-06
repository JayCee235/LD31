import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Player extends Mob implements KeyListener{
	boolean[] keysDown;
	int snowCount;
	int turretCooldown;
	
	Game game;
	
	int buildType;

	public Player(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color);
		this.moveSpeed = 5;
		
		this.keysDown = new boolean[65535];
		
		this.snowCount = 500;
		
		this.turretCooldown = 0;
		this.buildType = 0;
		
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent key) {
		int code = key.getKeyCode();
		if(code>=0 && code<keysDown.length) {
			keysDown[code] = true;
		}
		if(code == KeyEvent.VK_P) {
			game.pause();
		}
		if(code == KeyEvent.VK_X) {
			this.buildType++;
			if(this.buildType>2) {
				this.buildType = 0;
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
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void tick() {
		if(this.snowCount <= 0) {
			game.lose();
		}
		
		this.stop();
		
		if(keysDown[KeyEvent.VK_W]) {
			this.moving = true;
			this.dy = -moveSpeed;
		}
		if(keysDown[KeyEvent.VK_S]) {
			this.moving = true;
			this.dy = moveSpeed;
		}
		if(keysDown[KeyEvent.VK_A]) {
			this.moving = true;
			this.dx = -moveSpeed;
		}
		if(keysDown[KeyEvent.VK_D]) {
			this.moving = true;
			this.dx = moveSpeed;
		}
		if(keysDown[KeyEvent.VK_C]) {
			if (this.turretCooldown <= 0 && this.snowCount > 100) {
				switch (buildType) {
				case 0:
					Turret t = new Turret(this.x, this.y, 16, 16, Color.green,
							game);
					game.add(t);
					this.turretCooldown = 200;
					this.snowCount -= 100;
					break;
				case 1:
					SnowWell well = new SnowWell(this.x, this.y, 16, 16, Color.blue,
							game);
					game.add(well);
					this.turretCooldown = 200;
					this.snowCount -= 100;
					break;
				case 2:
					Wall wall = new Wall(this.x-8, this.y-8, 32, 32, Color.yellow,
							game);
					game.add(wall);
					this.turretCooldown = 200;
					this.snowCount -= 100;
					break;
				default:
				}
			} else {
				
			}
		}
		
		if(this.turretCooldown > 0) {
			this.turretCooldown--;
		}
		
		super.tick();
	}
}
