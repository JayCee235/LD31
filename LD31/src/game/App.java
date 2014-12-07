package game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;

public class App extends JApplet{

	
	@Override
	public void init() {
		int SCALE = Main.SCALE;
		int WIDTH = Main.WIDTH;
		int HEIGHT= Main.HEIGHT;
		
		Main.app = this;
		Main.appletMode = true;
		
		this.setSize(WIDTH*SCALE, HEIGHT*SCALE);
		
		Player player = new Player(WIDTH / 2 + 8, HEIGHT / 2 + 8, 16, 16,
				Color.blue, null);
		Game game = new Game(40, 30, player);
		player.game = game;
		player.loadSprites();
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		this.getContentPane().add(game);
		addKeyListener(player);
		Main.game = game;
		Main.player = player;
		
		this.setVisible(true);
		this.requestFocus();
		
		this.paintComponents(this.getGraphics());
		
		game.start();
	}
	
	public void appRestart() {
		int SCALE = Main.SCALE;
		int WIDTH = Main.WIDTH;
		int HEIGHT= Main.HEIGHT;
		
		
		Player player = new Player(WIDTH / 2 + 8, HEIGHT / 2 + 8, 16, 16,
				Color.blue, null);
		Game game = new Game(40, 30, player);
		player.game = game;
		player.loadSprites();
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		//this.setVisible(false);
		
		this.getContentPane().remove(Main.game);
		removeKeyListener(Main.player);
		this.getContentPane().add(game);
		addKeyListener(player);
		Main.game.stop();
		Main.game = game;
		Main.player = player;
		this.setVisible(true);
		this.requestFocus();
		
		this.paintComponents(this.getGraphics());
		
		game.start();
	}
}
