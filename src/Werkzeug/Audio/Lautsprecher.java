package Werkzeug.Audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Lautsprecher {
    private static final Map<String, Clip> sounds = new HashMap<>();

    public static void abspielen(String soundName) {
        abspielen(soundName, 0);
    }

    public static void abspielen(String soundName, int lautStärke) {
        try {
            if (!sounds.containsKey(soundName)) {
                String path = System.getProperty("user.dir") + "/assets/sounds/" + soundName + ".wav";
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file://" + path));

                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);

                sounds.put(soundName, clip);
            }
            Clip clip = sounds.get(soundName);
            if (!clip.isRunning()) {
                clip.setFramePosition(0);
                FloatControl gainControl =
                        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(lautStärke);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
