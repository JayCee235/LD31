import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Mob extends Entity {
	boolean moving;
	double moveSpeed;
	double dx, dy;
	
	boolean frozen;
	
	BufferedImage[] up, down, left, right;

	public Mob(int x, int y, int w, int h, Color color) {
		super(x, y, w, h, color);
		
		this.moveSpeed = 2;
		
		this.up = new BufferedImage[4];
		this.down = new BufferedImage[4];
		this.left = new BufferedImage[4];
		this.right = new BufferedImage[4];
		
	}
	
	public Mob (int x, int y, int w, int h, Color color, 
			BufferedImage[] up, BufferedImage[] down, BufferedImage[] left, BufferedImage[] right) {
		super(x, y, w, h, color);
		
		this.moveSpeed = 4;
		
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		
		
	}
	
	public void move(double dx, double dy) {
		this.moving = true;
		
		
		if(dx != 0)
			this.dx += (this.moveSpeed * dx);
		
		if(dy != 0)
			this.dy += (this.moveSpeed * dy);
	}
	
	public void stop() {
		this.moving = false;
		this.dx = 0;
		this.dy = 0;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (!frozen) {
			if (this.moving) {
				if(dx*dx >= dy*dy) {
					this.sprite = dx > 0?this.right:this.left;
				} else {
					this.sprite = dy > 0?this.down:this.up;
				}
				//TODO: stuff
				
				while (dx >= 1 && dy <= -1) {
					this.x ++;
					this.y --;
					dx--;
					dy++;
				}
				while (dx <= -1 && dy >= 1) {
					this.x --;
					this.y ++;
					dx++;
					dy--;
				}
				
				while (dx >= 1 && dy >= 1) {
					this.x ++;
					this.y ++;
					dx--;
					dy--;
				}
				while (dx >= 1) {
					this.x ++;
					dx--;
				}
				while (dy >= 1) {
					this.y ++;
					dy--;
				}
				
				
				while (dx <= -1 && dy <= -1) {
					this.x --;
					this.y --;
					dx++;
					dy++;
				}
				while (dx <= -1) {
					this.x --;
					dx++;
				}
				while (dy <= -1) {
					this.y --;
					dy++;
				}
				
				
				if (this.x < 0)
					this.x = 0;
				if (this.y < 0)
					this.y = 0;

				if (this.x > Main.WIDTH - this.width)
					this.x = Main.WIDTH - this.width;
				if (this.y > Main.HEIGHT - this.height)
					this.y = Main.HEIGHT - this.height;

			}
		} else {
			this.frozen = false;
		}
	}
	
	public void freeze() {
		this.frozen = true;
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int sc = Main.SCALE;
		
		g2.translate(this.x*sc, this.y*sc);
		this.spriteIndex = this.spriteIndex + 0.1;
		if(this.spriteIndex >= this.sprite.length) {
			this.spriteIndex = 0;
		}
		BufferedImage img = this.sprite[(int) this.spriteIndex];
		if(img != null) {
			g2.drawImage(img, 0, 0, this.width*sc, this.height*sc, 0, 0, img.getWidth(), img.getHeight(), null);
		}
		
		
		g2.translate(-this.x*sc, -this.y*sc);
		
	}
	
	

}
