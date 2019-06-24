package ss.sound;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import gt.async.ThreadWorker;

public class SoundPlayer {
    private static final float SAMPLE_RATE = 48000;
    private static final int NUM_FADE_SAMPLES = 80;
    private static final double VOLUME = 0.3;

    private static final double MIN_FREQ = 27.5; // A0
    private static final double MAX_FREQ = 4185; // C8

    private final ThreadWorker soundThreadWorker = new ThreadWorker();
    private final Queue<FrequencyAndDuration> playQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean keepPlaying = true;

    private SourceDataLine sdl = null;

    public SoundPlayer() {
        AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
        try {
            sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
        } catch (LineUnavailableException e) {
            closeSDL();
            throw new RuntimeException(e);
        }
        soundThreadWorker.workOn(() -> {
            while (keepPlaying) {
                while (playQueue.peek() == null && keepPlaying) {
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                double frequency = 0;
                double duration = 0;
                int n = 0;
                FrequencyAndDuration fd;
                while ((fd = playQueue.poll()) != null) {
                    frequency += fd.frequency;
                    duration = Math.max(duration, fd.duration);
                    ++n;
                }
                if (n > 0) {
                    playInternal(frequency / n, duration);
                }
            }
        });
        soundThreadWorker.waitForStart();
    }

    public synchronized void play(double n, int numElements, double desiredDuration) {
        playQueue.add(new FrequencyAndDuration(n * (MAX_FREQ - MIN_FREQ) / numElements, Math.max(desiredDuration, 10)));
        notify();
    }

    public void playInternal(double hz, double duration) {
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

    public void stop() {
        synchronized (this) {
            keepPlaying = false;
            notify();
        }
        soundThreadWorker.joinThread();
        closeSDL();
    }

    private void closeSDL() {
        if (sdl != null) {
            sdl.drain();
            sdl.close();
        }
        sdl = null;
    }

    private static class FrequencyAndDuration {
        final double frequency;
        final double duration;

        public FrequencyAndDuration(double frequency, double duration) {
            this.frequency = frequency;
            this.duration = duration;
        }
    }
}
