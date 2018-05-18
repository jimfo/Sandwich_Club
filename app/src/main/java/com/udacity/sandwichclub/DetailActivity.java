package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    Animation scaleUp, scaleDown;
    TextView originTv, akaTv, detail_aka_tv;
    TextView ingredientsTv, descriptionTv;
    TextView detail_place_of_origin_tv;
    Button descriptionBtn, ingredientsBtn;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        scaleUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.scale_up);

        scaleDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.scale_down);

        int position = -1;
        final ImageView ingredientsIv = findViewById(R.id.image_iv);

        originTv = findViewById(R.id.origin_tv);
        akaTv = findViewById(R.id.also_known_tv);
        detail_aka_tv = findViewById(R.id.detail_also_known_as_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);
        ingredientsBtn = findViewById(R.id.ingredients_btn);
        descriptionTv = findViewById(R.id.description_tv);
        descriptionBtn = findViewById(R.id.description_btn);
        detail_place_of_origin_tv = findViewById(R.id.detail_place_of_origin_tv);
        sv = findViewById(R.id.scrollview);

        Intent intent = getIntent();

        // If intent is null send message to user
        if (intent == null) {
            closeOnError();
        } else {
            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);

            if (position == DEFAULT_POSITION) {
                // EXTRA_POSITION not found in intent
                closeOnError();
                return;
            }
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);

        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        // Get the image / set the image
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                ingredientsIv.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        ingredientsIv.setTag(target);
        Picasso.with(this).load(sandwich.getImage())
                .placeholder(R.drawable.no_image_avalable)
                .error(R.drawable.no_image_avalable)
                .into(target);

        populateUI(sandwich);

        setTitle(sandwich.getMainName());
    }

    /**
     * Purpose : To notify user data is unavailable
     */
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Purpose : Populate the UI with sandwich data
     *
     * @param sw : Sandwich object
     */
    private void populateUI(Sandwich sw) {

        if (!sw.getPlaceOfOrigin().equals("")) {
            originTv.setText(sw.getPlaceOfOrigin());
        }

        if (!sw.getDescription().equals("")) {
            descriptionTv.setText(sw.getDescription());
        }

        String akaString = TextUtils.join("\n", sw.getAlsoKnownAs());
        String ingString = TextUtils.join("\n", sw.getIngredients());

        akaTv.setText(akaString);
        ingredientsTv.setText(ingString);
    }

    /**
     * Purpose : To provide some animation (Show / Hide text)
     *
     * @param v : Will be a button pressed
     */
    public void showTextView(View v) {

        switch (v.getId()) {
            case R.id.description_btn:
                if (descriptionTv.getVisibility() == View.GONE) {

                    descriptionTv.setVisibility(View.VISIBLE);
                    descriptionTv.startAnimation(scaleUp);

                    sv.post(new Runnable() {
                        public void run() {
                            sv.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                } else {
                    descriptionTv.startAnimation(scaleDown);
                    descriptionTv.setVisibility(View.GONE);

                    sv.post(new Runnable() {
                        public void run() {
                            sv.fullScroll(View.FOCUS_UP);
                        }
                    });
                }
                break;
            case R.id.ingredients_btn:
                if (ingredientsTv.getVisibility() == View.GONE) {

                    ingredientsTv.setVisibility(View.VISIBLE);
                    ingredientsTv.startAnimation(scaleUp);

                    sv.post(new Runnable() {
                        public void run() {
                            sv.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                } else {
                    ingredientsTv.startAnimation(scaleDown);
                    ingredientsTv.setVisibility(View.GONE);

                    sv.post(new Runnable() {
                        public void run() {
                            sv.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                break;
        }
    }
}
