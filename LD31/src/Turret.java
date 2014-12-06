import java.awt.Color;


public class Turret extends Building{
	int cooldown;

	public Turret(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color, game);
		
		this.cooldown = 0;
		this.sprite = Game.sprites.get("turret");
		
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if(this.collidingWith(game.player) && game.player.snowCount > 100 && this.snowCount < cap) {
			this.snowCount++;
			game.player.snowCount--;
		}
		if(this.snowCount > cap)
			this.snowCount = cap;
		float rr = (cap-snowCount)/((float) cap);
		float gg = snowCount/((float) cap);
		if(rr>1)
			rr = 1;
		if(rr<0)
			rr = 0;
		if(gg>1)
			gg = 1;
		if(gg<0)
			gg = 0;
		
		this.color = new Color(rr, gg, 0.0f);
		
		this.cooldown--;
		if(this.cooldown < 0)
			this.cooldown = 0;
		
		for (Entity e : game.entities) {
			if (e instanceof Enemy && this.distanceFrom(e) < 100) {
				if (this.cooldown <= 0 && this.snowCount >= 5) {
					this.snowCount -= 5;
					TurretBullet b = new TurretBullet(this.x + this.width/2 - 4, this.y + this.height / 2 - 4, 8, 8,
							Color.black, (Enemy) e, game);
					game.add(b);
					this.cooldown = 100;
				}
			}
		}
		
	}
	
}
