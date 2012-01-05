package mooninvaders;

import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class Sound {
    
    private Clip clip;
    
    public Sound( String filename ) {
        try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(
				Sound.class.getResource(filename));	
            clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, 
				ais.getFormat()));
            clip.open(ais);
        } catch (UnsupportedAudioFileException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (LineUnavailableException e) {
        	e.printStackTrace();
        }
    }

	public void play() {
		try {
			if(clip != null) {
				new Thread() {
					public void run() {
						synchronized(clip) {
							clip.start();
							clip.stop();
							clip.close();
						}
					}
				}.start();
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}

    public void loop() {
        clip.loop( 0 );
    }
}
