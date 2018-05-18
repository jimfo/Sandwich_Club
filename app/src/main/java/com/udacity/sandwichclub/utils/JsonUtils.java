package com.udacity.sandwichclub.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.udacity.sandwichclub.model.Sandwich;

public class JsonUtils {

    // Tag for Log statements
    private final static String TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich();
        List<String> aka = new ArrayList<>();
        List<String> ing = new ArrayList<>();

        // Validate json string
        if (!json.equals("")) {
            try {

                // Get the JSON object
                JSONObject jsonObj = new JSONObject(json);

                // Validate JSON object exists
                if (jsonObj.has("name")) {

                    // Get the JSON object
                    JSONObject jsonName = jsonObj.optJSONObject("name");
                    if (jsonName == null) {
                        return null;
                    }

                    // Create the sandwich object optString("name", "fall_back_string")
                    sandwich.setMainName(jsonName.optString("mainName", "Data not available"));
                    sandwich.setDescription(jsonObj.optString("description", "Data Not Available"));
                    sandwich.setImage(jsonObj.optString("image", "No Image"));
                    sandwich.setPlaceOfOrigin(jsonObj.optString("placeOfOrigin", "Data Not Available"));

                    // Separate the parsing of JSON arrays
                    if (jsonName.optJSONArray("alsoKnownAs") == null) {
                        aka.add("No Alias Found");
                    } else {
                        aka = parseJsonArray(jsonName.optJSONArray("alsoKnownAs"));
                    }

                    if (jsonObj.optJSONArray("ingredients") == null) {
                        ing.add("No Ingredients Found");
                    } else {
                        ing = parseJsonArray(jsonObj.optJSONArray("ingredients"));
                    }

                    sandwich.setAlsoKnownAs(aka);
                    sandwich.setIngredients(ing);
                }

                return sandwich;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param jsonArr : The JSON array to be parsed
     * @return List<String>
     */
    private static List<String> parseJsonArray(JSONArray jsonArr) {

        List<String> list = new ArrayList<>();

        if (jsonArr.length() > 0) {
            for (int idx = 0; idx < jsonArr.length(); idx++) {
                list.add(jsonArr.optString(idx, "Data not available"));
            }
        } else {
            list.add("Data not available");
        }

        return list;
    }
}
