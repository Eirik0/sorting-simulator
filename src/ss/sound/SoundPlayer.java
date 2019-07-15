package ss.sound;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import gt.async.ThreadWorker;

public class SoundPlayer {
    private static final float SAMPLE_RATE = 44100;
    private static final int NUM_FADE_SAMPLES = 250;
    private static final double VOLUME = 0.3;

    private static final double MIN_DURATION = 10;

    private static final double[] FREQUENCIES = { 4186.009, 3951.066, 3729.310, 3520.000, 3322.438, 3135.963, 2959.955, 2793.826, 2637.020, 2489.016, 2349.318,
            2217.461, 2093.005, 1975.533, 1864.655, 1760.000, 1661.219, 1567.982, 1479.978, 1396.913, 1318.510, 1244.508, 1174.659, 1108.731, 1046.502,
            987.7666, 932.3275, 880.0000, 830.6094, 783.9909, 739.9888, 698.4565, 659.2551, 622.2540, 587.3295, 554.3653, 523.2511, 493.8833, 466.1638,
            440.0000, 415.3047, 391.9954, 369.9944, 349.2282, 329.6276, 311.1270, 293.6648, 277.1826, 261.6256, 246.9417, 233.0819, 220.0000, 207.6523,
            195.9977, 184.9972, 174.6141, 164.8138, 155.5635, 146.8324, 138.5913, 130.8128, 123.4708, 116.5409, 110.0000, 103.8262, 97.99886, 92.49861,
            87.30706, 82.40689, 77.78175, 73.41619, 69.29566, 65.40639 };

    private static final double MIN_FREQ = 65.40639; // C2
    private static final double NUM_OCTAVES = 5;

    private final ThreadWorker soundThreadWorker = new ThreadWorker();
    private final Queue<FrequenciesAndDuration> playQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean keepPlaying = true;

    private SourceDataLine sdl = null;

    public SoundPlayer() {
        AudioFormat af = new AudioFormat(SAMPLE_RATE * 2, 16, 1, true, true);
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
            double approxFreq = MIN_FREQ * Math.pow(2, key);
            freqs[i] = findFreq(approxFreq);
        }
        playQueue.add(new FrequenciesAndDuration(freqs, duration));
        notify();
    }

    private static double findFreq(double approxFreq) {
        if (approxFreq < FREQUENCIES[FREQUENCIES.length - 1]) {
            return FREQUENCIES[FREQUENCIES.length - 1];
        }
        for (int i = FREQUENCIES.length - 2; i > 0; --i) {
            if (approxFreq < FREQUENCIES[i]) {
                return FREQUENCIES[i + 1];
            }
        }
        return FREQUENCIES[0];
    }

    public void playInternal(FrequenciesAndDuration fd, double offBy) {
        int numFds = fd.frequencies.length;
        double duration = Math.max(fd.duration - offBy, MIN_DURATION);
        int numSamples = (int) Math.round(SAMPLE_RATE * duration / 1000);

        double[] dSinWave = new double[numSamples];
        for (double freq : fd.frequencies) {
            for (int i = 0; i < numSamples; ++i) {
                double angle = 2 * Math.PI * i * (freq / (2 * SAMPLE_RATE));
                dSinWave[i] += Math.sin(angle) / numFds;
            }
        }

        for (int i = 0; i < NUM_FADE_SAMPLES && i < dSinWave.length / 2; i++) {
            double scale = (double) i / NUM_FADE_SAMPLES;
            dSinWave[i] = dSinWave[i] * scale;
            dSinWave[dSinWave.length - 1 - i] = dSinWave[dSinWave.length - 1 - i] * scale;
        }

        ByteBuffer buffer = ByteBuffer.allocate(numSamples * 2);
        for (int i = 0; i < numSamples; ++i) {
            buffer.putShort((short) Math.round(dSinWave[i] * VOLUME * Short.MAX_VALUE));
        }

        sdl.flush();

        sdl.write(buffer.array(), 0, numSamples * 2);
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
