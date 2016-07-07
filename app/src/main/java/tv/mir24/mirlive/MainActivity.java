package tv.mir24.mirlive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    static final int PAGE_COUNT = 4;
    static final String[] channels = {"МИР","МИР24","МИРHD","Радио «МИР»"};
    static final String[] streams =
            {"http://hls.mirtv.cdnvideo.ru/mirtv-parampublish/mirtv_mobil/playlist.m3u8",
             "http://hls.mirtv.cdnvideo.ru/mirtv-parampublish/mir24_mobil/playlist.m3u8",
             "http://hls.mirtv.cdnvideo.ru/mirtv-parampublish/hd_low/playlist.m3u8",
             "http://icecast.mirtv.cdnvideo.ru:8000/radio_mir128"};

    ViewPager    pager;
    PagerAdapter pagerAdapter;

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
            public void onPageSelected(final int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        Button button = (Button) findViewById(R.id.startBtn);
        button.setOnClickListener(new View.OnClickListener() {
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
        });

    }

    private class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

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

    }
}
