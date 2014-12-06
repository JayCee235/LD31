import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;


public class Main {
	public static final int HEIGHT = 600;
	public static final int WIDTH = HEIGHT * 4 / 3;
	
	public static final String TITLE = "Ludum Dare 31";
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame(TITLE);
		frame.setSize(new Dimension(WIDTH, HEIGHT));
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		
		Player player = new Player(WIDTH/2 + 8, HEIGHT/2 + 8, 16, 16, Color.red);
		Game game = new Game(80, 60, player);
		
		frame.add(game);
		frame.addKeyListener(player);
		
		frame.pack();
		frame.setVisible(true);
		
		game.start();
	}
}
