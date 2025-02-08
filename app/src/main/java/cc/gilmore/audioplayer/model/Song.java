package cc.gilmore.audioplayer.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "songs")
public class Song {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String artist;
    public String album;
    public String filePath;
    public String albumCoverUrl;
    public long duration;

    public Song(String title, String artist, String album, String filePath, String albumCoverUrl, long duration) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.filePath = filePath;
        this.albumCoverUrl = albumCoverUrl;
        this.duration = duration;
    }
}

