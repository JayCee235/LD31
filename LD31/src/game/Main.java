package game;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;


public class Main extends Applet{
	public static final int HEIGHT = 300;
	public static final int WIDTH = HEIGHT * 4 / 3;
	public static final int SCALE = 2;
	
	public static final String TITLE = "Sno-Man";
	private static JFrame frame;
	
	private static Game game;
	private static Player player;
	
	private static boolean appletMode;
	
	private static Main app;
	
	
	public static void main(String[] args) {
		Main.appletMode = false;
		
		frame = new JFrame(TITLE);
		frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
		
		Player player = new Player(WIDTH/2 + 8, HEIGHT/2 + 8, 16, 16, Color.blue, null);
		Game game = new Game(40, 30, player);
		
		player.game = game;
		player.loadSprites();
		
		game.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		game.setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		game.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		
		frame.add(game);
		frame.addKeyListener(player);
		
		Main.game = game;
		Main.player = player;
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		
		frame.setVisible(true);
		
		game.start();
	}
	
	public static void restart() {
		if (!appletMode) {
			frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
			Player player = new Player(WIDTH / 2 + 8, HEIGHT / 2 + 8, 16, 16,
					Color.blue, null);
			Game newGame = new Game(40, 30, player);
			player.game = newGame;
			player.loadSprites();
			newGame.setMinimumSize(new Dimension(Main.WIDTH * Main.SCALE,
					Main.HEIGHT * Main.SCALE));
			newGame.setMaximumSize(new Dimension(Main.WIDTH * Main.SCALE,
					Main.HEIGHT * Main.SCALE));
			newGame.setPreferredSize(new Dimension(Main.WIDTH * Main.SCALE,
					Main.HEIGHT * Main.SCALE));
			frame.remove(Main.game);
			frame.removeKeyListener(Main.player);
			frame.add(newGame);
			frame.addKeyListener(player);
			Main.game = newGame;
			Main.player = player;
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			newGame.start();
		} else {
			Player player = new Player(WIDTH/2 + 8, HEIGHT/2 + 8, 16, 16, Color.blue, null);
			Game game = new Game(40, 30, player);
			
			player.game = game;
			player.loadSprites();
			
			game.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
			game.setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
			game.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
			
			app.add(game);
			app.addKeyListener(player);
			
			Main.game = game;
			Main.player = player;
			
			app.setVisible(true);
			
			game.start();
		}
	}
	
	@Override
	public void init() {
		Main.appletMode = true;
		Main.app = this;
		
		Player player = new Player(WIDTH/2 + 8, HEIGHT/2 + 8, 16, 16, Color.blue, null);
		Game game = new Game(40, 30, player);
		
		player.game = game;
		player.loadSprites();
		
		game.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		game.setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		game.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		
		this.add(game);
		this.addKeyListener(player);
		
		Main.game = game;
		Main.player = player;
		
		this.setVisible(true);
		
		game.start();
	}
}
