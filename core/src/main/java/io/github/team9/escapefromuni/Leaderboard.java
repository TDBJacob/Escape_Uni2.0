package io.github.team9.escapefromuni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The Leaderboard class acts as a middle man between the LeaderboardEntry and the leaderboard repository file,
 * adding the entries into the leaderboard repository file.
 */
public class Leaderboard {

    private final String repositoryName;


    public Leaderboard(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    /**
     * Simple getter for repositoryName.
     *
     * @return String repositoryName.
     */
    public String getRepositoryName() {
        return this.repositoryName;
    }

    /**
     * Gives the top 3 entries in the leaderboard.
     *
     * @return top 3.
     */
    public List<List<Object>> getTopThree() throws IOException, JSONException {
        // Create holders.
        List<List<Object>> entries = new ArrayList<>();
        //List<Object> entryHolder = new ArrayList<>();
        // Read file.
        //String fileContent = new String(Files.readAllBytes(Paths.get(this.repositoryName)));
        //String fileContent = Gdx.files.internal("leaderboard.json").readString();
        FileHandle leaderboardFile = Gdx.files.local(this.repositoryName);
        String fileContent = leaderboardFile.readString();

        // Get to the leaderboardEntries.
        JSONObject leaderboardEntriesContainer = new JSONObject(fileContent);
        JSONArray leaderboardEntries = leaderboardEntriesContainer.getJSONArray("leaderboardEntries");
        // Getting json objects into a list.
        List<JSONObject> jsonObjectStorer = new ArrayList<>();
        for (int i = 0; i < leaderboardEntries.length(); i++) {
            jsonObjectStorer.add(leaderboardEntries.getJSONObject(i));
        }
        // Sorting them by score in descending order.
        //jsonObjectStorer.sort((a, b) -> {
        //try {
        //return Integer.compare(b.getInt("score"), a.getInt("score"));
        //} catch (JSONException e) {
        //throw new RuntimeException(e);
        //}
        //});
        jsonObjectStorer.sort((a, b) -> Integer.compare(b.getInt("score"), a.getInt("score")));
        // Got top 3.
        List<JSONObject> top3 = jsonObjectStorer.subList(0, Math.min(3, jsonObjectStorer.size()));
        // Adding into list in desired format.
        for (int i = 0; i < Math.min(3, jsonObjectStorer.size()); i ++) {
            JSONObject o = jsonObjectStorer.get(i);
            List<Object> entry = new ArrayList<>();
            entry.add(o.getString("name"));
            entry.add(o.getInt("score"));
            entries.add(entry);
            //entryHolder.add(o.getString("name"));
            //entryHolder.add(o.getInt("score"));
            //entries.add(entryHolder);
            //entryHolder.clear();
        }
        return entries;
    }

    /**
     * Adds an entry into the leaderboard repository file.
     *
     * @param entry information of the entry.
     */
    public void addEntry(LeaderboardEntry entry) throws IOException, JSONException {
        // Read file.
        //String fileContent = new String(Files.readAllBytes(Paths.get(this.repositoryName)));
        //String fileContent = Gdx.files.internal(this.repositoryName).readString();
        FileHandle file = Gdx.files.local(this.repositoryName);

        // Get to the leaderboardEntries.
        JSONObject leaderboardEntriesContainer = new JSONObject(file.readString());
        JSONArray leaderboardEntries = leaderboardEntriesContainer.getJSONArray("leaderboardEntries");
        // Create new entry.
        JSONObject newEntry = new JSONObject();
        newEntry.put("name", entry.entryName);
        newEntry.put("score", entry.score);
        // Add entry to leaderboardEntries.
        leaderboardEntries.put(newEntry);
        // Write, and make it readable to the eye.
        //Files.write(Paths.get(this.repositoryName), leaderboardEntriesContainer.toString(2).getBytes());
        file.writeString(leaderboardEntriesContainer.toString(2), false);
    }
}
