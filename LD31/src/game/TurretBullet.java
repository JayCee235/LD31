package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class TurretBullet extends Mob{
	Enemy target;
	Game game;

	public TurretBullet(int x, int y, int w, int h, Color color, Enemy target, Game game) {
		super(x, y, w, h, color);
		
		this.target = target;
		
		this.moveSpeed = 2;
		
		this.game = game;
		
		this.sprite = Game.sprites.get("snowball");
		this.spriteIndex = 0;
		
		this.up = sprite;
		this.down = sprite;
		this.left = sprite;
		this.right = sprite;
	}
	
	public void tick() {
		this.spriteIndex = 0;
		double ddx = target.x - this.x;
		double ddy = target.y - this.y;
		if(ddx!=0||ddy!=0) {
			double r = Math.sqrt(ddx*ddx + ddy*ddy);
			ddx /= r;
			ddy /= r;
		}
		this.move(ddx, ddy);
		
		super.tick();
		
		if(!game.entities.contains(target)) {
			this.die();
		}
		
		if(this.collidingWith(target)) {
			target.hp -= 10;
			this.die();
		}
		
	}

	private void die() {
		game.remove(this);
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int sc = Main.SCALE;
		
		g2.translate(this.x*sc, this.y*sc);
		this.spriteIndex = 0;
		BufferedImage img = this.sprite[(int) this.spriteIndex];
		if(img != null) {
			g2.drawImage(img, 0, 0, this.width*sc, this.height*sc, 0, 0, img.getWidth(), img.getHeight(), null);
		} else {
			System.out.println("No image found");
		}
		
		g2.translate(-this.x*sc, -this.y*sc);
		
	}

}
