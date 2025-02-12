package cc.gilmore.audioplayer.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import cc.gilmore.audioplayer.model.Playlist;
import cc.gilmore.audioplayer.model.PlaylistWithSongs;

import java.util.List;

@Dao
public interface PlaylistDao {
    @Insert
    long insert(Playlist playlist);

    @Update
    void update(Playlist playlist);

    @Delete
    void delete(Playlist playlist);

    @Query("SELECT * FROM playlists")
    List<Playlist> getAllPlaylists();

    @Query("SELECT * FROM playlists WHERE id = :id")
    Playlist getPlaylistById(int id);

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    PlaylistWithSongs getPlaylistWithSongs(int playlistId);

    @Query("INSERT INTO playlist_song_cross_ref (playlist_id, song_id) VALUES(:playlistId, :songId)")
    void addSongToPlaylist(int playlistId, int songId);

    @Query("DELETE FROM playlist_song_cross_ref WHERE playlist_id = :playlistId AND song_id = :songId")
    void removeSongFromPlaylist(int playlistId, int songId);
}

