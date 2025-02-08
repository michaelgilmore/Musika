package cc.gilmore.audioplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import cc.gilmore.audioplayer.adapter.SongAdapter;
import cc.gilmore.audioplayer.database.AppDatabase;
import cc.gilmore.audioplayer.model.Song;
import cc.gilmore.audioplayer.service.AudioPlayerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnSongClickListener {
    private AudioPlayerService audioService;
    private boolean serviceBound = false;
    private List<Song> songList = new ArrayList<>();
    private SongAdapter songAdapter;

    private TextView songTitleView;
    private TextView artistView;
    private ImageView albumCoverView;
    private SeekBar seekBar;
    private ImageButton playPauseButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private Button shuffleButton;

    private Handler handler = new Handler();
    private static final int READ_REQUEST_CODE = 42;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioPlayerService.LocalBinder binder = (AudioPlayerService.LocalBinder) service;
            audioService = binder.getService();
            serviceBound = true;
            audioService.setPlaylist(songList);
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

        songTitleView = findViewById(R.id.now_playing_title);
        artistView = findViewById(R.id.now_playing_artist);
        albumCoverView = findViewById(R.id.album_cover);
        seekBar = findViewById(R.id.now_playing_seek_bar);
        nextButton = findViewById(R.id.now_playing_next);
        previousButton = findViewById(R.id.now_playing_previous);
        shuffleButton = findViewById(R.id.shuffle_button);
        playPauseButton = findViewById(R.id.now_playing_play_pause);

        playPauseButton.setOnClickListener(v -> {
            if (serviceBound) {
                if (audioService.isPlaying()) {
                    audioService.pause();
                } else {
                    audioService.resume();
                }
                updatePlaybackState();
            }
        });

        previousButton.setOnClickListener(v -> {
            if (serviceBound) {
                audioService.previous();
                updateNowPlayingInfo();
            }
        });

        nextButton.setOnClickListener(v -> {
            if (serviceBound) {
                audioService.next();
                updateNowPlayingInfo();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && serviceBound) {
                    audioService.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        Intent intent = new Intent(this, AudioPlayerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        RecyclerView recyclerView = findViewById(R.id.song_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter(songList, this);
        recyclerView.setAdapter(songAdapter);

        Button uploadButton = findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(v -> {
//            Intent uploadIntent = new Intent(MainActivity.this, FileUploadActivity.class);
//            startActivity(uploadIntent);
            Intent uploadIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            uploadIntent.addCategory(Intent.CATEGORY_OPENABLE);
            uploadIntent.setType("audio/*");
            startActivityForResult(uploadIntent, READ_REQUEST_CODE);
        });

        loadSongs();

        playPauseButton.setOnClickListener(v -> {
            if (audioService.isPlaying()) {
                audioService.pause();
//                playPauseButton.setText("Play");
            } else {
                audioService.resume();
//                playPauseButton.setText("Pause");
            }
            updatePlaybackState();
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
        handler.removeCallbacksAndMessages(null);
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
            updateUI(song);
        }
    }

    private void updatePlaybackState() {
        if (serviceBound) {
            Log.d("MainActivity", "audioService.isPlaying(): " + audioService.isPlaying());
            playPauseButton.setImageResource(audioService.isPlaying() ?
                    R.drawable.ic_pause : R.drawable.ic_play);
        }
    }

    private void updateNowPlayingInfo() {
        if (serviceBound) {
            Song currentSong = audioService.getCurrentSong();
            if (currentSong != null) {
                songTitleView.setText(currentSong.title);
                artistView.setText(currentSong.artist);
                seekBar.setMax(audioService.getDuration());
                updateSeekBar();
            }
        }
    }

    private void updateSeekBar() {
        if (serviceBound && audioService.isPlaying()) {
            seekBar.setProgress(audioService.getCurrentPosition());
            handler.postDelayed(this::updateSeekBar, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                String fileName = getFileName(uri);
//                selectedFileTextView.setText(fileName);
                uploadFile(uri, fileName);
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void uploadFile(Uri uri, String fileName) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getFilesDir(), fileName);
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // 4k buffer
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
            saveSongToDatabase(fileName, file.getAbsolutePath());
            Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("FileUploadActivity", "Error uploading file", e);
            Toast.makeText(this, "Error uploading file", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSongToDatabase(String fileName, String filePath) {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        Song song = new Song(fileName, "Unknown Artist", "Unknown Album", filePath, null, 0);
        new Thread(() -> db.songDao().insert(song)).start();
    }

}

