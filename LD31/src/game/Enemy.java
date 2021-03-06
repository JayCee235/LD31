package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;


public class Enemy extends Mob{
	Game game;
	
	int hp, hpMax;
	
	EnemyType behaviour;
	
	boolean hunterMode;
	int cooldown;
	Entity target;

	public Enemy(int x, int y, int w, int h, Color color, Game game) {
		super(x, y, w, h, color);
		this.moveSpeed = 1;
		this.game = game;
		
		this.loadSprites();
		
		this.behaviour = EnemyType.random();
		
		this.hunterMode = false;
		this.cooldown = 0;
		
		this.target = game.player;
		
		switch(behaviour){
		case siege:
			this.hp = this.hpMax = 100;
			LinkedList<Entity> p = game.entities;
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
			this.hp = this.hpMax = 50;
			break;
		case circler:
		case chaser:
		default:
			this.hp = this.hpMax = 100;
			
		}
		
	}
	
	public void loadSprites() {
		this.up = game.sprites.getSprite("enemyUp");
		this.down = game.sprites.getSprite("enemyDown");
		this.left = game.sprites.getSprite("enemyLeft");
		this.right = game.sprites.getSprite("enemyRight");
	}
	
	@Override
	public void tick() {
		if(this.hp <= 0) {
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
				this.moveSpeed = 2.25;
				ddx = game.player.x - this.x;
				ddy = game.player.y - this.y;
				if (ddx != 0 || ddy != 0) {
					double r = Math.sqrt(ddx * ddx + ddy * ddy);
					ddx /= r;
					ddy /= r;
				}
				this.move(ddx, ddy);
			} else {
				this.moveSpeed = 2;
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
				LinkedList<Entity> p = game.entities;
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
					this.target = game.player;
				}
			}
			if(this.distanceFrom(game.player) < 100) {
				ddx = -game.player.x + this.x;
				ddy = -game.player.y + this.y;
			} else {
				if (!this.collidingWith(target)) {
					ddx = this.target.x - this.x;
					ddy = this.target.y - this.y;
				} else {
					ddx = this.target.x - this.x;
					ddy = this.target.y - this.y;
				}
				
			}
			if (ddx != 0 || ddy != 0) {
				double r = Math.sqrt(ddx * ddx + ddy * ddy);
				ddx /= r;
				ddy /= r;
				this.move(ddx, ddy);
			} else{
				this.stop();
			}
			
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
		
		Tile[][] foot = this.getFoot(game.board, game.tileX, game.tileY);
		for(Tile[] ta : foot) {
			for(Tile t : ta) {
				if (t!=null) {
					t.burn(1);
				}
			}
		}
		
		super.tick();
		
		if(this.collidingWith(game.player)) {
			if(this.behaviour == EnemyType.hunter) {
				if(this.hunterMode) {
					this.hunterMode = false;
					game.player.snowCount -= 30;
					this.cooldown = 200;
					SoundUtil.playSound("bite");
				}
			} else {
				game.player.snowCount--;
			}
		}
		
	}

	public void die() {
		game.remove(this);
		game.player.snowCount += 100;
	}
	
	public void displayHealth(Graphics g) {
		int sc = Main.SCALE;
		
		int x = (this.x - 1);
		int y = (this.y - 3);
		
		int w = (this.width + 2);
		int h = 2;
		
		double frac = ((double) this.hp) / ((double) this.hpMax);
		
		Rectangle2D.Double fill = new Rectangle2D.Double(x * sc, 
				y * sc, w * sc * frac, h * sc);
		BufferedImage img = game.sprites.getSprite("bars")[2];
		
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color.red);
		g2.fill(fill);
		g2.drawImage(img, (x)*sc, (y)*sc, (x+w)*sc, (y+h)*sc, 0, 0, img.getWidth(), img.getHeight(), null);
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		this.displayHealth(g);
	}

}
