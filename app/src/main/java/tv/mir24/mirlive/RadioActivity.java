package tv.mir24.mirlive;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class RadioActivity extends Activity {

    private static boolean isPlayed = false;
    private static MediaPlayer mediaPlayer;
    private static ImageButton radioPlayBtn;
    private static ProgressBar loadingStatus;
    private static TextView    currentSong;
    private static Handler     mHandler;
    private static int         mInterval = 15000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio);

        mHandler = new Handler();

        Button backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final String url = getIntent().getStringExtra("liveUrl");

        loadingStatus = (ProgressBar) findViewById(R.id.radioLoadingStatus);
        currentSong = (TextView) findViewById(R.id.currentSong);

        radioPlayBtn = (ImageButton) findViewById(R.id.radioPlayBtn);
        radioPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPlayButton(1);
                if (isPlayed) {
                    stopRadio();
                    currentSong.setText(getText(R.string.default_radio_meta));
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playRadio(url);
                        }
                    }, 1000);
                }
            }
        });
    }

    private void playRadio(String url) {
        mediaPlayer = new MediaPlayer();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                switchPlayButton(2);
                startRepeatingTask();
            }
        });
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            switchPlayButton(0);
        }
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mediaPlayer.release();
                switchPlayButton(0);
                currentSong.setText(getText(R.string.default_radio_meta));
                stopRepeatingTask();
                return false;
            }
        });
    }

    private void stopRadio() {
        mediaPlayer.release();
        stopRepeatingTask();

        switchPlayButton(0);
    }

    private static void switchPlayButton(int status) {
        switch (status) {
            case 0:
                isPlayed = false;
                radioPlayBtn.setVisibility(View.VISIBLE);
                loadingStatus.setVisibility(View.INVISIBLE);
                radioPlayBtn.setImageResource(R.drawable.radio_play_btn);
                break;
            case 1:
                radioPlayBtn.setVisibility(View.INVISIBLE);
                loadingStatus.setVisibility(View.VISIBLE);
                break;
            case 2:
                isPlayed = true;
                radioPlayBtn.setVisibility(View.VISIBLE);
                loadingStatus.setVisibility(View.INVISIBLE);
                radioPlayBtn.setImageResource(R.drawable.radio_stop_btn);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        stopRepeatingTask();
    }

    class SongUpdateTask extends AsyncTask<Void, Void, Void> {

        String meta = "Вы слушаете - радио \"МИР\"";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            meta = getCurrentSong();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentSong.setText(meta);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                SongUpdateTask task = new SongUpdateTask();
                task.execute();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };


    private static String getCurrentSong() {
        String meta = "Вы слушаете - радио \"МИР\"";
        Log.d("Info", "current song get start");
        URL url = null;
        BufferedReader in = null;
        try {
            url = new URL("http://data.radiomir.fm/metadata");
            in = new BufferedReader(
                    new InputStreamReader(
                            url.openStream()));
            if(in.ready()) meta = in.readLine();
        }
        catch (Exception e) {
            Log.d("ERROR", meta + " " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            if(in != null) try {
                in.close();
            } catch (IOException e) {}

        }
        Log.d("Info", "current song got - " + meta);
        return meta;
    }

    @Override
    protected void onStop(){
        if(!isChangingConfigurations()){
            super.onStop();
        }
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}
