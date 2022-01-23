package com.corgibot.engine.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

// TODO use event queue speaker actions
public class Speaker {
    private static final Map<String, Sound> sounds = new HashMap<>();

    private static class Sound {
        private final byte[] data;
        private final AudioFormat format;

        private Sound(byte[] data, AudioFormat format) {
            this.data = data;
            this.format = format;
        }

        private Clip getClip() throws LineUnavailableException {
            Clip clip = AudioSystem.getClip();
            clip.open(this.format, this.data, 0, this.data.length);

            return clip;
        }
    }

    public static void play(String soundName) {
        play(soundName, 0);
    }

    public static void play(String soundName, int volume) {
        try {
            Clip clip = getSound(soundName);
            clip.setFramePosition(0);
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loop(String soundName) {
        loop(soundName, 0);
    }

    public static void loop(String soundName, int volume) {
        try {
            Clip clip = getSound(soundName);

            if (!clip.isRunning()) {
                clip.setFramePosition(0);
                FloatControl gainControl =
                        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Clip getSound(String soundName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Sound sound = loadSoundOnce(soundName);
        return sound.getClip();
    }

    private static Sound loadSoundOnce(String soundName) throws IOException, UnsupportedAudioFileException {
        if (!sounds.containsKey(soundName)) {
            String path = System.getProperty("user.dir") + "/assets/sounds/" + soundName + ".wav";
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("file://" + path));
            sounds.put(soundName, new Sound(audioIn.readAllBytes(), audioIn.getFormat()));
        }

        return sounds.get(soundName);
    }
}
