package com.udacity.sandwichclub;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    Animation scaleUp, scaleDown;
    LinearLayout detailActivityLl;
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
        detailActivityLl = findViewById(R.id.detail_activity_ll);
        sv = findViewById(R.id.scrollview);

        Intent intent = getIntent();

        // If intent is null send message to user
        if (intent == null) {
            closeOnError();
        }
        else {
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

        populateUI(sandwich);

        // Get the image / set the image
        Picasso.with(this).load(sandwich.getImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ingredientsIv.setImageBitmap(bitmap);
                getColors(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

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
     * Purpose : to get the average color of the image
     * @param bitmap : The image of the sandwich
     */
    private void getColors(Bitmap bitmap ){

        int redColors = 0;
        int greenColors = 0;
        int blueColors = 0;
        int pixelCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y++)
        {
            for (int x = 0; x < bitmap.getWidth(); x++)
            {
                int c = bitmap.getPixel(x, y);
                pixelCount++;
                redColors += Color.red(c);
                greenColors += Color.green(c);
                blueColors += Color.blue(c);
            }
        }

        int red = (redColors/pixelCount);
        int green = (greenColors/pixelCount);
        int blue = (blueColors/pixelCount);

        setColors(red, green, blue);
    }

    /**
     * Purpose : Set colors of widgets and text. Widgets will be lighter than background color
     * of LinearLayout, Text will be darker
     * @param r : int red color
     * @param g : int green color
     * @param b : int blue color
     */
    private void setColors(int r, int g, int b){

        detailActivityLl.setBackgroundColor(Color.rgb(r, g, b));

        int textRed   = (int) (r * (1 - .75));
        int textGreen = (int) (g * (1 - .75));
        int textBlue  = (int) (b * (1 - .75));

        ingredientsTv.setTextColor(Color.rgb(textRed, textGreen, textBlue));
        ingredientsBtn.setTextColor(Color.rgb(textRed, textGreen, textBlue));
        descriptionTv.setTextColor(Color.rgb(textRed, textGreen, textBlue));
        descriptionBtn.setTextColor(Color.rgb(textRed, textGreen, textBlue));
        akaTv.setTextColor(Color.rgb(textRed, textGreen, textBlue));
        originTv.setTextColor(Color.rgb(textRed, textGreen, textBlue));
        detail_aka_tv.setTextColor(Color.rgb(textRed, textGreen, textBlue));
        detail_place_of_origin_tv.setTextColor(Color.rgb(textRed, textGreen, textBlue));

        int bgRed   = (int) (r + (255 - r) * .25);
        int bgGreen = (int) (g + (255 - g) * .25);
        int bgBlue  = (int) (b + (255 - b) * .25);

        ingredientsBtn.setBackgroundColor(Color.rgb(bgRed, bgGreen, bgBlue));
        descriptionBtn.setBackgroundColor(Color.rgb(bgRed, bgGreen, bgBlue));
    }

    /**
     * Purpose : Populate the UI with sandwich data
     * @param sw : Sandwich object
     */
    private void populateUI(Sandwich sw) {
        originTv.append(sw.getPlaceOfOrigin());
        descriptionTv.append(sw.getDescription());


        for (int i = 0; i < sw.getAlsoKnownAs().size(); i++) {
            akaTv.append(sw.getAlsoKnownAs().get(i) + "\n");
        }

        for (int i = 0; i < sw.getIngredients().size(); i++) {
            ingredientsTv.append(sw.getIngredients().get(i) + "\n"); }
    }

    /**
     * Purpose : To provide some animation (Show / Hide text)
     * @param v : Will be a button pressed
     */
    public void showTextView(View v){
        Animation animate;
        switch(v.getId()){
            case R.id.description_btn:
                if (descriptionTv .getVisibility() == View.GONE) {

                    descriptionTv.setVisibility(View.VISIBLE);
                    descriptionTv.startAnimation(scaleUp);

                    sv.post(new Runnable() {
                        public void run() {
                            sv.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                else{
                    descriptionTv.setVisibility(View.GONE);
                    descriptionTv.startAnimation(scaleDown);
                }
                break;
            case R.id.ingredients_btn:
                if (ingredientsTv .getVisibility() == View.GONE) {

                    ingredientsTv.setVisibility(View.VISIBLE);
                    ingredientsTv.startAnimation(scaleUp);

                    sv.post(new Runnable() {
                        public void run() {
                            sv.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                else{
                    ingredientsTv.setVisibility(View.GONE);
                    ingredientsTv.startAnimation(scaleDown);
                }
                break;
        }
    }
}
