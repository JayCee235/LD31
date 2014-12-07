import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;


public class Sound implements Runnable{
	Clip clip;
	
	public Sound(Clip clip) {
		this.clip = clip;
	}

	@Override
	public void run() {
		try {
			this.clip.open();
		} catch (LineUnavailableException e) {
			if(clip.isOpen()) {
				clip.stop();
			} else {
				
			}
		}
		this.clip.start();
		do {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while(this.clip.isActive());
		
		clip.close();
	}
}
