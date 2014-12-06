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
		
		Game game = new Game(80, 60);
		frame.add(game);
		
		frame.setVisible(true);
		
		game.start();
	}
}
