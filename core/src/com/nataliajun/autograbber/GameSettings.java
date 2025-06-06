package com.nataliajun.autograbber;

public class GameSettings {

    // Device settings

    public static final int SCREEN_WIDTH = 720;
    public static final int SCREEN_HEIGHT = 1280;

    // Physics settings

    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final float SCALE = 0.05f;

    public static float CAR_FORCE_RATIO = 10;
    public static float TRASH_VELOCITY = 30;
    public static long STARTING_TRASH_APPEARANCE_COOL_DOWN = 2000; // in [ms] - milliseconds
    public static long STARTING_BONUS_APPEARANCE_COOL_DOWN = 1000;

    public static final short TRASH_BIT = 2;
    public static final short CAR_BIT = 4;
    public static final short BONUS_BIT = 8;
    public static final int HEALTH_DRAIN_COOLDOWN = 80;

    // Object sizes

    public static final int CAR_WIDTH = 60;
    public static final int CAR_HEIGHT = 125;
    public static final int TRASH_WIDTH = 56;
    public static final int TRASH_HEIGHT = 74;
    public static final int BONUS_WIDTH = 67;
    public static final int BONUS_HEIGHT = 52;

}
