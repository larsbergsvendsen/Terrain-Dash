package com.terraindash.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages background music with crossfade transitions between tracks.
 */
public class MusicManager implements Disposable {

    private final Map<String, Music> tracks = new HashMap<>();
    private Music currentTrack;
    private String currentTrackId;
    private float volume = 0.8f;
    private boolean crossfading = false;
    private float crossfadeTimer = 0f;
    private float crossfadeDuration = 1f;
    private Music nextTrack;

    public void load(String id, String path) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.setLooping(true);
        tracks.put(id, music);
    }

    public void play(String id) {
        if (id.equals(currentTrackId)) return;

        Music track = tracks.get(id);
        if (track == null) return;

        if (currentTrack != null) {
            startCrossfade(track, id);
        } else {
            currentTrack = track;
            currentTrackId = id;
            currentTrack.setVolume(volume);
            currentTrack.play();
        }
    }

    private void startCrossfade(Music next, String nextId) {
        crossfading = true;
        crossfadeTimer = 0f;
        nextTrack = next;
        nextTrack.setVolume(0f);
        nextTrack.play();
        currentTrackId = nextId;
    }

    public void update(float delta) {
        if (!crossfading) return;

        crossfadeTimer += delta;
        float progress = Math.min(crossfadeTimer / crossfadeDuration, 1f);

        if (currentTrack != null) {
            currentTrack.setVolume(volume * (1f - progress));
        }
        if (nextTrack != null) {
            nextTrack.setVolume(volume * progress);
        }

        if (progress >= 1f) {
            if (currentTrack != null) currentTrack.stop();
            currentTrack = nextTrack;
            nextTrack = null;
            crossfading = false;
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (currentTrack != null) {
            currentTrack.setVolume(volume);
        }
    }

    public void pause() {
        if (currentTrack != null) currentTrack.pause();
    }

    public void resume() {
        if (currentTrack != null) currentTrack.play();
    }

    @Override
    public void dispose() {
        for (Music music : tracks.values()) {
            music.dispose();
        }
        tracks.clear();
    }
}
