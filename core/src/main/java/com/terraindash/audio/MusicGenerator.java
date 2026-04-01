package com.terraindash.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates simple looping background music tracks procedurally.
 * Each world gets a unique musical theme based on simple synthesis.
 */
public class MusicGenerator {

    private final Map<String, FileHandle> trackFiles = new HashMap<>();

    public void generateAll() {
        generateTrack("menu", 100, 0, new int[]{60, 64, 67, 72, 67, 64, 60, 55});
        generateTrack("countryside", 130, 1, new int[]{60, 62, 64, 67, 69, 67, 64, 62});
        generateTrack("desert", 140, 2, new int[]{57, 60, 62, 65, 69, 65, 62, 60});
        generateTrack("arctic", 150, 3, new int[]{62, 65, 69, 72, 74, 72, 69, 65});
        generateTrack("neon", 145, 4, new int[]{60, 63, 67, 70, 72, 70, 67, 63});
        generateTrack("volcano", 160, 5, new int[]{55, 58, 60, 63, 67, 63, 60, 58});
        generateTrack("sky", 150, 6, new int[]{64, 67, 71, 74, 76, 74, 71, 67});
    }

    public Music loadTrack(String id) {
        FileHandle file = trackFiles.get(id);
        if (file == null || !file.exists()) return null;
        Music music = Gdx.audio.newMusic(file);
        music.setLooping(true);
        return music;
    }

    private void generateTrack(String id, int bpm, int style, int[] notes) {
        int sampleRate = 22050;
        float beatDuration = 60f / bpm;
        int totalBeats = notes.length * 4;
        int totalSamples = (int) (sampleRate * beatDuration * totalBeats);
        short[] samples = new short[totalSamples];

        for (int beat = 0; beat < totalBeats; beat++) {
            int noteIdx = beat % notes.length;
            int midiNote = notes[noteIdx];
            double freq = 440.0 * Math.pow(2, (midiNote - 69) / 12.0);

            int beatStart = (int) (beat * beatDuration * sampleRate);
            int beatEnd = Math.min((int) ((beat + 1) * beatDuration * sampleRate), totalSamples);

            for (int i = beatStart; i < beatEnd; i++) {
                double t = (double) (i - beatStart) / sampleRate;
                double beatT = t / beatDuration;
                double env = Math.min(beatT * 10, 1.0) * Math.max(0, 1.0 - beatT * 1.2);

                double val = 0;

                switch (style) {
                    case 0: // Menu: gentle pad
                        val = Math.sin(2 * Math.PI * freq * t) * 0.25
                            + Math.sin(2 * Math.PI * freq * 2 * t) * 0.08;
                        break;
                    case 1: // Countryside: bright pluck
                        val = Math.sin(2 * Math.PI * freq * t) * 0.3 * Math.exp(-t * 6)
                            + Math.sin(2 * Math.PI * freq * 3 * t) * 0.1 * Math.exp(-t * 8);
                        break;
                    case 2: // Desert: metallic
                        val = Math.sin(2 * Math.PI * freq * t + Math.sin(2 * Math.PI * freq * 1.5 * t) * 2) * 0.25;
                        break;
                    case 3: // Arctic: ethereal
                        val = Math.sin(2 * Math.PI * freq * t) * 0.2
                            + Math.sin(2 * Math.PI * (freq + 1) * t) * 0.15;
                        break;
                    case 4: // Neon: square-ish synth
                        val = Math.signum(Math.sin(2 * Math.PI * freq * t)) * 0.15
                            + Math.sin(2 * Math.PI * freq * t) * 0.1;
                        break;
                    case 5: // Volcano: distorted
                        double raw = Math.sin(2 * Math.PI * freq * t) * 1.5;
                        val = Math.max(-0.3, Math.min(0.3, raw));
                        break;
                    case 6: // Sky: airy chords
                        val = Math.sin(2 * Math.PI * freq * t) * 0.15
                            + Math.sin(2 * Math.PI * freq * 1.25 * t) * 0.1
                            + Math.sin(2 * Math.PI * freq * 1.5 * t) * 0.08;
                        break;
                }

                val *= env * 0.5;

                // Bass drum on every 4th beat
                if (beat % 4 == 0) {
                    double kickT = t;
                    if (kickT < 0.15) {
                        double kickFreq = 60 * Math.exp(-kickT * 30);
                        val += Math.sin(2 * Math.PI * kickFreq * kickT) * 0.25 * Math.exp(-kickT * 15);
                    }
                }

                // Hi-hat on every 2nd beat
                if (beat % 2 == 1) {
                    if (t < 0.05) {
                        val += (Math.random() - 0.5) * 0.1 * Math.exp(-t * 80);
                    }
                }

                int sampleIdx = i;
                if (sampleIdx >= 0 && sampleIdx < totalSamples) {
                    samples[sampleIdx] = clampSample(samples[sampleIdx] + (short) (val * Short.MAX_VALUE));
                }
            }
        }

        byte[] wav = encodeWav(samples, sampleRate);
        FileHandle file = Gdx.files.local("music_" + id + ".wav");
        file.writeBytes(wav, false);
        trackFiles.put(id, file);
    }

    private short clampSample(int val) {
        return (short) Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, val));
    }

    private byte[] encodeWav(short[] samples, int sampleRate) {
        try {
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
        } catch (IOException e) {
            throw new RuntimeException("Failed to encode music WAV", e);
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

    public void dispose() {
        for (FileHandle file : trackFiles.values()) {
            if (file.exists()) file.delete();
        }
        trackFiles.clear();
    }
}
