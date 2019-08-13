package classes;

import java.io.File;
import javax.sound.sampled.*;

public class PlaySound{

    public static Clip clip;


    public void playSound(String soundName) {
        //clip = null;
        //clip.addLineListener(LineListener);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            while(clip.getMicrosecondLength() != clip.getMicrosecondPosition()) {}
            clip.loop(1);
        } catch (Exception ex) {
            System.out.println("Erro ao executar SOM!");
            ex.printStackTrace();
        }
    }

    /*@Override
    public void update(LineEvent event) {
        if(event.getType() == LineEvent.Type.STOP){
            return;
        }
    }

    public void playSound(String soundName) {
        //clip = null;
        LineListener listener = this::update;
        clip.addLineListener(listener);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Erro ao executar SOM!");
            ex.printStackTrace();
        }
    }*/

    public static boolean executando(){
        //if(clip == null)
        //    return true;
        return clip.isOpen();
    }
}