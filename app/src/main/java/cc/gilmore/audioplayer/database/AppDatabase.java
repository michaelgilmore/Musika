package cc.gilmore.audioplayer.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import cc.gilmore.audioplayer.model.PlaylistSongCrossRef;
import cc.gilmore.audioplayer.model.PlaylistWithSongs;
import cc.gilmore.audioplayer.model.Song;
import cc.gilmore.audioplayer.database.SongDao;
import cc.gilmore.audioplayer.database.PlaylistDao;
import cc.gilmore.audioplayer.model.Playlist;

@Database(entities = {Song.class, Playlist.class, PlaylistSongCrossRef.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract SongDao songDao();
    public abstract PlaylistDao playlistDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "audio_player_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            System.out.println("AppDatabase.onCreate");
                            super.onCreate(db);
                            Log.d("AppDatabase", "Database created");
                            Cursor cursor = db.query("SELECT name FROM sqlite_master WHERE type='table'");
                            while (cursor.moveToNext()) {
                                Log.d("AppDatabase", "Table created: " + cursor.getString(0));
                            }
                            cursor.close();
                        }
                    })
                    .build();

            //TEMPORARY
            // Force database recreation
//            context.deleteDatabase("audio_player_database");
        }
        return instance;
    }
}

