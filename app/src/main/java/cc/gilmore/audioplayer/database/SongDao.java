package cc.gilmore.audioplayer.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import cc.gilmore.audioplayer.model.Song;

import java.util.List;

@Dao
public interface SongDao {
    @Insert
    void insert(Song song);

    @Update
    void update(Song song);

    @Delete
    void delete(Song song);

    @Query("SELECT * FROM songs")
    List<Song> getAllSongs();

    @Query("SELECT * FROM songs WHERE id = :id")
    Song getSongById(int id);

    @Query("SELECT * FROM songs WHERE title LIKE :search OR artist LIKE :search")
    List<Song> searchSongs(String search);
}

