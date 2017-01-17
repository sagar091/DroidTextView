package com.droidbyme.droidtextview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_revised);

        startActivity(new Intent(this, TextActivity.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Surya Motors");

        dynamicToolbarColor();
        toolbarTextAppernce();

        // ((Button) findViewById(R.id.btn)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/regular.ttf"));


        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        ((DroidTextView) findViewById(R.id.offerTextView)).startAnimation(myFadeInAnimation);

       /* LinearLayout addLayout = (LinearLayout) findViewById(R.id.addLayout);

        DroidTextView droidTextView = new DroidTextView(this);
        droidTextView.setText("My Name is Sagar");


        // setHighLightText(String text, int textColor, boolean isUnderline, boolean isBold)
        // droidTextView.setHighLightText("is", Color.RED, true, true);

        // setEndDots(int maxLines)
        // droidTextView.setEndDots(2);

        // setBigText(int charLength, int bigTimes)
        // droidTextView.setBigText(2, 5);

        addLayout.addView(droidTextView);*/
    }

    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.createFromAsset(getAssets(), "fonts/regular.ttf"));
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.createFromAsset(getAssets(), "fonts/regular.ttf"));
    }

    private void dynamicToolbarColor() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.sc);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
            }
        });
    }
}
