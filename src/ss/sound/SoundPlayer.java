package ss.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundPlayer {
    private static final float SAMPLE_RATE = 48000;
    private static final int NUM_FADE_SAMPLES = 80;
    private static final double VOLUME = 0.3;

    private static final double MIN_FREQ = 27.5; // A0
    private static final double MAX_FREQ = 4185; // C8

    private SourceDataLine sdl = null;

    private final double hzPerUnit;

    public SoundPlayer(int numElements) {
        AudioFormat af = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        try {
            sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
        } catch (LineUnavailableException e) {
            closeSDL();
            throw new RuntimeException(e);
        }
        hzPerUnit = (MAX_FREQ - MIN_FREQ) / numElements;
    }

    public void play(int n, double desiredDuration) {
        double duration = Math.max(desiredDuration, 10);

        double hz = n * hzPerUnit;
        int numSamples = (int) Math.round(SAMPLE_RATE * duration / 1000);

        byte[] sinWave = new byte[numSamples];
        for (int i = 0; i < numSamples; ++i) {
            double angle = 2 * Math.PI * i * (hz / SAMPLE_RATE);
            sinWave[i] = (byte) Math.round(Math.sin(angle) * 127 * VOLUME);
        }

        for (int i = 0; i < NUM_FADE_SAMPLES && i < sinWave.length / 2; i++) {
            double scale = (double) i / NUM_FADE_SAMPLES;
            sinWave[i] = (byte) Math.round(sinWave[i] * scale);
            sinWave[sinWave.length - 1 - i] = (byte) Math.round(sinWave[sinWave.length - 1 - i] * scale);
        }
        sdl.flush();
        sdl.write(sinWave, 0, sinWave.length);
    }

    public void closeSDL() {
        if (sdl != null) {
            sdl.drain();
            sdl.close();
        }
    }
}
