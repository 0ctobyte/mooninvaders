package mooninvaders;

import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class Sound {
    
    private Clip clip;
    
    public Sound( String filename ) {
        try {
          clip = AudioSystem.getClip();
			    AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(filename));	
          clip.open(ais);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	public void play() {
    clip.stop();
    clip.setFramePosition(0);
    clip.start();
	}
  
  public void loop() {
    clip.loop( 0 );
  }
}
