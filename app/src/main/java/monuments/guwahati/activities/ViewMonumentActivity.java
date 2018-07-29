package monuments.guwahati.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import monuments.guwahati.R;
import monuments.guwahati.adapter.SimpleRecyclerAdapter1;
import monuments.guwahati.gps.GPSServices;
import monuments.guwahati.gps.GPSTracker;
import monuments.guwahati.helper.OnTaskCompleted;
import monuments.guwahati.utils.MyRecyclerScroll;

import java.util.Timer;
import java.util.TimerTask;


public class ViewMonumentActivity extends AppCompatActivity implements OnTaskCompleted
{

    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;

    public static String[] IMAGE_NAME;

    private int currentPage;


    CollapsingToolbarLayout collapsingToolbar;
    RecyclerView recyclerView;

    //int mutedColor = R.attr.colorPrimary;
    SimpleRecyclerAdapter1 simpleRecyclerAdapter;

    FrameLayout fab;
    int fabMargin;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monument);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        String[] listArray = getResources().getStringArray(R.array.home_activities);
        int pos = getIntent().getIntExtra("POS", 0);

        collapsingToolbar.setTitle(listArray[pos]);


        IMAGE_NAME = imageArray(pos);


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
            simpleRecyclerAdapter = new SimpleRecyclerAdapter1(this, pos);
            recyclerView.setAdapter(simpleRecyclerAdapter);
        }


        simpleRecyclerAdapter.SetOnItemClickListener(new SimpleRecyclerAdapter1.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position)
            {

            }
        });


        fab = (FrameLayout) findViewById(R.id.myfab_main);
        ImageButton fabBtn = (ImageButton) findViewById(R.id.myfab_main_btn);


        fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);


        recyclerView.addOnScrollListener(new MyRecyclerScroll() {

            @Override
            public void show() {
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }


            @Override
            public void hide() {
                fab.animate().translationY(fab.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });



        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabShadow.setVisibility(View.GONE);
            fabBtn.setBackground(getDrawable(R.drawable.ripple_accent));
        }*/



        fab.startAnimation(animation);


        fabBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                Log.d("CLICK", "FAB CLICK");
                startActivity(new Intent(ViewMonumentActivity.this, MapsActivity.class));
            }
        });


        // Code for image swipe slider
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);

        autoSlideImages();
        calculateDistance();
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



    private String[] imageArray(int pos)
    {

        String[] IMAGE_NAME;

        switch(pos)
        {

            case 0:

                IMAGE_NAME = new String[] { "kamakhya1", "kamakhya2", "kamakhya3" };
                break;

            case 1:

                IMAGE_NAME = new String[] { "balaji1", "balaji2", "balaji3" };
                break;

            case 2:

                IMAGE_NAME = new String[] { "umananda1", "umananda2", "umananda3" };
                break;

            case 3:

                IMAGE_NAME = new String[] { "basistha1", "basistha2", "basistha3" };
                break;

            case 4:

                IMAGE_NAME = new String[] { "navagraha1", "navagraha2", "navagraha3" };
                break;

            default:
                IMAGE_NAME = new String[] { "kamakhya2", "balaji2", "umananda2", "basistha2", "navagraha2" };
        }

        return IMAGE_NAME;
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


    private void calculateDistance()
    {

        GPSTracker gps = new GPSTracker(ViewMonumentActivity.this, this);

        if(gps.canGetLocation()) // check if GPS enabled
        {

            LatLng l1 = new LatLng(gps.getLatitude(), gps.getLongitude());
            LatLng l2 = new LatLng(26.1664, 91.7055);

            String distance = GPSServices.getDistance(l1, l2);
            String direction = GPSServices.getDirection(l1, l2);

            //Toast.makeText(getApplicationContext(), "Direction " + direction, Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "Distance " + distance, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onTaskCompleted(boolean flag, String message)
    {

    }


    @Override
    public void onTaskCompleted(Location location)
    {

    }
}