package game;
import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Main class for the game. Sets up the frame, and then starts the game.
 * @author JayCee235
 *
 */
public class Main extends Applet{
	/**
	 * Un-scaled height of the game.
	 */
	public static final int HEIGHT = 300;
	/**
	 * Un-scaled width of the game.
	 */
	public static final int WIDTH = HEIGHT * 4 / 3;
	/**
	 * Scale of the game. Height and width are multiplied by this.
	 */
	public static final int SCALE = 2;
	
	/**
	 * Title of the window.
	 */
	public static final String TITLE = "Sno-Man";
	private static JFrame frame;
	
	static Game game;
	static Player player;
	
	/**
	 * True if the game is running in an applet. 
	 */
	static boolean appletMode = false;
	
	static App app;
	
	public static void main(String[] args) {
			//Setting up the frame.
			frame = new JFrame(TITLE);
			frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
			//Creating the player to put into the frame as a KeyListener.
			Player player = new Player(WIDTH / 2 + 8, HEIGHT / 2 + 8, 16, 16,
					Color.blue, null);
			//Creates the game here. Loading sprites and sounds happens here as well.
			Game game = new Game(40, 30, player, null, null);
			player.game = game;
			player.loadSprites();
			game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
			game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
			game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
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
	
	/**
	 * Restarts the game, to a fresh instance of Game class.
	 */
	public static void restart() {
		if (!appletMode) {
			frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
			Player player = new Player(WIDTH / 2 + 8, HEIGHT / 2 + 8, 16, 16,
					Color.blue, null);
			Game newGame = new Game(40, 30, player, game==null?null:game.getLibrary(),
					game==null?null:game.font);
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
			Main.game.stop();
			Main.game = newGame;
			Main.player = player;
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			newGame.start();
		} else {
			app.appRestart();
		}
		
	}

}
