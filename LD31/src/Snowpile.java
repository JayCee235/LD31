import java.awt.Color;
import java.util.ArrayList;


public class Snowpile extends Entity{
	int snowCount;
	Player player;
	Game game;

	public Snowpile(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color);
		this.snowCount = (int) (1000 * Math.random());
		this.player = game.player;
		this.game = game;
	}

	public int getSnowCount() {
		return snowCount;
	}

	public void setSnowCount(int snowCount) {
		this.snowCount = snowCount;
	}
	
	@Override
	public void tick() {
		if(this.snowCount < 0) {
			game.remove(this);
		}
		
		if(game.blizzard) {
			this.snowCount++;
		}
		
		
		if(this.collidingWith(this.player)) {
			player.snowCount++;
			this.snowCount--;
		}
		
		for(Entity e : game.entities) {
			if (this.collidingWith(e)) {
				if (e instanceof Enemy) {
					this.snowCount--;
				}
			}
		}
		
		for(Tile[] ta : this.getFoot(game.board, game.tileX, game.tileY)) {
			for(Tile t : ta) {
				if (t!=null && !t.snow) {
					t.fill(10);
					this.snowCount -= 10;
				}
			}
		}
		
	}

}
