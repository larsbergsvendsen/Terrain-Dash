package com.terraindash.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates sound effects procedurally as WAV files, then loads
 * them as LibGDX Sound objects. Each sound gets its own file
 * to avoid Android audio backend issues.
 */
public class SoundGenerator {

    private final Map<String, Sound> sounds = new HashMap<>();
    private final List<FileHandle> tempFiles = new ArrayList<>();
    private int fileCounter = 0;

    public void generateAll() {
        sounds.put("coin", generateCoin());
        sounds.put("boost", generateBoost());
        sounds.put("crash", generateCrash());
        sounds.put("land_soft", generateLanding(0.3f, 200));
        sounds.put("land_hard", generateLanding(0.6f, 350));
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
            if (s != null) s.dispose();
        }
        sounds.clear();
        for (FileHandle f : tempFiles) {
            try { if (f.exists()) f.delete(); } catch (Exception ignored) {}
        }
        tempFiles.clear();
    }

    private Sound generateCoin() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.12);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double freq = 800 + t * 3000;
            double env = 1.0 - t / 0.12;
            double val = Math.sin(2 * Math.PI * freq * t) * env * 0.5;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateBoost() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.3);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double freq = 100 + t * 200;
            double env = Math.exp(-t * 3);
            double val = Math.sin(2 * Math.PI * freq * t) * 0.3
                + (Math.random() - 0.5) * 0.3 * env;
            val *= env * 0.5;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateCrash() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.35);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double env = Math.exp(-t * 6);
            double val = (Math.random() - 0.5) * env
                + Math.sin(2 * Math.PI * 60 * t) * env * 0.3;
            val *= 0.6;
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
            double env = Math.exp(-t * 10);
            double val = Math.sin(2 * Math.PI * 80 * t) * env * 0.4
                + (Math.random() - 0.5) * env * 0.2;
            val *= intensity;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateNitro() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.4);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double env = Math.min(t * 10, 1.0) * Math.exp(-t * 3);
            double freq = 150 + Math.sin(t * 30) * 50;
            double val = (Math.random() - 0.5) * 0.4 * env
                + Math.sin(2 * Math.PI * freq * t) * 0.2 * env;
            val *= 0.4;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateFlipWarning() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.15);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double val = Math.sin(2 * Math.PI * 600 * t) * 0.3;
            double env = 1.0 - t / 0.15;
            samples[i] = (short) (val * env * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateClick() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.04);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double env = Math.exp(-t * 100);
            double val = Math.sin(2 * Math.PI * 1000 * t) * env * 0.3;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound generateStar() {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * 0.25);
        short[] samples = new short[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double t = (double) i / sampleRate;
            double freq = 500 + t * 1000;
            double env = (1.0 - t / 0.25) * Math.min(t * 20, 1);
            double val = Math.sin(2 * Math.PI * freq * t) * 0.3 * env;
            samples[i] = (short) (val * Short.MAX_VALUE);
        }

        return createSound(samples, sampleRate);
    }

    private Sound createSound(short[] samples, int sampleRate) {
        try {
            byte[] wav = encodeWav(samples, sampleRate);
            String filename = "sfx_" + (fileCounter++) + ".wav";
            FileHandle file = Gdx.files.local(filename);
            file.writeBytes(wav, false);
            Sound sound = Gdx.audio.newSound(file);
            tempFiles.add(file);
            return sound;
        } catch (Exception e) {
            Gdx.app.error("SoundGenerator", "Failed to create sound: " + e.getMessage());
            return null;
        }
    }

    private byte[] encodeWav(short[] samples, int sampleRate) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        int dataSize = samples.length * 2;

        dos.writeBytes("RIFF");
        writeIntLE(dos, 36 + dataSize);
        dos.writeBytes("WAVE");
        dos.writeBytes("fmt ");
        writeIntLE(dos, 16);
        writeShortLE(dos, (short) 1);
        writeShortLE(dos, (short) 1);
        writeIntLE(dos, sampleRate);
        writeIntLE(dos, sampleRate * 2);
        writeShortLE(dos, (short) 2);
        writeShortLE(dos, (short) 16);
        dos.writeBytes("data");
        writeIntLE(dos, dataSize);

        for (short sample : samples) {
            writeShortLE(dos, sample);
        }

        dos.flush();
        return baos.toByteArray();
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
