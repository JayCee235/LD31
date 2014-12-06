import java.awt.Color;


public class TurretBullet extends Mob{
	Enemy target;
	Game game;

	public TurretBullet(int x, int y, int w, int h, Color color, Enemy target, Game game) {
		super(x, y, w, h, color);
		
		this.target = target;
		
		this.game = game;
	}
	
	public void tick() {
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

}
