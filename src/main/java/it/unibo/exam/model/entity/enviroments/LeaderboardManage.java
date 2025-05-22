package it.unibo.exam.model.entity.enviroments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import it.unibo.exam.model.entity.PlayerScore;

public class LeaderboardManage {
    private static final int MAX_SIZE = 10;
    private final List<PlayerScore> scores = new ArrayList<>();

    public void addScore(final PlayerScore score) {
        scores.add(score);
        Collections.sort(scores);
        if (scores.size() > MAX_SIZE) {
            scores.remove(scores.size() - 1);
        }
    }

    public List<PlayerScore> getTopScores(){
        return new ArrayList<>(scores);
    }

    public void clear(){
        scores.clear();
    }
}
