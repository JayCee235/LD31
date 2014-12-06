import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;


public class Main {
	public static final int HEIGHT = 300;
	public static final int WIDTH = HEIGHT * 4 / 3;
	public static final int SCALE = 3;
	
	public static final String TITLE = "Ludum Dare 31";
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame(TITLE);
		frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
		
		Player player = new Player(WIDTH/2 + 8, HEIGHT/2 + 8, 16, 16, Color.blue, null);
		Game game = new Game(40, 30, player);
		
		player.game = game;
		player.loadSprites();
		
	//	frame.setSize(new Dimension(WIDTH, HEIGHT));
		frame.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		
		frame.add(game);
		frame.addKeyListener(player);
		
		//frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		
		frame.setVisible(true);
		
		game.start();
	}
}
