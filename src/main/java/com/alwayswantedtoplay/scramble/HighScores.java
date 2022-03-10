package com.alwayswantedtoplay.scramble;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class HighScores {

  List <HighScore>highScores = new ArrayList<>();

  public void add(int score, String string) {
    add(new HighScore(score, string)); // add a new
  }
  public void add(HighScore highScore) {
    highScores.add(highScore);
  }
  public List<HighScore> getList() {
    Collections.sort(highScores, (HighScore a1, HighScore a2) -> a2.score - a1.score );
    return highScores
            .stream()
            .limit(10)
            .collect(Collectors.toList());
  }

  public String toString() {
    var buf = "";
    for (HighScore hs: getList()) {
      buf += hs.toString() + "\n";
    }
    return buf;
  }

}
