package monuments.guwahati.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import monuments.guwahati.R;
import monuments.guwahati.adapter.SimpleRecyclerAdapter2;


public class AnimateToolbar extends AppCompatActivity
{

    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;

    public static final String[] IMAGE_NAME = { "kamakhya2", "balaji2", "umananda2", "basistha2", "navagraha2" };

    private int currentPage;


    CollapsingToolbarLayout collapsingToolbar;
    RecyclerView recyclerView;

    //int mutedColor = R.attr.colorPrimary;
    SimpleRecyclerAdapter2 simpleRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_toolbar);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("MONUMENTS OF GUWAHATI");


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.balaji2);


        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette)
            {

                int mutedColor = palette.getMutedColor(R.color.myPrimaryColor);
                collapsingToolbar.setContentScrimColor(mutedColor);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.scrollableview);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        if (simpleRecyclerAdapter == null)
        {
            simpleRecyclerAdapter = new SimpleRecyclerAdapter2(this);
            recyclerView.setAdapter(simpleRecyclerAdapter);
        }


        simpleRecyclerAdapter.SetOnItemClickListener(new SimpleRecyclerAdapter2.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position)
            {

                Intent intent  = new Intent(AnimateToolbar.this, ViewMonumentActivity.class);
                intent.putExtra("POS", position);
                startActivity(intent);
            }
        });


        // Code for image swipe slider
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);

        autoSlideImages();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {

            case android.R.id.home:
                finish();
                return true;

            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter
    {

        public ImageFragmentPagerAdapter(FragmentManager fm)
        {

            super(fm);
            Log.v("TAG: ", "PagerAdapter");
        }


        @Override
        public int getCount()
        {
            return IMAGE_NAME.length;
        }


        @Override
        public Fragment getItem(int position)
        {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }
    }


    public static class SwipeFragment extends Fragment
    {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {

            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
            Bundle bundle = getArguments();

            int position = bundle.getInt("position");

            String imageFileName = IMAGE_NAME[position];
            int imgResId = getResources().getIdentifier(imageFileName, "drawable", "monuments.guwahati");
            imageView.setImageResource(imgResId);

            Log.v("TAG: ", "SwipeFragement");

            return swipeView;
        }


        static SwipeFragment newInstance(int position)
        {

            Log.v("TAG: ", "newInstatnce");

            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);

            return swipeFragment;
        }
    }



    private void autoSlideImages()
    {

        final Handler handler = new Handler();

        final Runnable Update = new Runnable()
        {

            public void run()
            {

                if (currentPage == IMAGE_NAME.length)
                {
                    currentPage = 0;
                }

                viewPager.setCurrentItem(currentPage++, true);
            }
        };


        Timer swipeTimer = new Timer();


        swipeTimer.schedule(new TimerTask()
        {

            @Override
            public void run()
            {
                handler.post(Update);
            }
        }, 2000, 3000);
    }
}