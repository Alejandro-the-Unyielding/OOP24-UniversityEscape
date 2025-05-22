package it.unibo.exam.model.entity;

/**
 * Player Class
 * Contains playerId, time, score
 */
public class PlayerScore implements Comparable<PlayerScore>{
    private final String playerId;
    private final int time;
    private final int score;

    public PlayerScore(String playerId, int time, int score){
        this.playerId = playerId;
        this.score = score;
        this.time = time;
    }
    
    //Get player Id
    public String getPlayerId(){
        return this.playerId;
    }

    // Get score
    public int getScore(){
        return this.score;
    }

    // get time in seconds
    public int getTime(){
        return this.time;
    }

    // Get time in minutes:seconds
    public String getMinuteTime(){
        int minute = time / 60;
        int second = time % 60;
        return String.format("%02d:%02d", minute, second);
    }

    // Compare the player
    @Override
    public int compareTo(PlayerScore o){
        return Integer.compare(this.time, o.time);
    }
}

