package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteLibrary {
	private HashMap<String, BufferedImage[]> library;
	
	/**
	 * Makes a new empty SpriteLibrary
	 */
	public SpriteLibrary() {
		this.library = new HashMap<String, BufferedImage[]>();
	}
	
	/**
	 * Makes a new SpriteLibrary from a sprite sheet.
	 * @param imgPath
	 * -the file path for the image
	 * @param x
	 * -The number of images horizontally
	 * @param y
	 * -The number of images vertically
	 * @param dx
	 * -The width of the image
	 * @param dy
	 * -The height of the image
	 * @param offsetX
	 * -Offset X between images
	 * @param offsetY
	 * -Offset Y between images
	 * @throws IOException 
	 */
	public SpriteLibrary(String nameRoot, String[] names, String imgPath, 
			int dx, int dy, int offsetX, int offsetY) throws IOException {
		this.library = new HashMap<String, BufferedImage[]>();
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imgPath));
		} catch(IOException e) {
			throw new IOException("File not found! " + imgPath);
		}
		
		int x = img.getWidth() / dx;
		int y = img.getHeight() / dy;
		
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				BufferedImage[] add = new BufferedImage[]{
						img.getSubimage(i*(dx+offsetX), j*(dy+offsetY), dx, dy)};
				System.out.println(names[i*x+j]);
				this.library.put(nameRoot + names[i*x+j], add);
			}
		}
		
	}
	
	/**
	 * Adds a sprite to the library. The file (root + variable[i] + post) will be at index i in the sprite.
	 * @param name
	 * @param root
	 * @param variable
	 * @param post
	 * @throws IOException 
	 */
	public void addSprite(String name, String root, String[] variable, String post) throws IOException {
		int length = variable.length;
		BufferedImage[] add = new BufferedImage[length];
		for(int i = 0; i < length; i++) {
			String file = root + variable[i] + post;
			try {
				add[i] = ImageIO.read(this.getClass().getResource(file));
			} catch (IOException e) {
				throw new IOException("Could not find file " + file);
			}
		}
		library.put(name, add);
	}
	
	/**
	 * Adds a new sprite to the library that is made from a sprite sheet (multiple sprites in the same image.)
	 * @param name
	 * -Name of new sprite in library
	 * @param file
	 * -Path to image
	 * @param rows
	 * -Number of rows in image
	 * @param columns
	 * -Number of columns in image
	 * @param offsetX
	 * -Offset horizontally
	 * @param offsetY
	 * -Offset vertically
	 * @throws IOException 
	 */
	public void addSpriteFromStrip(String name, String file, 
			int rows, int columns, int offsetX, int offsetY) throws IOException {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(file));
		} catch (IOException e) {
			throw new IOException("Could not find file " + file);
		}
		
		int dx = img.getWidth() / columns;
		int dy = img.getHeight() / rows;
		BufferedImage[] sprite = new BufferedImage[rows*columns];
		
		for(int i = 0; i < columns; i++) {
			for(int j = 0; j < rows; j++) {
				sprite[i*columns+j] = img.getSubimage(i*(dx+offsetX), j*(dy+offsetY), dx, dy);
			}
		}
		
	}
	
	/**
	 * Adds an image to the library. The image is treated as a single-frame sprite.
	 * @param name
	 * @param file
	 * @throws IOException
	 */
	public void addImage(String name, String file) throws IOException {
		BufferedImage[] add = new BufferedImage[1];
		try {
			add[0] = ImageIO.read(this.getClass().getResource(file));
		} catch (IOException e) {
			throw new IOException("Could not find file " + file);
		}
		library.put(name, add);
	}
	
	public BufferedImage[] getSprite(String name) {
		return library.get(name);
	}
	
}
