import java.awt.Color;


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

}
