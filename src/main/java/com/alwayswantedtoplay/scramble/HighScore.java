package com.alwayswantedtoplay.scramble;
public class HighScore {
    public int score;
    public String string;

    public HighScore(int score, String string) {
        this.score = score;
        this.string = string;
    }
    public String toString() {
        return string;
    }
}
