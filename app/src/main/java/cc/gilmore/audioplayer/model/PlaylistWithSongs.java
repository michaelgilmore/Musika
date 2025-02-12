package cc.gilmore.audioplayer.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class PlaylistWithSongs {
    @Embedded
    public Playlist playlist;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = PlaylistSongCrossRef.class,
                    parentColumn = "playlist_id",
                    entityColumn = "song_id"
            )
    )
    public List<Song> songs;
}

