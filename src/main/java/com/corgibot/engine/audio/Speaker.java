package com.corgibot.engine.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Speaker {
    private static final Map<String, Clip> sounds = new HashMap<>();

    public static void play(String soundName) {
        play(soundName, 0);
    }

    public static void play(String soundName, int volume) {
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
                gainControl.setValue(volume);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
