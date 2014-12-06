import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


public class Building extends Entity{
	Game game;
	int snowCount, cap;

	public Building(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color);
		
		this.game = game;
		
		this.snowCount = 100;
		this.cap = 100;
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
	}
	
	public void displayHealth(Graphics g) {
		int sc = Main.SCALE;
		
		double frac = ((double) this.snowCount) / ((double) this.cap);
		Rectangle2D.Double hpBar = new Rectangle2D.Double((this.x - 1) * sc, (this.y - 3) * sc, 
				(this.width + 1) * sc, 2 * sc);
		Rectangle2D.Double fill = new Rectangle2D.Double((this.x - 1) * sc, (this.y - 3) * sc,
				(this.width + 1) * sc * frac, 2 * sc);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		g2.fill(hpBar);
		g2.setColor(Color.green);
		g2.fill(fill);
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		this.displayHealth(g);
	}

}
