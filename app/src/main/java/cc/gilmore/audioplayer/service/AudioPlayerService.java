package cc.gilmore.audioplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import cc.gilmore.audioplayer.model.Song;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class AudioPlayerService extends Service {
    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;
    private List<Song> playlist;
    private int currentSongIndex;
    private boolean isShuffleOn;
    private Random random;

    public class LocalBinder extends Binder {
        public AudioPlayerService getService() {
            return AudioPlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        System.out.println("AudioPlayerService.onCreate");
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        random = new Random();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setPlaylist(List<Song> playlist) {
        this.playlist = playlist;
        currentSongIndex = 0;
    }

    public void playSong(int index) {
        //Log.d("AudioPlayerService", "playSong: " + index);
        if (playlist == null || playlist.isEmpty()) {
            Log.d("AudioPlayerService", "playlist is empty");
            return;
        }

        currentSongIndex = index;
        Song song = playlist.get(currentSongIndex);

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AudioPlayerService", "Error playing song: " + e.getMessage());
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void next() {
        if (isShuffleOn) {
            currentSongIndex = random.nextInt(playlist.size());
        } else {
            currentSongIndex = (currentSongIndex + 1) % playlist.size();
        }
        playSong(currentSongIndex);
    }

    public void previous() {
        if (isShuffleOn) {
            currentSongIndex = random.nextInt(playlist.size());
        } else {
            currentSongIndex = (currentSongIndex - 1 + playlist.size()) % playlist.size();
        }
        playSong(currentSongIndex);
    }

    public void toggleShuffle() {
        isShuffleOn = !isShuffleOn;
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public Song getCurrentSong() {
        if (playlist != null && !playlist.isEmpty() && currentSongIndex >= 0 && currentSongIndex < playlist.size()) {
            return playlist.get(currentSongIndex);
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

