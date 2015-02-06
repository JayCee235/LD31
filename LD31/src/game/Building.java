package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;


public class Building extends Entity{
	Game game;
	int snowCount, cap;

	public Building(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color);
		
		this.game = game;
		
		this.snowCount = 100;
		this.cap = 100;
		
		this.spriteIndex = 0;
		
		
	}
	
	@Override
	public void tick() {
		super.tick();
		if(this.snowCount <= 0) {
			this.die();
		}
		
		if(game.blizzard) {
			this.snowCount++;
		}
		if(this.snowCount > this.cap){
			this.snowCount = this.cap;
		}
		
		
		for (Entity e : game.entities) {
				if (e instanceof Enemy) {
					if (this.collidingWith(e)) {
						this.snowCount--;
						if (((Enemy) e).behaviour == EnemyType.siege)
							this.snowCount--;
					}
				}
			}
		}

	public void die() {
		game.remove(this);
		SoundUtil.playSound("buildingDestroyed");
	}
	
	public void displayHealth(Graphics g) {
		int sc = Main.SCALE;
		
		int x = (this.x - 1);
		int y = (this.y - 3);
		
		int w = (this.width + 2);
		int h = 2;
		
		double frac = ((double) this.snowCount) / ((double) this.cap);
		
		Rectangle2D.Double fill = new Rectangle2D.Double(x * sc, 
				y * sc, w * sc * frac, h * sc);
		BufferedImage img = (this instanceof Wall)?game.sprites.getSprite("bars")[3]
				:game.sprites.getSprite("bars")[2];
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.green);
		g2.fill(fill);
		g2.drawImage(img, (x)*sc, (y)*sc, (x+w)*sc, (y+h)*sc, 0, 0, img.getWidth(), img.getHeight(), null);
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int sc = Main.SCALE;
		
		g2.translate(this.x*sc, this.y*sc);
		BufferedImage img = this.sprite[(int) this.spriteIndex];
		
		
		
		if(img != null) {
			
			g2.drawImage(img, 0, 0, this.width*sc, this.height*sc, 0, 0, img.getWidth(), img.getHeight(), null);
		}
		
		g2.translate(-this.x*sc, -this.y*sc);
		
		this.displayHealth(g);
	}
	
	@Override
	public void fade(Graphics g, float f) {
		Graphics2D g2 = (Graphics2D) g;
		int sc = Main.SCALE;
		
		g2.translate(this.x*sc, this.y*sc);

		float[] scale = {f, f, f, f};
		float[] offs = new float[4];
		RescaleOp op = new RescaleOp(scale, offs, null);
		BufferedImage img = this.sprite[(int) this.spriteIndex];
		if(img != null) {
			BufferedImage draww = op.filter(img, null);
			g2.drawImage(draww, 0, 0, this.width*sc, this.height*sc, 0, 0, img.getWidth(), img.getHeight(), null);
			
			//g2.drawImage(img, op, this.width/2, this.height/2);
			//g2.drawImage(img, 0, 0, this.width*sc, this.height*sc, 0, 0, img.getWidth(), img.getHeight(), null);
		}
		
		g2.translate(-this.x*sc, -this.y*sc);
	}

}
