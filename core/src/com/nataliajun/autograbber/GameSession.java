package com.nataliajun.autograbber;
import com.badlogic.gdx.utils.TimeUtils;

import com.nataliajun.autograbber.managers.MemoryManager;

import java.util.ArrayList;


public class GameSession {

    public GameState state;
    long nextTrashSpawnTime;
    long nextBonusSpawnTime;
    long sessionStartTime;
    long pauseStartTime;
    private int score;

    private int cooldownShortifyingCounter;
    private int healthDrainCooldownCounter;

    public GameSession() {
    }

    public void startGame() {
        state = GameState.PLAYING;
        score = 0;
        sessionStartTime = TimeUtils.millis();
        nextTrashSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN
                * getTrashPeriodCoolDown());
        nextBonusSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_BONUS_APPEARANCE_COOL_DOWN
                * getBonusPeriodCoolDown());

        cooldownShortifyingCounter = 1;
        healthDrainCooldownCounter = 0;
    }

    public void pauseGame() {
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();
    }

    public void resumeGame() {
        state = GameState.PLAYING;
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
    }

    public void endGame() {
        updateScore();
        state = GameState.ENDED;

        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();
        if (recordsTable == null) {
            recordsTable = new ArrayList<>();
        }
        int foundIdx = 0;
        for (; foundIdx < recordsTable.size(); foundIdx++) {
            if (recordsTable.get(foundIdx) < getScore()) break;
        }
        recordsTable.add(foundIdx, getScore());
        MemoryManager.saveTableOfRecords(recordsTable);
    }

    public void updateScore() {
        score = (int) (TimeUtils.millis() - sessionStartTime) / 100;
    }

    public boolean shouldDrainHealth() {
        if(healthDrainCooldownCounter < GameSettings.HEALTH_DRAIN_COOLDOWN) healthDrainCooldownCounter++;
        else {
            healthDrainCooldownCounter = 0;
            return true;
        }
        return false;
    }

    public int getScore() {
        return score;
    }

    public boolean shouldSpawnTrash() {
        if (nextTrashSpawnTime <= TimeUtils.millis()) {
            nextTrashSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN
                    * getTrashPeriodCoolDown() / Math.pow(cooldownShortifyingCounter, 0.35));
            cooldownShortifyingCounter+=1;
            return true;
        }
        return false;
    }

    public boolean shouldSpawnBonus() {
        if (nextBonusSpawnTime <= TimeUtils.millis()) {
            nextBonusSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_BONUS_APPEARANCE_COOL_DOWN
                    * getBonusPeriodCoolDown() * Math.min(10000 / cooldownShortifyingCounter, 1));
            return true;
        }
        return false;
    }

    private float getTrashPeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }

    private float getBonusPeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }
}
