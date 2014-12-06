import java.awt.Color;


public class Wall extends Building{

	public Wall(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color, game);
		
		this.snowCount = 1000;
		this.cap = 1000;
		
	}
	
	@Override
	public void tick() {
		super.tick();
		for(Entity e : game.entities) {
			if (this.collidingWith(e)) {
				if (e instanceof Enemy) {
					((Enemy) e).stop();
					((Enemy) e).freeze();
					double ddx = e.x - this.x;
					double ddy = e.y - this.y;
					if (ddx != 0 || ddy != 0) {
						double r = Math.sqrt(ddx * ddx + ddy * ddy);
						ddx /= r;
						ddy /= r;
					}
					((Enemy) e).move(ddx, ddy);
					break;
				}
			}
		}
	}
	
	

}
