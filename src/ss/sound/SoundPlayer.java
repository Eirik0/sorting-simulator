package ss.sound;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import gt.async.ThreadWorker;

public class SoundPlayer {
    private static final float SAMPLE_RATE = 44100;
    private static final int NUM_FADE_SAMPLES = 300;
    private static final double VOLUME = 0.3;

    private static final double MIN_DURATION = 10;

    private static final double C5_FREQ = 261.63;
    private static final int NUM_OCTAVES = 5;
    private static final double MIN_FREQ = C5_FREQ / 4; // C3

    private final ThreadWorker soundThreadWorker = new ThreadWorker();
    private final Queue<FrequenciesAndDuration> playQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean keepPlaying = true;

    private SourceDataLine sdl = null;

    public SoundPlayer() {
        AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
        try {
            sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af, (int) SAMPLE_RATE);
            sdl.start();
        } catch (LineUnavailableException e) {
            closeSDL();
            throw new RuntimeException(e);
        }
        soundThreadWorker.workOn(() -> {
            for (;;) {
                while (playQueue.peek() == null && keepPlaying) {
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                FrequenciesAndDuration toPlay = null;
                FrequenciesAndDuration fd;
                while ((fd = playQueue.poll()) != null) {
                    toPlay = fd;
                }
                if (keepPlaying && toPlay != null) {
                    double offBy = waitForSDL();
                    playInternal(toPlay, offBy);
                }
            }
        });
        soundThreadWorker.waitForStart();
    }

    private double waitForSDL() {
        int sdlRemaining = sdl.getBufferSize() - sdl.available();
        double offBy = (double) sdlRemaining / SAMPLE_RATE * 1000;
        while (sdlRemaining > 0) {
            try {
                Thread.sleep(Math.round((double) sdlRemaining / SAMPLE_RATE * 1000) + 1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sdlRemaining = sdl.getBufferSize() - sdl.available();
        }
        return offBy;
    }

    public synchronized void play(double[] ns, int numElements, double duration) {
        double[] freqs = new double[ns.length];
        for (int i = 0; i < ns.length; ++i) {
            double percent = ns[i] / numElements;
            double key = percent * NUM_OCTAVES;
            freqs[i] = MIN_FREQ * Math.pow(2, key);
        }
        playQueue.add(new FrequenciesAndDuration(freqs, duration));
        notify();
    }

    public void playInternal(FrequenciesAndDuration fd, double offBy) {
        int numFds = fd.frequencies.length;
        double duration = Math.max(fd.duration - offBy, MIN_DURATION);
        int numSamples = (int) Math.round(SAMPLE_RATE * duration / 1000);

        double[] dSinWave = new double[numSamples];
        for (double freq : fd.frequencies) {
            for (int i = 0; i < numSamples; ++i) {
                double angle = 2 * Math.PI * i * (freq / SAMPLE_RATE);
                dSinWave[i] += Math.sin(angle);
            }
        }

        byte[] sinWave = new byte[numSamples];
        for (int i = 0; i < numSamples; ++i) {
            sinWave[i] = (byte) Math.round(dSinWave[i] * 127 * VOLUME / numFds);
        }

        for (int i = 0; i < NUM_FADE_SAMPLES && i < sinWave.length / 2; i++) {
            double scale = (double) i / NUM_FADE_SAMPLES;
            sinWave[i] = (byte) Math.round(sinWave[i] * scale);
            sinWave[sinWave.length - 1 - i] = (byte) Math.round(sinWave[sinWave.length - 1 - i] * scale);
        }

        sdl.flush();

        sdl.write(sinWave, 0, sinWave.length);
    }

    public void start() {
        keepPlaying = true;
    }

    public void stop() {
        synchronized (this) {
            keepPlaying = false;
            notify();
        }
        clear();
    }

    public void clear() {
        playQueue.clear();
        sdl.drain();
    }

    private void closeSDL() {
        if (sdl != null) {
            sdl.drain();
            sdl.close();
        }
        sdl = null;
    }

    private static class FrequenciesAndDuration {
        final double[] frequencies;
        final double duration;

        public FrequenciesAndDuration(double[] frequencies, double duration) {
            this.frequencies = frequencies;
            this.duration = duration;
        }
    }
}
