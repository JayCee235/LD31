import java.awt.Color;
import java.util.ArrayList;


public class Mob extends Entity {
	boolean moving;
	int moveSpeed;
	double dx, dy;
	
	boolean frozen;

	public Mob(int x, int y, int w, int h, Color color) {
		super(x, y, w, h, color);
		
		this.moveSpeed = 4;
		
	}
	
	public void move(double dx, double dy) {
		this.moving = true;
		
		if(dx != 0)
			this.dx = (this.moveSpeed * dx);
		
		if(dy != 0)
			this.dy = (this.moveSpeed * dy);
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
				this.x += this.dx;
				this.y += this.dy;

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

}
