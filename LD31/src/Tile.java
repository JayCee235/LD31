import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Tile {
	int x, y;
	Color color;
	
	public Tile(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		
		this.color = color;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void draw(Graphics g, int x, int y, int w, int h) {
		g.translate(x, y);
		g.setColor(this.color);
		((Graphics2D) g).fillRect(0, 0, w, h);
		g.translate(-x, -y);
	}
}
