package io.github.team9.escapefromuni;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {

    public String entryName;
    public int score;

    public LeaderboardEntry(String EntryName, int EntryScore) {
        entryName = EntryName;
        score = EntryScore;
    }

    @Override
    public int compareTo(LeaderboardEntry b) {
        return Integer.compare(b.score, this.score); // descending
    }

}
