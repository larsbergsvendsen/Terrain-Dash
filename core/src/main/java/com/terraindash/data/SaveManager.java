package com.terraindash.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Manages persistent game data using LibGDX Preferences
 * (backed by SharedPreferences on Android).
 */
public class SaveManager {

    private static final String PREFS_NAME = "terrain_dash_save";
    private Preferences prefs;

    private Preferences getPrefs() {
        if (prefs == null) {
            prefs = Gdx.app.getPreferences(PREFS_NAME);
        }
        return prefs;
    }

    // Player progress
    public int getPlayerXP() {
        return getPrefs().getInteger("player_xp", 0);
    }

    public void addPlayerXP(int amount) {
        getPrefs().putInteger("player_xp", getPlayerXP() + amount);
        getPrefs().flush();
    }

    public int getPlayerLevel() {
        return getPrefs().getInteger("player_level", 1);
    }

    public void setPlayerLevel(int level) {
        getPrefs().putInteger("player_level", level);
        getPrefs().flush();
    }

    // Currency
    public int getCoins() {
        return getPrefs().getInteger("coins", 0);
    }

    public void addCoins(int amount) {
        getPrefs().putInteger("coins", getCoins() + amount);
        getPrefs().flush();
    }

    public boolean spendCoins(int amount) {
        int current = getCoins();
        if (current < amount) return false;
        getPrefs().putInteger("coins", current - amount);
        getPrefs().flush();
        return true;
    }

    public int getGems() {
        return getPrefs().getInteger("gems", 0);
    }

    public void addGems(int amount) {
        getPrefs().putInteger("gems", getGems() + amount);
        getPrefs().flush();
    }

    // Level progress
    public int getLevelStars(String worldId, int levelIndex) {
        return getPrefs().getInteger("stars_" + worldId + "_" + levelIndex, 0);
    }

    public void setLevelStars(String worldId, int levelIndex, int stars) {
        String key = "stars_" + worldId + "_" + levelIndex;
        int current = getPrefs().getInteger(key, 0);
        if (stars > current) {
            getPrefs().putInteger(key, stars);
            getPrefs().flush();
        }
    }

    public float getLevelBestTime(String worldId, int levelIndex) {
        return getPrefs().getFloat("time_" + worldId + "_" + levelIndex, Float.MAX_VALUE);
    }

    public void setLevelBestTime(String worldId, int levelIndex, float time) {
        String key = "time_" + worldId + "_" + levelIndex;
        float current = getPrefs().getFloat(key, Float.MAX_VALUE);
        if (time < current) {
            getPrefs().putFloat(key, time);
            getPrefs().flush();
        }
    }

    // Vehicle upgrades
    public int getUpgradeLevel(String vehicleId, String upgradeType) {
        return getPrefs().getInteger("upgrade_" + vehicleId + "_" + upgradeType, 0);
    }

    public void setUpgradeLevel(String vehicleId, String upgradeType, int level) {
        getPrefs().putInteger("upgrade_" + vehicleId + "_" + upgradeType, level);
        getPrefs().flush();
    }

    public boolean isVehicleUnlocked(String vehicleId) {
        if ("buggy".equals(vehicleId)) return true;
        return getPrefs().getBoolean("vehicle_unlocked_" + vehicleId, false);
    }

    public void unlockVehicle(String vehicleId) {
        getPrefs().putBoolean("vehicle_unlocked_" + vehicleId, true);
        getPrefs().flush();
    }

    // Selected vehicle
    public String getSelectedVehicle() {
        return getPrefs().getString("selected_vehicle", "buggy");
    }

    public void setSelectedVehicle(String vehicleId) {
        getPrefs().putString("selected_vehicle", vehicleId);
        getPrefs().flush();
    }

    // Settings
    public float getMusicVolume() {
        return getPrefs().getFloat("music_volume", 0.8f);
    }

    public void setMusicVolume(float volume) {
        getPrefs().putFloat("music_volume", volume);
        getPrefs().flush();
    }

    public float getSfxVolume() {
        return getPrefs().getFloat("sfx_volume", 1.0f);
    }

    public void setSfxVolume(float volume) {
        getPrefs().putFloat("sfx_volume", volume);
        getPrefs().flush();
    }

    public float getTiltSensitivity() {
        return getPrefs().getFloat("tilt_sensitivity", 0.7f);
    }

    public void setTiltSensitivity(float sensitivity) {
        getPrefs().putFloat("tilt_sensitivity", sensitivity);
        getPrefs().flush();
    }

    // Endless mode highscores
    public float getEndlessHighscore(String worldId) {
        return getPrefs().getFloat("endless_" + worldId, 0f);
    }

    public void setEndlessHighscore(String worldId, float distance) {
        String key = "endless_" + worldId;
        float current = getPrefs().getFloat(key, 0f);
        if (distance > current) {
            getPrefs().putFloat(key, distance);
            getPrefs().flush();
        }
    }
}
