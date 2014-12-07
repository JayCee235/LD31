package game;
import java.awt.Color;


public class Wall extends Building{

	public Wall(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color, game);
		
		this.snowCount = 1000;
		this.cap = 1000;
		
		this.sprite = Game.sprites.get("wall");
		
	}
	
	@Override
	public void tick() {
		super.tick();
		for(Entity e : game.entities) {
			if (this.collidingWith(e)) {
				if (e instanceof Enemy) {
					((Enemy) e).stop();
					((Enemy) e).freeze();
				}
				if(e instanceof Snowpile) {
					game.remove(e);
				}
			}
		}
	}
	
	

}
