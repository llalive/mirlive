package tv.mir24.mirlive;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static final int PAGE_COUNT = 5;
    static final String[] channels = {"МИР", "МИР24", "МИРHD", "Радио «МИР»", "МИР+3"};
    static final String[] streams =
            {"http://hls.mirtv.cdnvideo.ru/mirtv-parampublish/mirtv_mobil/playlist.m3u8",
             "http://hls.mirtv.cdnvideo.ru/mirtv-parampublish/mir24_mobil/playlist.m3u8",
             "http://hls.mirtv.cdnvideo.ru/mirtv-parampublish/hd_low/playlist.m3u8",
             "http://icecast.mirtv.cdnvideo.ru:8000/radio_mir128",
             "http://hls.mirtv.cdnvideo.ru/mirtv-parampublish/mirtv3_mobil/playlist.m3u8"};

    ViewPager    pager;
    CustomFragmentPagerAdapter pagerAdapter;
    Button aboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(final int position) {
                toggleChannelDesc(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        aboutBtn = (Button) findViewById(R.id.aboutBtn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleChannelDesc(false);
            }
        });
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if(pager.getCurrentItem() == 3){
                    i = new Intent(getApplicationContext(), RadioActivity.class);
                }
                else {
                    i = new Intent(getApplicationContext(), LiveActivity.class);
                }
                i.putExtra("liveUrl", streams[pager.getCurrentItem()]);
                startActivity(i);
            }
        });*/

    }

    private void toggleChannelDesc(boolean hide){
        View currentView = pagerAdapter.getCurrentFragment().getView();
        RelativeLayout descLayout = (RelativeLayout)currentView.findViewById(R.id.channelLayout);
        RelativeLayout playLayout = (RelativeLayout)findViewById(R.id.playLayout);
        if (hide || descLayout.getVisibility() == View.VISIBLE) {
            descLayout.setVisibility(View.INVISIBLE);
            playLayout.setVisibility(View.VISIBLE);
            aboutBtn.setText("О канале");
        } else {
            descLayout.setVisibility(View.VISIBLE);
            playLayout.setVisibility(View.INVISIBLE);
            aboutBtn.setText("К эфиру");
        }
    }

    private class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

        private Fragment currentFragment;

        public CustomFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return channels[position];
        }

        public Fragment getCurrentFragment() {
            return currentFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                currentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

    }
}
