package cc.gilmore.audioplayer.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "playlist_song_cross_ref",
        primaryKeys = {"playlist_id", "song_id"},
        foreignKeys = {
                @ForeignKey(entity = Playlist.class,
                        parentColumns = "id",
                        childColumns = "playlist_id"),
                @ForeignKey(entity = Song.class,
                        parentColumns = "id",
                        childColumns = "song_id")
        }
)
public class PlaylistSongCrossRef {
    public int playlist_id;
    public int song_id;

    public PlaylistSongCrossRef(int playlist_id, int song_id) {
        this.playlist_id = playlist_id;
        this.song_id = song_id;
    }
}

