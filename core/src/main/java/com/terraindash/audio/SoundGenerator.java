package com.terraindash.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates sound effects procedurally as WAV byte arrays, then loads
 * them as LibGDX Sound objects. No external audio files needed.
 */
public class SoundGenerator {

    private final Map<String, Sound> sounds = new HashMap<>();

    public void generateAll() {
        sounds.put("engine_idle", generateEngine(200, 80f, 0.3f));
        sounds.put("engine_rev", generateEngine(200, 150f, 0.4f));
        sounds.put("coin", generateCoin());
        sounds.put("boost", generateBoost());
        sounds.put("crash", generateCrash());
        sounds.put("land_soft", generateLanding(0.3f, 300));
        sounds.put("land_hard", generateLanding(0.6f, 500));
        sounds.put("nitro", generateNitro());
        sounds.put("flip_warning", generateFlipWarning());
        sounds.put("button_click", generateClick());
        sounds.put("star", generateStar());
    }

    public Sound get(String id) {
        return sounds.get(id);
    }

    public void dispose() {
        for (Sound s : sounds.values()) {
            s.dispose();
        }
        sounds.clear();
    }

    private Sound generateEngine(int durationMs, float freq, float volume) {
        int sampleRate = 22050;
        int numSamples = sampleRate * durationMs / 1000;
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double val = Math.sin(2 * Math.PI * freq * t) * 0.4
                + Math.sin(2 * Math.PI * freq * 2 * t) * 0.2
                + Math.sin(2 * Math.PI * freq * 3 * t) * 0.1
                + (Math.random() - 0.5) * 0.15;
            samples[i] = (short) (val * volume * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateCoin() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.15);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double freq = 800 + t * 3000;
            double env = 1.0 - t / 0.15;
            double val = Math.sin(2 * Math.PI * freq * t) * env * 0.5;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateBoost() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.4);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double freq = 100 + t * 200;
            double env = Math.exp(-t * 3);
            double val = Math.sin(2 * Math.PI * freq * t) * 0.3
                + (Math.random() - 0.5) * 0.4 * env;
            val *= env * 0.6;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateCrash() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.5);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double env = Math.exp(-t * 5);
            double val = (Math.random() - 0.5) * env
                + Math.sin(2 * Math.PI * 60 * t) * env * 0.3
                + Math.sin(2 * Math.PI * 120 * t) * env * 0.2;
            val *= 0.7;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateLanding(float intensity, int durationMs) {
        int sampleRate = 22050;
        int numSamples = sampleRate * durationMs / 1000;
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double env = Math.exp(-t * 8);
            double val = Math.sin(2 * Math.PI * 80 * t) * env * 0.4
                + (Math.random() - 0.5) * env * 0.3;
            val *= intensity;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateNitro() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.6);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double env = Math.min(t * 10, 1.0) * Math.exp(-t * 2);
            double freq = 150 + Math.sin(t * 30) * 50;
            double val = (Math.random() - 0.5) * 0.5 * env
                + Math.sin(2 * Math.PI * freq * t) * 0.3 * env;
            val *= 0.5;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateFlipWarning() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.2);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double val = Math.sin(2 * Math.PI * 600 * t) * 0.3
                * (1 + Math.sin(2 * Math.PI * 15 * t) * 0.5);
            double env = 1.0 - t / 0.2;
            samples[i] = (short) (val * env * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateClick() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.05);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double env = Math.exp(-t * 80);
            double val = Math.sin(2 * Math.PI * 1000 * t) * env * 0.4;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateStar() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.3);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double freq = 500 + t * 1000;
            double env = (1.0 - t / 0.3) * Math.min(t * 20, 1);
            double val = Math.sin(2 * Math.PI * freq * t) * 0.3 * env
                + Math.sin(2 * Math.PI * freq * 1.5 * t) * 0.15 * env;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    /** Creates a WAV byte array and loads it as a LibGDX Sound */
    private Sound createSound(short[] samples, int sampleRate) {
        byte[] wav = encodeWav(samples, sampleRate);
        FileHandle tempFile = Gdx.files.local("temp_sound.wav");
        tempFile.writeBytes(wav, false);
        Sound sound = Gdx.audio.newSound(tempFile);
        tempFile.delete();
        return sound;
    }

    private byte[] encodeWav(short[] samples, int sampleRate) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            int numChannels = 1;
            int bitsPerSample = 16;
            int byteRate = sampleRate * numChannels * bitsPerSample / 8;
            int blockAlign = numChannels * bitsPerSample / 8;
            int dataSize = samples.length * blockAlign;

            // RIFF header
            dos.writeBytes("RIFF");
            writeIntLE(dos, 36 + dataSize);
            dos.writeBytes("WAVE");

            // fmt sub-chunk
            dos.writeBytes("fmt ");
            writeIntLE(dos, 16);
            writeShortLE(dos, (short) 1); // PCM
            writeShortLE(dos, (short) numChannels);
            writeIntLE(dos, sampleRate);
            writeIntLE(dos, byteRate);
            writeShortLE(dos, (short) blockAlign);
            writeShortLE(dos, (short) bitsPerSample);

            // data sub-chunk
            dos.writeBytes("data");
            writeIntLE(dos, dataSize);
            for (short sample : samples) {
                writeShortLE(dos, sample);
            }

            dos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to encode WAV", e);
        }
    }

    private void writeIntLE(DataOutputStream dos, int val) throws IOException {
        dos.write(val & 0xFF);
        dos.write((val >> 8) & 0xFF);
        dos.write((val >> 16) & 0xFF);
        dos.write((val >> 24) & 0xFF);
    }

    private void writeShortLE(DataOutputStream dos, short val) throws IOException {
        dos.write(val & 0xFF);
        dos.write((val >> 8) & 0xFF);
    }
}
