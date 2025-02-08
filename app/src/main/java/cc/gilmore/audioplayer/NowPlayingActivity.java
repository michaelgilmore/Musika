package cc.gilmore.audioplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cc.gilmore.audioplayer.model.Song;
import cc.gilmore.audioplayer.service.AudioPlayerService;

public class NowPlayingActivity extends AppCompatActivity {

    private AudioPlayerService audioService;
    private boolean serviceBound = false;
    private TextView songTitleView;
    private TextView artistView;
    private SeekBar seekBar;
    private ImageButton playPauseButton;
    private Handler handler = new Handler();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioPlayerService.LocalBinder binder = (AudioPlayerService.LocalBinder) service;
            audioService = binder.getService();
            serviceBound = true;
            updatePlaybackState();
            updateNowPlayingInfo();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        songTitleView = findViewById(R.id.now_playing_title);
        artistView = findViewById(R.id.now_playing_artist);
        seekBar = findViewById(R.id.now_playing_seek_bar);
        playPauseButton = findViewById(R.id.now_playing_play_pause);
        ImageButton prevButton = findViewById(R.id.now_playing_previous);
        ImageButton nextButton = findViewById(R.id.now_playing_next);

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

        prevButton.setOnClickListener(v -> {
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
    }

    private void updatePlaybackState() {
        if (serviceBound) {
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
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
        handler.removeCallbacksAndMessages(null);
    }
}

