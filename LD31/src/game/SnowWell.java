package game;
import java.awt.Color;


public class SnowWell extends Building{

	public SnowWell(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color, game);
		this.snowCount = 500;
		this.cap = 1000;
		
		this.sprite = game.sprites.getSprite("well");
		this.spriteIndex = 0;
	}
	
	@Override
	public void tick() {
		super.tick();
		if(this.snowCount > 100) {
			for(Entity e : game.entities) {
				if(e instanceof Building) {
					if (((Building) e).snowCount < ((Building) e).cap) {
						this.snowCount--;
						((Building) e).snowCount++;
					}
				}
			}
		}
		this.spriteIndex = 0;
		if(this.snowCount < 500)
			this.spriteIndex = 1;
		if(this.snowCount < 250)
			this.spriteIndex = 2;
	}

}
