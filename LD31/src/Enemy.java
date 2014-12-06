import java.awt.Color;
import java.util.ArrayList;


public class Enemy extends Mob{
	Game game;
	
	int hp;
	
	EnemyType behaviour;
	
	boolean hunterMode;
	int cooldown;
	Entity target;

	public Enemy(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color);
		this.moveSpeed = 3;
		this.game = game;
		
		this.hp = 100;
		
		this.behaviour = EnemyType.random();
		
		this.hunterMode = false;
		this.cooldown = 0;
		
		this.target = game.player;
		
		switch(behaviour){
		case siege:
			ArrayList<Entity> p = game.entities;
			Entity poten = null;
			int dis = -1;
			for(Entity t : p) {
				if (t instanceof Building) {
					if (poten == null) {
						poten = t;
						dis = this.distanceFrom(t);
					} else {
						if(this.distanceFrom(t) < dis) {
							poten = t;
							dis = this.distanceFrom(t);
						}
					}
				}
			}
			this.target = poten;
			break;
			
		case hunter:
		case circler:
		case chaser:
		default:
			
		}
		
	}
	
	@Override
	public void tick() {
		if(this.hp < 0) {
			this.die();
		}
		
		double ddx = 0;
		double ddy = 0;
		switch(this.behaviour) {
		
		case hunter:
			this.cooldown--;
			if(!this.hunterMode && this.cooldown <= 0 && this.distanceFrom(game.player) < 400) {
				this.hunterMode = true;
			}
			
			if(this.hunterMode) {
				this.moveSpeed = 6;
				ddx = game.player.x - this.x;
				ddy = game.player.y - this.y;
				if (ddx != 0 || ddy != 0) {
					double r = Math.sqrt(ddx * ddx + ddy * ddy);
					ddx /= r;
					ddy /= r;
				}
				this.move(ddx, ddy);
			} else {
				this.moveSpeed = 3;
				ddx = 2 * Math.random() - 1;
				ddy = 2 * Math.random() - 1;
				if (ddx != 0 || ddy != 0) {
					double r = Math.sqrt(ddx * ddx + ddy * ddy);
					ddx /= r;
					ddy /= r;
				}
				this.move(ddx, ddy);
			}
			
			break;
		case siege:
			if(!game.entities.contains(this.target)) {
				ArrayList<Entity> p = game.entities;
				Entity poten = null;
				int dis = -1;
				for(Entity t : p) {
					if (t instanceof Building) {
						if (poten == null) {
							poten = t;
							dis = this.distanceFrom(t);
						} else {
							if(this.distanceFrom(t) < dis) {
								poten = t;
								dis = this.distanceFrom(t);
							}
						}
					}
				}
				if (poten!=null) {
					this.target = poten;
				} else {
					this.die();
					return;
				}
			}
			if(this.distanceFrom(game.player) < 100) {
				ddx = -game.player.x + this.x;
				ddy = -game.player.y + this.y;
			} else {
				ddx = this.target.x - this.x;
				ddy = this.target.y - this.y;
				
			}
			if (ddx != 0 || ddy != 0) {
				double r = Math.sqrt(ddx * ddx + ddy * ddy);
				ddx /= r;
				ddy /= r;
			}
			this.move(ddx, ddy);
			break;
			
			
		case circler:
			if(this.distanceFrom(game.player) > 115) {
				ddx = game.player.x - this.x;
				ddy = game.player.y - this.y;
			} else {
				int dddx = game.player.y - this.y;
				int dddy = -game.player.x + this.x;
				ddx = dddx - dddy/2;
				ddy = dddy + dddx/2;
			}
			
			if (ddx != 0 || ddy != 0) {
				double r = Math.sqrt(ddx * ddx + ddy * ddy);
				ddx /= r;
				ddy /= r;
			}
			this.move(ddx, ddy);
			break;
		
		case chaser:
		default:
			ddx = game.player.x - this.x;
			ddy = game.player.y - this.y;
			if (ddx != 0 || ddy != 0) {
				double r = Math.sqrt(ddx * ddx + ddy * ddy);
				ddx /= r;
				ddy /= r;
			}
			this.move(ddx, ddy);
			break;
		}
		
		
		
		super.tick();
		
		if(this.collidingWith(game.player)) {
			if(this.behaviour == EnemyType.hunter) {
				if(this.hunterMode) {
					this.hunterMode = false;
					game.player.snowCount -= 10;
					this.cooldown = 100;
				}
			} else {
				game.player.snowCount--;
				System.out.println("Taking damage! " + game.player.snowCount);
			}
		}
		
	}

	public void die() {
		game.remove(this);
	}

}
