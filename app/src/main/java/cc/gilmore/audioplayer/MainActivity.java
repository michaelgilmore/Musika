package cc.gilmore.audioplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import cc.gilmore.audioplayer.adapter.SongAdapter;
import cc.gilmore.audioplayer.database.AppDatabase;
import cc.gilmore.audioplayer.model.Song;
import cc.gilmore.audioplayer.service.AudioPlayerService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnSongClickListener {
    static {
        System.out.println("MyApplication: Static initializer block");
    }

    private AudioPlayerService audioService;
    private boolean serviceBound = false;
    private List<Song> songList = new ArrayList<>();
    private SongAdapter songAdapter;
    private List<Song> playlist;

    private TextView songTitleView;
    private TextView artistView;
    private ImageView albumCoverView;
    private SeekBar seekBar;
    private Button playPauseButton;
    private Button nextButton;
    private Button previousButton;
    private Button shuffleButton;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioPlayerService.LocalBinder binder = (AudioPlayerService.LocalBinder) service;
            audioService = binder.getService();
            serviceBound = true;
            audioService.setPlaylist(playlist);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songTitleView = findViewById(R.id.song_title);
        artistView = findViewById(R.id.artist);
        albumCoverView = findViewById(R.id.album_cover);
        seekBar = findViewById(R.id.seek_bar);
        playPauseButton = findViewById(R.id.play_pause_button);
        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);
        shuffleButton = findViewById(R.id.shuffle_button);

        // Initialize playlist (this should be populated from your database in a real app)
        playlist = new ArrayList<>();
        playlist.add(new Song("Song 1", "Artist 1", "Album 1", "path/to/song1.mp3", "https://example.com/album1.jpg", 180000));
        playlist.add(new Song("Song 2", "Artist 2", "Album 2", "path/to/song2.mp3", "https://example.com/album2.jpg", 200000));

        Intent intent = new Intent(this, AudioPlayerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        RecyclerView recyclerView = findViewById(R.id.song_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter(songList, this);
        recyclerView.setAdapter(songAdapter);

        Button uploadButton = findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(MainActivity.this, FileUploadActivity.class);
            startActivity(uploadIntent);
        });

        loadSongs();

        playPauseButton.setOnClickListener(v -> {
            if (audioService.isPlaying()) {
                audioService.pause();
                playPauseButton.setText("Play");
            } else {
                audioService.resume();
                playPauseButton.setText("Pause");
            }
        });

        nextButton.setOnClickListener(v -> audioService.next());
        previousButton.setOnClickListener(v -> audioService.previous());
        shuffleButton.setOnClickListener(v -> audioService.toggleShuffle());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioService.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateUI(Song song) {
        songTitleView.setText(song.title);
        artistView.setText(song.artist);
        Glide.with(this).load(song.albumCoverUrl).into(albumCoverView);
        seekBar.setMax((int) song.duration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSongs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
    }

    private void loadSongs() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Song> songs = db.songDao().getAllSongs();
            runOnUiThread(() -> {
                songList.clear();
                songList.addAll(songs);
                songAdapter.notifyDataSetChanged();
                if (serviceBound) {
                    audioService.setPlaylist(songList);
                }
            });
        }).start();
    }

    @Override
    public void onSongClick(Song song) {
        if (serviceBound) {
            int index = songList.indexOf(song);
            audioService.playSong(index);
            Intent intent = new Intent(MainActivity.this, NowPlayingActivity.class);
            startActivity(intent);
        }
    }
}

