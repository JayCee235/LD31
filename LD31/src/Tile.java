import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Tile {
	int x, y;
	Color color;
	boolean snow;
	
	int snowCount;
	
	public Tile(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		
		this.color = Color.white;
		
		this.snow = true;
		
		this.snowCount = 10;
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
	public void highlite() {
		this.color = Color.cyan;
	}
	
	public void burn(int i) {
		this.snowCount -= i;
		this.checkSnow();
	}
	
	public void fill(int i) {
		this.snowCount += i;
		this.checkSnow();
	}
	
	private void checkSnow() {
		if(this.snowCount >= 9) {
			this.snow = true;
		} else {
			this.snow = false;
		}
		
		if(this.snowCount < 0) 
			this.snowCount = 0;
		if(this.snowCount > 10)
			this.snowCount = 10;
	}

	public void draw(Graphics g, int x, int y, int w, int h) {
		g.translate(x, y);
		g.setColor(snow? Color.white.darker() : Color.black);
		((Graphics2D) g).fillRect(0, 0, w, h);
		g.translate(-x, -y);
	}
}
