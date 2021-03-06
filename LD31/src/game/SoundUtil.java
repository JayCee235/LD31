package game;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Static class to handle sounds.
 * @author JayCee235
 *
 */
public class SoundUtil {
	private static Mixer mixer;
	public static HashMap<String, URL> clips = new HashMap<String, URL>();
	public static HashMap<String, Clip> clean = new HashMap<String, Clip>();
	
	public static boolean loaded = false;
	
	public static Clip music;
	public static String musicName;
	
	/**
	 * Name of all sounds in the game so far. The file is found at 'res/LD31/Sound/[name]'.
	 */
	public static final String[] SOUNDS = {"bite","enemyDeath","snowballShot","step", 
		"buildingPlaced", "buildingDestroyed", "LD31_Sno-Man.wav"};
	
	public static void setup() {
		musicName = "";
		loaded = true;
		Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
		mixer = AudioSystem.getMixer(mixInfos[0]);
		
		for(String s : SOUNDS) {
			SoundUtil.loadSound(s, "res/LD31/Sound/"+s);
		}
		
		
	}
	
	/**
	 * Loads the sound from path, and puts it in the table under the key name.
	 * @param name
	 * Key to put the sound under.
	 * @param path
	 * URL path to follow.
	 */
	private static void loadSound(String name, String path) {
//		String prePath = "res/LD31/Sound/";
		URL file = SoundUtil.class.getResource(path);
		
		clips.put(name, file);
		
	}
	
	/**
	 * Plays the specified sound once.
	 * @param name
	 * name of the clip to play.
	 */
	public static void playSound(String name) {
		DataLine.Info dataInf = new DataLine.Info(Clip.class, null);
		
		Clip clip = null;
		
		try {
			AudioInputStream aux = AudioSystem.getAudioInputStream(clips.get(name));
			clip = (Clip) mixer.getLine(dataInf);
			clip.open(aux);
		} catch (UnsupportedAudioFileException e) {
			return;
		} catch (IOException e) {
			return;
		} catch (LineUnavailableException e) {
			return;
		} catch (NullPointerException e) {
			return;
		}
		
		if(clip.isRunning()) {
			clip.stop();
		}
		clip.setFramePosition(0);
		clip.start();
	}
	
	/**
	 * Plays the given music. If music is currently playing, stops that music first.
	 * @param name
	 * Name of sound to play as music.
	 */
	public static void playMusic(String name) {
		DataLine.Info dataInf = new DataLine.Info(Clip.class, null);
		
		Clip clip = null;
		
		try {
			AudioInputStream aux = AudioSystem.getAudioInputStream(clips.get(name));
			clip = (Clip) mixer.getLine(dataInf);
			clip.open(aux);
		} catch (UnsupportedAudioFileException e) {
			return;
		} catch (IOException e) {
			return;
		} catch (LineUnavailableException e) {
			return;
		}
		
		if(clip.isRunning()) {
			clip.stop();
		}
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		if (music!=null) {
			music.stop();
		}
		music = clip;
		musicName = name;
		clip.setFramePosition(0);
		clip.start();
	}

}
