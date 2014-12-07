import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;


public class SoundUtil {
	private static Mixer mixer;
	public static HashMap<String, File> clips = new HashMap<String, File>();
	public static HashMap<String, Clip> clean = new HashMap<String, Clip>();
	
	public static boolean loaded = false;
	
	public static Clip music;
	public static String musicName;
	
	public static void setup() {
		musicName = "";
		loaded = true;
		Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
		mixer = AudioSystem.getMixer(mixInfos[0]);
		String[] load = {"bite","enemyDeath","snowballShot","step", 
				"buildingPlaced", "buildingDestroyed", "LD31_Sno-Man.wav"};
		
		for(String s : load) {
			SoundUtil.loadSound(s, s);
		}
		
		
	}
	
	private static void loadSound(String name, String path) {
		String prePath = "res/LD31/Sound/";
		File file = new File(prePath + path);
		
		clips.put(name, file);
		
	}
	
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
		}
		
		if(clip.isRunning()) {
			clip.stop();
		}
		clip.setFramePosition(0);
		clip.start();
	}
	
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
		if(music!=null && music.isRunning() && musicName!=name) {
			music.stop();
		}
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		music = clip;
		musicName = name;
		clip.setFramePosition(0);
		clip.start();
	}

}
