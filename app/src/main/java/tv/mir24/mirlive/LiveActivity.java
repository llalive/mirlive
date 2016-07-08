package tv.mir24.mirlive;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class LiveActivity extends Activity {

    CustomMediaController mediaControls;
    VideoView             videoView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.live);

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.loadingStatus);
        progressBar.setVisibility(View.VISIBLE);

        ImageButton closeBtn = (ImageButton) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.suspend();
                finish();
            }
        });

        Intent i = getIntent();

        if(mediaControls == null){
            mediaControls = new CustomMediaController(LiveActivity.this, closeBtn);
        }

        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setMediaController(mediaControls);
        try {
            String streamUrl = i.getStringExtra("liveUrl");
            videoView.setVideoURI(Uri.parse(streamUrl));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
