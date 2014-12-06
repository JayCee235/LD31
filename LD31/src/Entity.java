import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Entity {
	int x, y, width, height;
	Color color;
	
	public Entity(int x, int y, int w, int h, Color color) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.color = color;
	}
	
	public void draw(Graphics g) {
		g.translate(x, y);
		g.setColor(this.color);
		((Graphics2D) g).fillRect(0, 0, width, height);
		g.translate(-x, -y);
	}

	public void tick() {
		
	}
	
	public boolean collidingWith(Entity e) {
		if(e == this)
			return false;
		
		boolean xBound = (this.x > e.x - this.width)&&(this.x < e.x + e.width);
		boolean yBound = (this.y > e.y - this.height)&&(this.y < e.y + e.height);
		
		return xBound && yBound;
	}
	
	public int distanceFrom(Entity e) {
		int x = this.x - e.x;
		int y = this.y - e.y;
		double out = Math.sqrt(x*x + y*y);
		return (int) out;
	}
}
