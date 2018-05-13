package com.udacity.sandwichclub.utils;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.udacity.sandwichclub.model.Sandwich;

public class JsonUtils {

    // Tag for Log statements
    private final static String TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json){
        Sandwich sandwich = new Sandwich();

        // Validate json string
        if(!json.equals("")) {
            try {

                // Get the JSON object
                JSONObject jsonObj = new JSONObject(json);

                // Validate JSON object exists
                if(jsonObj.has("name")){

                    // Get the JSON object
                    JSONObject jsonName = jsonObj.getJSONObject("name");

                    // Create the sandwich object
                    sandwich.setMainName(jsonName.getString("mainName"));
                    sandwich.setDescription(jsonObj.getString("description"));
                    sandwich.setImage(jsonObj.getString("image"));
                    sandwich.setPlaceOfOrigin(jsonObj.getString("placeOfOrigin"));

                    // Separate the parsing of JSON arrays
                    List<String> aka = parseJsonArray(jsonName.getJSONArray("alsoKnownAs"));
                    List<String> ing = parseJsonArray(jsonObj.getJSONArray("ingredients"));

                    sandwich.setAlsoKnownAs(aka);
                    sandwich.setIngredients(ing);

//                    Log.i(TAG, sandwich.getMainName() + ">>>>");
//                    Log.i(TAG, sandwich.getDescription() + ">>>>");
//                    Log.i(TAG, sandwich.getImage() + ">>>>");
//                    Log.i(TAG, sandwich.getPlaceOfOrigin() + ">>>>");
//                    Log.i(TAG, sandwich.getAlsoKnownAs().get(0) + ">>>>");
//                    Log.i(TAG, sandwich.getIngredients().get(0) + ">>>>");
                }

                return sandwich;

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            Log.i(TAG, json);
        }
        return null;
    }

    /**
     *
     * @param jsonArr : The JSON array to be parsed
     * @return List<String>
     */
    private static List<String> parseJsonArray( JSONArray jsonArr){

        List<String> list = new ArrayList<>();
        if(jsonArr.length() > 0) {

            for (int idx = 0; idx < jsonArr.length(); idx++) {
                try {
                    list.add(jsonArr.getString(idx));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }
}
