package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
		
		this.sprite = Game.sprites.get("snowPile");
		this.spriteIndex = 0;
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
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int sc = Main.SCALE;
		
		g2.translate(this.x*sc, this.y*sc);
		this.spriteIndex = this.snowCount > 500?0:this.snowCount > 250?1:2;
		BufferedImage img = this.sprite[(int) this.spriteIndex];
		if(img != null) {
			g2.drawImage(img, 0, 0, this.width*sc, this.height*sc, 0, 0, img.getWidth(), img.getHeight(), null);
		}
		
		g2.translate(-this.x*sc, -this.y*sc);
		
	}

}
